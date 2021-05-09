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

import org.javatuples.Triplet;

import it.polimi.db.business.CareerBean;
import it.polimi.db.business.ExamRegistrationBean;
import it.polimi.db.business.ExamResult;
import it.polimi.db.business.ExamStatus;
import it.polimi.db.business.Role;
import it.polimi.db.business.UserBean;

public class ExamRegistrationDAO {
	private DataSource dataSrc;
	private static final String DSRC_ERROR = "DataSource not present";
	private static final Logger logger = Logger.getLogger(ExamRegistrationDAO.class.getName());

	public ExamRegistrationDAO(DataSource courseDataSource) {
		this.dataSrc = courseDataSource;
		if(dataSrc == null)
			logger.log(Level.SEVERE, DSRC_ERROR);
	}

	public List<ExamRegistrationBean> getExamRegistrationsByExamId(int examId) {
		if(dataSrc == null) {
			logger.log(Level.WARNING, DSRC_ERROR);
			return Collections.emptyList();
		}

		String query = "SELECT * "
		             + "FROM exam_registration as registration "
		             + "WHERE exam_id = ?";

		try (Connection connection = dataSrc.getConnection();
			PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setInt(1, examId);
			return getExamRegistrations(statement);
		} catch (SQLException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}

		return Collections.emptyList();
	}
	
	public List<Triplet<UserBean, CareerBean, ExamRegistrationBean>> getStudentCareerExamRegistrations(int examId, String orderBy, boolean descending){
		if(dataSrc == null) {
			logger.log(Level.WARNING, DSRC_ERROR);
			return Collections.emptyList();
		}

		String order = (descending) ? "DESC" : "ASC" ;

		String query = "SELECT * "
		             + "FROM exam_registration "
		             + "JOIN user_career "
		             + "ON exam_registration.student_id = user_career.id "
		             + "JOIN user "
		             + "ON user_career.person_code = user.person_code "
		             + "WHERE user_career.role = 'student' "
		             + "AND exam_registration.exam_id = ? "
		             + "ORDER BY " + orderBy + " " + order;

		List<Triplet<UserBean, CareerBean, ExamRegistrationBean>> studentCareerExamRegistrations = new ArrayList<>();

		try (Connection connection = dataSrc.getConnection();
			PreparedStatement statement = connection.prepareStatement(query)) {
				statement.setInt(1, examId);
				try (ResultSet result = statement.executeQuery()) {
					while(result.next()) {
						UserBean user = new UserBean();
						CareerBean career = new CareerBean();
						ExamRegistrationBean registration = new ExamRegistrationBean();

						user.setPersonCode(result.getString("user.person_code"));
						user.setEmail(result.getString("user.email"));
						user.setHashedPassword(result.getBytes("user.password"));
						user.setName(result.getString("user.name"));
						user.setSurname(result.getString("user.surname"));

						career.setPersonCode(result.getString("user_career.person_code"));
						career.setId(result.getInt("user_career.id"));
						career.setRole(Role.fromString(result.getString("user_career.role")));
						career.setMajor(result.getString("user_career.major"));

						registration.setExamId(result.getInt("exam_registration.exam_id"));
						registration.setStudentId(result.getInt("exam_registration.student_id"));
						registration.setStatus(ExamStatus.valueOf(result.getString("exam_registration.status")));
						registration.setResult(ExamResult.valueOf(result.getString("exam_registration.result")));
						registration.setGrade(result.getInt("exam_registration.grade"));
						registration.setLaude(result.getInt("exam_registration.laude") > 0);
						registration.setResultRepresentation(result.getString("exam_registration.repr"));
						if (result.getInt("exam_registration.record_id") != 0) {
							registration.setRecordId(result.getInt("exam_registration.record_id"));
						}
						
						studentCareerExamRegistrations.add(new Triplet<>(user, career, registration));
					}
					return studentCareerExamRegistrations;
				}
			} catch (SQLException e) {
				logger.log(Level.SEVERE, e.getMessage(), e);
			}

		return Collections.emptyList();
	}

	public ExamRegistrationBean getStudentExamRegistration(int studentId, int examId) {
		if(dataSrc == null) {
			logger.log(Level.WARNING, DSRC_ERROR);
			return null;
		}

		String query = "SELECT * "
		             + "FROM exam_registration_student as registration "
		             + "WHERE exam_id = ? "
		             + "AND student_id = ?";

		try (Connection connection = dataSrc.getConnection();
			PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setInt(1, examId);
			statement.setInt(2, studentId);
			return getExamRegistration(statement);
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

		String query = "INSERT INTO exam_unrecorded "
		             + "(exam_id, student_id) "
		             + "VALUES (?, ?)";

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

	public boolean deregisterFromExam(int studentId, int examId){
		if(dataSrc == null) {
			logger.log(Level.WARNING, DSRC_ERROR);
			return false;
		}

		String query = "DELETE FROM exam_unrecorded "
		             + "WHERE exam_id = ? "
		             + "AND student_id = ? "
		             + "AND status IN ('" + ExamStatus.NINS + "', '" + ExamStatus.INS + "')";

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
		
		String query = "UPDATE exam_unrecorded "
		             + "SET status = ?, "
		             + "result = ? "
		             + "WHERE exam_id = ? "
		             + "AND student_id = ? "
		             + "AND status = ?";

		try (Connection connection = dataSrc.getConnection();
			PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, ExamStatus.RIF.toString());
			statement.setString(2, ExamResult.RM.toString());
			statement.setInt(3, examId);
			statement.setInt(4, studentId);
			statement.setString(5, ExamStatus.PUB.toString());
			int rows = statement.executeUpdate();
			if(rows > 0)
				return true;
		} catch (SQLException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
		
		return false;
	}

	public static ExamRegistrationBean createExamBean(ResultSet rs) throws SQLException {
		ExamRegistrationBean registration = new ExamRegistrationBean();
		registration.setExamId(rs.getInt("registration.exam_id"));
		registration.setStudentId(rs.getInt("registration.student_id"));
		registration.setStatus(rs.getString("registration.status"));
		registration.setResult(rs.getString("registration.result"));
		registration.setGrade(rs.getInt("registration.grade"));
		registration.setLaude(rs.getBoolean("registration.laude"));
		registration.setResultRepresentation(rs.getString("registration.repr"));
		registration.setRecordId(rs.getInt("registration.record_id"));
		return registration;
	}

	private ExamRegistrationBean getExamRegistration(PreparedStatement ps) throws SQLException {
		try (ResultSet result = ps.executeQuery()) {
			if(!result.next())
				return null;
			return createExamBean(result);
		}
	}

	private List<ExamRegistrationBean> getExamRegistrations(PreparedStatement ps) throws SQLException {
		try (ResultSet result = ps.executeQuery()) {
			List<ExamRegistrationBean> exams = new ArrayList<>();
			while(result.next())
				exams.add(createExamBean(result));
			return exams;
		}
	}
}
