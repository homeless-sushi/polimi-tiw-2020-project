package it.polimi.db.business;

import java.io.Serializable;

public class CourseBean implements Serializable {
	private int id;
	private String name;
	private String semester;
	private int cfu;
	private int year;
	private int professorId;
	
	public int getId() { return id;	}
	public void setId(int id) { this.id = id; }
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	public String getSemester() { return semester; }
	public void setSemester(String semester) { this.semester = semester; }
	public int getCfu() { return cfu; }
	public void setCfu(int cfu) { this.cfu = cfu; }
	public int getYear() { return year; }
	public void setYear(int year) { this.year = year; }
	public int getProfessorId() { return professorId; }
	public void setProfessorId(int professorId) { this.professorId = professorId; }
}
