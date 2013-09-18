package com.the.dev.guys.fazan;

import java.io.IOException;
import java.util.Locale;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.the.dev.guys.Repository.Repository;
import com.the.dev.guys.fazan.R;

public class PlayActivity extends Activity {
	
	private int mScore;
	private int mWrongAnswers;
	private Repository mRepository;
	private TextView mGivenWordTextView;
	private TextView mScoreTextView;
	private TextView mWrongTextView;
	private EditText mWordEditText;
	private Button mSubmitButton;
	
/////////////////////////////////////////////////////////////////////////

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play);
		
		mScore = 0;
		mWrongAnswers = 0;
		
		ActionBar bar = getActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.green)));
		// Show the Up button in the action bar.
		setupActionBar();
		
		mRepository= Repository.getRepository(getApplicationContext());
		try {
			this.mRepository.loadWordsFromFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Typeface robotoMediumFont = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Medium.ttf"); 
		Typeface robotoThinFont = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Thin.ttf");
		Typeface cartonSlabFont = Typeface.createFromAsset(getAssets(), "fonts/Carton-Slab.otf");
		
		mGivenWordTextView = (TextView) findViewById(R.id.given_word_textView);
		String randomWord = mRepository.getRandom();
		Spannable WordtoSpan = new SpannableString(randomWord);
		WordtoSpan.setSpan(new ForegroundColorSpan(Color.RED), randomWord.length() - 2,
				randomWord.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		mGivenWordTextView.setText(WordtoSpan);
		mGivenWordTextView.setTypeface(robotoMediumFont);
		
		mScoreTextView = (TextView) findViewById(R.id.score_textView);
		String scor = null;
		scor = "Score: " + this.mScore;
		mScoreTextView.setText(scor);
		mScoreTextView.setTypeface(robotoThinFont);
		
		mWrongTextView = (TextView) findViewById(R.id.wrong_textView);
		mWrongTextView.setTypeface(robotoThinFont);
		mWrongTextView.setText("Wrong: 0");
		
		mWordEditText = (EditText) findViewById(R.id.word_editText);
		mWordEditText.setInputType(InputType.TYPE_CLASS_TEXT 
				+ InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
		mWordEditText.setTypeface(robotoMediumFont);
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
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.play, menu);
		return true;
	}

/////////////////////////////////////////////////////////////////////////
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
/////////////////////////////////////////////////////////////////////////
	
public boolean game(View view){
		mWordEditText.setInputType(InputType.TYPE_CLASS_TEXT);
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
		} else {
			mWrongAnswers = mWrongAnswers + 1;
			String randomWord = mRepository.getRandom();
			Spannable WordtoSpan = new SpannableString(randomWord);
			WordtoSpan.setSpan(new ForegroundColorSpan(Color.RED), randomWord.length() - 2, randomWord.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			mGivenWordTextView.setText(WordtoSpan);
		}
		
		mWordEditText.setText("");
		String scor = new String();
		scor = "Score: " + mScore;
		mScoreTextView.setText(scor);
		String b = new String();
		b = Integer.toString(mWrongAnswers);
		mWrongTextView.setText("Wrong: " + b);
		if (mWrongAnswers > 5){
			Intent intent = new Intent(this, NameActivity.class);
			String message = Integer.toString(this.mScore);
			intent.putExtra(NameActivity.EXTRA_MESSAGE, message);
			startActivity(intent);
		}
				
		return true;
	}
}
