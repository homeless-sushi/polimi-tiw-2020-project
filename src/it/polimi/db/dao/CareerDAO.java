package it.polimi.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

import it.polimi.db.business.CareerBean;
import it.polimi.db.business.Role;

public class CareerDAO {
	private DataSource dataSrc;
	private static final String DSRC_ERROR = "DataSource not present";
	private static final Logger logger = Logger.getLogger(CareerDAO.class.getName());
	
	public CareerDAO(DataSource careerDataSource) {
		this.dataSrc = careerDataSource;
		if(dataSrc == null)
			logger.log(Level.SEVERE, DSRC_ERROR);
	}
	
	public List<CareerBean> getUserCareers(int personCode){
		if(dataSrc == null) {
			logger.log(Level.WARNING, DSRC_ERROR);
			return Collections.emptyList();
		}
		
		String query = "SELECT * "
		             + "FROM user_career as career "
		             + "WHERE person_code = ?";
		
		try (Connection connection = dataSrc.getConnection();
			PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setInt(1, personCode);
			return getCareers(statement);
		} catch (SQLException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}

		return Collections.emptyList();
	}

	public boolean isValidCareer(int personCode, int career, Role role){
		if(dataSrc == null) {
			logger.log(Level.WARNING, DSRC_ERROR);
			return false;
		}
		
		String query = "SELECT 1 "
		             + "FROM user_career "
		             + "WHERE person_code = ? "
		             + "AND id = ? "
		             + "AND role = ?";
		
		try (Connection connection = dataSrc.getConnection();
			PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setInt(1, personCode);
			statement.setInt(2, career);
			statement.setString(3, role.toString());
			try (ResultSet result = statement.executeQuery()) {
				return result.next();
			}
		} catch (SQLException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
		
		return false;
	}

	public static CareerBean createCareerBean(ResultSet rs) throws SQLException {
		CareerBean career = new CareerBean();
		career.setPersonCode(rs.getInt("career.person_code"));
		career.setId(rs.getInt("career.id"));
		career.setRole(rs.getString("career.role"));
		career.setMajor(rs.getString("career.major"));
		try {
			career.setUser(UserDAO.createUserBean(rs));
		} catch (Exception ignore) { /* No recursive user */ }
		return career;
	}

	private List<CareerBean> getCareers(PreparedStatement ps) throws SQLException {
		try (ResultSet result = ps.executeQuery()) {
			List<CareerBean> careers = new ArrayList<>();
			while(result.next())
				careers.add(createCareerBean(result));
			return careers;
		}
	}
}
