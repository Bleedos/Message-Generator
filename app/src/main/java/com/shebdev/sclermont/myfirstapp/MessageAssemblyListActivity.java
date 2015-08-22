package com.shebdev.sclermont.myfirstapp;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.shebdev.sclermont.myfirstapp.adapter.MessageAssemblyListRecyclerAdapter;
import com.shebdev.sclermont.myfirstapp.adapter.MessagePartRecyclerAdapter;
import com.shebdev.sclermont.myfirstapp.db.MessageAssemblyData;
import com.shebdev.sclermont.myfirstapp.db.MessageDbHelper;
import com.shebdev.sclermont.myfirstapp.db.MessagePartData;

import java.util.ArrayList;


public class MessageAssemblyListActivity extends ActionBarActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_assembly_list);

        ArrayList<StringBuilder> list = new ArrayList<StringBuilder>();
        MessageDbHelper dbHelper = new MessageDbHelper(getBaseContext());
        ArrayList<MessageAssemblyData> mpdList = dbHelper.getAllAssembly();
        for (MessageAssemblyData mpd : mpdList) {
            list.add(new StringBuilder(mpd.getTitle() + "::"+mpd.get_id()));
            // TODO: Au lieu d'ajouter juste les "title", ajouter l'objet au complet et modifier l'adapter
            // pour recevoir les obj au complet.  le dataset devra tout contenir car sur un click, on pourra
            // vouloir obtenir une information non affichée et s'en servir
        }
        mRecyclerView = (RecyclerView) findViewById(R.id.message_assembly_list_recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MessageAssemblyListRecyclerAdapter(mpdList);
        mRecyclerView.setAdapter(mAdapter);
        //assemblyId
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_message_assembly_list, menu);
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
        }

        return super.onOptionsItemSelected(item);
    }
}
