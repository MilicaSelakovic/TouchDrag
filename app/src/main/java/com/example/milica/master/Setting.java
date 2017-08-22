package com.example.milica.master;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;

import com.pes.androidmaterialcolorpickerdialog.ColorPicker;
import com.pes.androidmaterialcolorpickerdialog.ColorPickerCallback;

public class Setting extends AppCompatActivity {

    private int colorMOVE;
    private int colorFIXED;
    private int colorACTIVE;
    private int colorCANFREE;
    private int colorCANNOTFREE;
    private int colorOHTER;

    private int pointSize;
    private int textSize;
    private boolean enableLabel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Spinner spinner = (Spinner) findViewById(R.id.spinner2);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sizes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        Spinner spinner1 = (Spinner) findViewById(R.id.spinner3);
        spinner1.setAdapter(adapter);

        // TODO postaviti vrednosti za polja.
        this.findViewById(R.id.imageButton5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(Setting.this, Drawing.class);
//                startActivity(intent);
                finish();
            }
        });

        this.findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ColorPicker cp = new ColorPicker(Setting.this, 1, 100, 100, 100);

                cp.show();

    /* Set a new Listener called when user click "select" */
                cp.setCallback(new ColorPickerCallback() {
                    @Override
                    public void onColorChosen(@ColorInt int color) {
                        // Do whatever you want
                        // Examples
//                        Log.d("Alpha", Integer.toString(Color.alpha(color)));
//                        Log.d("Red", Integer.toString(Color.red(color)));
//                        Log.d("Green", Integer.toString(Color.green(color)));
//                        Log.d("Blue", Integer.toString(Color.blue(color)));
//
                        Log.d("Pure Hex", Integer.toHexString(color));
//                        Log.d("#Hex no alpha", String.format("#%06X", (0xFFFFFF & color)));
//                        Log.d("#Hex with alpha", String.format("#%08X", (0xFFFFFFFF & color)));

                        cp.dismiss();
                    }
                });
            }
        });
    }

    @Override
    public void finish() {

        super.finish();
    }
}
