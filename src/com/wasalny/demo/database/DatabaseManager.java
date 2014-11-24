package com.wasalny.demo.database;

import java.util.ArrayList;
import java.util.List;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.wasalny.demo.AppController;

/**
 * Singleton class to manage database operations [Creation - Upgrade] and manage connections to database.
 * @author Morabea
 */
public class DatabaseManager extends SQLiteOpenHelper {

	/** name of the database file */
	private static final String DATABASE_NAME = "app.db";
	/** database version that should be incremented if there is any structural changes */
	private static final int DATABASE_VERSION = 1;
	/** List of registered tables in the application */
	private List<AbstractTable> dbTables;

	private static DatabaseManager instance;
	private SQLiteDatabase db;

	/**
	 * @return An instance of database manager.
	 */
	public synchronized static DatabaseManager getInstance() {
		if (instance == null)
			instance = new DatabaseManager();
		return instance;
	}

	private DatabaseManager() {
		super(AppController.getInstance().getApplicationContext(),
				DATABASE_NAME, null, DATABASE_VERSION);
		dbTables = new ArrayList<AbstractTable>();
	}

	public void initializeTables() {
	 	VenueTable .getInstance();
		
	}
	
	public void clearAppDatabase() {
		for (AbstractTable table : dbTables)
			table.clearAll();
	}

	public SQLiteDatabase getDb() {
		if (db == null)
			db = getWritableDatabase();
		return db;
	}
	
	public void addTable(AbstractTable table) {
		dbTables.add(table);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			for (AbstractTable table : dbTables)
				table.create(db);
		} catch (SQLException e) {
			Log.e("DatabaseManager#onCreate", "An error has occured while creating database tables");
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		try {
			if (oldVersion < newVersion) {
				for (AbstractTable table : dbTables)
					table.upgrade(db, newVersion);
			} else {
				for (AbstractTable table : dbTables) {
					dropTable(db, table.getTableName());
					onCreate(db);
				}
			}
		} catch (SQLException e) {
			Log.e("DatabaseManager#onCreate", "An error has occured while creating database tables");
			e.printStackTrace();
		}
	}
	
	public static void dropTable(SQLiteDatabase db, String table) {
		db.execSQL("DROP TABLE IF EXISTS " + table + ";");
	}

	public static void renameTable(SQLiteDatabase db, String table,
			String newTable) {
		db.execSQL("ALTER TABLE " + table + " RENAME TO " + newTable + ";");
	}

}
