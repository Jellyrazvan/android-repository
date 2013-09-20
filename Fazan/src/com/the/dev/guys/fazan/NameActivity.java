package com.the.dev.guys.fazan;


import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;


public class NameActivity extends SherlockActivity {
	private final static String KEY_NAME = "name";
	
	public final static String EXTRA_MESSAGE = "com.the.dev.guys.Fazan.Message";
	
	private String mMessage;
	private TextView mScoreTextView;
	private TextView mNameTextView;
	private Button mNameButton;
	private EditText mNameEditText;
	
	
/////////////////////////////////////////////////////////////////////////
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_name);
		
		ActionBar bar = getSupportActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.yellow)));
		// Show the Up button in the action bar.
		//setupActionBar();
		bar.setDisplayHomeAsUpEnabled(true);
		
		Typeface cartonSlabFont = Typeface.createFromAsset(getAssets(), "fonts/Carton-Slab.otf");
		
		Intent intent = getIntent();
		mMessage = intent.getStringExtra(EXTRA_MESSAGE);
		mScoreTextView = (TextView) findViewById(R.id.score_textView);
		mScoreTextView.setText(mMessage);
		mScoreTextView.setTypeface(cartonSlabFont);
		
		mNameTextView = (TextView) findViewById(R.id.name_textView); 
		mNameTextView.setTypeface(cartonSlabFont);
		
		mNameButton = (Button) findViewById(R.id.name_button);  
		mNameButton.setTypeface(cartonSlabFont);
		
		mNameEditText = (EditText) findViewById(R.id.name_editText);
		if (savedInstanceState != null){
			mNameEditText.setText(savedInstanceState.getString(KEY_NAME));
		}
		mNameEditText.setInputType(InputType.TYPE_CLASS_TEXT + InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
		mNameEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
				@Override
				public boolean onEditorAction(TextView arg0, int actionId, KeyEvent key) {
					if (actionId == EditorInfo.IME_ACTION_DONE){
						//Log.d("MyLogs", "enter pressed");
						SendName(getCurrentFocus());
					}
					return true;
				}
			});
	}
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState){
		super.onSaveInstanceState(savedInstanceState);
		String name = mNameEditText.getText().toString();
		savedInstanceState.putString(KEY_NAME, name);
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
		getSupportMenuInflater().inflate(R.menu.name, menu);
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
			//NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
/////////////////////////////////////////////////////////////////////////
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {}
		return true;
	}
	
/////////////////////////////////////////////////////////////////////////
	
	public void SendName(View v){
		String nume = mNameEditText.getText().toString();
		//Log.d("QuizActivity", nume);
		if (nume.equals("")){
			nume = "Unknown";
			//Log.d("QuizActivity", "in if");
		}
		Intent intent2 = new Intent(this, HighscoreActivity.class);
		mMessage=mMessage + ";" + nume;
		intent2.putExtra(EXTRA_MESSAGE, mMessage);
		startActivity(intent2);
	}
}
