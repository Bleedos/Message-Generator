package com.shebdev.sclermont.myfirstapp.db;

import android.provider.BaseColumns;

/**
 * Created by sclermont on 13/07/15.
 */
public final class MessageContract  {

    public MessageContract() {}

    public static abstract class MessagePart implements BaseColumns {
        public static final String TABLE_NAME = "part";
        public static final String COLUMN_NAME_PART_ID = "partid";
        public static final String COLUMN_NAME_TXT = "txt";
    }
}
