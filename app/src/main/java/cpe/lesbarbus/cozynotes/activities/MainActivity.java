package cpe.lesbarbus.cozynotes.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import butterknife.Bind;
import butterknife.ButterKnife;
import cpe.lesbarbus.cozynotes.R;
import cpe.lesbarbus.cozynotes.adapter.NoteAdapter;
import cpe.lesbarbus.cozynotes.utils.CouchBaseNote;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SwipeRefreshLayout.OnRefreshListener {


    private NoteAdapter na = null;
    private CouchBaseNote couchDB = null;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Database init

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
        //set the size of action button
        _noteAction.setSize(FloatingActionButton.SIZE_MINI);
        _notebookAction.setSize(FloatingActionButton.SIZE_MINI);

        //drawer related
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Config Recyclerview et cards
        _recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        _recList.setLayoutManager(llm);

        couchDB = new CouchBaseNote(this);

        //adapter for recyclerview
         na = new NoteAdapter(couchDB.getAllNotes(),this);
        _recList.setAdapter(na);

        //refresh comportement for swipe refresh
        _spr.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        na.refreshData(couchDB.getAllNotes());
                        na.notifyDataSetChanged();
                        _spr.setRefreshing(false);
                    }
                });
            }
        });
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_settings_test) {
            Intent i = new Intent(this, TestActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.all_notes) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    na.refreshData(couchDB.getAllNotes());
                    na.notifyDataSetChanged();
                }
            }).start();

        } else if (id == R.id.all_notebooks) {
            //TODO
        } else if (id == R.id.disconnect) {
            //TODO
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onRefresh() {
        Toast.makeText(this, "Update asked", Toast.LENGTH_SHORT).show();
    }
}
