package touch.drag.milica.master;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.util.HashMap;
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

        pointInformations = new PointInformations(getResources().getDisplayMetrics().densityDpi);

        ConstructionParser parser = new ConstructionParser();

        HashMap<String, Vector<String>> trics = new HashMap<>();

        parser.fillConstructions(trics, getApplicationContext(), "allconstuctions.json");

        final DrawingController view = ((DrawingController) this.findViewById(R.id.view));
        view.setTrics(trics);
        view.setDensity(getResources().getDisplayMetrics().density, getResources().getDisplayMetrics().densityDpi);
        view.setTextView((TextView) this.findViewById(R.id.textView));

        this.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DrawingController) findViewById(R.id.view)).clearPanel();
            }
        });
        this.findViewById(R.id.undo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DrawingController) findViewById(R.id.view)).undo();
            }
        });
        this.findViewById(R.id.redo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DrawingController) findViewById(R.id.view)).redo();
            }
        });

        this.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });

        this.findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open();
            }
        });

        this.findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.center();

                view.invalidate();
            }
        });

        this.findViewById(R.id.button8).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Help.class);
                startActivity(intent);
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

                intent.putExtra("pointSize", pointInformations.getPointSizeFactor());
                intent.putExtra("label", pointInformations.isLabel());
                intent.putExtra("textSize", pointInformations.getTextSizeFactor());
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
                        ((DrawingController) findViewById(R.id.view)).setMode(DrawingController.Mode.MODE_MOVE);
                        fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.moveFloatBackGround)));
                        fab.setImageResource(R.drawable.ic_action_move);
                        break;
                    case 2:
                        ((DrawingController) findViewById(R.id.view)).setMode(DrawingController.Mode.MODE_SELECT);
                        fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.selectFloatBackGround)));
                        fab.setImageResource(R.drawable.ic_action_select);
                        break;
                    default:
                        ((DrawingController) findViewById(R.id.view)).setMode(DrawingController.Mode.MODE_USUAL);
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

            pointInformations.setPointSize(extras.getInt("pointSize"));
            pointInformations.setLabel(extras.getBoolean("label"));
            pointInformations.setTextSize(extras.getInt("textSize"));
            pointInformations.setShowSignInfo(extras.getBoolean("signInfo"));
            factor = extras.getFloat("factor");
            DrawingController view = (DrawingController) findViewById(R.id.view);
            view.setPointInformations(pointInformations);
            view.setSenisitivityFactor(factor);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    public void save() {

        AlertDialog dialog = new AlertDialog.Builder(MainActivity.this).create();
        dialog.setTitle("Chose project name");

        final EditText et = new EditText(getApplicationContext());

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
                        final String filename = et.getText() + ".tg";

                        String[] names = getFilesDir().list(new FilenameFilter() {
                            @Override
                            public boolean accept(File dir, String name) {
                                return name.compareTo(filename) == 0;
                            }
                        });

                        if (names.length > 0) {
                            final AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                            alertDialog.setMessage("Project with this name exists. Do you want to overwrite it?");

                            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "DISCARD", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    alertDialog.cancel();
                                }
                            });

                            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OVERWRITE", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    FileOutputStream outputStream;
                                    DrawingController view = (DrawingController) findViewById(R.id.view);
                                    String array = view.save();

                                    try {
                                        outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                                        outputStream.write(array.getBytes());
                                        outputStream.close();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    view.setUnsaved(false);
                                    alertDialog.cancel();
                                }
                            });

                            alertDialog.show();

                        } else {

                            FileOutputStream outputStream;
                            DrawingController view = (DrawingController) findViewById(R.id.view);
                            String array = view.save();

                            try {
                                outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                                outputStream.write(array.getBytes());
                                outputStream.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            view.setUnsaved(false);

                        }


                        dialog.cancel();
                    }
                });
        dialog.show();

    }

    public void open() {
        DrawingController view = (DrawingController) findViewById(R.id.view);
        if (view.isUnsaved()) {
            final AlertDialog alertDialog = new AlertDialog.Builder(this).create();

            alertDialog.setMessage("There is unsaved changes. Do you want to save them?");

            alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "DISCARD", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    alertDialog.cancel();
                }
            });

            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "SAVE", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    alertDialog.cancel();
                    save();
                }
            });
        }
        LoadDialog dialog = new LoadDialog(this, this, view);
        dialog.show();
        view.setUnsaved(false);

    }

}
