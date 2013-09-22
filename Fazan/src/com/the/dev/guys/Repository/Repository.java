package com.the.dev.guys.Repository;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Collections;
import java.util.Locale;
import java.util.Random;
import java.util.Vector;

import android.content.Context;

import com.the.dev.guys.Domain.Player;
import com.the.dev.guys.Domain.Word;


public class Repository {
	private static final String HIGHSCORES_FILENAME = "highscores.txt";
	
	private static Repository sRepository;
	
	private Vector<Word> mWordsVector;
	private Vector<Player> mHighScoresVector;
	private Context mAppContext;
	
	
////////////////////////////////////////////////////////////

	public void add(Word cuvant){
		mWordsVector.add(cuvant);
	}
	
////////////////////////////////////////////////////////////

	private Repository(Context context){
		mAppContext = context;
		mWordsVector = new Vector<Word>(73000,1000);
		mHighScoresVector = new Vector<Player>(6,1);
	}
	
//////////////////////////////////////////////////////////////
	
	public static Repository getRepository(Context context){
		if (sRepository == null){
			sRepository = new Repository(context);
		}
		
		return sRepository;
	}
	
////////////////////////////////////////////////////////////

	public void loadWordsFromFile() throws IOException {
		
		BufferedReader br = new BufferedReader(
				new InputStreamReader(mAppContext.getAssets().open("words_ro_v2.txt")));
		try {
			String line = br.readLine();
			while (line != null){
				Word cuvant = new Word(line.toLowerCase(Locale.getDefault()));
				this.mWordsVector.add(cuvant);
				line = br.readLine();
			}
		} catch ( Exception err) {
			//Log.e("There was an error reading the file", err.toString());
		} finally {
			br.close();
		}
	}
	
////////////////////////////////////////////////////////////
	
	public String findBy(String value){
		String result = "0";
		Vector<Word> matchingWords = new Vector<Word>();
		for (Word word: mWordsVector){
			String text = word.get_text().substring(0, 2);
			if ((text.equals(value)) && (word.get_verify() == false)){
				matchingWords.add(word);
			}
		}
		if (!matchingWords.isEmpty()){
			Random generator = new Random();
			int index = generator.nextInt(matchingWords.size());
			matchingWords.get(index).set_verify(true);
			return matchingWords.get(index).get_text().toUpperCase(Locale.getDefault());
		}
		return result;
	}
	
////////////////////////////////////////////////////////////
	
	public boolean find(String value){
		for (Word el: mWordsVector){
			String text = el.get_text();
			if (((text.equals(value)) && (el.get_verify() == false))){
				el.set_verify(true);
				return true;
			}
		}
		return false;
	}
	
////////////////////////////////////////////////////////////

	
	public Vector<Word> getVector(){
		return mWordsVector;
	}
	
////////////////////////////////////////////////////////////

	
	
	public Vector<Player> getVectorHighScores(){
		return mHighScoresVector;
	}
	
////////////////////////////////////////////////////////////
	
	public void removePlayerFromHighscores(Player player){
		mHighScoresVector.remove(player);
	}
	
////////////////////////////////////////////////////////////

	
	public void addPlayerToHighScores(String text,int value){
		Player newPlayer = new Player(text, value);
		if (mHighScoresVector.size() >= 6){
			Player lastPlayer = mHighScoresVector.lastElement();
			if (lastPlayer.get_score() <= newPlayer.get_score()){
				mHighScoresVector.remove(lastPlayer);
				mHighScoresVector.add(newPlayer);
			}
		} else {
			mHighScoresVector.add(newPlayer);
		}
		sortVector();
		
	}
	
////////////////////////////////////////////////////////////

	private void sortVector(){
		Collections.sort(mHighScoresVector);
	}
	
////////////////////////////////////////////////////////////
	
	public String getRandom(){
		Random generator = new Random();
		int index = generator.nextInt(mWordsVector.size());
		String result = mWordsVector.get(index).get_text();
		mWordsVector.get(index).set_verify(true);
		return result.toUpperCase(Locale.getDefault());
	}
	
////////////////////////////////////////////////////////////
	
	public void writeHighscoresToFile() throws IOException {
		File file = new File(mAppContext.getFilesDir().toString() + "/" + HIGHSCORES_FILENAME);
		if(!(file.exists())){
			try {
				file.createNewFile();
			} catch (Exception e) {
				e.fillInStackTrace();
				return;
			}
		}
		
		try {
			FileOutputStream outputStream = new FileOutputStream(file, false); //True = Append to file, false = Overwrite
			PrintStream printStream = new PrintStream(outputStream);
			for(Player el: this.mHighScoresVector){
				printStream.print(el.get_string());
			}
		
			printStream.close();
			outputStream.close();
		}catch (FileNotFoundException e) {
			//Catch the exception
		} catch (IOException e) {
			//Catch the exception
		}
	}
		
////////////////////////////////////////////////////////////
		 
	public void readHighscoresFromFile()  throws IOException  {
		mHighScoresVector.clear();
		
		BufferedReader br = new BufferedReader(new FileReader(
				mAppContext.getFilesDir().toString() + "/" + HIGHSCORES_FILENAME));
		try{
			String line = br.readLine();
			while (line != null){
				try {
					String lista[] = line.split(";");
					String nume = lista[0];
					int value = Integer.parseInt(lista[1]);
					Player play = new Player(nume,value);
					//Log.d("MyLogs", line);
					this.mHighScoresVector.add(play);
				} catch (Exception e) {
					e.fillInStackTrace();
					//Log.d("MyLogs", "bla nu mere");
				}finally {
					line = br.readLine();
				}
			}
		}catch( Exception err) {
			//Log.e("There was an error reading the file", err.toString());
		}finally {
			br.close();
		}
	}

/////////////////////////////////////////////////////////////////////

	public void deverify() {
		for (Word word: mWordsVector){
			if (word.get_verify()){
				word.set_verify(false);
			}
		}
	}
}