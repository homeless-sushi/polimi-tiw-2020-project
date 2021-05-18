package it.polimi.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

import it.polimi.db.business.UserBean;

public class UserDAO {
	private DataSource dataSrc;
	private static final String DSRC_ERROR = "DataSource not present";
	private static final Logger logger = Logger.getLogger(UserDAO.class.getName());
	
	public UserDAO(DataSource userDataSource) {
		dataSrc = userDataSource;
		if(dataSrc == null)
			logger.log(Level.SEVERE, DSRC_ERROR);
	}
	
	public UserBean getUserByPersonCode(int personCode) {
		if(dataSrc == null) {
			logger.log(Level.WARNING, DSRC_ERROR);
			return null;
		}
		
		String query = "SELECT * "
		             + "FROM user "
		             + "WHERE person_code = ?";

		try (Connection connection = dataSrc.getConnection();
			PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setInt(1, personCode);
			return getUser(statement);
		} catch (SQLException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
		return null;
	}

	public UserBean getUserByEmail(String email) {
		if(dataSrc == null) {
			logger.log(Level.WARNING, DSRC_ERROR);
			return null;
		}
		
		String query = "SELECT * "
		             + "FROM user "
		             + "WHERE email = ?";

		try (Connection connection = dataSrc.getConnection();
			PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, email);
			return getUser(statement);
		} catch (SQLException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
		return null;
	}

	public byte[] getUserHashedPsw(int personCode) {
		if(dataSrc == null) {
			logger.log(Level.WARNING, DSRC_ERROR);
			return new byte[0];
		}
		
		String query = "SELECT password "
		             + "FROM user "
		             + "WHERE person_code = ?";

		try (Connection connection = dataSrc.getConnection();
			PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setInt(1, personCode);
			try (ResultSet result = statement.executeQuery()){
				if(result.next()){
					return result.getBytes("user.password");
				}
			}
		} catch (SQLException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
		return new byte[0];
	}

	public boolean addUser(String email, String name, String surname, byte[] password) {
		if(dataSrc == null) {
			logger.log(Level.WARNING, DSRC_ERROR);
			return false;
		}
		
		String query = "INSERT INTO user (email, password, name, surname) "
		             + "VALUES (?, ?, ?, ?)";

		try (Connection connection = dataSrc.getConnection();
			PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, email);
			statement.setBytes(2, password);
			statement.setString(3, name);
			statement.setString(4, surname);
			int rows = statement.executeUpdate();
			if(rows > 0)
				return true;
		} catch (SQLException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
		return false;
	}
	
	public boolean addUser(UserBean user, byte[] password) {
		return this.addUser(user.getEmail(), user.getName(), user.getSurname(), password);
	}

	public static UserBean createUserBean(ResultSet rs) throws SQLException {
		UserBean user = new UserBean();
		user.setPersonCode(rs.getInt("user.person_code"));
		user.setEmail(rs.getString("user.email"));
		user.setName(rs.getString("user.name"));
		user.setSurname(rs.getString("user.surname"));
		return user;
	}

	private UserBean getUser(PreparedStatement ps) throws SQLException {
		try (ResultSet result = ps.executeQuery()) {
			if(!result.next())
				return null;
			return createUserBean(result);
		}
	}
}
