package com.the.dev.guys.fazan;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.the.dev.guys.Repository.Repository;

public class PlayActivity extends SherlockActivity {
	private static final String KEY_GIVEN_WORD = "givenword";
	private static final String KEY_WRITTEN_WORD = "writtenword";
	private static final String KEY_SCORE = "score";
	private static final String KEY_WRONG_ANSWERS = "wronganswers";
	private static final String KEY_WORNG_TEXT = "wrongtext";
	private static final String DEXONLINE_ADDRESS = "http://dexonline.ro/definitie/";
	
	private int mScore;
	private int mWrongAnswers;
	private Repository mRepository;
	private TextView mGivenWordTextView;
	private TextView mScoreTextView;
	private TextView mWrongTextView;
	private TextView mBackgroundScoreTextView;
	private EditText mWordEditText;
	private Button mSubmitButton;
	private boolean mToastShown;
	private MediaPlayer mMediaPlayer;
	private EditText mWordEditText2;
	
/////////////////////////////////////////////////////////////////////////

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play);
		if (savedInstanceState == null){
			mScore = 0;
			mWrongAnswers = 0;
			mToastShown = false;
		} else {
			mScore = savedInstanceState.getInt(KEY_SCORE);
			mWrongAnswers = savedInstanceState.getInt(KEY_WRONG_ANSWERS);
		}
		
		mToastShown = false;
		mMediaPlayer = MediaPlayer.create(this,R.raw.click);
		
		ActionBar bar = getSupportActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.red)));
		// Show the Up button in the action bar.
		//setupActionBar();
		bar.setDisplayHomeAsUpEnabled(true);
		
		mRepository = Repository.getRepository(getApplicationContext());
		if (mRepository.getVector().isEmpty()) {
			try {
				if (mRepository.get_diacritice()){
					mRepository.loadWordsFromFile(Repository.DIACRITICE_FILENAME);
				} else {
					mRepository.loadWordsFromFile(Repository.FARA_DIACRITICE_FILENAME);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (savedInstanceState == null){
			mRepository.deverify();
		}
		Typeface robotoMediumFont = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Medium.ttf"); 
		Typeface cartonSlabFont = Typeface.createFromAsset(getAssets(), "fonts/Carton-Slab.otf");
		
		mGivenWordTextView = (TextView) findViewById(R.id.given_word_textView);
		String randomWord;
		if (savedInstanceState == null) {
			randomWord = mRepository.getRandom();
		} else {
			randomWord = savedInstanceState.getString(KEY_GIVEN_WORD);
		}
		Spannable WordtoSpan = new SpannableString(randomWord);
		WordtoSpan.setSpan(new ForegroundColorSpan(Color.RED), randomWord.length() - 2,
				randomWord.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		mGivenWordTextView.setText(WordtoSpan);
		mGivenWordTextView.setTypeface(robotoMediumFont);
		
		mScoreTextView = (TextView) findViewById(R.id.score_textView);
		String scor = null;
		scor = "Score: " + this.mScore;
		mScoreTextView.setText(scor);
		//mScoreTextView.setTypeface(robotoMediumFont);
		
		mBackgroundScoreTextView = (TextView) findViewById(R.id.background_score);
		mBackgroundScoreTextView.setText(Integer.toString(mScore));
		mBackgroundScoreTextView.setTypeface(robotoMediumFont);
		
		mWrongTextView = (TextView) findViewById(R.id.wrong_textView);
		//mWrongTextView.setTypeface(robotoMediumFont);
		//mWrongTextView.setText("Wrong: " + mWrongAnswers);
		if (savedInstanceState != null) {
			mWrongTextView.setText(savedInstanceState.getString(KEY_WORNG_TEXT));
		}
		
		mWordEditText = (EditText) findViewById(R.id.word_editText);
		mWordEditText.setInputType(InputType.TYPE_CLASS_TEXT 
				+ InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
		mWordEditText.setTypeface(robotoMediumFont);
		if (savedInstanceState != null){
			mWordEditText.setText(savedInstanceState.getString(KEY_WRITTEN_WORD));
		}
		mWordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView arg0, int actionId, KeyEvent key) {
				if (actionId == EditorInfo.IME_ACTION_DONE){
					//Log.d("MyLogs", "enter pressed");
					game(getCurrentFocus());
				}
				return true;
			}
		});
		
		mSubmitButton = (Button) findViewById(R.id.submit_button);
		mSubmitButton.setTypeface(cartonSlabFont);
		
	}
	
/////////////////////////////////////////////////////////////////////

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		String givenWord = mGivenWordTextView.getText().toString();
		String writtenWord = mWordEditText.getText().toString();
		savedInstanceState.putString(KEY_GIVEN_WORD, givenWord);
		savedInstanceState.putString(KEY_WRITTEN_WORD, writtenWord);
		savedInstanceState.putInt(KEY_SCORE, mScore);
		savedInstanceState.putInt(KEY_WRONG_ANSWERS, mWrongAnswers);
		savedInstanceState.putString(KEY_WORNG_TEXT, mWrongTextView.getText().toString());
	}
	
