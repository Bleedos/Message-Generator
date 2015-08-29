package com.shebdev.sclermont.myfirstapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by sclermont on 13/07/15.
 */
public class MessageDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "Message.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String BOOLEAN_TYPE =" BOOLEAN";
    private static final String NOT_NULL = " NOT NULL";
    private static final String DEFAULT = " DEFAULT";
    private static final String CHECK_IN_0_1 = " CHECK (${column_name} IN (0,1))";
    private static final String COLUMN_NAME_TOKEN = "${column_name}";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_MESSAGE_PART =
            "CREATE TABLE " + MessageContract.MessagePart.TABLE_NAME + " (" +
                    MessageContract.MessagePart._ID + " INTEGER PRIMARY KEY," +
                    MessageContract.MessagePart.COLUMN_NAME_PART_ID + TEXT_TYPE + COMMA_SEP +
                    MessageContract.MessagePart.COLUMN_NAME_TXT + TEXT_TYPE +
                    MessageContract.MessagePart.COLUMN_NAME_IS_GREETING + BOOLEAN_TYPE + NOT_NULL +
                    CHECK_IN_0_1.replace(COLUMN_NAME_TOKEN, MessageContract.MessagePart.COLUMN_NAME_IS_GREETING) +
                    ")";

    private static final String SQL_CREATE_PART_ASSEMBLY =
            "CREATE TABLE " + MessageContract.MessagePartAssembly.TABLE_NAME + " (" +
                    MessageContract.MessagePartAssembly._ID + " INTEGER PRIMARY KEY," +
                    MessageContract.MessagePartAssembly.COLUMN_NAME_ASSEMBLY_ID + TEXT_TYPE + COMMA_SEP +
                    MessageContract.MessagePartAssembly.COLUMN_NAME_ASSEMBLY_TITLE + TEXT_TYPE + COMMA_SEP +
                    MessageContract.MessagePartAssembly.COLUMN_NAME_ASSEMBLY_DESCRIPTION + TEXT_TYPE +
                    " )";

    private static final String SQL_CREATE_PART_ASSEMBLY_LINK =
            "CREATE TABLE " + MessageContract.MessagePartAssemblyLink.TABLE_NAME + " (" +
                    MessageContract.MessagePartAssemblyLink._ID + " INTEGER PRIMARY KEY," +
                    MessageContract.MessagePartAssemblyLink.COLUMN_NAME_LINK_ID + TEXT_TYPE + COMMA_SEP +
                    MessageContract.MessagePartAssemblyLink.COLUMN_NAME_ASSEMBLY_ID + INTEGER_TYPE + COMMA_SEP +
                    MessageContract.MessagePartAssemblyLink.COLUMN_NAME_PART_ID + INTEGER_TYPE + COMMA_SEP +
                    MessageContract.MessagePartAssemblyLink.COLUMN_NAME_PART_ORDER + INTEGER_TYPE +
                    " )";

    private static final String SQL_ALTER_MESSAGE_PART_V2_TO_V3 =
            "ALTER TABLE " + MessageContract.MessagePart.TABLE_NAME + " ADD COLUMN " +
                    MessageContract.MessagePart.COLUMN_NAME_IS_GREETING + BOOLEAN_TYPE + NOT_NULL +
                    CHECK_IN_0_1.replace(COLUMN_NAME_TOKEN, MessageContract.MessagePart.COLUMN_NAME_IS_GREETING) +
                    DEFAULT + " 0";

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
        switch (oldVersion) {
            case 2:
                db.execSQL(SQL_ALTER_MESSAGE_PART_V2_TO_V3);
                break;
            default:
                db.execSQL(SQL_DELETE_MESSAGE_PART);
                db.execSQL(SQL_DELETE_PART_ASSEMBLY);
                db.execSQL(SQL_DELETE_PART_ASSEMBLY_LINK);
                onCreate(db);
                break;
        }
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Don't try to preserve db info on downgrade.  Just recreate database.
        db.execSQL(SQL_DELETE_MESSAGE_PART);
        db.execSQL(SQL_DELETE_PART_ASSEMBLY);
        db.execSQL(SQL_CREATE_PART_ASSEMBLY_LINK);
        onCreate(db);
    }


    public long createMessagePart(String text, boolean isGreeting) {

        SQLiteDatabase db = this.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(MessageContract.MessagePart.COLUMN_NAME_PART_ID, System.currentTimeMillis());
        values.put(MessageContract.MessagePart.COLUMN_NAME_TXT, text);
        values.put(MessageContract.MessagePart.COLUMN_NAME_IS_GREETING, isGreeting ? 1 : 0);

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

    public MessagePartData getMessagePart(long id) {

        MessagePartData mpd = new MessagePartData();
        SQLiteDatabase db = this.getWritableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                MessageContract.MessagePart._ID,
                MessageContract.MessagePart.COLUMN_NAME_PART_ID,
                MessageContract.MessagePart.COLUMN_NAME_TXT,
                MessageContract.MessagePart.COLUMN_NAME_IS_GREETING
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                MessageContract.MessagePart.COLUMN_NAME_TXT + " DESC";
        String whereCols = MessageContract.MessagePart._ID + " = ?";
        String[] whereVals = { Long.toString(id) };

        Cursor c = db.query(
                MessageContract.MessagePart.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                whereCols,                                // The columns for the WHERE clause
                whereVals,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );


        c.moveToFirst();
        mpd.set_id(c.getLong(c.getColumnIndexOrThrow(MessageContract.MessagePart._ID)));
        mpd.setPartId(c.getString(c.getColumnIndexOrThrow(MessageContract.MessagePart.COLUMN_NAME_PART_ID)));
        mpd.setText(c.getString(c.getColumnIndexOrThrow(MessageContract.MessagePart.COLUMN_NAME_TXT)));
        mpd.setIsGreeting(c.getInt(c.getColumnIndexOrThrow(MessageContract.MessagePart.COLUMN_NAME_IS_GREETING)) == 1 ? true : false);
        c.close();

        return mpd;
    }

    public MessagePartData getMessagePartWhereTextIs(String text, boolean selectGreeting) {

        MessagePartData mpd = new MessagePartData();
        SQLiteDatabase db = this.getReadableDatabase();
        String whereCols;
        String[] whereVals;

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                MessageContract.MessagePart._ID,
                MessageContract.MessagePart.COLUMN_NAME_PART_ID,
                MessageContract.MessagePart.COLUMN_NAME_TXT,
                MessageContract.MessagePart.COLUMN_NAME_IS_GREETING
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                MessageContract.MessagePart.COLUMN_NAME_TXT + " ASC";

        if (selectGreeting) {
            whereCols = MessageContract.MessagePart.COLUMN_NAME_TXT + " = ? AND " +
                    MessageContract.MessagePart.COLUMN_NAME_IS_GREETING + " = ?";
            whereVals = new String[2];
            whereVals[0] = text;
            whereVals[1] = String.valueOf(1);
        }
        else {
            whereCols = MessageContract.MessagePart.COLUMN_NAME_TXT + " = ?";
            whereVals = new String[1];
            whereVals[0] = text;
        }

        Cursor c = db.query(
                MessageContract.MessagePart.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                whereCols,                                // The columns for the WHERE clause
                whereVals,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        if (c.getCount() > 0) {
            c.moveToFirst();
            mpd.set_id(c.getLong(c.getColumnIndexOrThrow(MessageContract.MessagePart._ID)));
            mpd.setPartId(c.getString(c.getColumnIndexOrThrow(MessageContract.MessagePart.COLUMN_NAME_PART_ID)));
            mpd.setText(c.getString(c.getColumnIndexOrThrow(MessageContract.MessagePart.COLUMN_NAME_TXT)));
            mpd.setIsGreeting(c.getInt(c.getColumnIndexOrThrow(MessageContract.MessagePart.COLUMN_NAME_IS_GREETING)) == 1 ? true : false);
        }
        else {
            mpd = null;
        }
        c.close();

        return mpd;
    }

    public long createPartAssembly(String title, String description) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MessageContract.MessagePartAssembly.COLUMN_NAME_ASSEMBLY_ID, System.currentTimeMillis());
        values.put(MessageContract.MessagePartAssembly.COLUMN_NAME_ASSEMBLY_TITLE, title);
        values.put(MessageContract.MessagePartAssembly.COLUMN_NAME_ASSEMBLY_DESCRIPTION, description);

        return db.insert(MessageContract.MessagePartAssembly.TABLE_NAME, null, values);
    }

    public void deletePartAssembly(long id) {

        SQLiteDatabase db = this.getWritableDatabase();

        // Define 'where' part of query.
        String selection = MessageContract.MessagePartAssembly._ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        db.delete(MessageContract.MessagePartAssembly.TABLE_NAME, selection, selectionArgs);
    }

    public int updatePartAssembly(long id, String description) {

        SQLiteDatabase db = this.getWritableDatabase();

        String selection = MessageContract.MessagePartAssembly._ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        ContentValues values = new ContentValues();
        values.put(MessageContract.MessagePartAssembly.COLUMN_NAME_ASSEMBLY_DESCRIPTION, description);

        // updating row
        return db.update(MessageContract.MessagePartAssembly.TABLE_NAME, values, selection, selectionArgs);
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

    public void deletePartAssemblyLink(long linkId) {

        SQLiteDatabase db = this.getWritableDatabase();

        // Define 'where' part of query.
        String selection = MessageContract.MessagePartAssemblyLink._ID + " = ?";
        String[] selectionArgs = {String.valueOf(linkId)};

        db.delete(MessageContract.MessagePartAssemblyLink.TABLE_NAME, selection, selectionArgs);
    }

    public void deletePartAssemblyLinkWhereAssemblyIdIs(String assemblyId) {

        SQLiteDatabase db = this.getWritableDatabase();

        // Define 'where' part of query.
        String selection = MessageContract.MessagePartAssemblyLink.COLUMN_NAME_ASSEMBLY_ID + " = ?";
        String[] selectionArgs = {assemblyId};

        db.delete(MessageContract.MessagePartAssemblyLink.TABLE_NAME, selection, selectionArgs);
    }

    public int updatePartAssemblyLink(long id, long order) {

        SQLiteDatabase db = this.getWritableDatabase();

        String selection = MessageContract.MessagePartAssemblyLink._ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        ContentValues values = new ContentValues();
        values.put(MessageContract.MessagePartAssemblyLink.COLUMN_NAME_PART_ORDER, order);

        // updating row
        return db.update(MessageContract.MessagePartAssemblyLink.TABLE_NAME, values, selection, selectionArgs);
    }

    public void deletePartAssemblyLinkFromMessagePartId(long id) {

        SQLiteDatabase db = this.getWritableDatabase();

        // Define 'where' part of query.
        String selection = MessageContract.MessagePartAssemblyLink.COLUMN_NAME_PART_ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        db.delete(MessageContract.MessagePartAssemblyLink.TABLE_NAME, selection, selectionArgs);

        // TODO: il faudrait peut etre faire le menage dans les assembly si ne contien plus aucune ligne
    }



    public ArrayList<MessagePartData> getAllMessagePart(boolean loadGreeting) {

        ArrayList<MessagePartData> messagePartDatas = new ArrayList<MessagePartData>();
        SQLiteDatabase db = this.getReadableDatabase();
        String whereCols = MessageContract.MessagePart.COLUMN_NAME_IS_GREETING + " = ?";
        String[] whereVals = new String[1];

        // Define a projection that specifies which columns from the database
// you will actually use after this query.
        String[] projection = {
                MessageContract.MessagePart._ID,
                MessageContract.MessagePart.COLUMN_NAME_PART_ID,
                MessageContract.MessagePart.COLUMN_NAME_TXT,
                MessageContract.MessagePart.COLUMN_NAME_IS_GREETING
        };

// How you want the results sorted in the resulting Cursor
        String sortOrder =
                MessageContract.MessagePart.COLUMN_NAME_TXT + " DESC";
        if (loadGreeting) {
            whereVals[0] = String.valueOf(1);
        }
        else {
            whereVals[0] = String.valueOf(0);
        }

        Cursor c = db.query(
                MessageContract.MessagePart.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                whereCols,                                // The columns for the WHERE clause
                whereVals,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                MessagePartData mpd = new MessagePartData();
                mpd.set_id(c.getLong(c.getColumnIndexOrThrow(MessageContract.MessagePart._ID)));
                mpd.setPartId(c.getString(c.getColumnIndexOrThrow(MessageContract.MessagePart.COLUMN_NAME_PART_ID)));
                mpd.setText(c.getString(c.getColumnIndexOrThrow(MessageContract.MessagePart.COLUMN_NAME_TXT)));
                mpd.setIsGreeting(c.getInt(c.getColumnIndexOrThrow(MessageContract.MessagePart.COLUMN_NAME_IS_GREETING)) == 1 ? true : false);

                messagePartDatas.add(mpd);
            } while (c.moveToNext());
        }

        c.close();

        return messagePartDatas;

    }

    public ArrayList<MessagePartData> getAllMessagePartExcept(ArrayList<Long> excludedMessageList) {
        return null;
        //TODO: coder methode  en fait mettre le code de la methode getAllMessagePart ici
        // et de l'autre méthode, appeler getAllMessagePartExcept en passant null
        // si la liste d'exception est nulle, on ne filtre rien, sinon, on n'ajoute pas les éléments filtrés

        //TODO: Est-ce qu'on pourrait faire un template pour gérer les méthodes getAll vu que probablement
        // juste les objets changent?  Ensuite avec la reflexion on call tous les setter?  Est-ce que
        // ça vaut la peine?

    }

    public MessageAssemblyData getAssemblyData(long id) {

        MessageAssemblyData mad = new MessageAssemblyData();
        SQLiteDatabase db = this.getWritableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                MessageContract.MessagePartAssembly._ID,
                MessageContract.MessagePartAssembly.COLUMN_NAME_ASSEMBLY_TITLE,
                MessageContract.MessagePartAssembly.COLUMN_NAME_ASSEMBLY_DESCRIPTION
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                MessageContract.MessagePartAssembly.COLUMN_NAME_ASSEMBLY_TITLE + " DESC";
        String whereCols = MessageContract.MessagePartAssembly._ID + " = ?";
        String[] whereVals = { Long.toString(id) };

        Cursor c = db.query(
                MessageContract.MessagePartAssembly.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                whereCols,                                // The columns for the WHERE clause
                whereVals,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );


        c.moveToFirst();
        mad.set_id(c.getLong(c.getColumnIndexOrThrow(MessageContract.MessagePartAssembly._ID)));
        mad.setTitle(c.getString(c.getColumnIndexOrThrow(MessageContract.MessagePartAssembly.COLUMN_NAME_ASSEMBLY_TITLE)));
        mad.setDescription(c.getString(c.getColumnIndexOrThrow(MessageContract.MessagePartAssembly.COLUMN_NAME_ASSEMBLY_DESCRIPTION)));
        c.close();

        return mad;
    }

    public MessageAssemblyData getAssemblyDataWhereTitleIs(String title) {

        MessageAssemblyData mad = new MessageAssemblyData();
        SQLiteDatabase db = this.getWritableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                MessageContract.MessagePartAssembly._ID,
                MessageContract.MessagePartAssembly.COLUMN_NAME_ASSEMBLY_TITLE,
                MessageContract.MessagePartAssembly.COLUMN_NAME_ASSEMBLY_DESCRIPTION
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                MessageContract.MessagePartAssembly.COLUMN_NAME_ASSEMBLY_TITLE + " DESC";
        String whereCols = MessageContract.MessagePartAssembly.COLUMN_NAME_ASSEMBLY_TITLE + " = ?";
        String[] whereVals = { title };

        Cursor c = db.query(
                MessageContract.MessagePartAssembly.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                whereCols,                                // The columns for the WHERE clause
                whereVals,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        if (c.getCount() > 0) {
            c.moveToFirst();
            mad.set_id(c.getLong(c.getColumnIndexOrThrow(MessageContract.MessagePartAssembly._ID)));
            mad.setTitle(c.getString(c.getColumnIndexOrThrow(MessageContract.MessagePartAssembly.COLUMN_NAME_ASSEMBLY_TITLE)));
            mad.setDescription(c.getString(c.getColumnIndexOrThrow(MessageContract.MessagePartAssembly.COLUMN_NAME_ASSEMBLY_DESCRIPTION)));
        }
        else {
            mad = null;
        }
        c.close();

        return mad;
    }
    

    // TODO: voir a mettre un filtre dans getall et si filtre = null on renvoie tout, sinon on garde juste ce qui match
    public ArrayList<MessageAssemblyData> getAllAssembly() {

        ArrayList<MessageAssemblyData> MessageAssemblyDatas = new ArrayList<MessageAssemblyData>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                MessageContract.MessagePartAssembly._ID,
                MessageContract.MessagePartAssembly.COLUMN_NAME_ASSEMBLY_ID,
                MessageContract.MessagePartAssembly.COLUMN_NAME_ASSEMBLY_TITLE,
                MessageContract.MessagePartAssembly.COLUMN_NAME_ASSEMBLY_DESCRIPTION
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                MessageContract.MessagePartAssembly.COLUMN_NAME_ASSEMBLY_TITLE + " ASC";

        Cursor c = db.query(
                MessageContract.MessagePartAssembly.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                MessageAssemblyData mad = new MessageAssemblyData();
                mad.set_id(c.getLong(c.getColumnIndexOrThrow(MessageContract.MessagePartAssembly._ID)));
                mad.setAssemblyId(c.getString(c.getColumnIndexOrThrow(MessageContract.MessagePartAssembly.COLUMN_NAME_ASSEMBLY_ID)));
                mad.setTitle(c.getString(c.getColumnIndexOrThrow(MessageContract.MessagePartAssembly.COLUMN_NAME_ASSEMBLY_TITLE)));
                mad.setDescription(c.getString(c.getColumnIndexOrThrow(MessageContract.MessagePartAssembly.COLUMN_NAME_ASSEMBLY_DESCRIPTION)));

                MessageAssemblyDatas.add(mad);
            } while (c.moveToNext());
        }

        c.close();

        return MessageAssemblyDatas;
    }

    public ArrayList<MessageAssemblyLinkData> getAssemblyLinkData(long id, boolean selectGreeting) {

        ArrayList<MessageAssemblyLinkData> msgAssemDatas = new ArrayList<MessageAssemblyLinkData>();
        SQLiteDatabase db = this.getReadableDatabase();
        StringBuilder whereCols = new StringBuilder();
        String[] whereVals = new String[2];

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                MessageContract.MessagePartAssemblyLink._ID,
                MessageContract.MessagePartAssemblyLink.COLUMN_NAME_ASSEMBLY_ID,
                MessageContract.MessagePartAssemblyLink.COLUMN_NAME_PART_ID,
                MessageContract.MessagePartAssemblyLink.COLUMN_NAME_PART_ORDER
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                MessageContract.MessagePartAssemblyLink.COLUMN_NAME_PART_ORDER + " ASC";

        whereCols.append(MessageContract.MessagePartAssemblyLink.COLUMN_NAME_ASSEMBLY_ID + " = ? AND ");
        whereCols.append(MessageContract.MessagePartAssemblyLink.COLUMN_NAME_PART_ORDER);
        whereVals[0] = Long.toString(id);
        whereVals[1] = Long.toString(0l);
        if (selectGreeting) {
            whereCols.append(" = ?");
        }
        else {
            whereCols.append(" > ?");
        }

        Cursor c = db.query(
                MessageContract.MessagePartAssemblyLink.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                whereCols.toString(),                                // The columns for the WHERE clause
                whereVals,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                MessageAssemblyLinkData mad = new MessageAssemblyLinkData();
                mad.setId(c.getLong(c.getColumnIndexOrThrow(MessageContract.MessagePartAssemblyLink._ID)));
                mad.setAssemblyId(c.getString(c.getColumnIndexOrThrow(MessageContract.MessagePartAssemblyLink.COLUMN_NAME_ASSEMBLY_ID)));
                mad.setPartId(c.getString(c.getColumnIndexOrThrow(MessageContract.MessagePartAssemblyLink.COLUMN_NAME_PART_ID)));
                mad.setPartOrder(c.getString(c.getColumnIndexOrThrow(MessageContract.MessagePartAssemblyLink.COLUMN_NAME_PART_ORDER)));

                msgAssemDatas.add(mad);
            } while (c.moveToNext());
        }

        c.close();

        return msgAssemDatas;
    }

    public MessageAssemblyLinkData getAssemblyLinkDataWhereAssemblyIdPartId(String assemblyId, String partId) {

        MessageAssemblyLinkData mald = null;
        SQLiteDatabase db = this.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                MessageContract.MessagePartAssemblyLink._ID,
                MessageContract.MessagePartAssemblyLink.COLUMN_NAME_ASSEMBLY_ID,
                MessageContract.MessagePartAssemblyLink.COLUMN_NAME_PART_ID,
                MessageContract.MessagePartAssemblyLink.COLUMN_NAME_PART_ORDER
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                MessageContract.MessagePartAssemblyLink.COLUMN_NAME_PART_ORDER + " ASC";
        String whereCols = MessageContract.MessagePartAssemblyLink.COLUMN_NAME_ASSEMBLY_ID + " = ? AND " +
                           MessageContract.MessagePartAssemblyLink.COLUMN_NAME_PART_ID + " = ?";
        String[] whereVals = { assemblyId, partId };

        Cursor c = db.query(
                MessageContract.MessagePartAssemblyLink.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                whereCols,                                // The columns for the WHERE clause
                whereVals,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            mald = new MessageAssemblyLinkData();
            mald.setId(c.getLong(c.getColumnIndexOrThrow(MessageContract.MessagePartAssemblyLink._ID)));
            mald.setAssemblyId(c.getString(c.getColumnIndexOrThrow(MessageContract.MessagePartAssemblyLink.COLUMN_NAME_ASSEMBLY_ID)));
            mald.setPartId(c.getString(c.getColumnIndexOrThrow(MessageContract.MessagePartAssemblyLink.COLUMN_NAME_PART_ID)));
            mald.setPartOrder(c.getString(c.getColumnIndexOrThrow(MessageContract.MessagePartAssemblyLink.COLUMN_NAME_PART_ORDER)));
        }

        c.close();

        return mald;
    }


}
