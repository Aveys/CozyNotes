package cpe.lesbarbus.cozynotes.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import cpe.lesbarbus.cozynotes.R;
import cpe.lesbarbus.cozynotes.adapter.NoteAdapter;
import cpe.lesbarbus.cozynotes.models.Note;
import cpe.lesbarbus.cozynotes.utils.CouchBaseNote;

public class NoteDetailActivity extends AppCompatActivity {

    private final String tag ="NoteDetailActivity";

    @Bind(R.id.note_detail_title)
    TextView _title;
    @Bind(R.id.note_detail_content)
    TextView _content;
    @Bind(R.id.note_detail_date)
    TextView _date;
    @Bind(R.id.note_detail_backbutton)
    ImageButton _backbutton;
    @Bind(R.id.note_detail_sharebutton)
    ImageButton _sharebutton;
    @Bind(R.id.note_detail_editbutton)
    ImageButton _editbutton;
    @Bind(R.id.note_detail_deletebutton)
    ImageButton _deletebutton;
    private Note note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);
        ButterKnife.bind(this);

        note = (Note) getIntent().getSerializableExtra("note");
        _title.setText(note.getTitle());
        _content.setText(Html.fromHtml(note.getContent()));
        _date.setText(note.getFormattedDateTime());

        _editbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editIntent = new Intent(NoteDetailActivity.this, EditNoteActivity.class);
                editIntent.putExtra("note", note);
                NoteDetailActivity.this.startActivity(editIntent);
                finish();
            }
        });
        _deletebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(NoteDetailActivity.this);
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CouchBaseNote db = new CouchBaseNote();
                        db.deleteNote(note.get_id());
                        finish();

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
        _backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        _sharebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(note.getContent()).toString());
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
