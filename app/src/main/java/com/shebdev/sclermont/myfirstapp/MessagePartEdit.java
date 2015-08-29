package com.shebdev.sclermont.myfirstapp;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;


public class MessagePartEdit extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_part_edit);

        Intent intent = getIntent();
        String messagePart = intent.getStringExtra(MyActivity.EXTRA_MESSAGE_PART);

        if (messagePart != null) {
            EditText messagePartEdit = (EditText) findViewById(R.id.message_part_edit);
            messagePartEdit.setText(messagePart);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_message_part_edit, menu);
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

    public void annulerMessageEdit(View view) {
        finish();
    }

    public void okMessageEdit(View view) {
        EditText messagePart = (EditText) findViewById(R.id.message_part_edit);
        Intent returnIntent = new Intent();
        returnIntent.putExtra("messagePart",messagePart.getText().toString());
        setResult(RESULT_OK, returnIntent);
        finish();
    }

}
