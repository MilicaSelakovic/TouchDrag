package touch.drag.milica.master;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;

import com.pes.androidmaterialcolorpickerdialog.ColorPicker;
import com.pes.androidmaterialcolorpickerdialog.ColorPickerCallback;

public class Setting extends AppCompatActivity {

    private int moveColor;
    private int fixedColor;
    private int activeColor;
    private int canChooseColor;
    private int cannotChooseColor;
    private int otherColor;

    private int pointSize;
    private boolean label;
    private int textSize;

    private boolean hasExtra = false;

    private float factor;

    private boolean signInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            hasExtra = true;
            moveColor = extras.getInt("moveColor");
            fixedColor = extras.getInt("fixedColor");
            activeColor = extras.getInt("activeColor");
            canChooseColor = extras.getInt("canChooseColor");
            cannotChooseColor = extras.getInt("cannotChooseColor");
            otherColor = extras.getInt("otherColor");

            pointSize = extras.getInt("pointSize");
            label = extras.getBoolean("label");
            textSize = extras.getInt("textSize");
            factor = extras.getFloat("factor");
            signInfo = extras.getBoolean("signInfo");
        }


        if (hasExtra) {
            setValues();
        }

        this.findViewById(R.id.imageButton5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        this.findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ColorPicker cp = new ColorPicker(Setting.this, Color.red(moveColor), Color.green(moveColor), Color.blue(moveColor));
                cp.show();

                cp.setCallback(new ColorPickerCallback() {
                    @Override
                    public void onColorChosen(@ColorInt int color) {
                        moveColor = color;
                        findViewById(R.id.button2).setBackgroundColor(moveColor);
                        cp.dismiss();
                    }
                });
            }
        });

        this.findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ColorPicker cp = new ColorPicker(Setting.this, Color.red(fixedColor), Color.green(fixedColor), Color.blue(fixedColor));
                cp.show();

                cp.setCallback(new ColorPickerCallback() {
                    @Override
                    public void onColorChosen(@ColorInt int color) {
                        fixedColor = color;
                        findViewById(R.id.button3).setBackgroundColor(fixedColor);
                        cp.dismiss();
                    }
                });
            }
        });


        this.findViewById(R.id.button4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ColorPicker cp = new ColorPicker(Setting.this, Color.red(activeColor), Color.green(activeColor), Color.blue(activeColor));
                cp.show();

                cp.setCallback(new ColorPickerCallback() {
                    @Override
                    public void onColorChosen(@ColorInt int color) {
                        activeColor = color;
                        findViewById(R.id.button4).setBackgroundColor(activeColor);
                        cp.dismiss();
                    }
                });
            }
        });

        this.findViewById(R.id.button5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ColorPicker cp = new ColorPicker(Setting.this, Color.red(canChooseColor), Color.green(canChooseColor), Color.blue(canChooseColor));
                cp.show();

                cp.setCallback(new ColorPickerCallback() {
                    @Override
                    public void onColorChosen(@ColorInt int color) {
                        canChooseColor = color;
                        findViewById(R.id.button5).setBackgroundColor(canChooseColor);
                        cp.dismiss();
                    }
                });
            }
        });


        this.findViewById(R.id.button6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ColorPicker cp = new ColorPicker(Setting.this, Color.red(cannotChooseColor), Color.green(cannotChooseColor), Color.blue(cannotChooseColor));
                cp.show();

                cp.setCallback(new ColorPickerCallback() {
                    @Override
                    public void onColorChosen(@ColorInt int color) {
                        cannotChooseColor = color;
                        findViewById(R.id.button6).setBackgroundColor(cannotChooseColor);
                        cp.dismiss();
                    }
                });
            }
        });


        this.findViewById(R.id.button7).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ColorPicker cp = new ColorPicker(Setting.this, Color.red(otherColor), Color.green(otherColor), Color.blue(otherColor));
                cp.show();

                cp.setCallback(new ColorPickerCallback() {
                    @Override
                    public void onColorChosen(@ColorInt int color) {
                        otherColor = color;
                        findViewById(R.id.button7).setBackgroundColor(otherColor);
                        cp.dismiss();
                    }
                });
            }
        });

        this.findViewById(R.id.switch1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Switch s = (Switch) findViewById(R.id.switch1);

                label = s.isChecked();
            }
        });


        this.findViewById(R.id.show).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Switch s = (Switch) findViewById(R.id.show);

                signInfo = s.isChecked();
            }
        });

        Spinner spinner = (Spinner) findViewById(R.id.spinner3);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                textSize = parent.getSelectedItemPosition();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner spinner1 = (Spinner) findViewById(R.id.spinner2);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int pos = parent.getSelectedItemPosition();

                pointSize = pos;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final SeekBar seekBar = (SeekBar) this.findViewById(R.id.factor);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                factor = progress / 10f;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public void finish() {

        Intent intent = new Intent();

        intent.putExtra("moveColor", moveColor);
        intent.putExtra("fixedColor", fixedColor);
        intent.putExtra("activeColor", activeColor);
        intent.putExtra("canChooseColor", canChooseColor);
        intent.putExtra("cannotChooseColor", cannotChooseColor);
        intent.putExtra("otherColor", otherColor);

        intent.putExtra("pointSize", pointSize);
        intent.putExtra("label", label);
        intent.putExtra("textSize", textSize);
        intent.putExtra("factor", factor);
        intent.putExtra("signInfo", signInfo);

        setResult(RESULT_OK, intent);
        super.finish();
    }


    private void setValues() {

        Spinner spinner = (Spinner) findViewById(R.id.spinner3);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sizes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setSelection(getTextSize());

        Spinner spinner1 = (Spinner) findViewById(R.id.spinner2);
        spinner1.setAdapter(adapter);
        spinner1.setSelection(getPointSize());

        Button button2 = (Button) findViewById(R.id.button2);
        button2.setBackgroundColor(moveColor);

        Button button3 = (Button) findViewById(R.id.button3);
        button3.setBackgroundColor(fixedColor);

        Button button4 = (Button) findViewById(R.id.button4);
        button4.setBackgroundColor(activeColor);

        Button button5 = (Button) findViewById(R.id.button5);
        button5.setBackgroundColor(canChooseColor);

        Button button6 = (Button) findViewById(R.id.button6);
        button6.setBackgroundColor(cannotChooseColor);

        Button button7 = (Button) findViewById(R.id.button7);
        button7.setBackgroundColor(otherColor);

        Switch switch1 = (Switch) findViewById(R.id.switch1);
        switch1.setChecked(label);

        Switch show = (Switch) findViewById(R.id.show);
        show.setChecked(signInfo);


        SeekBar seekBar = (SeekBar) findViewById(R.id.factor);
        seekBar.setProgress((int) factor * 10);

    }

    private int getPointSize() {
        return pointSize;
    }

    private int getTextSize() {
        return textSize;
    }


}
