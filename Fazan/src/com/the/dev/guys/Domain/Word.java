package com.the.dev.guys.Domain;


public class Word {
	
	private String text;
	private boolean verify;
	
/////////////////////////////////////////////////
	
	public Word(String text){
		this.text=text;
		this.verify=false;
	}
	
/////////////////////////////////////////////////
	
	public String get_text(){
		return this.text;
	}
	
/////////////////////////////////////////////////
	
	public void set_text(String text){
		this.text=text;
	}
	
/////////////////////////////////////////////////

	public boolean get_verify(){
		return this.verify;
	}
	
/////////////////////////////////////////////////
	
	public void set_verify(boolean value){
		this.verify=value;
	}
	
	@Override
	public boolean equals(Object other){
		if (other == null) return false;
		if (other == this) return true;
		if (!(other instanceof Word)) return false;
		Word otherWord = (Word) other;
		if (this.get_text().equals(otherWord.get_text()) &&
				otherWord.get_verify() == true) {
			return true;
		}
		return false;
	}
	
}
