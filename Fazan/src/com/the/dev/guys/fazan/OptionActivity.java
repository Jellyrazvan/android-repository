package com.the.dev.guys.fazan;

import java.io.IOException;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.the.dev.guys.Repository.Repository;

public class OptionActivity extends SherlockActivity {
	
	private ToggleButton mSoundButton;
	private ToggleButton mDiacriticsButton;
	private TextView mSettingsTextView, mDiacriticeTextView,
			mSoundsTextView;
	
	private Repository mRepository;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_option);
		// Show the Up button in the action bar.
		ActionBar bar = getSupportActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.red)));
		bar.setDisplayHomeAsUpEnabled(true);
		
		Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Carton-Slab.otf");
		Typeface robotoMediumFont = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Medium.ttf"); 
		
		mSettingsTextView = (TextView) findViewById(R.id.settings_textView);
		mSettingsTextView.setTypeface(font);
		
		mDiacriticeTextView = (TextView) findViewById(R.id.diacritice_textView);
		mDiacriticeTextView.setTypeface(robotoMediumFont);
		
		mSoundsTextView = (TextView) findViewById(R.id.sound_textView);
		mSoundsTextView.setTypeface(robotoMediumFont);
		
		mSoundButton = (ToggleButton) findViewById(R.id.sound_toggleButton);
		mDiacriticsButton = (ToggleButton) findViewById(R.id.diacritice_toggleButton);
		
		mRepository = Repository.getRepository(getApplicationContext());
		
		mSoundButton.setChecked(mRepository.get_sunet());
		mDiacriticsButton.setChecked(mRepository.get_diacritice());
		
		mSoundButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final MediaPlayer mp = MediaPlayer.create(OptionActivity.this,R.raw.onoff);
				mp.start();
			}
		});
	
		mDiacriticsButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final MediaPlayer mp = MediaPlayer.create(OptionActivity.this,R.raw.onoff);
				mp.start();
			}
		});
		mDiacriticsButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					new AlertDialog.Builder(buttonView.getContext()).setTitle("Important")
					.setMessage("Pentru a folosi aceastã opþiune este nevoie ca" +
							" tastatura sã fie setatã pe limba românã. Asigurã-te cã tastatura ta " + 
							"suportã caractere cu diacritice înainte de a folosi aceastã opþiune.")
					.setNeutralButton("OK", null).show();
				}
				
			}
		});
	}
	
	@Override
	public void onPause() {
		super.onPause();
		
		mRepository.set_sunet(mSoundButton.isChecked());
		mRepository.set_diacritice(mDiacriticsButton.isChecked());
		if (mDiacriticsButton.isChecked()) {
			try {
				mRepository.loadWordsFromFile(Repository.DIACRITICE_FILENAME);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			try {
				mRepository.loadWordsFromFile(Repository.FARA_DIACRITICE_FILENAME);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		getSharedPreferences(MainActivity.SAVED_SETTINGS, MODE_PRIVATE)
		.edit()
		.putBoolean("sound_setting", mRepository.get_sunet())
		.putBoolean("diacritice_setting", mRepository.get_diacritice())
		.commit();
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.option, menu);
		return true;
	}

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
			if (this.mRepository.get_sunet()){
				final MediaPlayer mp = MediaPlayer.create(this,R.raw.click);
				mp.start();
			}
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
