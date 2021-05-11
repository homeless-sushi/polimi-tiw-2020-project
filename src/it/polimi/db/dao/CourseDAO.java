package it.polimi.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;
import java.time.MonthDay;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Collections;

import javax.sql.DataSource;

import it.polimi.db.business.CourseBean;

public class CourseDAO {
	private DataSource dataSrc;
	private static final String DSRC_ERROR = "DataSource not present";
	private static final MonthDay ACADEMIC_YEAR_BEGIN = MonthDay.of(Month.AUGUST, 1);
	private static final Logger logger = Logger.getLogger(CourseDAO.class.getName());
	
	public CourseDAO(DataSource courseDataSource) {
		this.dataSrc = courseDataSource;
		if(dataSrc == null)
			logger.log(Level.SEVERE, DSRC_ERROR);
	}
	
	public List<CourseBean> getProfessorCourses(int careerId, int year){
		if(dataSrc == null) {
			logger.log(Level.WARNING, DSRC_ERROR);
			return Collections.emptyList();
		}
		
		String query = "SELECT * "
		             + "FROM course_full as course "
		             + "WHERE professor_id = ? "
		             + "AND year = ? "
		             + "ORDER BY name";
		
		try (Connection connection = dataSrc.getConnection();
			PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setInt(1, careerId);
			statement.setInt(2, year);
			return getCourses(statement);
		} catch (SQLException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}

		return Collections.emptyList();
	}

	public List<CourseBean> getStudentCourses(int careerId, int year){
		if(dataSrc == null) {
			logger.log(Level.WARNING, DSRC_ERROR);
			return Collections.emptyList();
		}
		
		String query = "SELECT course.* "
		             + "FROM course_full as course "
		             + "JOIN attend on (course.id, course.year) = (attend.course_id, attend.year) "
		             + "WHERE attend.student_id = ? "
		             + "AND course.year = ?";
		
		try (Connection connection = dataSrc.getConnection();
			PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setInt(1, careerId);
			statement.setInt(2, year);
			return getCourses(statement);
		} catch (SQLException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}

		return Collections.emptyList();
	}

	public CourseBean getCourseFromExam(int examId){
		if(dataSrc == null) {
			logger.log(Level.WARNING, DSRC_ERROR);
			return null;
		}
		
		String query = "SELECT course.* "
		             + "FROM course_full as course "
		             + "JOIN exam "
		             + "ON (course.id, course.year) = (exam.course_id, exam.year) "
		             + "WHERE exam.id = ?";
		
		try (Connection connection = dataSrc.getConnection();
			PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setInt(1, examId);
			return getCourse(statement);
		} catch (SQLException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}

		return null;
	}

	public static int getAcademicYear(LocalDate date){
		int academicYear = date.getYear();

		if(MonthDay.from(date).isBefore(ACADEMIC_YEAR_BEGIN))
			academicYear--;
		return academicYear;
	}

	public static int getAcademicYear() {
		return getAcademicYear(LocalDate.now());
	}

	public static CourseBean createCourseBean(ResultSet rs) throws SQLException {
		CourseBean course = new CourseBean();
		course.setId(rs.getInt("course.id"));
		course.setName(rs.getString("course.name"));
		course.setCfu(rs.getInt("course.cfu"));
		course.setSemester(rs.getString("course.semester"));
		course.setYear(rs.getInt("course.year"));
		course.setProfessorId(rs.getInt("course.professor_id"));
		return course;
	}

	private CourseBean getCourse(PreparedStatement ps) throws SQLException {
		try (ResultSet result = ps.executeQuery()) {
			if(!result.next())
				return null;
			return createCourseBean(result);
		}
	}

	private List<CourseBean> getCourses(PreparedStatement ps) throws SQLException {
		try (ResultSet result = ps.executeQuery()) {
			List<CourseBean> courses = new ArrayList<>();
			while(result.next())
				courses.add(createCourseBean(result));
			return courses;
		}
	}
}
