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

import it.polimi.db.business.CourseBean;
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
	
	public ExamBean getExamById(int examId){
		if(dataSrc == null) {
			logger.log(Level.WARNING, DSRC_ERROR);
			return null;
		}
		
		String query = "SELECT * "
		             + "FROM exam "
		             + "JOIN course_full AS course "
		             + "ON (exam.course_id, exam.year) = (course.id, course.year) "
		             + "WHERE exam.id = ? ";
		
		try (Connection connection = dataSrc.getConnection();
			PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setInt(1, examId);
			return getExam(statement);
		} catch (SQLException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}

		return null;
	}

	public ExamBean getProfessorExamById(int examId, int professorId){
		if(dataSrc == null) {
			logger.log(Level.WARNING, DSRC_ERROR);
			return null;
		}
		
		String query = "SELECT * "
		             + "FROM exam "
		             + "JOIN course_full AS course "
		             + "ON (exam.course_id, exam.year) = (course.id, course.year) "
		             + "WHERE exam.id = ? "
		             + "AND course.professor_id = ?";
		
		try (Connection connection = dataSrc.getConnection();
			PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setInt(1, examId);
			statement.setInt(2, professorId);
			return getExam(statement);
		} catch (SQLException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}

		return null;
	}

	public void fetchCourseExams(List<CourseBean> courses){
		fail: {
			if(dataSrc == null) {
				logger.log(Level.WARNING, DSRC_ERROR);
				break fail;
			}
			
			String query = "SELECT * "
			             + "FROM exam "
			             + "WHERE course_id = ? "
			             + "AND year = ? "
			             + "ORDER BY date DESC";
			
			try (Connection connection = dataSrc.getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {
				for(CourseBean course : courses) {
					statement.setInt(1, course.getId());
					statement.setInt(2, course.getYear());
					course.setExams(getExams(statement));
				}
			} catch (SQLException e) {
				logger.log(Level.SEVERE, e.getMessage(), e);
				break fail;
			}

			return;
		}

		/* FAIL */
		for(CourseBean course : courses)
			course.setExams(Collections.emptyList());
	}

	public boolean isExamCourseAttendee(int studentId, int examId) {
		if(dataSrc == null) {
			logger.log(Level.WARNING, DSRC_ERROR);
			return false;
		}

		String query = "SELECT 1 "
		             + "FROM exam "
		             + "JOIN attend "
		             + "ON (exam.course_id, exam.year) = (attend.course_id, attend.year) "
		             + "WHERE attend.student_id = ? "
		             + "AND exam.id = ?";

		try (Connection connection = dataSrc.getConnection();
			PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setInt(1, studentId);
			statement.setInt(2, examId);
			statement.executeQuery();
			try (ResultSet result = statement.executeQuery()) {
				return result.next();
			}
		} catch (SQLException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
		
		return false;
	}

	public static ExamBean createExamBean(ResultSet rs) throws SQLException {
		ExamBean exam = new ExamBean();
		exam.setId(rs.getInt("exam.id"));
		exam.setCourseId(rs.getInt("exam.course_id"));
		exam.setYear(rs.getInt("exam.year"));
		exam.setDate(rs.getDate("exam.date"));
		try {
			exam.setCourse(CourseDAO.createCourseBean(rs));
		} catch (Exception ignore) { /* No recursive course */ }
		return exam;
	}

	private ExamBean getExam(PreparedStatement ps) throws SQLException {
		try (ResultSet result = ps.executeQuery()) {
			if(!result.next())
				return null;
			return createExamBean(result);
		}
	}

	private List<ExamBean> getExams(PreparedStatement ps) throws SQLException {
		try (ResultSet result = ps.executeQuery()) {
			List<ExamBean> exams = new ArrayList<>();
			while(result.next())
				exams.add(createExamBean(result));
			return exams;
		}
	}
}
