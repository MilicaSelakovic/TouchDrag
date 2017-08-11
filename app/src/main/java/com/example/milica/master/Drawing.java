package com.example.milica.master;

import android.app.Activity;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.ToggleButton;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;

public class Drawing extends Activity {


    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i("OpenCV", "OpenCV loaded successfully");

                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawing);
        this.findViewById(R.id.imageButton4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DrawingView) findViewById(R.id.view)).clearPanel();
            }
        });
        this.findViewById(R.id.imageButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DrawingView) findViewById(R.id.view)).undo();
            }
        });
        this.findViewById(R.id.imageButton2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DrawingView) findViewById(R.id.view)).redo();
            }
        });
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            //Log.d("OpenCV", "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            //Log.d("OpenCV", "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    public void moveEnable(View view){
        boolean value = ((ToggleButton) this.findViewById(R.id.toggleButton)).isChecked();
        ((DrawingView) this.findViewById(R.id.view)).setMoving(value);

        if (value) {

            this.findViewById(R.id.radioButton).setEnabled(false);
            this.findViewById(R.id.radioButton2).setEnabled(false);
            this.findViewById(R.id.radioButton3).setEnabled(false);

        } else {

            this.findViewById(R.id.radioButton).setEnabled(true);
            this.findViewById(R.id.radioButton2).setEnabled(true);
            this.findViewById(R.id.radioButton3).setEnabled(true);

        }
//        if (value) {
//            this.findViewById(R.id.toggleButton2).setEnabled(false);
//        } else {
//            this.findViewById(R.id.toggleButton2).setEnabled(true);
//        }
    }


//    public void choose(View view) {
//        boolean value = ((ToggleButton) this.findViewById(R.id.toggleButton2)).isChecked();
//        ((DrawingView) this.findViewById(R.id.view)).setChoose(value);
//
//        if (value) {
//            this.findViewById(R.id.toggleButton).setEnabled(false);
//        } else {
//            this.findViewById(R.id.toggleButton).setEnabled(true);
//        }
//    }


    public void radioButton(View view) {
        if (((RadioButton) this.findViewById(R.id.radioButton2)).isChecked()) {
            ((DrawingView) this.findViewById(R.id.view)).setMode(DrawingView.Mode.MODE_USUAL);
            this.findViewById(R.id.toggleButton).setEnabled(true);
        }

        if (((RadioButton) this.findViewById(R.id.radioButton3)).isChecked()) {
            ((DrawingView) this.findViewById(R.id.view)).setMode(DrawingView.Mode.MODE_FREE);
            this.findViewById(R.id.toggleButton).setEnabled(false);
        }

        if (((RadioButton) this.findViewById(R.id.radioButton)).isChecked()) {
            ((DrawingView) this.findViewById(R.id.view)).setMode(DrawingView.Mode.MODE_FIX);
            this.findViewById(R.id.toggleButton).setEnabled(false);
        }

    }
}
