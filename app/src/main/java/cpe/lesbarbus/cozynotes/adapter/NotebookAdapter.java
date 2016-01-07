package cpe.lesbarbus.cozynotes.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.ArraySwipeAdapter;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;

import java.util.List;

import cpe.lesbarbus.cozynotes.R;
import cpe.lesbarbus.cozynotes.models.Note;
import cpe.lesbarbus.cozynotes.models.Notebook;

/**
 * Created by p_rib_000 on 05/01/2016.
 */
public class NotebookAdapter extends BaseSwipeAdapter {


    private List<Notebook> notebookList;
    private final Context mContext;
    /*
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
    }

    /**
     * Refresh the list of notes
     * @param list the new list of notes
     */
    /*public void refreshData(List<Notebook> list){
        this.notebookList = list;
    }*/

    public NotebookAdapter(Context mContext, List<Notebook> notebookList) {
        this.mContext = mContext;
        this.notebookList = notebookList;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.notebooks_swipelayout;
    }

    @Override
    public View generateView(int position, ViewGroup parent) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.list_notebook, null);
        SwipeLayout swipeLayout = (SwipeLayout)v.findViewById(getSwipeLayoutResourceId(position));
        swipeLayout.addSwipeListener(new SimpleSwipeListener() {
            @Override
            public void onOpen(SwipeLayout layout) {
                YoYo.with(Techniques.Tada).duration(500).delay(100).playOn(layout.findViewById(R.id.notebooks_deletebutton));
            }
        });
        swipeLayout.setOnDoubleClickListener(new SwipeLayout.DoubleClickListener() {
            @Override
            public void onDoubleClick(SwipeLayout layout, boolean surface) {
                Toast.makeText(mContext, "DoubleClick", Toast.LENGTH_SHORT).show();
            }
        });
        v.findViewById(R.id.notebooks_deletebutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "click delete", Toast.LENGTH_SHORT).show();
            }
        });
        return v;
    }

    @Override
    public void fillValues(int position, View convertView) {

        TextView t = (TextView)convertView.findViewById(R.id.notebooks_title);
        t.setText(notebookList.get(position).getName());

    }

    @Override
    public int getCount() {
        return notebookList.size();
    }

    @Override
    public Object getItem(int position) {
        return notebookList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
