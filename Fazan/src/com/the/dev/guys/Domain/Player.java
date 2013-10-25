package com.the.dev.guys.Domain;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

public class Player implements Comparable<Player>{
	
	private static final String JSON_NAME = "name";
	private static final String JSON_SCORE = "score";
	private static final String JSON_POSITION = "position";
	
	private String name;
	private int score;
	private int position;

////////////////////////////////////////////////////////////
	
	public Player(JSONObject json) 
			throws JSONException, IOException{
		name = json.getString(JSON_NAME);
		score = json.getInt(JSON_SCORE);
		position = json.getInt(JSON_POSITION);
	}
	
////////////////////////////////////////////////////////////
	
	public int getPosition() {
		return position;
	}

////////////////////////////////////////////////////////////
	
	public void setPosition(int position) {
		this.position = position;
	}
	
////////////////////////////////////////////////////////////

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
