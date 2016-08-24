package com.example.lifilibrarydemo;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import oledcomm.android.lifi.GeoAudioAnalysis;
import oledcomm.android.lifi.JackStatus;
import oledcomm.android.lifi.LiFiLocation;

public class LiFiLibDemoActivity extends Activity {

	TextView text1;
	GeoAudioAnalysis location;
	boolean start_flag = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		text1 = (TextView) findViewById(R.id.freq);
		if (!start_flag) {
			// create a new GeoAudioAnalysis object
			location = new GeoAudioAnalysis(jack_status,
					getApplicationContext());
			// start to watch the position changes
			location.watchPosition(position);

			start_flag = true;
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (start_flag) {
			if (location != null) {
				location.clearPosition();
				location = null;
				start_flag = false;
			}
		}
		finish();
		// when you press home button, the app will stop and release the
		// resource.

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (start_flag) {

			if (location != null) {
				location.clearPosition();
				location = null;
				start_flag = false;
			}
		}
		// When the activity is destroyed, the position must be cleared.

	}

	@Override
	protected void onRestart() {
		super.onRestart();
		if (!start_flag) {
			if (location == null) {
				location = new GeoAudioAnalysis(jack_status,
						getApplicationContext());
				location.watchPosition(position);
				start_flag = true;
			}
		}
	}

	// For switching direction of screen, no actions executed
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			// Nothing need to be done here

		} else {
			// Nothing need to be done here

		}
	}

	LiFiLocation position = new LiFiLocation() {

		@Override
		public void onLocationSuccess(String value) {
			// TODO Auto-generated method stub

			// the tag ID and current light intensity are received in this value, perform your UI operations based on this
			//0x6390 = lampe 2
			//0xd5d5 = lampe 3
			// 0x6e71 = lampe 1

			if(value.contains("6e71")){
				text1.setText("Lampe 1 connexion \n "+
				"Bienvenue dans la salle de classe 101 - fillière Economie et service");
				Uri uri = Uri.parse("http://intranet.hevs.ch/index.asp?noCategorie=4&nolangue=1");
				Intent intent = new Intent (Intent.ACTION_VIEW, uri);
				startActivity(intent);
			}
			if(value.contains("6390")){
				text1.setText("Lampe 2 connexion \n "+
						"Bienvenue dans la salle de classe 201 - fillière Informatique de gestion");
				Uri uri = Uri.parse("http://intranet.hevs.ch/index.asp?noCategorie=3&nolangue=1");
				Intent intent = new Intent (Intent.ACTION_VIEW, uri);
				startActivity(intent);
			}
			if(value.contains("d5d5")){
				text1.setText("Lampe 3 connexion \n "+
						"Bienvenue dans la salle de classe 301 - fillière Tourisme");
				Uri uri = Uri.parse("http://intranet.hevs.ch/index.asp?noCategorie=5&nolangue=1");
				Intent intent = new Intent (Intent.ACTION_VIEW, uri);
				startActivity(intent);
			}

		}

		@Override
		public void onLocationError(int error) {
			// TODO Auto-generated method stub

			// instead of getting a valid ID, the errors are received, also
			// useful for your UI operations
			if (error == LiFiLocation.ERROR_NO_TAG) {
				text1.setText("no tag");
			} else if (error == LiFiLocation.ERROR_WEAK_SIGNAL) {
				text1.setText("signal is too weak");
			}
		}

	};

	JackStatus jack_status = new JackStatus() {

		@Override
		public void onJackEvent(int Event) {
			// TODO Auto-generated method stub

			// you can write your reactions when different JACK sensor status is
			// present
			// If you have an integrated sensor inside the device, no need to
			// implement this
			switch (Event) {
			case JACK_PLUGGED:
				Toast.makeText(getApplicationContext(), "LiFi sensor plugged",
						Toast.LENGTH_SHORT).show();
				break;
			case JACK_UNPLUGGED:
				Toast.makeText(getApplicationContext(),
						"LiFi sensor unplugged", Toast.LENGTH_SHORT).show();
				break;
			case JACK_ABSENCE:
				Toast.makeText(getApplicationContext(), "LiFi sensor absence",
						Toast.LENGTH_SHORT).show();
				break;
			}

		}

	};

}
