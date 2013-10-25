package com.the.dev.guys.fazan;


import java.util.concurrent.ExecutionException;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
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
	private MediaPlayer mMediaPlayer;
	private Button mPlayButton, mHighscoresButton,
			mQuitButton, mSettingsButton;
	
	private Repository mRepository;
	
public class BackgroundThread extends AsyncTask<Void, Void, Repository> {
		
		
		@Override
		protected void onPreExecute() {
		}
		
		@Override
		protected Repository doInBackground(Void... voids){
			return Repository.getRepository(getApplicationContext());
		}
		
		@Override
		protected void onPostExecute(Repository result) {
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		
		BackgroundThread backgroundThread = new BackgroundThread();
		backgroundThread.execute();
		try {
			mRepository = backgroundThread.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		mMediaPlayer = MediaPlayer.create(this,R.raw.click);
		
		ActionBar bar = getSupportActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.red)));
		bar.setDisplayHomeAsUpEnabled(true);

		mPlayButton = (Button) findViewById(R.id.play_button);  
		Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Carton-Slab.otf");
		mPlayButton.setTypeface(font);
		
		mHighscoresButton = (Button) findViewById(R.id.highscores_button);  
		mHighscoresButton.setTypeface(font);
		
		//mSettingsButton = (Button) findViewById(R.id.settings_button);
		//mSettingsButton.setTypeface(font);
		
		if (getResources().getConfiguration().orientation == 
				getResources().getConfiguration().ORIENTATION_PORTRAIT) {
			mQuitButton = (Button) findViewById(R.id.quit_button);   
			mQuitButton.setTypeface(font);
			
			mSettingsButton = (Button) findViewById(R.id.settings_button);
			mSettingsButton.setTypeface(font);
		}
			
		mWelcomeTextView = (TextView) findViewById(R.id.welcome_textView);
		mWelcomeTextView.setTypeface(font);
		
		boolean firstRun = getSharedPreferences(FIRST_RUN, MODE_PRIVATE)
				.getBoolean("firstRunValue", true);
		if (firstRun){
			String text = "Regulile jocului sunt foarte simple, trebuie " +
					"sã faci cât mai multe cuvinte cu ultimele douã litere ale cuvântului anterior! Baftã!";
			//new AlertDialog.Builder(this).setTitle("Instrucþiuni")
			//		.setMessage("Regulile jocului sunt foarte simple, trebuie " +
			//		"sã faci cât mai multe cuvinte cu ultimele douã litere ale cuvântului anterior! Baftã!")
			//		.setNeutralButton("OK", null).show();
			CustomDialog cd = new CustomDialog(this, text);
			cd.show();
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
	}

/////////////////////////////////////////////////////////////////////////
	
	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
////////////////////////////////////////////////////////////////////////////////
	
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
			CloseApp(getCurrentFocus());
			return true;
		case R.id.facebook:
			facebook(getCurrentFocus());
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
/////////////////////////////////////////////////////////////////////////
	
	public void onResume() {
		super.onResume();
		mMediaPlayer = MediaPlayer.create(this,R.raw.click);
	}
	
///////////////////////////////////////////////////////////////////////////
	
	public void onStop() {
		super.onStop();
		mMediaPlayer.release();
		mMediaPlayer = null;
	}
	
/////////////////////////////////////////////////////////////////////////
	
	public void openPlayActivity(View view){
		if (this.mRepository.get_sunet()){
			mMediaPlayer.start();
		}
		Intent intent=new Intent(this, PlayActivity.class);
		startActivity(intent);
	}
	
/////////////////////////////////////////////////////////////////////////
	
	public void CloseApp(View view){
		if (this.mRepository.get_sunet()){
			mMediaPlayer.start();
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
			mMediaPlayer.start();
		}
		Intent intent = new Intent(this, HighscoreActivity.class);
		startActivity(intent);
	}

//////////////////////////////////////////////////////////////////////////

	public void openSettingsActivity(View view){
		if (this.mRepository.get_sunet()){
			mMediaPlayer.start();
		}
		Intent intent=new Intent(this, OptionActivity.class);
		startActivity(intent);
	}

public void facebook(View view){
	if (this.mRepository.get_sunet()){
		mMediaPlayer.start();
	}
	//Log.d("plm", "facebook");
	 Intent intent=getOpenFacebookIntent(getApplicationContext());
	 startActivity(intent);
	 //Log.d("plm", "facebookasd");
	 }

	 
	  public static Intent getOpenFacebookIntent(Context context) {

	      try {
	       context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
	       return new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/1428542174023832"));
	      } catch (Exception e) {
	       return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/FazanRo"));
	      }
	   }

}