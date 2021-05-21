package it.polimi.db.business;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public class ExamRecordBean {
	private int id;
	private int examId;
	private LocalDateTime time;
	private List<ExamRegistrationBean> examRegistrations;

	public int getId(){ return this.id; }
	public void setId(int id) { this.id = id; }
	public int getExamId() { return this.examId; }
	public void setExamId(int examId) { this.examId = examId; }
	public LocalDateTime getTime(){ return this.time; }
	public void setTime(LocalDateTime time) { this.time = time; }
	public void setTime(Timestamp time){ this.time = time.toLocalDateTime(); }
	public List<ExamRegistrationBean> getExamRegistrations() { return examRegistrations; }
	public void setExamRegistrations(List<ExamRegistrationBean> examRegistrations) { this.examRegistrations = examRegistrations; }
}
