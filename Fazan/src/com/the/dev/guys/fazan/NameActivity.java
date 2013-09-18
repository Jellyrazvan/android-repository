package com.the.dev.guys.fazan;


import com.the.dev.guys.fazan.R;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class NameActivity extends Activity {
	
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
		
		ActionBar bar = getActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.yellow)));
		// Show the Up button in the action bar.
		setupActionBar();
		
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
		getMenuInflater().inflate(R.menu.name, menu);
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
