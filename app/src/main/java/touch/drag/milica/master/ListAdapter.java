package touch.drag.milica.master;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class ListAdapter extends BaseAdapter {
    private ArrayList<File> listDir;
    private LayoutInflater layoutInflater;

    public ListAdapter(Context context, ArrayList<File> files) {
        listDir = files;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listDir.size();
    }

    @Override
    public Object getItem(int position) {
        return listDir.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_row_layout, null);
            viewHolder = new ViewHolder();
            viewHolder.fileName = (TextView) convertView.findViewById(R.id.file_name);
            viewHolder.deleteFile = (ImageButton) convertView.findViewById(R.id.delete_file);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        String name = listDir.get(position).getName();
        viewHolder.fileName.setText(name.substring(0, name.length() - 3));
        final ListView listView = (ListView) parent;
        final View view = convertView;
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int a = 0; a < listView.getChildCount(); a++) {
                    listView.getChildAt(a).setBackgroundColor(Color.TRANSPARENT);
                    listView.setItemChecked(a, false);
                }

                listView.setItemChecked(position, true);
                view.setBackgroundColor(Color.GRAY);
            }
        });
        viewHolder.deleteFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File f = listDir.get(position);
                if (f.delete()) {
                    listDir.remove(position);
                    notifyDataSetChanged();
                }
            }
        });

        return convertView;
    }


    static private class ViewHolder {
        TextView fileName;
        ImageButton deleteFile;
    }

}
