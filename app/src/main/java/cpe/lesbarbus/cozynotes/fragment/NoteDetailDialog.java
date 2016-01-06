package cpe.lesbarbus.cozynotes.fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import cpe.lesbarbus.cozynotes.R;



public class NoteDetailDialog extends AppCompatDialogFragment {

    @Bind(R.id.fragment_detail_title)
    TextView _title;
    @Bind(R.id.fragment_detail_content)
    TextView _content;
    @Bind(R.id.fragment_detail_date)
    TextView _Date;
    @Bind(R.id.fragment_detail_closebutton)
    ImageButton _close;
    @Bind(R.id.fragment_detail_editbutton)
    ImageButton _edit;

    /** The system calls this to get the DialogFragment's layout, regardless
     of whether it's being displayed as a dialog or an embedded fragment. */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout to use as dialog or embedded fragment
        ButterKnife.bind(this,container);
        return inflater.inflate(R.layout.fragment_note_detail_dialog, container, false);

    }

    /** The system calls this only when creating the layout in a dialog. */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // The only reason you might override this method when using onCreateView() is
        // to modify any dialog characteristics. For example, the dialog includes a
        // title by default, but your custom layout might not need it. So here you can
        // remove the dialog title, but you must call the superclass to get the Dialog.
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_ACTION_BAR);
        return dialog;
    }
}
