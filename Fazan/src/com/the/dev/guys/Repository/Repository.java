package com.the.dev.guys.Repository;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.the.dev.guys.Domain.Player;
import com.the.dev.guys.Domain.Word;
import com.the.dev.guys.fazan.HighScoresHandler;
import com.the.dev.guys.fazan.MainActivity;


public class Repository {
	private static final String HIGHSCORES_FILENAME = "highscores.txt";
	
	public static final String DIACRITICE_FILENAME = "words_ro_v2_diacritice.txt";
	public static final String FARA_DIACRITICE_FILENAME = "words_ro_v2.txt";
	
	private static Repository sRepository;
	
	private Vector<Word> mWordsVector;
	private Vector<Player> mHighScoresVector;
	private Context mAppContext;
	private Context mProgressDialogContext;
	private ProgressDialog mProgressDialog;
	
	private boolean diacritice;
	private boolean sunet;
	
//////////////////////////////////////////////////////////////////////////////////////
	
	public void setProgressDialogContext(Context dialogContext) {
		mProgressDialogContext = dialogContext;
	}
	
	public class LoadHighScoresThread extends AsyncTask<Void, Void, Vector<Player>> {
		
		@Override
		protected void onPreExecute() {
			mProgressDialog = ProgressDialog.show(mProgressDialogContext, "", "Please wait");
		}
		
		@Override
		protected Vector<Player> doInBackground(Void... voids){
			HighScoresHandler highscoresHandler = new HighScoresHandler();
			try {
				Vector<Player> array = highscoresHandler
						.getPlayersFromServer("http://thedevguys.us.to/users.json");
						//.getPlayersFromServer("http://192.168.1.181:3000/users.json");
				/*try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
				return array;
			} catch (JSONException e) {
				//Log.d("plm", "JSONEXCEPTION" + e);
				e.printStackTrace();
			} catch (IOException e) {
				//Log.d("plm", "IOEXCEPTION" + e);
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Vector<Player> result) {
			mProgressDialog.dismiss();
		}

	}
	
////////////////////////////////////////////////////////////
	
public class AddHighScoresThread extends AsyncTask<String, Void, String> {
		
		@Override
		protected String doInBackground(String... strings){
			HttpClient httpclient = new DefaultHttpClient();
		    HttpPost httppost = new HttpPost("http://thedevguys.us.to/users/");
			//HttpPost httppost = new HttpPost("http://192.168.1.181:3000/users/");

		    try {
		        // Add your data
		        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		        nameValuePairs.add(new BasicNameValuePair("name", strings[0]));
		        nameValuePairs.add(new BasicNameValuePair("score", strings[1]));
		        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		        // Execute HTTP Post Request
		        HttpResponse response = httpclient.execute(httppost);
		        return response.toString();
		    } catch (ClientProtocolException e) {
		        // TODO Auto-generated catch block
		    } catch (IOException e) {
		        // TODO Auto-generated catch block
		    }
		    return null;
		}

	}
	
////////////////////////////////////////////////////////////
	
	public static String getWithoutDiacritics(String string) {
		string = string.replace("Ã", "A")
				.replace("Î", "I")
				.replace("ª", "S")
				.replace("Þ", "T")
				.replace("Â", "A");
		Log.d("plm", string);
		return string;
	}
	
////////////////////////////////////////////////////////////

	public void add(Word cuvant){
		mWordsVector.add(cuvant);
	}
	
////////////////////////////////////////////////////////////

	private Repository(Context context){
		mAppContext = context;
		mWordsVector = new Vector<Word>(73000,1000);
	}
	
//////////////////////////////////////////////////////////////
	
	public static Repository getRepository(Context context){
		if (sRepository == null){
			sRepository = new Repository(context);
		}
		
		return sRepository;
	}
	
////////////////////////////////////////////////////////////

	public void loadWordsFromFile(String filename) throws IOException {
		
		mWordsVector.clear();
		BufferedReader br = new BufferedReader(
				new InputStreamReader(mAppContext.getAssets().open(filename)));
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
	
/////////////////////////////////////////////////////////////////////
	
	public boolean hasNext(String value){
		String valueEnd = value.substring(value.length() - 2, value.length());
		for (Word el: mWordsVector){
			String text = el.get_text().substring(0, 2);
			if (((text.equals(valueEnd)) && (el.get_verify() == false))){
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
	
//////////////////////////////////////////////////////////////////////
	
	public void loadHighScoresFromServer(Context context) {
		mProgressDialogContext = context;
		LoadHighScoresThread bg = new LoadHighScoresThread();
		bg.execute();
		try {
			try {
				mHighScoresVector = bg.get(3, TimeUnit.SECONDS);
			} catch (TimeoutException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (InterruptedException e) {
			//Log.d("plm", "IntrreputedException" + e);
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			//Log.d("plm", "ExecutionEXCEPTION" + e);
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
////////////////////////////////////////////////////////////
	
	public void removePlayerFromHighscores(Player player){
		mHighScoresVector.remove(player);
	}
	
////////////////////////////////////////////////////////////

	
	public void addPlayerToHighScores(String text,int value){
		/*Player newPlayer = new Player(text, value);
		if (mHighScoresVector.size() >= 6){
			Player lastPlayer = mHighScoresVector.lastElement();
			if (lastPlayer.get_score() <= newPlayer.get_score()){
				mHighScoresVector.remove(lastPlayer);
				mHighScoresVector.add(newPlayer);
			}
		} else {
			mHighScoresVector.add(newPlayer);
		}
		sortVector();*/
		AddHighScoresThread addThread = new AddHighScoresThread();
		addThread.execute(text, Integer.toString(value));
		//Log.d("plm", "addeds");
		
	}
	
////////////////////////////////////////////////////////////

	private void sortVector(){
		Collections.sort(mHighScoresVector);
	}
	
////////////////////////////////////////////////////////////
	
	public String getRandom(){
		Random generator = new Random();
		String result;
		int index;
		do {
			index = generator.nextInt(mWordsVector.size());
			result = mWordsVector.get(index).get_text();
		} while (!hasNext(result));
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
	
////////////////////////////////////////////////////////////

	public boolean get_sunet(){
		return this.sunet;
	}

////////////////////////////////////////////////////////////

	public boolean get_diacritice(){
		return this.diacritice;
	}

////////////////////////////////////////////////////////////

	public void set_sunet(boolean val){
		this.sunet=val;
	}

////////////////////////////////////////////////////////////

	public void set_diacritice(boolean val){
		this.diacritice=val;
	}

////////////////////////////////////////////////////////////
	
	public boolean is_marked(String text) {
		Word word = new Word(text);
		return mWordsVector.contains(word);
	}
	
	
}