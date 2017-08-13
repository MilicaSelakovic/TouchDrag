package com.example.milica.master;

import android.app.Activity;
import android.content.Intent;
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

import java.util.HashMap;
import java.util.Vector;

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


        ConstructionParser parser = new ConstructionParser();

        HashMap<String, Vector<String>> trics = new HashMap<>();

        parser.fillConstructions(trics, getApplicationContext(), "allconstuctions.json");

        ((DrawingView) this.findViewById(R.id.view)).setTrics(trics);

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

        this.findViewById(R.id.imageButton3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Drawing.this, SettingsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
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


        if (value) {
            ((DrawingView) this.findViewById(R.id.view)).setMode(DrawingView.Mode.MODE_MOVE);
            this.findViewById(R.id.toggleButton2).setEnabled(false);
        } else {
            ((DrawingView) this.findViewById(R.id.view)).setMode(DrawingView.Mode.MODE_USUAL);
            this.findViewById(R.id.toggleButton2).setEnabled(true);
        }
    }


    public void choose(View view) {
        boolean value = ((ToggleButton) this.findViewById(R.id.toggleButton2)).isChecked();

        if (value) {
            ((DrawingView) this.findViewById(R.id.view)).setMode(DrawingView.Mode.MODE_SELECT);
            this.findViewById(R.id.toggleButton).setEnabled(false);
        } else {
            ((DrawingView) this.findViewById(R.id.view)).setMode(DrawingView.Mode.MODE_USUAL);
            this.findViewById(R.id.toggleButton).setEnabled(true);
        }
    }


}
