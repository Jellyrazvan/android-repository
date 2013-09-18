package com.the.dev.guys.fazan;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.the.dev.guys.Domain.Player;
import com.the.dev.guys.Repository.Repository;

public class HighscoreActivity extends Activity {
	
	private Repository mRepository;
	private ListView mHighScoresListView;
	private TextView mHighScoresTextView;
	
/////////////////////////////////////////////////////////////////////////
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_highscore);
		
		mRepository = Repository.getRepository(getApplicationContext());
		mHighScoresListView = (ListView) findViewById(R.id.highscores_listView);
		
		ActionBar bar = getActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.yellow)));
		// Show the Up button in the action bar.
		setupActionBar();
		
		try {
			this.mRepository.readHighscoresFromFile();
		} catch (IOException e1) {
			//Log.d("MyLogs", "plm");
			e1.printStackTrace();
		}
		
		Intent intent = getIntent();
		if (intent.hasExtra(NameActivity.EXTRA_MESSAGE)){
			String message = intent.getStringExtra(NameActivity.EXTRA_MESSAGE);
			String messageList[] = message.split(";");
			String name = messageList[1];
			int value = Integer.parseInt(messageList[0]);
			
			mRepository.addPlayerToHighScores(name, value);
			
			try {
				this.mRepository.writeHighscoresToFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		Vector<Player> playersVector = mRepository.getVectorHighScores();
		ArrayList<String> entrysList= new ArrayList<String>();
		for (Player player: playersVector){
			entrysList.add(player.get_name() + "           " + player.get_score());
		}
		
		ArrayAdapter<String> arrayAdapter =
					new ArrayAdapter<String>(this, R.layout.list_layout, entrysList);
		mHighScoresListView.setAdapter(arrayAdapter); 
		
		mHighScoresTextView = (TextView) findViewById(R.id.highscores_textView);
		Typeface cartonSlabFont = Typeface.createFromAsset(getAssets(), "fonts/Carton-Slab.otf"); 
		mHighScoresTextView.setTypeface(cartonSlabFont);
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
		getMenuInflater().inflate(R.menu.highscore, menu);
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
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			NavUtils.navigateUpFromSameTask(this);
		}
		return true;
	}
	

}
