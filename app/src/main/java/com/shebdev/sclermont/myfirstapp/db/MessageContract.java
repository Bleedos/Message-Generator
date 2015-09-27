package com.shebdev.sclermont.myfirstapp.db;

import android.provider.BaseColumns;

import java.security.PublicKey;

/**
 * Created by sclermont on 13/07/15.
 */
public final class MessageContract  {

    public MessageContract() {}

    public static abstract class MessagePart implements BaseColumns {
        public static final String TABLE_NAME = "part";
        public static final String COLUMN_NAME_PART_ID = "partid";
        public static final String COLUMN_NAME_TXT = "txt";
        public static final String COLUMN_NAME_IS_GREETING = "isgreeting";
        public static final String COLUMN_NAME_AUDIO_FILE_NAME = "audiofilename";
    }

    public static abstract class MessagePartAssembly implements BaseColumns {
        public static final String TABLE_NAME = "assembly";
        public static final String COLUMN_NAME_ASSEMBLY_ID = "assemblyid";
        public static final String COLUMN_NAME_ASSEMBLY_TITLE = "assemblytitle";
        public static final String COLUMN_NAME_ASSEMBLY_DESCRIPTION = "assemblydescription";
    }

    public static abstract class MessagePartAssemblyLink implements BaseColumns {
        public static final String TABLE_NAME = "partassemblylink";
        public static final String COLUMN_NAME_LINK_ID = "partassemblylinkid";
        public static final String COLUMN_NAME_ASSEMBLY_ID = "assemblyid";
        public static final String COLUMN_NAME_PART_ID = "partid";
        public static final String COLUMN_NAME_PART_ORDER = "partorder";
    }
}
