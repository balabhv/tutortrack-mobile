package com.tutortrack.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.tutortrack.R;
import com.tutortrack.api.API;
import com.tutortrack.dialog.LoginDialog;

public class MainActivity extends Activity {

	public static final String KEY_LOGIN = "LOGIN_TYPE";
	public static final String KEY_STUDENT = "STUDENT";
	public static final String KEY_TUTOR = "TUTOR";
	public static final String KEY_ADMIN = "ADMIN";
	public static final String PREFS_TAG = "PREFS";
	
	// test git

	public static final int LOGIN_REQUESTED = 1;

	private Button studentLogin, tutorLogin, adminLogin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		API.mainActivity = this;
		API.setUseDev(false);
		
		if(!API.hasConnectivity()) {
			// no connectivity warning here
		}

		studentLogin = (Button) findViewById(R.id.student_login_button);
		tutorLogin = (Button) findViewById(R.id.tutor_login_button);
		adminLogin = (Button) findViewById(R.id.admin_login_button);

		studentLogin.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(),
						LoginDialog.class);
				i.putExtra(KEY_LOGIN, KEY_STUDENT);
				startActivityForResult(i, LOGIN_REQUESTED);

			}
		});

		tutorLogin.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(),
						LoginDialog.class);
				i.putExtra(KEY_LOGIN, KEY_TUTOR);
				startActivityForResult(i, LOGIN_REQUESTED);

			}
		});

		adminLogin.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(),
						LoginDialog.class);
				i.putExtra(KEY_LOGIN, KEY_ADMIN);
				startActivityForResult(i, LOGIN_REQUESTED);

			}
		});

	}

	public void onResume() {
		super.onResume();

		// Initialize action bar customization for API >= 11
		if (android.os.Build.VERSION.SDK_INT >= 11) {
			ActionBar bar = getActionBar();

			// make the actionbar clickable
			Drawable logo = this.getResources().getDrawable(R.drawable.logo);
			bar.setLogo(this.resize(logo));
			bar.setDisplayUseLogoEnabled(true);
			bar.setDisplayShowTitleEnabled(false);
		}
		
	}

	private Drawable resize(Drawable image) {

		final TypedArray styledAttributes = getBaseContext().getTheme()
				.obtainStyledAttributes(
						new int[] { android.R.attr.actionBarSize });
		int mActionBarSize = (int) styledAttributes.getDimension(0, 0);
		styledAttributes.recycle();

		Bitmap b = ((BitmapDrawable) image).getBitmap();
		Bitmap bitmapResized = Bitmap.createScaledBitmap(b, b.getWidth(),
				mActionBarSize, false);
		return new BitmapDrawable(getResources(), bitmapResized);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == LOGIN_REQUESTED && resultCode == RESULT_OK) {
			String key = data.getStringExtra(KEY_LOGIN);
			if (key.equals(KEY_STUDENT)) {
				startActivity(new Intent(getApplicationContext(),
						StudentMain.class));
			} else if (key.equals(KEY_TUTOR)) {
				startActivity(new Intent(getApplicationContext(),
						TutorMain.class));
			} else if (key.equals(KEY_ADMIN)) {

			} else {
				// shouldn't get here - invalid key
				Toast.makeText(getApplicationContext(), "Login error.",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

}
