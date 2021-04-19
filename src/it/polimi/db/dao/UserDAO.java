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
	
	public UserBean getUserByPersonCode(String personCode) {
		if(dataSrc == null) {
			logger.log(Level.WARNING, DSRC_ERROR);
			return null;
		}
		
		String query = "SELECT * "
		             + "FROM user "
		             + "WHERE person_code = ?";

		try (Connection connection = dataSrc.getConnection();
		     PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, personCode);
			try (ResultSet result = statement.executeQuery()) {
				if(!result.next())
					return null;
				UserBean user = new UserBean();
				user.setPersonCode(result.getString("person_code"));
				user.setEmail(result.getString("email"));
				user.setHashedPassword(result.getBytes("password"));
				user.setName(result.getString("name"));
				user.setSurname(result.getString("surname"));
				return user;
			}
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
			try (ResultSet result = statement.executeQuery()) {
				if(!result.next())
					return null;
				UserBean user = new UserBean();
				user.setPersonCode(result.getString("person_code"));
				user.setEmail(result.getString("email"));
				user.setHashedPassword(result.getBytes("password"));
				user.setName(result.getString("name"));
				user.setSurname(result.getString("surname"));
				return user;
			}
		} catch (SQLException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
		return null;
	}

	public boolean addUser(String email, byte[] password, String name, String surname) {
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
	
	public boolean addUser(UserBean user) {
		return this.addUser(user.getEmail(), user.getHashedPassword(), user.getName(), user.getSurname());
	}
}
