package com.the.dev.guys.fazan;


import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.the.dev.guys.Repository.Repository;


public class MainActivity extends SherlockActivity {
	private static final String FIRST_RUN = "firstrun";
	public static final String SAVED_SETTINGS = "savedsettings"; 
	
	private TextView mWelcomeTextView;
	private Button mPlayButton, mHighscoresButton,
			mQuitButton, mSettingsButton;
	
	private Repository mRepository;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		
		mRepository = Repository.getRepository(getApplicationContext());
		
		ActionBar bar = getSupportActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.red)));
		bar.setDisplayHomeAsUpEnabled(true);

		mPlayButton = (Button) findViewById(R.id.play_button);  
		Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Carton-Slab.otf");
		mPlayButton.setTypeface(font);
		
		mHighscoresButton = (Button) findViewById(R.id.highscores_button);  
		mHighscoresButton.setTypeface(font);
		
		mSettingsButton = (Button) findViewById(R.id.settings_button);
		mSettingsButton.setTypeface(font);
		
		if (getResources().getConfiguration().orientation == 
				getResources().getConfiguration().ORIENTATION_PORTRAIT) {
			mQuitButton = (Button) findViewById(R.id.quit_button);   
			mQuitButton.setTypeface(font);
		}
			
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
		
		boolean soundSetting = getSharedPreferences(SAVED_SETTINGS, MODE_PRIVATE)
				.getBoolean("sound_setting", true);
		boolean diacriticeSetting = getSharedPreferences(SAVED_SETTINGS, MODE_PRIVATE)
				.getBoolean("diacritice_setting", false);
		
		mRepository.set_diacritice(diacriticeSetting);
		mRepository.set_sunet(soundSetting);
		Log.d("plm", "is aci");
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
		if (this.mRepository.get_sunet()){
			final MediaPlayer mp = MediaPlayer.create(this,R.raw.click);
			mp.start();
		}
		Intent intent=new Intent(this, PlayActivity.class);
		startActivity(intent);
	}
	
/////////////////////////////////////////////////////////////////////////
	
	public void CloseApp(View view){
		if (this.mRepository.get_sunet()){
			final MediaPlayer mp = MediaPlayer.create(this,R.raw.click);
			mp.start();
		}
		getSharedPreferences(SAVED_SETTINGS, MODE_PRIVATE)
				.edit()
				.putBoolean("sound_setting", mRepository.get_sunet())
				.putBoolean("diacritice_setting", mRepository.get_diacritice())
				.commit();
		finish();
		System.exit(0);
	}
	
/////////////////////////////////////////////////////////////////////////
	
	public void openHighScores(View view){
		if (this.mRepository.get_sunet()){
			final MediaPlayer mp = MediaPlayer.create(this,R.raw.click);
			 mp.start();
		}
		Intent intent = new Intent(this, HighscoreActivity.class);
		startActivity(intent);
	}

//////////////////////////////////////////////////////////////////////////

	public void openSettingsActivity(View view){
		if (this.mRepository.get_sunet()){
			final MediaPlayer mp = MediaPlayer.create(this,R.raw.click);
			mp.start();
		}
		Intent intent=new Intent(this, OptionActivity.class);
		startActivity(intent);
	}
}