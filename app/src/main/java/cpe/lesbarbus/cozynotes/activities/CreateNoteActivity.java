package cpe.lesbarbus.cozynotes.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;
import cpe.lesbarbus.cozynotes.R;
import cpe.lesbarbus.cozynotes.adapter.NotebookAdapter;
import cpe.lesbarbus.cozynotes.adapter.NotebookSpinnerAdapter;
import cpe.lesbarbus.cozynotes.models.Note;
import cpe.lesbarbus.cozynotes.models.Notebook;
import cpe.lesbarbus.cozynotes.utils.CouchBaseManager;
import cpe.lesbarbus.cozynotes.utils.CouchBaseNote;
import cpe.lesbarbus.cozynotes.utils.CouchBaseNotebook;
import io.github.mthli.knife.KnifeTagHandler;
import io.github.mthli.knife.KnifeText;

public class CreateNoteActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    @Bind(R.id.knife)
    KnifeText _knife;
    @Bind(R.id.note_create_title)
    EditText _title;
    @Bind(R.id.note_create_notebook_spinner)
    Spinner _spinner;
    @Bind(R.id.note_create_toolbar)
    Toolbar _toolbar;
    @Bind(R.id.save_note_button)
    ImageButton _saveButton;

    private ArrayAdapter<Notebook> adapter;
    private CouchBaseNote cbn;
    private CouchBaseNotebook cbk;
    private ArrayList<Notebook> list;
    private String text_shared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        ButterKnife.bind(this);


        setSupportActionBar(_toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        if(getIntent().getSerializableExtra("text_shared") != null) {
            text_shared = (String) getIntent().getSerializableExtra("text_shared");
            _knife.setText(text_shared);
            _knife.setSelection(_knife.getEditableText().length());
        }
        else
        {
            // Use async would better; ImageGetter coming soon...
            _knife.setText(Html.fromHtml("<p> write text here</p>", null, new KnifeTagHandler()));
            _knife.setSelection(_knife.getEditableText().length());
        }
        cbn = new CouchBaseNote();
        cbk = new CouchBaseNotebook();
        list = (ArrayList<Notebook>) cbk.getAllNotebooks();
        list.add(new Notebook("Create a Notebook ...."));

        adapter = new NotebookSpinnerAdapter(this, android.R.layout.simple_spinner_dropdown_item, list);
        _spinner.setAdapter(adapter);
        _spinner.setOnItemSelectedListener(this);

        setupBold();
        setupItalic();
        setupUnderline();
        setupStrikethrough();
        setupBullet();
        setupQuote();
        setupLink();
        setupClear();

        _saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(_title.getText().toString().isEmpty())
                    Toast.makeText(getApplicationContext(),"Title can't be empty",Toast.LENGTH_SHORT).show();
                else if(_knife.getText().toString().isEmpty())
                    Toast.makeText(getApplicationContext(),"Content can't be empty",Toast.LENGTH_SHORT).show();
                else{
                    Note n = new Note();
                    n.setContent(_knife.toHtml());
                    n.setTitle(_title.getText().toString());
                    n.setCurrentDatetime();

                    Notebook nb = (Notebook) _spinner.getSelectedItem();
                    n.setNotebookId(nb.get_id());
                    cbn.createNote(n);
                    finish();
                }

            }
        });

    }

    private void setupBold() {
        ImageButton bold = (ImageButton) findViewById(R.id.bold);

        bold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _knife.bold(!_knife.contains(KnifeText.FORMAT_BOLD));
            }
        });
    }

    private void setupItalic() {
        ImageButton italic = (ImageButton) findViewById(R.id.italic);

        italic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _knife.italic(!_knife.contains(KnifeText.FORMAT_ITALIC));
            }
        });
    }

    private void setupUnderline() {
        ImageButton underline = (ImageButton) findViewById(R.id.underline);

        underline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _knife.underline(!_knife.contains(KnifeText.FORMAT_UNDERLINED));
            }
        });
    }

    private void setupStrikethrough() {
        ImageButton strikethrough = (ImageButton) findViewById(R.id.strikethrough);

        strikethrough.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _knife.strikethrough(!_knife.contains(KnifeText.FORMAT_STRIKETHROUGH));
            }
        });
    }

    private void setupBullet() {
        ImageButton bullet = (ImageButton) findViewById(R.id.bullet);

        bullet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _knife.bullet(!_knife.contains(KnifeText.FORMAT_BULLET));
            }
        });

    }

    private void setupQuote() {
        ImageButton quote = (ImageButton) findViewById(R.id.quote);

        quote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _knife.quote(!_knife.contains(KnifeText.FORMAT_QUOTE));
            }
        });

    }

    private void setupLink() {
        ImageButton link = (ImageButton) findViewById(R.id.link);

        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLinkDialog();
            }
        });
    }

    private void setupClear() {
        ImageButton clear = (ImageButton) findViewById(R.id.clear);


        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(_knife.getText().toString());
                _knife.clearFormats();
            }
        });
    }

    private void showLinkDialog() {
        final int start = _knife.getSelectionStart();
        final int end = _knife.getSelectionEnd();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);

        View view = getLayoutInflater().inflate(R.layout.dialog_link, null, false);
        final EditText editText = (EditText) view.findViewById(R.id.edit);
        builder.setView(view);
        builder.setTitle(R.string.dialog_title);

        builder.setPositiveButton(R.string.dialog_button_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String link = editText.getText().toString().trim();
                if (TextUtils.isEmpty(link)) {
                    return;
                }
                // When KnifeText lose focus, use this method
                _knife.link(link, start, end);
            }
        });

        builder.setNegativeButton(R.string.dialog_button_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // DO NOTHING HERE
            }
        });

        builder.create().show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //IF add notebook selected
        Notebook nb = (Notebook) parent.getItemAtPosition(position);
        if (nb.getName().equals("Create a Notebook ....")) {
            final String[] name = {null};
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(true);

            View vw = getLayoutInflater().inflate(R.layout.dialog_addnotebook, parent, false);
            final EditText notebookName = (EditText) vw.findViewById(R.id.dialog_addNotebook_edit);
            builder.setView(vw);
            builder.setTitle("Notebook title");
            builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    name[0] = notebookName.getText().toString();
                    if (TextUtils.isEmpty(name[0])) {
                        Toast.makeText(getApplicationContext(), "Tittle cannot be empty", Toast.LENGTH_LONG).show();
                    } else {
                        Notebook newNB = new Notebook(name[0]);
                        cbk.createNotebook(newNB);
                        adapter.add(newNB);
                        int pos = getAdapterItemPosition(newNB);
                        _spinner.setSelection(pos);

                    }
                }
            });
            if(cbn.getNotesCount()>0) {
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //DO NOTHING
                    }
                });
            }
            builder.show();
        }
        else{
            _spinner.setSelection(position);
        }

    }

    private int getAdapterItemPosition(Notebook  nb) {
        for(int position=0;position<adapter.getCount();position++){
            if(adapter.getItem(position).equals(nb)){
                return position;
            }
        }
        return 0;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
