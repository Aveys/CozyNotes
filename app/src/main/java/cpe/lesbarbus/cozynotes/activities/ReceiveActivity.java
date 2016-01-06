package cpe.lesbarbus.cozynotes.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import cpe.lesbarbus.cozynotes.R;

public class ReceiveActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Intent receiveIntent = getIntent();
        String action = receiveIntent.getAction();
        if (Intent.ACTION_SEND.equals(action)){
            if (receiveIntent != null && receiveIntent.getType() != null && receiveIntent.getType().contains("image/")) {
                Uri uri = receiveIntent.getParcelableExtra(Intent.EXTRA_STREAM);
            }
            else if (receiveIntent != null && receiveIntent.getType() != null && receiveIntent.getType().contains("text/")) {
                String text = receiveIntent.getStringExtra(Intent.EXTRA_TEXT);
            }
        }
    }
}
