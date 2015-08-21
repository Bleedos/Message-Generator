package com.shebdev.sclermont.myfirstapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by sclermont on 13/07/15.
 */
public class MessageDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "Message.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_MESSAGE_PART =
            "CREATE TABLE " + MessageContract.MessagePart.TABLE_NAME + " (" +
                    MessageContract.MessagePart._ID + " INTEGER PRIMARY KEY," +
                    MessageContract.MessagePart.COLUMN_NAME_PART_ID + TEXT_TYPE + COMMA_SEP +
                    MessageContract.MessagePart.COLUMN_NAME_TXT + TEXT_TYPE + " )";

    private static final String SQL_CREATE_PART_ASSEMBLY =
            "CREATE TABLE " + MessageContract.MessagePartAssembly.TABLE_NAME + " (" +
                    MessageContract.MessagePartAssembly._ID + " INTEGER PRIMARY KEY," +
                    MessageContract.MessagePartAssembly.COLUMN_NAME_ASSEMBLY_ID + TEXT_TYPE + COMMA_SEP +
                    MessageContract.MessagePartAssembly.COLUMN_NAME_ASSEMBLY_TITLE + TEXT_TYPE + COMMA_SEP +
                    MessageContract.MessagePartAssembly.COLUMN_NAME_ASSEMBLY_DESCRIPTION + TEXT_TYPE + " )";

    private static final String SQL_CREATE_PART_ASSEMBLY_LINK =
            "CREATE TABLE " + MessageContract.MessagePartAssemblyLink.TABLE_NAME + " (" +
                    MessageContract.MessagePartAssemblyLink._ID + " INTEGER PRIMARY KEY," +
                    MessageContract.MessagePartAssemblyLink.COLUMN_NAME_LINK_ID + TEXT_TYPE + COMMA_SEP +
                    MessageContract.MessagePartAssemblyLink.COLUMN_NAME_ASSEMBLY_ID + INTEGER_TYPE + COMMA_SEP +
                    MessageContract.MessagePartAssemblyLink.COLUMN_NAME_PART_ID + INTEGER_TYPE + COMMA_SEP +
                    MessageContract.MessagePartAssemblyLink.COLUMN_NAME_PART_ORDER + INTEGER_TYPE + " )";

    private static final String SQL_DELETE_MESSAGE_PART =
            "DROP TABLE IF EXISTS " + MessageContract.MessagePart.TABLE_NAME;
    private static final String SQL_DELETE_PART_ASSEMBLY =
            "DROP TABLE IF EXISTS " + MessageContract.MessagePartAssembly.TABLE_NAME;
    private static final String SQL_DELETE_PART_ASSEMBLY_LINK =
            "DROP TABLE IF EXISTS " + MessageContract.MessagePartAssemblyLink.TABLE_NAME;

    public MessageDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(SQL_CREATE_MESSAGE_PART);
        db.execSQL(SQL_CREATE_PART_ASSEMBLY);
        db.execSQL(SQL_CREATE_PART_ASSEMBLY_LINK);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_CREATE_PART_ASSEMBLY);
        db.execSQL(SQL_CREATE_PART_ASSEMBLY_LINK);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_PART_ASSEMBLY);
        db.execSQL(SQL_CREATE_PART_ASSEMBLY_LINK);
    }


    public long createMessagePart(String text) {

        SQLiteDatabase db = this.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(MessageContract.MessagePart.COLUMN_NAME_PART_ID, System.currentTimeMillis());
        values.put(MessageContract.MessagePart.COLUMN_NAME_TXT, text);

        // Insert the new row, returning the primary key value of the new row
        return db.insert(MessageContract.MessagePart.TABLE_NAME, null, values);
    }

    public void deleteMessagePart(long id) {

        SQLiteDatabase db = this.getWritableDatabase();

        // Define 'where' part of query.
        String selection = MessageContract.MessagePart._ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        db.delete(MessageContract.MessagePart.TABLE_NAME, selection, selectionArgs);
    }

    public long createPartAssembly(String title, String description) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MessageContract.MessagePartAssembly.COLUMN_NAME_ASSEMBLY_ID, System.currentTimeMillis());
        values.put(MessageContract.MessagePartAssembly.COLUMN_NAME_ASSEMBLY_TITLE, title);
        values.put(MessageContract.MessagePartAssembly.COLUMN_NAME_ASSEMBLY_DESCRIPTION, title);

        return db.insert(MessageContract.MessagePartAssembly.TABLE_NAME, null, values);
    }

    public long createPartAssemblyLink(long assemblyId, long partId, long partOrder) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MessageContract.MessagePartAssemblyLink.COLUMN_NAME_LINK_ID, System.currentTimeMillis());
        values.put(MessageContract.MessagePartAssemblyLink.COLUMN_NAME_ASSEMBLY_ID, assemblyId);
        values.put(MessageContract.MessagePartAssemblyLink.COLUMN_NAME_PART_ID, partId);
        values.put(MessageContract.MessagePartAssemblyLink.COLUMN_NAME_PART_ORDER, partOrder);

        return db.insert(MessageContract.MessagePartAssemblyLink.TABLE_NAME, null, values);
    }

}
