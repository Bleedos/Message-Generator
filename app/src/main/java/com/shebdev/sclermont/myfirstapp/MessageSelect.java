package com.shebdev.sclermont.myfirstapp;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.shebdev.sclermont.myfirstapp.db.MessageContract;
import com.shebdev.sclermont.myfirstapp.db.MessageDbHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MessageSelect extends ListActivity {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            MessageDbHelper dbHelper = new MessageDbHelper(getBaseContext());
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            // Define a projection that specifies which columns from the database
// you will actually use after this query.
            String[] projection = {
                MessageContract.MessagePart._ID,
                MessageContract.MessagePart.COLUMN_NAME_PART_ID,
                MessageContract.MessagePart.COLUMN_NAME_TXT
            };

// How you want the results sorted in the resulting Cursor
            String sortOrder =
                MessageContract.MessagePart.COLUMN_NAME_TXT + " DESC";

            Cursor c = db.query(
                MessageContract.MessagePart.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
            );

            startManagingCursor(c);
            // now create a new list adapter bound to the cursor.
            // SimpleListAdapter is designed for binding to a Cursor.
            final ListAdapter adapter = new SimpleCursorAdapter(this, // Context.
                android.R.layout.two_line_list_item, // Specify the row template
                // to use (here, two
                // columns bound to the
                // two retrieved cursor
                // rows).
                c, // Pass in the cursor to bind to.
                // Array of cursor columns to bind to.
                new String[] { MessageContract.MessagePart._ID,
                        MessageContract.MessagePart.COLUMN_NAME_TXT },
                // Parallel array of which template objects to bind to those
                // columns.
                new int[] { android.R.id.text1, android.R.id.text2 });

            // Bind to our new adapter.
            setListAdapter(adapter);
            ListView listView = getListView();
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {


                    MessageDbHelper dbHelper = new MessageDbHelper(getBaseContext());
                    SQLiteDatabase db = dbHelper.getReadableDatabase();

                    // Define a projection that specifies which columns from the database
// you will actually use after this query.
                    String[] projection = {
                            MessageContract.MessagePart._ID,
                            MessageContract.MessagePart.COLUMN_NAME_PART_ID,
                            MessageContract.MessagePart.COLUMN_NAME_TXT
                    };

// How you want the results sorted in the resulting Cursor
                    String sortOrder =
                            MessageContract.MessagePart.COLUMN_NAME_TXT + " DESC";

                    Cursor c = db.query(
                            MessageContract.MessagePart.TABLE_NAME,  // The table to query
                            projection,                               // The columns to return
                            null,                                // The columns for the WHERE clause
                            null,                            // The values for the WHERE clause
                            null,                                     // don't group the rows
                            null,                                     // don't filter by row groups
                            sortOrder                                 // The sort order
                    );

                    c.moveToPosition(position);
                    long itemId = c.getLong(
                            c.getColumnIndexOrThrow(MessageContract.MessagePart._ID)
                    );

                    String txt = c.getString( c.getColumnIndexOrThrow(MessageContract.MessagePart.COLUMN_NAME_TXT));

                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("message",txt);
                    setResult(RESULT_OK,returnIntent);
                    finish();
                }
            });

            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view,
                                               int position, long id) {


                    MessageDbHelper dbHelper = new MessageDbHelper(getBaseContext());
                    SQLiteDatabase db = dbHelper.getWritableDatabase();

                    // Define 'where' part of query.
                    /*String selection = MessageContract.MessagePart.COLUMN_NAME_PART_ID + " LIKE ?";
// Specify arguments in placeholder order.
                    String[] selectionArgs = { String.valueOf(id) };
// Issue SQL statement.
                    db.delete(MessageContract.MessagePart.TABLE_NAME, selection, selectionArgs);*/

                    Toast.makeText(MessageSelect.this,
                            "Item in position " + position + " clicked",
                            Toast.LENGTH_LONG).show();
                    // Return true to consume the click event. In this case the
                    // onListItemClick listener is not called anymore.

                    return true;
                }
            });
        }



}
