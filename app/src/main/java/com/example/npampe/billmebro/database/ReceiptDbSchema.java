package com.example.npampe.billmebro.database;

/**
 * Created by Nick Pampe on 4/9/2016.
 */
public class ReceiptDbSchema {
    public static final class ReceiptTable {
        public static final String NAME = "receipt";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String DATE = "date";
            public static final String TOTOAL = "price";
        }
    }
}
