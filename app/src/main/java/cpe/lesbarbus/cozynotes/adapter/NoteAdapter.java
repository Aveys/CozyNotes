package cpe.lesbarbus.cozynotes.adapter;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import cpe.lesbarbus.cozynotes.R;
import cpe.lesbarbus.cozynotes.activities.EditNoteActivity;
import cpe.lesbarbus.cozynotes.activities.MainActivity;
import cpe.lesbarbus.cozynotes.listener.CustomCardListener;
import cpe.lesbarbus.cozynotes.models.Note;
import cpe.lesbarbus.cozynotes.utils.CouchBaseNote;

/**
 * Created by arthurveys on 29/12/15.
 */
public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder>{

    private List<Note> noteList;
    private final Context mContext;
    private final CustomCardListener listener;
    private static final String TAG = "NoteAdapter";

    public NoteAdapter(List<Note> noteList, Context c,CustomCardListener ccl) {
        this.noteList = noteList;
        this.mContext = c;
        this.listener=ccl;
    }


    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardlayout_note,parent,false);
        final NoteViewHolder nvh = new NoteViewHolder(itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v, nvh.getPosition());
            }
        });
        return new NoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NoteViewHolder holder, final int position) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        Note n = noteList.get(position);
        holder._delete.setTag(position);
        holder._delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO : replace this dialog by a toast with cancel button
                Integer tag = (Integer) v.getTag();
                final Note n = getItemAtPosition(tag);
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CouchBaseNote db = new CouchBaseNote();
                        db.deleteNote(n.get_id());
                        noteList.remove(position);
                        notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //DO NOTHING
                    }
                });
                builder.setMessage("Are you sure you want to delete this note ?");
                builder.setTitle("Delete a note");
                builder.create().show();


            }
        });
        holder._edit.setTag(position);
        holder._edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editIntent = new Intent(mContext, EditNoteActivity.class);
                Note note = getItemAtPosition(position);
                editIntent.putExtra("note", note);
                mContext.startActivity(editIntent);
            }
        });
        if(!n.getTitle().isEmpty())
            holder._vTitle.setText(n.getTitle());
        else
            holder._vTitle.setText("Untitled");
        if(n.getDatetime() != null)
            holder._vDate.setText(sdf.format(n.getDatetime()));
        else
            holder._vDate.setText("No date");
        if(!n.getContent().isEmpty())
            holder._vText.setText(Html.fromHtml(n.getContent()));
        else
            holder._vText.setText("No content");
    }

    /**
     * Refresh the list of notes
     * @param list the new list of notes
     */
    public void refreshData(List<Note> list){
        this.noteList = list;
    }

    /**
     * return a Note from the position in the list
     * @param position the position in the list
     * @return a note object
     */
    public Note getItemAtPosition(int position){
        return noteList.get(position);
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    /**
     * Note View Holder
     */
    public static class NoteViewHolder extends RecyclerView.ViewHolder{


        TextView _vTitle;
        TextView _vText;
        TextView _vDate;
        Button _delete;
        Button _edit;
        public NoteViewHolder(View itemView) {
            super(itemView);
            _vTitle = (TextView) itemView.findViewById(R.id.card_note_title);
            _vText = (TextView) itemView.findViewById(R.id.card_note_text);
            _vDate = (TextView) itemView.findViewById(R.id.card_note_date);
            _delete = (Button) itemView.findViewById(R.id.card_note_delete);
            _edit = (Button) itemView.findViewById(R.id.card_note_edit);
        }
    }
}
