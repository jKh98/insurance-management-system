package com.company;

public class Main {

    public static void main(String[] args) {
	// 1. Connect to database
    SQLiteManager.setUpConnectionToDB("test.db");
    // 2. Close database
    SQLiteManager.disconnectAndCloseDB();
    }
}
