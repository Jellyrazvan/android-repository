package com.the.dev.guys.fazan;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.KeyEvent;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.the.dev.guys.Domain.Player;
import com.the.dev.guys.Repository.Repository;


public class HighscoreActivity extends SherlockActivity {
	private static final String KEY_EXTRA_ADDED = "extra_added";
	
	private Repository mRepository;
	private ListView mHighScoresListView;
	private TextView mHighScoresTextView;
	private MediaPlayer mMediaPlayer;
	
/////////////////////////////////////////////////////////////////////////
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_highscore);
		
		mRepository = Repository.getRepository(getApplicationContext());
		mHighScoresListView = (ListView) findViewById(R.id.highscores_listView);
		mMediaPlayer = MediaPlayer.create(this,R.raw.click);
		
		ActionBar bar = getSupportActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.red)));
		// Show the Up button in the action bar.
		//setupActionBar();
		bar.setDisplayHomeAsUpEnabled(true);
		/*
		try {
			this.mRepository.readHighscoresFromFile();
		} catch (IOException e1) {
			//Log.d("MyLogs", "plm");
			e1.printStackTrace();
		}
		*/
		
		boolean isPlayerAdded = false;
		if (savedInstanceState != null)
			isPlayerAdded = true;
		
		Intent intent = getIntent();
		if (intent.hasExtra(NameActivity.EXTRA_MESSAGE) && !isPlayerAdded){
			String message = intent.getStringExtra(NameActivity.EXTRA_MESSAGE);
			String messageList[] = message.split(";");
			String name = messageList[1];
			int value = Integer.parseInt(messageList[0]);
			
			mRepository.addPlayerToHighScores(name, value);
			
			/*try {
				//this.mRepository.writeHighscoresToFile();
			} catch (IOException e) {
				e.printStackTrace();
			}*/
		}
		
		if (savedInstanceState == null) {
			mRepository.loadHighScoresFromServer(this);
		}
		
		Vector<Player> playersVector = mRepository.getVectorHighScores();
		mHighScoresTextView = (TextView) findViewById(R.id.highscores_textView);
		Typeface cartonSlabFont = Typeface.createFromAsset(getAssets(), "fonts/Carton-Slab.otf"); 
		mHighScoresTextView.setTypeface(cartonSlabFont);
		if (playersVector == null) {
			String text = "Conexiunea cu serverul nu s-a putut realiza!";
			CustomDialog cd = new CustomDialog(this, text);
			cd.show();
			cd.setOnDismissListener(new DialogInterface.OnDismissListener() {
				
				@Override
				public void onDismiss(DialogInterface dialog) {
					NavUtils.navigateUpFromSameTask(HighscoreActivity.this);
					
				}
			});
			//NavUtils.navigateUpFromSameTask(this);
		} else {
			ArrayList<String> entrysList= new ArrayList<String>();
			for (Player player: playersVector){
				entrysList.add(player.getPosition() + ". " + 
						player.get_name() + "           " + player.get_score());
			}
			
			ArrayAdapter<String> arrayAdapter =
						new ArrayAdapter<String>(this, R.layout.list_layout, entrysList);
			mHighScoresListView.setAdapter(arrayAdapter);
			
		}
	}
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		savedInstanceState.putBoolean(KEY_EXTRA_ADDED, true);
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
		getSupportMenuInflater().inflate(R.menu.highscore, menu);
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
			if (this.mRepository.get_sunet()){
				mMediaPlayer.start();
			}
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
//////////////////////////////////////////////////////////////////////////
	
	public void onStop(){
		super.onStop();
		mMediaPlayer.release();
		mMediaPlayer = null;
	}
	
//////////////////////////////////////////////////////////////////////////
	
	public void onResume() {
		super.onResume();
		mMediaPlayer = MediaPlayer.create(this,R.raw.click);
	}
	
/////////////////////////////////////////////////////////////////////////
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			if (this.mRepository.get_sunet()){
				final MediaPlayer mp = MediaPlayer.create(this,R.raw.click);
				mp.start();
			}
			NavUtils.navigateUpFromSameTask(this);
		}
		return true;
	}
	

}
