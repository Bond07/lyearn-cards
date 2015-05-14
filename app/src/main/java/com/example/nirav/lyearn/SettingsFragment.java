package com.example.nirav.lyearn;


import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Date;
import java.util.Map;

import android.content.pm.PackageManager;

import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment implements View.OnClickListener{

    private Context mContext;
    private Activity mActivity;
    private OnFragmentInteractionListener mListener;

    private ImageView mImageView;
    private Button mAudioRecordButton;
    private Button mVideoRecordButton;

    private TextView mEditProfile;


    static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final int RESULT_OK  = -1;

    // Camera activity request codes
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    private Uri fileUri; // file url to store image/video

    private File imageFile;

    private String imageFileName = "sample_image";

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;
    private Intent intent;



    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = getActivity();
        mContext = getActivity().getApplicationContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_settings,
                container, false);

//        if (mListener != null) {
//            mListener.onFragmentInteraction("Settings");
//        }

        mImageView = (ImageView)  view.findViewById(R.id.imgAvatarSettings);
        mAudioRecordButton =(Button) view.findViewById(R.id.audioRecordButton);
        mVideoRecordButton = (Button) view.findViewById(R.id.videoRecordButton);
        mEditProfile = (TextView) view.findViewById(R.id.editProfile);

        mImageView.setOnClickListener(this);
        mAudioRecordButton.setOnClickListener(this);
        mVideoRecordButton.setOnClickListener(this);
        mEditProfile.setOnClickListener(this);

        //Load image using Glide
        Glide.with(this).load(HomeActivity.userProfile[2]).asBitmap().into(mImageView);

        return view;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgAvatarSettings: {
                dispatchTakePictureIntent();
                break;
            }

            case R.id.audioRecordButton: {
                Intent intent = new Intent(getActivity(), AudioRecordingActivity.class);
                startActivity(intent);
                break;
            }

            case R.id.videoRecordButton: {
                //dispatchTakeVideoIntent();
                Intent intent = new Intent(getActivity(), VideoViewActivity.class);
                startActivity(intent);
                break;
            }

            case R.id.editProfile: {
                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                //intent.putExtra("BitmapImage", HomeActivity.profilePhoto);
                startActivity(intent);
                break;
            }
        }
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


    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(String title);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void launchUploadActivity() {

        RequestQueue requestQueue = VolleySingleton.getsInstance().getRequestQueue();
        MultipartRequest request = new MultipartRequest(Config.FILE_UPLOAD_URL, imageFile, "random", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("User Info", response);
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