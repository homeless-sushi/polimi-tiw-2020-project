package it.polimi.db.business;

public class ExamRegistrationBean {
	private int studentId;
	private int examId;
	private ExamStatus status;
	private ExamResult result;
	private int grade;
	private boolean laude;
	private String resultRepresentation;
	private int recordId;

	public int getStudentId() { return this.studentId; }
	public void setStudentid(int studentId) { this.studentId = studentId; }
	public int getExamId() { return this.examId; }
	public void setExamId(int examId) { this.examId = examId; }
	public ExamStatus getStatus() { return this.status; }
	public void setStatus(ExamStatus status) { this.status = status; }
	public ExamResult getResult() { return this.result; }
	public void setResult(ExamResult result) { this.result = result; }
	public int getGrade() { return this.grade; }
	public void setGrade(int grade) { this.grade = grade; }
	public boolean getLaude() { return this.laude; }
	public void setLaude(boolean laude) { this.laude = laude; }
	public String getResultRepresentation() { return this.resultRepresentation; }
	public void setResultRepresentation(String resultRepresentation) { this.resultRepresentation = resultRepresentation; }
	public int getRecordId() { return this.recordId; }
	public void setRecordId(int recordId) { this.recordId = recordId; }

}