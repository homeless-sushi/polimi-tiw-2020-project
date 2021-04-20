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

public class CareerDAO {
	private DataSource dataSrc;
	private static final String DSRC_ERROR = "DataSource not present";
	private static final Logger logger = Logger.getLogger(CareerDAO.class.getName());
	
	public CareerDAO(DataSource careerDataSource) {
		this.dataSrc = careerDataSource;
		if(dataSrc == null)
			logger.log(Level.SEVERE, DSRC_ERROR);
	}
	
	
	public List<CareerBean> getUserCareers(String personCode){
		if(dataSrc == null) {
			logger.log(Level.WARNING, DSRC_ERROR);
			return Collections.emptyList();
		}
		
		String query = "SELECT person_code, id, role, major "
		             + "FROM user_career "
		             + "WHERE person_code = ?";
			
		List<CareerBean> careers = new ArrayList<>();
		
		try (Connection connection = dataSrc.getConnection();
		     PreparedStatement statement = connection.prepareStatement(query)) {
				statement.setString(1, personCode);
				try (ResultSet result = statement.executeQuery()) {
					while(result.next()) {
						CareerBean career = new CareerBean();
						career.setPersonCode(result.getString("person_code"));
						career.setId(result.getString("id"));
						career.setRole(result.getString("role"));
						career.setMajor(result.getString("major"));
						careers.add(career);
					}
					return careers;
				}
		} catch (SQLException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}

		return Collections.emptyList();
	}
}
