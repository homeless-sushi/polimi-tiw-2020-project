package it.polimi.db.business;

public enum ExamStatus {
	
	NINS ("NINS"),
	INS ("INS"),
	PUB ("PUB"),
	RIF ("RIF"),
	VERB ("VERB");

	private String string;

	private ExamStatus(String status){
		this.string = status;
	}

	@Override
	public String toString(){ return this.string; }

	static public ExamStatus fromString(String status){
		for(ExamStatus examStatus : ExamStatus.values()) {
			if(examStatus.string.equalsIgnoreCase(status)){
				return examStatus;
			}
		}
		return null;
	}
}
