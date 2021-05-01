package it.polimi.db.business;

public enum ExamResult {
	
	VUOTO ("VUOTO"),
	ASS ("ASS"),
	RM ("RM"),
	RP ("RP"),
	PASS ("PASS");

	private String string;

	private ExamResult(String result){ this.string = result; }

	@Override
	public String toString() { return this.string; }

	static public ExamResult fromString(String result){
		for(ExamResult examResult : ExamResult.values()){
			if(examResult.string.equalsIgnoreCase(result)){
				return examResult;
			}
		}
		return null;
	}

}
