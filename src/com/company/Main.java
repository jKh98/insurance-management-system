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

    /**
     * Sets up all database tables based on designed schema
     */
    private static void setUpTables() {

        //CHECK (LOWER(policy_type) = 'travel' OR LOWER(policy_type) = 'motor' OR LOWER(policy_type) = 'medical')

        // Policy table that stores all policies regardless what type
        SQLiteManager.addTableToDB("policy", new String[]
                {"id INTEGER PRIMARY KEY AUTOINCREMENT"
                        , "effective BIGINT NOT NULL"
                        , "expiry BIGINT NOT NULL CHECK (expiry > effective)"
                        , "premium DECIMAL NOT NULL"
                        , "is_valid INTEGER NOT NULL CHECK (is_valid = 0 OR is_valid = 1)"
                        , "policy_type TEXT NOT NULL"
                        , "policy_no TEXT UNIQUE NOT NULL"

                });
        // Table that stores data related to travel policies
        SQLiteManager.addTableToDB("travel", new String[]
                {"id INTEGER PRIMARY KEY AUTOINCREMENT"
                        , "policy_no TEXT NOT NULL UNIQUE REFERENCES policy(policy_no)"
                        , "departure TEXT NOT NULL"
                        , "destination TEXT NOT NULL"
                        , "family INTEGER NOT NULL CHECK (family = 0 OR family = 1)"
                });
        // Table that stores data related to motor policies
        SQLiteManager.addTableToDB("motor", new String[]
                {"id INTEGER PRIMARY KEY AUTOINCREMENT"
                        , "policy_no TEXT NOT NULL UNIQUE REFERENCES policy(policy_no)"
                        , "vehicle_price DECIMAL NOT NULL"
                });
        // Claims table
        SQLiteManager.addTableToDB("claim", new String[]
                {"id INTEGER PRIMARY KEY AUTOINCREMENT"
                        , "policy_no TEXT NOT NULL REFERENCES policy(policy_no)"
                        , "incurred_date BIGINT NOT NULL"
                        , "claimed_amount DECIMAL NOT NULL"
                });
        // Beneficiaries table
        SQLiteManager.addTableToDB("beneficiaries", new String[]
                {"id INTEGER PRIMARY KEY AUTOINCREMENT"
                        , "name TEXT NOT NULL"
                        , "relation TEXT NOT NULL CHECK (LOWER(relation) = 'self' OR LOWER(relation) = 'spouse'" +
                        " OR LOWER(relation) = 'son' OR LOWER(relation) = 'daughter')"
                        , "gender TEXT NOT NULL CHECK (LOWER(gender) = 'male' OR LOWER(gender) = 'female')"
                        , "birth_date BIGINT NOT NULL"
                        , "policy_no TEXT NOT NULL REFERENCES policy(policy_no)"
                });
    }
}
