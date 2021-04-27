package it.polimi.poliesami.business;

import java.io.Serializable;
import java.util.List;

import it.polimi.db.business.CourseBean;
import it.polimi.db.business.ExamBean;

public class CourseExamsBean implements Serializable {
	private CourseBean course;
	private List<ExamBean> exams;

	public CourseExamsBean(){}
	public CourseExamsBean(CourseBean course, List<ExamBean> exams){
		this.course = course;
		this.exams = exams;
	}

	public void setCourse(CourseBean course) { this.course = course; }
	public CourseBean getCourse() {	return this.course; }
	public void setExams(List<ExamBean> exams) { this.exams = exams; }
	public List<ExamBean> getExams() { return this.exams; }
}
