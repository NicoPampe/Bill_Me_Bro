package com.example.npampe.billmebro.database;

public class ReceiptDbSchema {

    public static final class UsersTable {
        public static final String NAME = "users";

        public static final class Cols {
            public static final String USER_ID = "user_id";
            public static final String USERNAME = "username";
        }
    }

    public static final class GroupsTable {
        public static final String NAME = "groups";

        public static final class Cols {
            public static final String GROUP_ID = "group_id";
            public static final String NAME = "name";
            public static final String TYPE = "type";
            public static final String DATE = "date_created";
        }
    }

    public static final class GroupsListTable {
        public static final String NAME = "groups_list";

        public static final class Cols {
            public static final String USER_ID = "user_id";
            public static final String GROUP_ID = "group_id";
        }
    }

    public static final class ReceiptsTable {
        public static final String NAME = "receipts";

        public static final class Cols {
            public static final String RECEIPT_ID = "receipt_id";
            public static final String GROUP_ID = "group_id";
            public static final String TITLE = "title";
            public static final String DATE = "date";
            public static final String DAY_OF_YEAR = "day_of_year";
            public static final String TOTAL = "total";
        }
    }

}
