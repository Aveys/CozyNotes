package cpe.lesbarbus.cozynotes.activities;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import cpe.lesbarbus.cozynotes.R;
import cpe.lesbarbus.cozynotes.authenticator.AccountGeneral;

public class TestActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);


        final FloatingActionButton actionB = (FloatingActionButton) findViewById(R.id.action_b);
        actionB.setSize(FloatingActionButton.SIZE_MINI);


        final FloatingActionButton actionA = (FloatingActionButton) findViewById(R.id.action_a);
        actionA.setSize(FloatingActionButton.SIZE_MINI);
        actionA.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                actionA.setTitle("Action A clicked");
            }
        });

    }
}