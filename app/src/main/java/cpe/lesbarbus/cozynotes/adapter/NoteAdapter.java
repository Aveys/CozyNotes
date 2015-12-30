package cpe.lesbarbus.cozynotes.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cpe.lesbarbus.cozynotes.R;
import cpe.lesbarbus.cozynotes.models.Note;

/**
 * Created by arthurveys on 29/12/15.
 */
public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder>{

    private List<Note> noteList;

    public NoteAdapter(List<Note> noteList) {
        this.noteList = noteList;
    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardlayout_note,parent,false);
        return new NoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NoteViewHolder holder, int position) {
        Note n = noteList.get(position);
        if(!n.getTitle().isEmpty())
            holder.vTitle.setText(n.getTitle());
        else
            holder.vTitle.setText("Untitled");
        if(n.getDatetime() != null)
            holder.vDate.setText(n.getDatetime().toString());
        else
            holder.vDate.setText("No date");
        if(!n.getContent().isEmpty())
            holder.vText.setText(Html.fromHtml(n.getContent()));
        else
            holder.vText.setText("No content");
    }
    public void refreshData(List<Note> list){
        this.noteList = list;
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }
    public static class NoteViewHolder extends RecyclerView.ViewHolder{


        TextView vTitle;
        TextView vText;
        TextView vDate;
        public NoteViewHolder(View itemView) {
            super(itemView);
            vTitle = (TextView) itemView.findViewById(R.id.card_note_title);
            vText = (TextView) itemView.findViewById(R.id.card_note_text);
            vDate = (TextView) itemView.findViewById(R.id.card_note_date);
        }
    }
}
