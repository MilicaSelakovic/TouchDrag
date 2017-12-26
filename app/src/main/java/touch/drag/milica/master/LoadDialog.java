package touch.drag.milica.master;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
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

    public LoadDialog(Context context, Activity main) {
        super(context);
        this.main = main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.save_dialog);

        final ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        listView.setAdapter(new ListAdapter(main, new ArrayList<File>(Arrays.asList(getContext().getFilesDir().listFiles(new Filter())))));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int a = 0; a < parent.getChildCount(); a++) {
                    parent.getChildAt(a).setBackgroundColor(Color.TRANSPARENT);
                }

                view.setBackgroundColor(Color.GRAY);
            }
        });
        findViewById(R.id.button9).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });

        findViewById(R.id.button10).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File selected = (File) listView.getSelectedItem();

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
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                DrawingView view = (DrawingView) findViewById(R.id.view);

                view.load(commands);

                view.setUnsaved(false);

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
