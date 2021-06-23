package it.polimi.poliesami.api;

import com.fasterxml.jackson.annotation.JsonValue;

public class NullableData {
	private Object data;

	public NullableData(Object data){
		this.data = data;
	}

	@JsonValue
	public Object getContent(){
		return this.data;
	}
}