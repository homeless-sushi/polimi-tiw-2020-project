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

import it.polimi.db.business.ExamBean;

public class ExamDAO {
	private DataSource dataSrc;
	private static final String DSRC_ERROR = "DataSource not present";
	private static final Logger logger = Logger.getLogger(ExamDAO.class.getName());

	public ExamDAO(DataSource courseDataSource) {
		this.dataSrc = courseDataSource;
		if(dataSrc == null)
			logger.log(Level.SEVERE, DSRC_ERROR);
	}

	public List<ExamBean> getCourseExams(int courseId, int year){
		if(dataSrc == null) {
			logger.log(Level.WARNING, DSRC_ERROR);
			return Collections.emptyList();
		}
		
		String query = "SELECT * "
		             + "FROM exam "
		             + "WHERE course_id = ? "
		             + "AND year = ? "
		             + "ORDER BY date DESC";
			
		List<ExamBean> exams = new ArrayList<>();
		
		try (Connection connection = dataSrc.getConnection();
			 PreparedStatement statement = connection.prepareStatement(query)) {
				statement.setInt(1, courseId);
				statement.setInt(2, year);
				try (ResultSet result = statement.executeQuery()) {
					while(result.next()) {
						ExamBean exam = new ExamBean();
						exam.setId(result.getInt("id"));
						exam.setCourseId(result.getInt("course_id"));
						exam.setYear(result.getInt("year"));
						exam.setDate(result.getDate("date"));
						exams.add(exam);
					}
					return exams;
				}
			} catch (SQLException e) {
				logger.log(Level.SEVERE, e.getMessage(), e);
			}

		return Collections.emptyList(); 
	}
}
