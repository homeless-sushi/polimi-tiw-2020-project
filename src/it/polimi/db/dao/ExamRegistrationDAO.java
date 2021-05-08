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

import it.polimi.db.business.ExamRegistrationBean;
import it.polimi.db.business.ExamResult;
import it.polimi.db.business.ExamStatus;

public class ExamRegistrationDAO {
	private DataSource dataSrc;
	private static final String DSRC_ERROR = "DataSource not present";
	private static final Logger logger = Logger.getLogger(ExamRegistrationDAO.class.getName());

	public ExamRegistrationDAO(DataSource courseDataSource) {
		this.dataSrc = courseDataSource;
		if(dataSrc == null)
			logger.log(Level.SEVERE, DSRC_ERROR);
	}

	public List<ExamRegistrationBean> getExamRegistrations(int examId){
		if(dataSrc == null) {
			logger.log(Level.WARNING, DSRC_ERROR);
			return Collections.emptyList();
		}

		String query = "SELECT * "
		             + "FROM exam_registration "
		             + "WHERE exam_id = ?";

		List<ExamRegistrationBean> registrations = new ArrayList<>();

		try (Connection connection = dataSrc.getConnection();
			PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setInt(1, examId);
			try (ResultSet result = statement.executeQuery()) {
				while(result.next()) {
					ExamRegistrationBean registration = new ExamRegistrationBean();
					registration.setExamId(result.getInt("exam_id"));
					registration.setStudentid(result.getInt("student_id"));
					registration.setStatus(ExamStatus.fromString(result.getString("status")));
					registration.setResult(ExamResult.fromString(result.getString("result")));
					registration.setGrade(result.getInt("grade"));
					registration.setLaude(result.getInt("laude") > 0);
					registration.setResultRepresentation(result.getString("repr"));
					int record_id = result.getInt("record_id");
					if (record_id != 0)
						registration.setRecordId(record_id);
					registrations.add(registration);
				}
				return registrations;
			}
		} catch (SQLException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}

		return Collections.emptyList();
	}

	public boolean isStudentRegistered(int studentId, int examId) {
		if(dataSrc == null) {
			logger.log(Level.WARNING, DSRC_ERROR);
			return false;
		}

		String query = "SELECT * "
		             + "FROM exam_registration "
		             + "WHERE exam_id = ? "
		             + "AND student_id = ?";

		try (Connection connection = dataSrc.getConnection();
			PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setInt(1, examId);
			statement.setInt(2, studentId);
			try (ResultSet result = statement.executeQuery()) {
				return result.next();
			}
		} catch (SQLException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}

		return false;
	}

	public ExamRegistrationBean getStudentExamRegistration(int studentId, int examId) {
		if(dataSrc == null) {
			logger.log(Level.WARNING, DSRC_ERROR);
			return null;
		}

		String query = "SELECT * "
		             + "FROM exam_registration "
		             + "WHERE exam_id = ? "
		             + "AND student_id = ?";

		ExamRegistrationBean registration = new ExamRegistrationBean();

		try (Connection connection = dataSrc.getConnection();
			PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setInt(1, examId);
			statement.setInt(2, studentId);
			try (ResultSet result = statement.executeQuery()) {
				if(result.next()) {
					registration.setExamId(result.getInt("exam_id"));
					registration.setStudentid(result.getInt("student_id"));
					registration.setStatus(ExamStatus.fromString(result.getString("status")));
					registration.setResult(ExamResult.fromString(result.getString("result")));
					registration.setGrade(result.getInt("grade"));
					registration.setLaude(result.getInt("laude") > 0);
					registration.setResultRepresentation(result.getString("repr"));
					int record_id = result.getInt("record_id");
					if (record_id != 0)
						registration.setRecordId(record_id);
					return registration;
				}
			}
		} catch (SQLException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}

		return null;
	}

	public boolean registerToExam(int studentId, int examId){
		if(dataSrc == null) {
			logger.log(Level.WARNING, DSRC_ERROR);
			return false;
		}

		String query = "INSERT INTO exam_registration "
		             + "(exam_id, student_id, status) "
		             + "VALUES (?, ?, ?)";

		try (Connection connection = dataSrc.getConnection();
			PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setInt(1, examId);
			statement.setInt(2, studentId);
			statement.setString(3, ExamStatus.NINS.toString());
			int rows = statement.executeUpdate();
			if(rows > 0)
				return true;
		} catch (SQLException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
		
		return false;
	}

	public boolean deregisterFromExam(int studentId, int examId){
		if(dataSrc == null) {
			logger.log(Level.WARNING, DSRC_ERROR);
			return false;
		}

		String query = "DELETE FROM exam_registration "
		             + "WHERE exam_id = ? "
		             + "AND student_id = ?";

		try (Connection connection = dataSrc.getConnection();
			PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setInt(1, examId);
			statement.setInt(2, studentId);
			int rows = statement.executeUpdate();
			if(rows > 0)
				return true;
		} catch (SQLException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
		
		return false;
	}

	public boolean rejectStudExam(int studentId, int examId){
		if(dataSrc == null) {
			logger.log(Level.WARNING, DSRC_ERROR);
			return false;
		}
		
		String query = "UPDATE exam_registration "
		             + "SET status = ?, "
		             + "result = ? "
		             + "WHERE exam_id = ? "
		             + "AND student_id = ?";

		try (Connection connection = dataSrc.getConnection();
			PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, ExamStatus.RIF.toString());
			statement.setString(2, ExamResult.RM.toString());
			statement.setInt(3, examId);
			statement.setInt(4, studentId);
			int rows = statement.executeUpdate();
			if(rows > 0)
				return true;
		} catch (SQLException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
		
		return false;
	}
}
