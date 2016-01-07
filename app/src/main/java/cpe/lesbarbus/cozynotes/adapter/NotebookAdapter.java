package cpe.lesbarbus.cozynotes.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import java.util.ArrayList;
import java.util.List;

import cpe.lesbarbus.cozynotes.R;
import cpe.lesbarbus.cozynotes.activities.NoteByNotebookActivity;
import cpe.lesbarbus.cozynotes.models.Note;
import cpe.lesbarbus.cozynotes.models.Notebook;
import cpe.lesbarbus.cozynotes.utils.CouchBaseNote;
import cpe.lesbarbus.cozynotes.utils.CouchBaseNotebook;

/**
 * Created by p_rib_000 on 05/01/2016.
 */
public class NotebookAdapter extends BaseSwipeAdapter {


    private List<Notebook> notebookList;
    private final Context mContext;

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
        final View v = LayoutInflater.from(mContext).inflate(R.layout.list_notebook, null);
        final SwipeLayout swipeLayout = (SwipeLayout)v.findViewById(getSwipeLayoutResourceId(position));
        swipeLayout.addSwipeListener(new SimpleSwipeListener() {
            @Override
            public void onOpen(SwipeLayout layout) {
                YoYo.with(Techniques.Tada).duration(500).delay(100).playOn(layout.findViewById(R.id.notebooks_deletebutton));
            }
        });
        swipeLayout.setOnDoubleClickListener(new SwipeLayout.DoubleClickListener() {
            @Override
            public void onDoubleClick(SwipeLayout layout, boolean surface) {

            }
        });
        swipeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int pos = (int) v.findViewById(R.id.notebooks_deletebutton).getTag();
                Notebook notebook = (Notebook) getItem(pos);
                System.out.println("item : " + pos);
                System.out.println("Notebook : " + notebook.toString());
                Intent intent = new Intent(mContext,NoteByNotebookActivity.class);
                intent.putExtra("notebook",notebook);
                mContext.startActivity(intent);
            }
        });
        v.findViewById(R.id.notebooks_deletebutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int pos = (int) view.findViewById(R.id.notebooks_deletebutton).getTag();
                System.out.println("Position in tag : "+pos);
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setPositiveButton(R.string.alertdialog_delete_action, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Notebook nb = (Notebook) getItem(pos);
                        CouchBaseNote cbn = new CouchBaseNote();
                        CouchBaseNotebook cbk = new CouchBaseNotebook();

                        for (Note n : cbn.getAllNotesByNotebook(nb.get_id())){
                            cbn.deleteNote(n.get_id());
                        }
                        cbk.deleteNotebook(nb.get_id());
                        swipeLayout.close();
                        notebookList.remove(nb);
                        notifyDataSetChanged();


                    }
                });
                builder.setNegativeButton(R.string.alertdialog_cancel_action, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //DO NOTHING
                    }
                });
                builder.setMessage(R.string.alertdialog_delete_notebook_message);
                builder.setTitle(R.string.alertdialog_delete_notebook_title);
                builder.create().show();
            }
        });
        return v;
    }

    @Override
    public void fillValues(int position, View convertView) {
        convertView.findViewById(R.id.notebooks_title).setTag(position);
        convertView.findViewById(R.id.notebooks_deletebutton).setTag(position);
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

    /**
     * refresh data and Ui of the adapter
     * @param l the new list of Notebooks
     */
    public void refreshData(List<Notebook> l){
        this.notebookList=l;
        this.notifyDataSetChanged();
    }
}
