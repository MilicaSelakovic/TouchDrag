package touch.drag.milica.master;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

import java.io.FileOutputStream;
import java.sql.Time;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Timer;
import java.util.Vector;

import geometry.calculations.descretegeometrycalculations.PointInformations;

public class MainActivity extends AppCompatActivity {

    private PointInformations pointInformations;
    private float factor = 1f;
    private int state = 0;

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    //  Log.i("OpenCV", "OpenCV loaded successfully");

                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pointInformations = new PointInformations();

        ConstructionParser parser = new ConstructionParser();

        HashMap<String, Vector<String>> trics = new HashMap<>();

        parser.fillConstructions(trics, getApplicationContext(), "allconstuctions.json");

        DrawingView view = ((DrawingView) this.findViewById(R.id.view));
        view.setTrics(trics);
        view.setDensity(getResources().getDisplayMetrics().density, getResources().getDisplayMetrics().densityDpi);
        view.setTextView((TextView) this.findViewById(R.id.textView));

        this.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DrawingView) findViewById(R.id.view)).clearPanel();
            }
        });
        this.findViewById(R.id.undo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DrawingView) findViewById(R.id.view)).undo();
            }
        });
        this.findViewById(R.id.redo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DrawingView) findViewById(R.id.view)).redo();
            }
        });

        this.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });
        this.findViewById(R.id.settings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Setting.class);

                intent.putExtra("moveColor", pointInformations.getMoveColor());
                intent.putExtra("fixedColor", pointInformations.getFixedColor());
                intent.putExtra("activeColor", pointInformations.getActiveColor());
                intent.putExtra("canChooseColor", pointInformations.getCanChooseColor());
                intent.putExtra("cannotChooseColor", pointInformations.getCannotChooseColor());
                intent.putExtra("otherColor", pointInformations.getOtherColor());

                intent.putExtra("pointSize", pointInformations.getPointSize());
                intent.putExtra("label", pointInformations.isLabel());
                intent.putExtra("textSize", pointInformations.getTextSize());
                intent.putExtra("factor", factor);
                intent.putExtra("signInfo", pointInformations.isShowSignInfo());

                startActivityForResult(intent, 17);
            }
        });

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                state += 1;
                state = state % 3;

                switch (state) {
                    case 1:
                        ((DrawingView) findViewById(R.id.view)).setMode(DrawingView.Mode.MODE_MOVE);
                        fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.moveFloatBackGround)));
                        fab.setImageResource(R.drawable.ic_action_move);
                        break;
                    case 2:
                        ((DrawingView) findViewById(R.id.view)).setMode(DrawingView.Mode.MODE_SELECT);
                        fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.selectFloatBackGround)));
                        fab.setImageResource(R.drawable.ic_action_select);
                        break;
                    default:
                        ((DrawingView) findViewById(R.id.view)).setMode(DrawingView.Mode.MODE_USUAL);
                        fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.drawFloatBackGround)));
                        fab.setImageResource(R.drawable.ic_action_draw);

                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 17) {
            Bundle extras = data.getExtras();

            pointInformations.setMoveColor(extras.getInt("moveColor"));
            pointInformations.setFixedColor(extras.getInt("fixedColor"));
            pointInformations.setActiveColor(extras.getInt("activeColor"));
            pointInformations.setCanChooseColor(extras.getInt("canChooseColor"));
            pointInformations.setCannotChooseColor(extras.getInt("cannotChooseColor"));
            pointInformations.setOtherColor(extras.getInt("otherColor"));

            pointInformations.setPointSize(extras.getFloat("pointSize"));
            pointInformations.setLabel(extras.getBoolean("label"));
            pointInformations.setTextSize(extras.getFloat("textSize"));
            pointInformations.setShowSignInfo(extras.getBoolean("signInfo"));
            factor = extras.getFloat("factor");
            DrawingView view = (DrawingView) findViewById(R.id.view);
            view.setPointInformations(pointInformations);
            view.setSenisitivityFactor(factor);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    public void save() {

        AlertDialog dialog = new AlertDialog.Builder(MainActivity.this).create();
        dialog.setTitle("Chose project name");

        EditText et = new EditText(getApplicationContext());

        et.setText("Untitled");

        dialog.setView(et, 30, 10, 30, 10);


        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "DISCARD",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "SAVE",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DrawingView view = (DrawingView) findViewById(R.id.view);
                        String array = view.save();

                        String filename = "Untilted" + Calendar.getInstance().getTime().toString();
                        FileOutputStream outputStream;

                        try {
                            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                            outputStream.write(array.getBytes());
                            outputStream.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        String[] fajlovi = getFilesDir().list();
                        Log.d("svi fajlovi", fajlovi.toString());

                        dialog.cancel();
                    }
                });
        dialog.show();

    }

}
