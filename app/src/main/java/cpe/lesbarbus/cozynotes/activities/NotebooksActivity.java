package cpe.lesbarbus.cozynotes.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cpe.lesbarbus.cozynotes.R;
import cpe.lesbarbus.cozynotes.adapter.NoteAdapter;
import cpe.lesbarbus.cozynotes.adapter.NotebookAdapter;
import cpe.lesbarbus.cozynotes.models.Notebook;
import cpe.lesbarbus.cozynotes.utils.CouchBaseNote;
import cpe.lesbarbus.cozynotes.utils.CouchBaseNotebook;

public class NotebooksActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private NotebookAdapter na = null;
    private CouchBaseNotebook couchDB = null;

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
        //FloatingActionMenu Init
        /*_notebookAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), CreateNotebookActivity.class);
                _fam.collapseImmediately();
                startActivityForResult(i, 1);
            }
        });*/
        //set the size of action button
        _noteAction.setSize(com.getbase.floatingactionbutton.FloatingActionButton.SIZE_MINI);
        _notebookAction.setSize(com.getbase.floatingactionbutton.FloatingActionButton.SIZE_MINI);

        //drawer related
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        couchDB = new CouchBaseNotebook();
        na = new NotebookAdapter(couchDB.getAllNotebooks(), this);
        _listNotebook.setAdapter(na);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.all_notes) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    na.refreshData(couchDB.getAllNotebooks());
                    na.notifyDataSetChanged();
                }
            }).start();

        } else if (id == R.id.all_notebooks) {
            Intent i = new Intent(getApplicationContext(), NotebooksActivity.class);
            startActivity(i);
        } else if (id == R.id.disconnect) {
            //TODO
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
