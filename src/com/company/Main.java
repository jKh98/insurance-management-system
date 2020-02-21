package com.company;

public class Main {

    public static void main(String[] args) {
        // 1. Connect to database
        SQLiteManager.setUpConnectionToDB("test.db");
        // 2. Add tables if they do not exist
        setUpTables();
        // 3. Close database
        SQLiteManager.disconnectAndCloseDB();
    }

    private static void setUpTables() {

        // Add policy table that stores all policies regardless what type
        SQLiteManager.addTableToDB("policy", new String[]
                {"id INTEGER PRIMARY KEY AUTOINCREMENT"
                        , "effective INTEGER NOT NULL"
                        , "expiry INTEGER NOT NULL CHECK (expiry > effective)"
                        , "policy_no TEXT UNIQUE NOT NULL"
                        , "premium DECIMAL NOT NULL"
                        , "is_valid INTEGER NOT NULL"
                        , "policy_type TEXT NOT NULL"
                });
        SQLiteManager.addTableToDB("travel", new String[]
                {"id INTEGER PRIMARY KEY AUTOINCREMENT"
                        , "policy_no TEXT NOT NULL UNIQUE REFERENCES policy(policy_no)"
                        , "departure TEXT NOT NULL"
                        , "destination TEXT NOT NULL"
                        , "family INTEGER NOT NULL"
                });
        SQLiteManager.addTableToDB("motor", new String[]
                {"id INTEGER PRIMARY KEY AUTOINCREMENT"
                        , "policy_no TEXT NOT NULL UNIQUE REFERENCES policy(policy_no)"
                        , "vehicle_price DECIMAL NOT NULL"
                });
        SQLiteManager.addTableToDB("claim", new String[]
                {"id INTEGER PRIMARY KEY AUTOINCREMENT"
                        , "policy_no TEXT NOT NULL REFERENCES policy(policy_no)"
                        , "incurred_date INTEGER NOT NULL"
                        , "claimed_amount DECIMAL NOT NULL"
                });
        SQLiteManager.addTableToDB("beneficiaries", new String[]
                {"id INTEGER PRIMARY KEY AUTOINCREMENT"
                        , "name TEXT NOT NULL"
                        , "relation TEXT NOT NULL"
                        , "gender TEXT NOT NULL CHECK (gender = 'male' or gender = 'female')"
                        , "birth_date INTEGER NOT NULL"
                        , "policy_no TEXT NOT NULL REFERENCES policy(policy_no)"
                });
    }
}
