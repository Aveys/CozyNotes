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
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.couchbase.lite.CouchbaseLiteException;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.io.IOException;
import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;
import cpe.lesbarbus.cozynotes.R;
import cpe.lesbarbus.cozynotes.adapter.NoteAdapter;
import cpe.lesbarbus.cozynotes.authenticator.AccountGeneral;
import cpe.lesbarbus.cozynotes.authenticator.CozyServerAuthenticate;
import cpe.lesbarbus.cozynotes.fragment.NoteDetailDialog;
import cpe.lesbarbus.cozynotes.listener.CustomCardListener;
import cpe.lesbarbus.cozynotes.models.Note;
import cpe.lesbarbus.cozynotes.models.Notebook;
import cpe.lesbarbus.cozynotes.synctasks.CloudExport;
import cpe.lesbarbus.cozynotes.synctasks.CloudImport;
import cpe.lesbarbus.cozynotes.utils.CouchBaseManager;
import cpe.lesbarbus.cozynotes.utils.CouchBaseNote;
import cpe.lesbarbus.cozynotes.utils.CouchBaseNotebook;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SwipeRefreshLayout.OnRefreshListener {

    private NoteAdapter na = null;
    private CouchBaseNotebook cbk;
    private CouchBaseNote cbn;

    @Bind(R.id.toolbar)
    Toolbar _toolbar;
    @Bind(R.id.drawer_layout)
    DrawerLayout _drawer;
    @Bind(R.id.multiple_actions)
    FloatingActionsMenu _fam;
    @Bind(R.id.add_note_action)
    FloatingActionButton _noteAction;
    @Bind(R.id.add_notebook_action)
    FloatingActionButton _notebookAction;
    @Bind(R.id.cards_note_recent_list)
    RecyclerView _recList;
    @Bind(R.id.recent_list_swiperefresh)
    SwipeRefreshLayout _spr;

    private boolean mIsLargeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setTitle(getString(R.string.R_main_appbar_title));
        //Database init
        try {
            CouchBaseManager.setDatabaseInstance(this);
        } catch (IOException | CouchbaseLiteException e) {
            Toast.makeText(this, R.string.error_database_load,Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        cbk = new CouchBaseNotebook();
        cbn = new CouchBaseNote();

        //bind widget (ButterKnife lib)
        ButterKnife.bind(this);
        //large screen ?
        mIsLargeLayout = getResources().getBoolean(R.bool.large_layout);
        //Toolbar Init



        /*_im_bt_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(na.getItemAtPosition(0).getContent()).toString());
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });*/


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
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setCancelable(true);

                View vw = getLayoutInflater().inflate(R.layout.dialog_addnotebook, null, false);
                final EditText notebookName = (EditText) vw.findViewById(R.id.dialog_addNotebook_edit);
                builder.setView(vw);
                builder.setTitle(R.string.notebook_title);
                builder.setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        name[0] = notebookName.getText().toString();
                        if (TextUtils.isEmpty(name[0])) {
                            Toast.makeText(getApplicationContext(), R.string.empty_input_title, Toast.LENGTH_LONG).show();
                        } else {
                            Notebook newNB = new Notebook(name[0]);
                            cbk.createNotebook(newNB);
                            _fam.toggle();
                        }
                    }
                });
                builder.setNegativeButton(R.string.dialog_button_cancel, new DialogInterface.OnClickListener() {

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

        //Config Recyclerview & cards
        _recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        _recList.setLayoutManager(llm);

        //adapter for recyclerview
         na = new NoteAdapter(cbn.getAllNotes(), this, new CustomCardListener() {
             @Override
             public void onItemClick(View v, Note n) {
                 Intent detailIntent = new Intent(MainActivity.this, NoteDetailActivity.class);
                detailIntent.putExtra("note", n);
                 startActivity(detailIntent);
             }
         });
        _recList.setAdapter(na);

        //refresh comportement for swipe refresh
        _spr.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        na.refreshData(cbn.getAllNotes());
                        na.notifyDataSetChanged();
                        _spr.setRefreshing(false);
                    }
                });
            }
        });


        //Receive shared COntent
        // Get intent, action and MIME type
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        //Call apropriate handling methods
        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                handleSendText(intent);
            }
        } else {
            // Handle other intents, such as being started from the home screen
        }
    }

    /**
     * Function that receives the text send by third party app
     * @param intent
     */
    void handleSendText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
            // Update UI to reflect text being shared
            Intent i = new Intent(this, CreateNoteActivity.class);
            i.putExtra("text_shared",sharedText);
            startActivity(i);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.all_notes) {
           runOnUiThread(new Runnable() {
               @Override
               public void run() {
                   na.refreshData(cbn.getAllNotes());
                   na.notifyDataSetChanged();
               }
           });

        } else if (id == R.id.all_notebooks) {
            Intent i = new Intent(getApplicationContext(), NotebooksActivity.class);
            startActivity(i);
        } else if (id == R.id.get_from_cloud){


        } else if (id == R.id.disconnect) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    AccountManager accountManager = AccountManager.get(MainActivity.this);
                    CozyServerAuthenticate sserver = new CozyServerAuthenticate();
                    Looper.prepare();
                    try {
                        sserver.userSignoff(AccountGeneral.getPassOwnerFromAccount(MainActivity.this),AccountGeneral.getUrlFromAccount(MainActivity.this));
                        accountManager.removeAccount(AccountGeneral.getAccountCozy(MainActivity.this), new AccountManagerCallback<Boolean>() {
                            @Override
                            public void run(AccountManagerFuture<Boolean> future) {
                                try {
                                    Boolean isSuppressed = future.getResult();
                                    Intent i = new Intent(MainActivity.this, SplahScreenActivity.class);
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
                        }, null);
                        CouchBaseManager.destroyDatabase();

                    } catch (Exception e) {
                        Log.e("MainActivity", Arrays.toString(e.getStackTrace()));
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, R.string.error_signoff, Toast.LENGTH_LONG).show();
                            }
                        });

                    }
                }
            }).start();

        } else if(id == R.id.export_from_cloud){
            new CloudExport().execute(this);

        } else if(id == R.id.import_from_cloud){
            new CloudImport().execute(this);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onRefresh() {
        Toast.makeText(this, R.string.update_asked, Toast.LENGTH_SHORT).show();
    }

    public void showDialog(Note n) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        NoteDetailDialog newFragment = NoteDetailDialog.newInstance(n);

        if (mIsLargeLayout) {
            // The device is using a large layout, so show the fragment as a dialog
            newFragment.show(fragmentManager, "dialog");
        } else {
            // The device is smaller, so show the fragment fullscreen
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            // For a little polish, specify a transition animation
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            // To make it fullscreen, use the 'content' root view as the container
            // for the fragment, which is always the root view for the activity
            transaction.add(android.R.id.content, newFragment)
                    .addToBackStack(null).commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        na.refreshData(cbn.getAllNotes());
        na.notifyDataSetChanged();
    }
}
