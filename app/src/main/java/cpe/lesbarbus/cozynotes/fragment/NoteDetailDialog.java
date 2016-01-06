package cpe.lesbarbus.cozynotes.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import cpe.lesbarbus.cozynotes.R;
import cpe.lesbarbus.cozynotes.activities.EditNoteActivity;
import cpe.lesbarbus.cozynotes.models.Note;


public class NoteDetailDialog extends AppCompatDialogFragment {
    private final String TAG = "NoteDetailDialog-Fragment";

    private TextView _title;
    private TextView _content;
    private TextView _date;
    private ImageButton _close;
    private ImageButton _edit;
    private ImageButton _delete;

    Note note;

    public static NoteDetailDialog newInstance(Note n){
        NoteDetailDialog myFragment = new NoteDetailDialog();

        Bundle args = new Bundle();
        args.putSerializable("note",n);
        myFragment.setArguments(args);

        return myFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        note= (Note) getArguments().getSerializable("note");
    }

    /** The system calls this to get the DialogFragment's layout, regardless
     of whether it's being displayed as a dialog or an embedded fragment. */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout to use as dialog or embedded fragment
        return inflater.inflate(R.layout.fragment_note_detail_dialog, container, false);

    }

    /** The system calls this only when creating the layout in a dialog. */
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // The only reason you might override this method when using onCreateView() is
        // to modify any dialog characteristics. For example, the dialog includes a
        // title by default, but your custom layout might not need it. So here you can
        // remove the dialog title, but you must call the superclass to get the Dialog.
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_ACTION_BAR);

        _title.setText(note.getTitle());
        _content.setText(note.getContent());
        _date.setText(note.getFormattedDateTime());

        _close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        _edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(getTag(),"Edit button pressed");
                Intent editIntent = new Intent(getActivity(), EditNoteActivity.class);
                editIntent.putExtra("note", note);
                getActivity().startActivity(editIntent);
                dismiss();
            }
        });
        _delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(getTag(),"delete button pressed");
            }
        });
        return dialog;
    }
}
