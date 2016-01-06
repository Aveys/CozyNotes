package cpe.lesbarbus.cozynotes.activities;

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
import cpe.lesbarbus.cozynotes.models.Note;

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
            }
        });
        _deletebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(tag, "delete button pressed");
            }
        });
        _backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
