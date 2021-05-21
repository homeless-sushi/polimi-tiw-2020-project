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

import it.polimi.db.business.ExamRecordBean;
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

	public List<ExamRegistrationBean> getExamRegistrationsByExamId(int examId) {
		return getExamRegistrationsByExamId(examId, null);
	}

	public List<ExamRegistrationBean> getExamRegistrationsByExamId(int examId, String orderByColumns){
		if(dataSrc == null) {
			logger.log(Level.WARNING, DSRC_ERROR);
			return Collections.emptyList();
		}

		String query = "SELECT * "
		             + "FROM exam_registration as registration "
		             + "JOIN user_career as career "
		             + "ON registration.student_id = career.id "
		             + "JOIN user "
		             + "ON career.person_code = user.person_code "
		             + "WHERE career.role = 'student' "
		             + "AND registration.exam_id = ?";
		
		if(orderByColumns != null)
			query += " ORDER BY " + orderByColumns;

		try (Connection connection = dataSrc.getConnection();
			PreparedStatement statement = connection.prepareStatement(query)) {
				statement.setInt(1, examId);
				return getExamRegistrations(statement);
			} catch (SQLException e) {
				logger.log(Level.SEVERE, e.getMessage(), e);
			}

		return Collections.emptyList();
	}

	public void fetchRecordRegistrations(List<ExamRecordBean> examRecords){
		fail: {
			if(dataSrc == null) {
				logger.log(Level.WARNING, DSRC_ERROR);
				break fail;
			}
			
			String query = "SELECT * "
			             + "FROM exam_registration as registration "
			             + "JOIN user_career as career "
			             + "ON registration.student_id = career.id "
			             + "JOIN user "
			             + "ON career.person_code = user.person_code "
			             + "WHERE career.role = 'student' "
			             + "AND registration.record_id = ?";
			
			try (Connection connection = dataSrc.getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {
				for(ExamRecordBean examRecord : examRecords) {
					statement.setInt(1, examRecord.getId());
					examRecord.setExamRegistrations(getExamRegistrations(statement));
				}
			} catch (SQLException e) {
				logger.log(Level.SEVERE, e.getMessage(), e);
				break fail;
			}

			return;
		}

		/* FAIL */
		for(ExamRecordBean examRecord : examRecords)
			examRecord.setExamRegistrations(Collections.emptyList());
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

	public ExamRegistrationBean getProfessorExamRegistration(int studentId, int examId) {
		if(dataSrc == null) {
			logger.log(Level.WARNING, DSRC_ERROR);
			return null;
		}

		String query = "SELECT * "
		             + "FROM exam_registration as registration "
		             + "JOIN user_career as career "
		             + "ON registration.student_id = career.id "
		             + "JOIN user "
		             + "ON career.person_code = user.person_code "
		             + "WHERE career.role = 'student' "
		             + "AND registration.exam_id = ? "
		             + "AND registration.student_id = ?";

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

	public boolean publishExamEval(int examId){
		if(dataSrc == null) {
			logger.log(Level.WARNING, DSRC_ERROR);
			return false;
		}

		String query = "UPDATE exam_unrecorded "
		             + "SET status = '" + ExamStatus.PUB + "' "
		             + "WHERE exam_id = ? "
		             + "AND status = '" + ExamStatus.INS + "'";

		try (Connection connection = dataSrc.getConnection();
			PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setInt(1, examId);
			if(statement.executeUpdate() > 0) {
				return true;
			}
		} catch (SQLException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
		
		return false;
	}

	public boolean verbalizeExamEval(int examId){
		if(dataSrc == null) {
			logger.log(Level.WARNING, DSRC_ERROR);
			return false;
		}

		String recordQuery = "INSERT INTO exam_record "
		                   + "(exam_id)"
		                   + "VALUES ( ? )";

		String verbQuery   = "UPDATE exam_unrecorded "
		                   + "SET record_id = last_insert_id(), "
		                   + "status = '" + ExamStatus.VERB + "' "
		                   + "WHERE exam_id = ? "
		                   + "AND status IN ('" + ExamStatus.PUB + "', '" + ExamStatus.RIF + "')";

		try (Connection connection = dataSrc.getConnection();
			PreparedStatement recordStmt = connection.prepareStatement(recordQuery);
			PreparedStatement verbStmt = connection.prepareStatement(verbQuery)){
			connection.setAutoCommit(false);
			try {
				recordStmt.setInt(1, examId);
				if(recordStmt.executeUpdate() < 1)
					throw new SQLException("Couldn't create record");
				
				verbStmt.setInt(1, examId);
				int rows = verbStmt.executeUpdate();
				if(rows < 1)
					throw new SQLException("No published exam to verbalize");
				
				connection.commit();
				return true;
			} catch (SQLException e) {
				connection.rollback();
				throw e;
			}
		} catch (SQLException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
		
		return false;
	}

	public boolean editExamEval(ExamRegistrationBean examRegistration){
		if(dataSrc == null) {
			logger.log(Level.WARNING, DSRC_ERROR);
			return false;
		}

		String query = "UPDATE exam_unrecorded "
		             + "SET status = ?, "
		             + "result = ?, "
		             + "grade = ?, "
		             + "laude = ? "
		             + "WHERE exam_id = ? "
		             + "AND student_id = ?";

		try (Connection connection = dataSrc.getConnection();
			PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, examRegistration.getStatus().toString());
			statement.setString(2, examRegistration.getResult().toString());
			statement.setInt(3, examRegistration.getGrade());
			statement.setInt(4, (examRegistration.getLaude()) ? 1 : 0);
			statement.setInt(5, examRegistration.getExamId());
			statement.setInt(6, examRegistration.getStudentId());
			if(statement.executeUpdate() > 0) {
				return true;
			}
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
		try {
			registration.setCareer(CareerDAO.createCareerBean(rs));
		} catch (Exception ignore) { /* No recursive career */ }
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
