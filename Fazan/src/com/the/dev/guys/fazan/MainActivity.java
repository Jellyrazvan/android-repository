package com.the.dev.guys.fazan;


import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;


public class MainActivity extends SherlockActivity {
	private static final String FIRST_RUN = "firstrun";
	
	private TextView mWelcomeTextView;
	private Button mPlayButton, mHighscoresButton,
			mQuitButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		
		ActionBar bar = getSupportActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.red)));

		mPlayButton = (Button) findViewById(R.id.play_button);  
		Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Carton-Slab.otf");
		mPlayButton.setTypeface(font);
		
		mHighscoresButton = (Button) findViewById(R.id.highscores_button);  
		mHighscoresButton.setTypeface(font);
		
		mQuitButton = (Button) findViewById(R.id.quit_button);   
		mQuitButton.setTypeface(font);
		
		mWelcomeTextView = (TextView) findViewById(R.id.welcome_textView);
		mWelcomeTextView.setTypeface(font);
		
		boolean firstRun = getSharedPreferences(FIRST_RUN, MODE_PRIVATE)
				.getBoolean("firstRunValue", true);
		if (firstRun){
			new AlertDialog.Builder(this).setTitle("Instrucþiuni")
					.setMessage("Regulile jocului sunt foarte simple, trebuie " +
					"sã faci cât mai multe cuvinte cu ultimele douã litere ale cuvântului anterior! Baftã!")
					.setNeutralButton("OK", null).show();
		}
		
		// Save the state
		getSharedPreferences(FIRST_RUN, MODE_PRIVATE)
				.edit()
				.putBoolean("firstRunValue", false)
				.commit();
	}

/////////////////////////////////////////////////////////////////////////
	
	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
/////////////////////////////////////////////////////////////////////////
	
	public void openPlayActivity(View view){
		Intent intent=new Intent(this, PlayActivity.class);
		startActivity(intent);
	}
	
/////////////////////////////////////////////////////////////////////////
	
	public void CloseApp(View view){
		finish();
		System.exit(0);
	}
	
/////////////////////////////////////////////////////////////////////////
	
	public void openHighScores(View view){
		Intent intent = new Intent(this, HighscoreActivity.class);
		startActivity(intent);
	}
}
