package it.polimi.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Collections;

import javax.sql.DataSource;

import it.polimi.db.business.CourseBean;

public class CourseDAO {
	private DataSource dataSrc;
	private static final String DSRC_ERROR = "DataSource not present";
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
		
		String query = "SELECT course.id, course.name, course.semester, course.cfu, "
		             + "course_details.year, course_details.professor_id "
		             + "FROM course "
		             + "INNER JOIN course_details "
		             + "ON course.id = course_details.course_id "
		             + "WHERE course_details.professor_id = ? "
		             + "AND course_details.year = ? "
		             + "ORDER BY course.name";
			
		List<CourseBean> courses = new ArrayList<>();
		
		try (Connection connection = dataSrc.getConnection();
			 PreparedStatement statement = connection.prepareStatement(query)) {
				statement.setInt(1, careerId);
				statement.setInt(2, year);
				try (ResultSet result = statement.executeQuery()) {
					while(result.next()) {
						CourseBean course = new CourseBean();
						course.setId(result.getInt("course.id"));
						course.setName(result.getString("course.name"));
						course.setCfu(result.getInt("course.cfu"));
						course.setSemester(result.getString("course.semester"));
						course.setYear(result.getInt("course_details.year"));
						course.setProfessorId(result.getInt("course_details.professor_id"));
						courses.add(course);
					}
					return courses;
				}
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
		
		String query = "SELECT course.id, course.name, course.semester, course.cfu, "
		             + "course_details.year, course_details.professor_id "
		             + "FROM course "
		             + "JOIN course_details "
		             + "ON course.id = course_details.course_id "
		             + "JOIN attend "
		             + "ON (attend.course_id = course_details.course_id "
		             + "AND attend.year = course_details.year) "
		             + "WHERE attend.student_id = ? "
		             + "AND attend.year = ?";
			
		List<CourseBean> courses = new ArrayList<>();
		
		try (Connection connection = dataSrc.getConnection();
			 PreparedStatement statement = connection.prepareStatement(query)) {
				statement.setInt(1, careerId);
				statement.setInt(2, year);
				try (ResultSet result = statement.executeQuery()) {
					while(result.next()) {
						CourseBean course = new CourseBean();
						course.setId(result.getInt("course.id"));
						course.setName(result.getString("course.name"));
						course.setCfu(result.getInt("course.cfu"));
						course.setSemester(result.getString("course.semester"));
						course.setYear(result.getInt("course_details.year"));
						course.setProfessorId(result.getInt("course_details.professor_id"));
						courses.add(course);
					}
					return courses;
				}
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
		
		String query = "SELECT course.id, course.name, course.semester, course.cfu, "
		             + "course_details.year, course_details.professor_id " 
		             + "FROM course "
		             + "JOIN course_details "
		             + "ON course.id = course_details.course_id "
		             + "WHERE (course_details.course_id, course_details.year) IN "
		             + "(SELECT exam.course_id, exam.year "
		             + "FROM exam "
		             + "WHERE exam.id = ?)";
			
		CourseBean course = new CourseBean();
		
		try (Connection connection = dataSrc.getConnection();
			 PreparedStatement statement = connection.prepareStatement(query)) {
				statement.setInt(1, examId);
				try (ResultSet result = statement.executeQuery()) {
					while(result.next()) {
						course.setId(result.getInt("course.id"));
						course.setName(result.getString("course.name"));
						course.setCfu(result.getInt("course.cfu"));
						course.setSemester(result.getString("course.semester"));
						course.setYear(result.getInt("course_details.year"));
						course.setProfessorId(result.getInt("course_details.professor_id"));
					}
					return course;
				}
			} catch (SQLException e) {
				logger.log(Level.SEVERE, e.getMessage(), e);
			}

		return null;
	}

	public int getAcademicYear(Date date){
		//the academic year begins on august first
		int beginDay = 1;
		int beginMonth = Calendar.AUGUST;
		
		Calendar calendarDate = Calendar.getInstance();
		calendarDate.setTime(date);
		int dateYear = calendarDate.get(Calendar.YEAR);

		Calendar academicBeginning = Calendar.getInstance();
		academicBeginning.set(dateYear, beginMonth, beginDay, 0, 0, 0);
		Date beginDate = academicBeginning.getTime();

		if(date.after(beginDate)){
			return dateYear;
		}else{
			return dateYear-1;
		}
	}

	public int getAcademicYear() {
		//the academic year begins on august first
		int beginDay = 1;
		int beginMonth = Calendar.AUGUST;

		Calendar calendarNow = Calendar.getInstance();
		int currentYear = calendarNow.get(Calendar.YEAR);
		Date now = calendarNow.getTime();
	
		Calendar academicBeginning = Calendar.getInstance();
		academicBeginning.set(currentYear, beginMonth, beginDay, 0, 0, 0);
		Date beginDate = academicBeginning.getTime();

		if(now.after(beginDate)){
			return academicBeginning.get(Calendar.YEAR);
		}else{
			return academicBeginning.get(Calendar.YEAR)-1;
		}
	}
}
