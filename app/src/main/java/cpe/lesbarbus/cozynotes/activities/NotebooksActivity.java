package cpe.lesbarbus.cozynotes.activities;

import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.util.Attributes;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.io.IOException;
import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;
import cpe.lesbarbus.cozynotes.R;
import cpe.lesbarbus.cozynotes.adapter.NotebookAdapter;
import cpe.lesbarbus.cozynotes.authenticator.AccountGeneral;
import cpe.lesbarbus.cozynotes.authenticator.CozyServerAuthenticate;
import cpe.lesbarbus.cozynotes.models.Notebook;
import cpe.lesbarbus.cozynotes.utils.CouchBaseNote;
import cpe.lesbarbus.cozynotes.utils.CouchBaseNotebook;

public class NotebooksActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private NotebookAdapter na = null;
    private CouchBaseNotebook cbk;
    private CouchBaseNote cbn;

    @Bind(R.id.toolbar)
    Toolbar _toolbar;
    @Bind(R.id.drawer_layout)
    DrawerLayout _drawer;
    @Bind(R.id.multiple_actions)
    FloatingActionsMenu _fam;
    @Bind(R.id.add_note_action)
    com.getbase.floatingactionbutton.FloatingActionButton _noteAction;
    @Bind(R.id.add_notebook_action)
    com.getbase.floatingactionbutton.FloatingActionButton _notebookAction;
    @Bind(R.id.list_notebooks)
    ListView _listNotebook;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notebooks);

        //bind widget (ButterKnife lib)
        ButterKnife.bind(this);

        //Toolbar Init
        setSupportActionBar(_toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, _drawer, _toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        _drawer.setDrawerListener(toggle);
        toggle.syncState();

        //FloatingActionMenu Init
        _noteAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), CreateNoteActivity.class);
                _fam.collapseImmediately();
                startActivityForResult(i, 1);
            }
        });
        _notebookAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] name = {null};
                AlertDialog.Builder builder = new AlertDialog.Builder(NotebooksActivity.this);
                builder.setCancelable(true);

                View vw = getLayoutInflater().inflate(R.layout.dialog_addnotebook, null , false);
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
                            _fam.toggle();
                            na.refreshData(cbk.getAllNotebooks());
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //DO NOTHING
                    }
                });
                builder.show();
            }
        });
        //set the size of action button
        _noteAction.setSize(FloatingActionButton.SIZE_MINI);
        _notebookAction.setSize(FloatingActionButton.SIZE_MINI);

        //drawer related
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        cbk = new CouchBaseNotebook();
        na = new NotebookAdapter(this,cbk.getAllNotebooks());
        na.setMode(Attributes.Mode.Single);
        _listNotebook.setAdapter(na);


    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.all_notes) {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
            finish();

        } else if (id == R.id.all_notebooks) {
            Intent i = new Intent(getApplicationContext(), NotebooksActivity.class);
            startActivity(i);
        } else if (id == R.id.disconnect) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    AccountManager accountManager = AccountManager.get(NotebooksActivity.this);
                    CozyServerAuthenticate sserver = new CozyServerAuthenticate();
                    Looper.prepare();
                    try {
                        sserver.userSignoff(AccountGeneral.getPassOwnerFromAccount(NotebooksActivity.this),AccountGeneral.getUrlFromAccount(NotebooksActivity.this));
                        accountManager.removeAccount(AccountGeneral.getAccountCozy(NotebooksActivity.this), new AccountManagerCallback<Boolean>() {
                            @Override
                            public void run(AccountManagerFuture<Boolean> future) {
                                try {
                                    Boolean isSuppressed = future.getResult();
                                    Intent  i = new Intent(NotebooksActivity.this,SplahScreenActivity.class);
                                    startActivity(i);
                                    finish();
                                } catch (OperationCanceledException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (AuthenticatorException e) {
                                    e.printStackTrace();
                                }

                            }
                        },null);

                    } catch (Exception e) {
                        Log.e("MainActivity", Arrays.toString(e.getStackTrace()));
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(NotebooksActivity.this, R.string.error_signoff, Toast.LENGTH_LONG).show();
                            }
                        });

                    }
                }
            }).start();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        na.refreshData(cbk.getAllNotebooks());
    }
}
