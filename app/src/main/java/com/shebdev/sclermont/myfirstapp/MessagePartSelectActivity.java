package com.shebdev.sclermont.myfirstapp;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.shebdev.sclermont.myfirstapp.adapter.MessagePartSelectRecyclerAdapter;
import com.shebdev.sclermont.myfirstapp.db.MessageDbHelper;
import com.shebdev.sclermont.myfirstapp.db.MessagePartData;

import java.util.ArrayList;


public class MessagePartSelectActivity extends ActionBarActivity {

    private RecyclerView mRecyclerView;
    private MessagePartSelectRecyclerAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        boolean loadGreeting = intent.getBooleanExtra(MyActivity.EXTRA_LOAD_GREETING, false);

        setContentView(R.layout.activity_message_part_select);
        MessageDbHelper dbHelper = new MessageDbHelper(getBaseContext());
        ArrayList<MessagePartData> mpdList = dbHelper.getAllMessagePart(loadGreeting);
        mRecyclerView = (RecyclerView) findViewById(R.id.message_part_select_recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MessagePartSelectRecyclerAdapter(mpdList);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_message_part_select, menu);
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
