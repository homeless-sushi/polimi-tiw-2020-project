package it.polimi.db.business;

import java.io.Serializable;
import java.util.Date;

public class ExamBean implements Serializable {
	private int id;
	private int courseId;
	private int year;
	private Date date;

	public void setId(int id) { this.id = id; }
	public int getId() { return this.id; }
	public void setCourseId(int courseId) { this.courseId = courseId; }
	public int getCourseId() { return this.courseId; }
	public void setYear(int year) { this.year = year; }
	public int getYear() { return this.year; }
	public void setDate(Date date) { this.date = date; }
	public Date getDate() { return this.date; }
}