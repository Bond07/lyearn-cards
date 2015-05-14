package com.example.nirav.lyearn;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;

import java.io.InputStream;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class HomeActivity extends AppCompatActivity implements NavigationDrawerCallbacks, View.OnClickListener, AssignedCardFragment.OnFragmentInteractionListener, SettingsFragment.OnFragmentInteractionListener, HomeFragment.OnFragmentInteractionListener, SubmissionFragment.OnFragmentInteractionListener {

    private NavigationDrawerFragment mNavigationDrawerFragment;

    private GoogleApiClient mGoogleApiClient;

    public static String[] userProfile;

    private ImageView profileImage;
    private TextView userName;
    private TextView userEmail;

    private Handler mHandler;

    private UserProfileInfo.UserInfo mUserInfo = new UserProfileInfo.UserInfo();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);
        //getSupportActionBar().setTitle(R.string.app_name);

        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mDrawerLayout.setStatusBarBackgroundColor(
                getResources().getColor(R.color.myPrimaryDarkColor));

        mHandler = new Handler();


        mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.fragment_drawer);
        mNavigationDrawerFragment.setup(R.id.fragment_drawer, (DrawerLayout) findViewById(R.id.drawer), mToolbar);

        profileImage = (ImageView) findViewById(R.id.imgAvatar);
        userName = (TextView) findViewById(R.id.txtUsername);
        userEmail = (TextView) findViewById(R.id.txtUserEmail);

        //sign out button
        Button signOutButton = (Button) findViewById(R.id.btn_sign_out);
        signOutButton.setOnClickListener(this);

        //get Intent from Main Activity
        Bundle extras = getIntent().getExtras();
        userProfile = extras.getStringArray(getString(R.string.key_name));


        Glide.with(this).load(mUserInfo.getUserProfilePhotoURl()).asBitmap().into(profileImage);
        userName.setText(mUserInfo.getUserProfileName());
        userEmail.setText(mUserInfo.getUserEmailId());



        //set font style
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/Roboto/Roboto-Medium.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );

        mGoogleApiClient = buildGoogleApiClient();
    }

    private void onSuccessful(String response) {

        Toast.makeText(this, "Response" + response, Toast.LENGTH_LONG).show();

    }

    private GoogleApiClient buildGoogleApiClient() {
        // When we build the GoogleApiClient we specify where connected and
        // connection failed callbacks should be returned, which Google APIs our
        // app uses and which OAuth 2.0 scopes our app requests.
        GoogleApiClient.Builder builder = new GoogleApiClient.Builder(this)
                .addApi(Plus.API, Plus.PlusOptions.builder().build())
                .addScope(Plus.SCOPE_PLUS_LOGIN);
        return builder.build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
        changeUserInfo();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        //Toast.makeText(this, "Menu item selected -> " + position, Toast.LENGTH_SHORT).show();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.setCustomAnimations(R.anim.slide_up, 0);

        switch (position) {
            case 0: {
                HomeFragment homeFragment = new HomeFragment();
                fragmentTransaction.replace(R.id.container, homeFragment).commit();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        findViewById(R.id.container).setVisibility(View.VISIBLE);
                    }
                }, 50);
                break;
            }
            case 1: {
                AssignedCardFragment assignedCardFragment = new AssignedCardFragment();
                fragmentTransaction.replace(R.id.container, assignedCardFragment).commit();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        findViewById(R.id.container).setVisibility(View.VISIBLE);
                    }
                }, 50);
                break;
            }
            case 2: {
                SubmissionFragment submissionFragment = new SubmissionFragment();
                fragmentTransaction.replace(R.id.container,submissionFragment).commit();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        findViewById(R.id.container).setVisibility(View.VISIBLE);
                    }
                }, 50);
                break;
            }
            case 3: {
                SettingsFragment settingsFragment = new SettingsFragment();
                fragmentTransaction.replace(R.id.container, settingsFragment).commit();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        findViewById(R.id.container).setVisibility(View.VISIBLE);
                    }
                }, 50);
                break;
            }
        }
    }

    @Override
    public void onDestroy(int position) {
        //Toast.makeText(this, "Menu item selected -> " + position, Toast.LENGTH_SHORT).show();
        setActionBar(position);
        findViewById(R.id.container).setVisibility(View.GONE);
    }


    @Override
    public void onBackPressed() {
        if (mNavigationDrawerFragment.isDrawerOpen())
            mNavigationDrawerFragment.closeDrawer();
        //else
            //super.onBackPressed();
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sign_out:
                if (mGoogleApiClient.isConnected()) {
                    mGoogleApiClient.clearDefaultAccountAndReconnect();
                    Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient);
                    mGoogleApiClient.disconnect();
                    mGoogleApiClient.connect();
                    finish();
                }
        }
    }

    @Override
    public void onFragmentInteraction(String title) {
        getSupportActionBar().setTitle(title);
    }

    public void setActionBar(int position) {
        switch (position) {
            case 0: {
                getSupportActionBar().setTitle("Home");
                break;
            }

            case 1: {
                getSupportActionBar().setTitle("Assigned Courses");
                break;
            }

            case 2: {
                getSupportActionBar().setTitle("My Submission");
                break;
            }

            case 3: {
                getSupportActionBar().setTitle("Settings");
                break;
            }
        }
    }


    public void changeUserInfo() {
        Glide.with(this).load(mUserInfo.getUserProfilePhotoURl()).asBitmap().into(profileImage);
        userName.setText(mUserInfo.getUserProfileName());
        userEmail.setText(mUserInfo.getUserEmailId());
    }

}

