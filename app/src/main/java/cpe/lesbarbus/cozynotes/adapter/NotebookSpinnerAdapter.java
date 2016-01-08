package cpe.lesbarbus.cozynotes.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import cpe.lesbarbus.cozynotes.R;
import cpe.lesbarbus.cozynotes.models.Notebook;

/**
 * Created by arthurveys on 05/01/16.
 */
public class NotebookSpinnerAdapter extends ArrayAdapter<Notebook> {

    private List<Notebook> notebookList;
    private final Context mContext;

    public NotebookSpinnerAdapter(Context context, int resource, List<Notebook> listNotebook) {
        super(context, resource, listNotebook);
        this.mContext = context;
        this.notebookList = listNotebook;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Notebook item = getItem(position);
        LayoutInflater layoutInf = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = layoutInf.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        }
        TextView name = (TextView) convertView.findViewById(android.R.id.text1);
        name.setText(item.getName());
        if(item.getName().equals(mContext.getString(R.string.create_notebook))){
            name.setTag("creation_notebook");
        }
        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            //inflate your customlayout for the textview
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        }
        //put the data in it
        Notebook item = notebookList.get(position);
        if (item != null) {
            TextView text1 = (TextView) row.findViewById(android.R.id.text1);
            text1.setTextColor(Color.BLACK);
            text1.setText(item.getName());
        }

        return row;
    }

    /**
     * Refresh the list of notes
     *
     * @param list the new list of notes
     */
    public void refreshData(List<Notebook> list) {
        this.notebookList = list;
    }

}

