package cpe.lesbarbus.cozynotes.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import cpe.lesbarbus.cozynotes.R;
import cpe.lesbarbus.cozynotes.listener.CustomCardListener;
import cpe.lesbarbus.cozynotes.models.Note;
import cpe.lesbarbus.cozynotes.models.Notebook;
import cpe.lesbarbus.cozynotes.utils.CouchBaseNotebook;


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
                //final Note n = getItemAtPosition(tag);
               int tag = (int) v.findViewById(R.id.card_note_title).getTag();
                listener.onItemClick(v, getItemAtPosition(tag));
            }
        });
        return new NoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NoteViewHolder holder, final int position) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        CouchBaseNotebook cbk = new CouchBaseNotebook();
        Note n = noteList.get(position);
        System.out.println("Note "+n);
        holder._vTitle.setTag(position);
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
        if(n.getnotebookId()!=null && !n.getnotebookId().isEmpty()) {
            Notebook nbk = cbk.getNotebookById(n.getnotebookId());
            if(nbk!=null)
                holder._vNotebook.setText(nbk.getName());
            else
                holder._vNotebook.setText("No Notebook !");
        }
        else
            holder._vNotebook.setText("No Notebook !");
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
        TextView _vNotebook;
        public NoteViewHolder(View itemView) {
            super(itemView);

            _vTitle = (TextView) itemView.findViewById(R.id.card_note_title);
            _vText = (TextView) itemView.findViewById(R.id.card_note_text);
            _vDate = (TextView) itemView.findViewById(R.id.card_note_date);
            _vNotebook = (TextView) itemView.findViewById(R.id.card_note_notebook);
        }
    }
}