/////////////////////////////////////////////////////////////////////////
	
	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

/////////////////////////////////////////////////////////////////////////
	
	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.play, menu);
		return true;
	}

/////////////////////////////////////////////////////////////////////////
	
	@Override
	public boolean onOptionsItemSelected(com.actionbarsherlock.view.MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			if (this.mRepository.get_sunet()){
				mMediaPlayer.start();
			}
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.bulb:
			postSuggestion(getCurrentFocus());
			return true;
		case R.id.definition_button:
			if (this.mRepository.get_sunet()){
				mMediaPlayer.start();
			}
			getDefinition(getCurrentFocus());
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
//////////////////////////////////////////////////////////////////////////
	
	public void onResume() {
		super.onResume();
		mMediaPlayer = MediaPlayer.create(this,R.raw.click);
	}
	
//////////////////////////////////////////////////////////////////////////
	
	public void onStop() {
		super.onStop();
		mMediaPlayer.release();
		mMediaPlayer = null;
	}
	
/////////////////////////////////////////////////////////////////////////
	
	public boolean game(View view){
		Toast toast = Toast.makeText(getApplicationContext(), "Cuvântul a fost deja folosit", Toast.LENGTH_LONG);
		if (this.mRepository.get_sunet()){
			mMediaPlayer.start();
		}
		String text = mWordEditText.getText().toString().toLowerCase(Locale.getDefault());
		if (this.mRepository.find(text)){
			String word = mRepository.findBy(text.substring(text.length()-2, text.length()));
			if (word.equals("0")){
				mScore = mScore + 10;
				String randomWord = mRepository.getRandom();
				Spannable WordtoSpan = new SpannableString(randomWord);
				WordtoSpan.setSpan(new ForegroundColorSpan(Color.RED), randomWord.length() - 2, randomWord.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				mGivenWordTextView.setText(WordtoSpan);
			} else {
				Spannable WordtoSpan = new SpannableString(word);
				WordtoSpan.setSpan(new ForegroundColorSpan(Color.RED), word.length() - 2, word.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				mGivenWordTextView.setText(WordtoSpan);
				mGivenWordTextView.setText(WordtoSpan);
				mScore = mScore + 1;
			}
			mWordEditText.setText("");
			mToastShown = false;
		} else {
			if (mRepository.is_marked(text)) {
				if (!mToastShown) {
					toast.show();
					mToastShown = true;
				}
				mWordEditText.setText(text.toUpperCase(Locale.getDefault()));
			} else {
				mWrongAnswers = mWrongAnswers + 1;
				String randomWord = mRepository.getRandom();
				Spannable WordtoSpan = new SpannableString(randomWord);
				WordtoSpan.setSpan(new ForegroundColorSpan(Color.RED), randomWord.length() - 2, randomWord.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				mGivenWordTextView.setText(WordtoSpan);
				mWordEditText.setText("");
				mToastShown = false;
			}
		}
		
		String scor = new String();
		scor = "Score: " + mScore;
		mScoreTextView.setText(scor);
		mBackgroundScoreTextView.setText(Integer.toString(mScore));
		switch (mWrongAnswers) {
		case 1:
			mWrongTextView.setText("F");
			break;
		case 2:
			mWrongTextView.setText("FA");
			break;
		case 3:
			mWrongTextView.setText("FAZ");
			break;
		case 4:
			mWrongTextView.setText("FAZA");
			break;
		case 5:
			mWrongTextView.setText("FAZAN");
			break;
		}
		if (mWrongAnswers == 5){
			Intent intent = new Intent(this, NameActivity.class);
			String message = Integer.toString(this.mScore);
			intent.putExtra(NameActivity.EXTRA_MESSAGE, message);
			startActivity(intent);
		}
				
		return true;
	}

/////////////////////////////////////////////////////////////////////////////

	public void getDefinition(View view) {
		String address = DEXONLINE_ADDRESS + Repository.getWithoutDiacritics(
				mGivenWordTextView.getText().toString());
		BackgroundThread backThread = new BackgroundThread();
		backThread.execute(address);
		//backThread.execute("");
		
	}
	
	public class BackgroundThread extends AsyncTask<String, Void, String> {
		
		private ProgressDialog mProgressDialog;
		
		@Override
		protected void onPreExecute() {
			mProgressDialog = ProgressDialog.show(PlayActivity.this, "", "Please wait");
		}
		
		@Override
		protected String doInBackground(String... strings){
			DefinitionFetcher def = new DefinitionFetcher();
			String items = def.downloadDefinition(strings[0]);
			return items;
		}
		
		@Override
		protected void onPostExecute(String result) {
			mProgressDialog.dismiss();
			if (!result.equals("")) {
				//new AlertDialog.Builder(PlayActivity.this).setTitle("Definiţie")
				//		.setMessage(result)
				//		.setNeutralButton("OK", null).show();
				CustomDialog cd = new CustomDialog(PlayActivity.this, result);
				cd.show();
			} else {
				//new AlertDialog.Builder(PlayActivity.this).setTitle("Eroare")
				//		.setMessage("Eroare de conexiune!")
				//		.setNeutralButton("OK", null).show();
				CustomDialog cd = new CustomDialog(PlayActivity.this, "Eroare de conexiune!");
				cd.show();
			}
		}

	}
	
	public void postSuggestion(View view) {
		if (this.mRepository.get_sunet()){
			mMediaPlayer.start();
		}
		//Log.d("plm","suggestion");
		SuggestionDialog cd = new SuggestionDialog(this);
		//mWordEditText2 = (EditText) cd.findViewById(R.id.suggestion_edit_text);
		
		//Button dialog_btn = (Button) cd.findViewById(R.id.send_button);
		/*dialog_btn.setOnClickListener(new View.OnClickListener() 
		{

			@Override
			public void onClick(View v) {
				send();
				
			}
		    // Perform button logic
		});*/
		cd.show();
		
		
	}
	public void send(){
		AddWordThread add=new AddWordThread();
		//Log.d("plm","word12121s");
		add.execute(mWordEditText2.getText().toString());
		//Log.d("plm","words");
	}
public class AddWordThread extends AsyncTask<String, Void, String> {
		
		@Override
		protected String doInBackground(String... strings){
			HttpClient httpclient = new DefaultHttpClient();
		    HttpPost httppost = new HttpPost("http://thedevguys.us.to/words/");

		    try {
		        // Add your data
		        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		        nameValuePairs.add(new BasicNameValuePair("text", strings[0]));
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
	
}
