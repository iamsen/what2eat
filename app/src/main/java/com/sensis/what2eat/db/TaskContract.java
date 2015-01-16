package com.sensis.what2eat.db;

import android.provider.BaseColumns;

/**
 * Created by sen.li on 1/12/15.
 */
public class TaskContract {
    public static final String DB_NAME = "com.sensis.what2eat.db.tasks";
    public static final int DB_VERSION = 2;
    public static final String TABLE = "tasks";

    public class Columns {
        public static final String TASK_NAME = "task_name";
        public static final String _ID = BaseColumns._ID;
        public static final String PHONE_NUM = "phone_num";
        public static final String ADDRESS = "address";
    }
}
