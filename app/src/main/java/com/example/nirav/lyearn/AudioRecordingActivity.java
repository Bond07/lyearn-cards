package com.example.nirav.lyearn;

/**
 * Created by nirav on 4/29/15.
 */

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.audiofx.Equalizer;
import android.media.audiofx.Visualizer;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.os.Bundle;
import android.os.Environment;
import android.view.ViewGroup;
import android.widget.Button;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.Context;
import android.util.Log;
import android.media.MediaRecorder;
import android.media.MediaPlayer;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;


public class AudioRecordingActivity extends Activity
{
    private static final String LOG_TAG = "AudioRecordTest";

    private static final float VISUALIZER_HEIGHT_DIP = 50f;

    private static String mFileName = null;

    private RecordButton mRecordButton = null;
    private MediaRecorder mRecorder = null;

    private PlayButton   mPlayButton = null;
    private MediaPlayer   mPlayer = null;

    private Visualizer mVisualizer;

    private AudioRecord mAudioRecord;

    private VisualizerView mVisualizerView;
    private LinearLayout mLinearLayout;
    private TextView mStatusTextView;

    private void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    private void onPlay(boolean start) {
        if (start) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }

    private void startPlaying() {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(mFileName);
            mPlayer.prepare();

            setupVisualizerFxAndUI();

            mVisualizer.setEnabled(true);

            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mediaPlayer) {
                    mVisualizer.setEnabled(false);
                }
            });

            mPlayer.start();
            mStatusTextView.setText("Playing audio...");
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    private void stopPlaying() {
        mPlayer.release();
        mPlayer = null;
    }

    private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        setupVisualizerFxAndUI();

        mVisualizer.setEnabled(true);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        mRecorder.start();
    }

    private void stopRecording() {
        mRecorder.stop();
        mRecorder.reset();
        mRecorder.release();
        mRecorder = null;
        mVisualizer.setEnabled(false);
    }

    class RecordButton extends Button {
        boolean mStartRecording = true;

        OnClickListener clicker = new OnClickListener() {
            public void onClick(View v) {
                onRecord(mStartRecording);
                if (mStartRecording) {
                    setText("Stop recording");
                } else {
                    setText("Start recording");
                }
                mStartRecording = !mStartRecording;
            }
        };

        public RecordButton(Context ctx) {
            super(ctx);
            setText("Start recording");
            setOnClickListener(clicker);
        }
    }

    class PlayButton extends Button {
        boolean mStartPlaying = true;

        OnClickListener clicker = new OnClickListener() {
            public void onClick(View v) {
                onPlay(mStartPlaying);
                if (mStartPlaying) {
                    setText("Stop playing");
                } else {
                    setText("Start playing");
                }
                mStartPlaying = !mStartPlaying;
            }
        };

        public PlayButton(Context ctx) {
            super(ctx);
            setText("Start playing");
            setOnClickListener(clicker);
        }
    }

    public AudioRecordingActivity() {
        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/audiorecordtest.3gp";
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        mLinearLayout = new LinearLayout(this);
        mLinearLayout.setOrientation(LinearLayout.VERTICAL);
        mLinearLayout.setVerticalScrollBarEnabled(true);
        mRecordButton = new RecordButton(this);
        mLinearLayout.addView(mRecordButton,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        0));

        mPlayButton = new PlayButton(this);
        mLinearLayout.addView(mPlayButton,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        0));

        mStatusTextView = new TextView(this);
        mLinearLayout.addView(mStatusTextView, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                0));

        mVisualizerView = new VisualizerView(this);
        mVisualizerView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                (int)(VISUALIZER_HEIGHT_DIP * getResources().getDisplayMetrics().density)));
        mLinearLayout.addView(mVisualizerView);

        setContentView(mLinearLayout);
    }

    @Override
    public void onBackPressed() {
        if(mPlayer != null) {
            stopPlaying();
        }
        else if (mRecorder != null) {
            stopRecording();
        }
        super.onBackPressed();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mRecorder != null) {
            mRecorder.reset();
            mRecorder.release();
            mRecorder = null;
        }

        if (mPlayer != null) {
            mRecorder.reset();
            mPlayer.release();
            mPlayer = null;
        }
    }

    private void setupVisualizerFxAndUI() {
        // Create the Visualizer object and attach it to our media player.
//        if(mAudioRecord!=null) {
//            mVisualizer = new Visualizer(mAudioRecord.getAudioSessionId());
//        }
//        else {
//            mVisualizer = new Visualizer(mPlayer.getAudioSessionId());
//        }
        mVisualizer = new Visualizer(mRecorder.getMaxAmplitude());
        mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
        mVisualizer.setDataCaptureListener(new Visualizer.OnDataCaptureListener() {
            public void onWaveFormDataCapture(Visualizer visualizer, byte[] bytes,
                                              int samplingRate) {
                mVisualizerView.updateVisualizer(bytes);
            }

            public void onFftDataCapture(Visualizer visualizer, byte[] bytes, int samplingRate) {}
        }, Visualizer.getMaxCaptureRate() / 2, true, false);
    }

    /**
     * A simple class that draws waveform data received from a
     * {@link Visualizer.OnDataCaptureListener#onWaveFormDataCapture }
     */
    class VisualizerView extends View {
        private byte[] mBytes;
        private float[] mPoints;
        private Rect mRect = new Rect();

        private Paint mForePaint = new Paint();

        public VisualizerView(Context context) {
            super(context);
            init();
        }

        private void init() {
            mBytes = null;

            mForePaint.setStrokeWidth(1f);
            mForePaint.setAntiAlias(true);
            mForePaint.setColor(Color.rgb(0, 128, 255));
        }

        public void updateVisualizer(byte[] bytes) {
            mBytes = bytes;
            invalidate();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            if (mBytes == null) {
                return;
            }

            if (mPoints == null || mPoints.length < mBytes.length * 4) {
                mPoints = new float[mBytes.length * 4];
            }

            mRect.set(0, 0, getWidth(), getHeight());

            for (int i = 0; i < mBytes.length - 1; i++) {
                mPoints[i * 4] = mRect.width() * i / (mBytes.length - 1);
                mPoints[i * 4 + 1] = mRect.height() / 2
                        + ((byte) (mBytes[i] + 128)) * (mRect.height() / 2) / 128;
                mPoints[i * 4 + 2] = mRect.width() * (i + 1) / (mBytes.length - 1);
                mPoints[i * 4 + 3] = mRect.height() / 2
                        + ((byte) (mBytes[i + 1] + 128)) * (mRect.height() / 2) / 128;
            }

            canvas.drawLines(mPoints, mForePaint);
        }
    }
}
