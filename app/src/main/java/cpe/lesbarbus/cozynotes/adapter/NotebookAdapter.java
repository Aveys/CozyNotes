package cpe.lesbarbus.cozynotes.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.daimajia.swipe.adapters.ArraySwipeAdapter;

import java.util.List;

import cpe.lesbarbus.cozynotes.R;
import cpe.lesbarbus.cozynotes.models.Note;
import cpe.lesbarbus.cozynotes.models.Notebook;

/**
 * Created by p_rib_000 on 05/01/2016.
 */
public class NotebookAdapter extends ArraySwipeAdapter<Notebook>{
    public NotebookAdapter(Context context, int resource) {
        super(context, resource);
    }

    public NotebookAdapter(Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }

    public NotebookAdapter(Context context, int resource, Notebook[] objects) {
        super(context, resource, objects);
    }

    public NotebookAdapter(Context context, int resource, int textViewResourceId, Notebook[] objects) {
        super(context, resource, textViewResourceId, objects);
    }

    public NotebookAdapter(Context context, int resource, List<Notebook> objects) {
        super(context, resource, objects);
    }

    public NotebookAdapter(Context context, int resource, int textViewResourceId, List<Notebook> objects) {
        super(context, resource, textViewResourceId, objects);
    }

    /*private List<Notebook> notebookList;
    private final Context mContext;

    public NotebookAdapter(List<Notebook> list , Context context){
        super(context, R.layout.list_notebook, list);
        this.mContext = context;
        this.notebookList = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Notebook item = getItem(position);
        LayoutInflater layoutInf = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView == null) {
            convertView = layoutInf.inflate(R.layout.list_notebook, parent, false);
        }
        TextView name = (TextView) convertView.findViewById(R.id.list_notebook_name);
        name.setText(item.getName());
        return convertView;
    }*/

    /**
     * Refresh the list of notes
     * @param list the new list of notes
     */
    public void refreshData(List<Notebook> list){
        this.notebookList = list;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.notebooks_swipelayout;
    }
}
