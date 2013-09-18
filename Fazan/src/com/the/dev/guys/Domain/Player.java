package com.the.dev.guys.Domain;

public class Player implements Comparable<Player>{
	private String name;
	private int score;
	public Player(String text,int value){
	this.name=text;
	this.score=value;
	}

////////////////////////////////////////////////////////////

	public Player(){
		
	}

////////////////////////////////////////////////////////////

	public void set_name(String nume){
		this.name=nume;
	}
	
////////////////////////////////////////////////////////////
	
	public void set_score(int n){
		this.score=n;
	}

////////////////////////////////////////////////////////////

	public String get_name(){
		return this.name;
	}

////////////////////////////////////////////////////////////
	
	public int get_score(){
		return this.score;
	}
	
////////////////////////////////////////////////////////////
	
	public String get_string(){
		String result;
		result=this.name+";"+Integer.toString(this.score) +"\n";
		return result;
	}
	
////////////////////////////////////////////////////////////
	
	public int compareTo(Player compare) {
		int compareQuantity = ((Player) compare).get_score(); 
 
		//ascending order
		//return this.score - compareQuantity;
 
		//descending order
		return compareQuantity - this.score;
 
	}
}
