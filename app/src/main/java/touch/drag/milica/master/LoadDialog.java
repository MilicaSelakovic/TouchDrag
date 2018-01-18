package touch.drag.milica.master;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

public class LoadDialog extends Dialog {
    private Activity main;
    private DrawingView drawingView;

    public LoadDialog(Context context, Activity main, DrawingView drawingView) {
        super(context);
        this.main = main;
        this.drawingView = drawingView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.save_dialog);

        final ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        listView.setAdapter(new ListAdapter(main, new ArrayList<>(Arrays.asList(getContext().getFilesDir().listFiles(new Filter())))));

        findViewById(R.id.button10).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });

        findViewById(R.id.button9).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = listView.getCheckedItemPosition();

                File selected = (File) listView.getAdapter().getItem(selectedId);
                if (selected == null) {
                    cancel();
                    return;
                }

                Vector<String> commands = new Vector<>();
                commands.clear();
                try {
                    FileReader reader = new FileReader(selected);

                    BufferedReader br = new BufferedReader(reader);
                    String line;
                    while ((line = br.readLine()) != null) {
                        commands.add(line);
                    }

                } catch (FileNotFoundException e) {
                    final AlertDialog alertDialog = new AlertDialog.Builder(main).create();

                    alertDialog.setMessage("File not found exception\n" + e.getMessage());

                    alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "CLOSE", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            alertDialog.cancel();
                        }
                    });

                    cancel();
                    e.printStackTrace();
                } catch (IOException e) {
                    final AlertDialog alertDialog = new AlertDialog.Builder(main).create();

                    alertDialog.setMessage("IOException\n" + e.getMessage());

                    alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "CLOSE", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            alertDialog.cancel();
                        }
                    });
                    cancel();
                    e.printStackTrace();
                }

                drawingView.load(commands);

                cancel();
            }
        });

    }

    public class Filter implements FileFilter {

        @Override
        public boolean accept(File pathname) {
            return pathname.getName().contains(".tg");
        }
    }
}
