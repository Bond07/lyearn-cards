package com.example.nirav.lyearn;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;


public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private ImageView mImageView;
    private Button mChangePhotoButton;

    private EditText mUserName;
    private EditText mRole;
    private EditText mSkypeId;
    private EditText mContactNumber;

    private File imageFile;

    private String imageFileName = "profile_image";

    private UserProfileInfo.UserInfo mUserInfo = new UserProfileInfo.UserInfo();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.title_activity_edit_profile);


        mImageView = (ImageView) findViewById(R.id.profilePhoto);
        Glide.with(this).load(mUserInfo.getUserProfilePhotoURl()).asBitmap().into(mImageView);

        //change photo button
        mChangePhotoButton  = (Button) findViewById(R.id.changePhoto);
        mChangePhotoButton.setOnClickListener(this);

        //defining all edit text field
        mUserName = (EditText) findViewById(R.id.contactNameTextField);
        mRole = (EditText) findViewById(R.id.roleTextField);
        mSkypeId = (EditText) findViewById(R.id.skypeIdTextField);
        mContactNumber = (EditText) findViewById(R.id.contactNumberTextField);

        //adding text change listener on edit text field
        mUserName.setText(mUserInfo.getUserProfileName());
        mUserName.addTextChangedListener(new GenericTextWatcher(mUserName));

        mRole.setText(mUserInfo.getUserRole());
        mRole.addTextChangedListener(new GenericTextWatcher(mRole));

        mSkypeId.setText(mUserInfo.getUserSkypeId());
        mSkypeId.addTextChangedListener(new GenericTextWatcher(mSkypeId));

        mContactNumber.setText(mUserInfo.getUserContactNumber());
        mContactNumber.addTextChangedListener(new GenericTextWatcher(mContactNumber));

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.myPrimaryDarkColor));
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.changePhoto: {
                dispatchTakePictureIntent();
                break;
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        uploadUserInfo();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            persistImage(imageBitmap, imageFileName);
            imageBitmap = Bitmap.createScaledBitmap(imageBitmap, 500, 500, true);
            ImageRounding image = new ImageRounding();
            mImageView.setImageBitmap(image.getBitmap(imageBitmap));

            launchUploadActivity();
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    //Declaration
    private class GenericTextWatcher implements TextWatcher{

        private View view;
        private GenericTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        public void afterTextChanged(Editable editable) {
            String text = editable.toString();
            switch(view.getId()){
                case R.id.contactNameTextField:
                    mUserInfo.setUserProfileName(text);
                    break;
                case R.id.roleTextField:
                    mUserInfo.setUserRole(text);
                    break;
                case R.id.skypeIdTextField:
                    mUserInfo.setUserSkypeId(text);
                    break;
                case R.id.contactNumberTextField:
                    mUserInfo.setUserContactNumber(text);
            }
        }

    }

    private void launchUploadActivity() {

        RequestQueue requestQueue = VolleySingleton.getsInstance().getRequestQueue();
        MultipartRequest request = new MultipartRequest(Config.FILE_UPLOAD_URL, imageFile, "random", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("User Info", response);
                mUserInfo.setUserProfilePhotoURL(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("User Info", error.toString());
            }
        });

        requestQueue.add(request);
    }


    private void uploadUserInfo() {

        String[] userName = mUserInfo.getUserProfileName().split("\\s+");

        JSONObject userInfo = new JSONObject();
        try {
            userInfo.put("userID", mUserInfo.getUserID());
            userInfo.put("firstName", userName[0]);
            userInfo.put("lastName", userName[1]);
            userInfo.put("imageUrl", mUserInfo.getUserProfilePhotoURl());
            userInfo.put("emailID", mUserInfo.getUserEmailId());
            userInfo.put("phoneNumber", mUserInfo.getUserContactNumber());
            userInfo.put("skype", mUserInfo.getUserSkypeId());
            userInfo.put("whatIDo", mUserInfo.getUserRole());

        } catch (Exception e) {

            e.printStackTrace();

        }

        JSONObject params = new JSONObject();
        try {
            params.put("data", userInfo.toString());

        } catch (Exception e) {

            e.printStackTrace();

        }


        RequestQueue requestQueue = VolleySingleton.getsInstance().getRequestQueue();
        JsonObjectRequest request  = new JsonObjectRequest(Config.UPDATE_USER_INFO, params ,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("User Info", response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("User Info", error.toString());
            }
        });

        requestQueue.add(request);
    }



    /**
     ------------ Helper Methods ---------------------- */


    private void persistImage(Bitmap bitmap, String name) {
        File filesDir = MainApplication.getAppContext().getFilesDir();
        imageFile = new File(filesDir, name + ".jpg");
        Log.d("File", imageFile.toString());
        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            Log.d("File Created", os.toString());
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();

        } catch (Exception e) {
            Log.e("Bitmap to File Error", "Error writing bitmap", e);
        }
    }

}
