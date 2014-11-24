package com.wasalny.demo.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

/**
 * An {@linkplain Object} that represents an abstraction of a database table.
 * @author Morabea
 */
abstract class AbstractTable {
	
	/**
	 * An interface that represents a database table fields.
	 * @author Morabea
	 */
	public static interface Fields extends BaseColumns {
		
	}
	
	/** A field of type text would have this value as type<br>
	 * NOTE: A space is included at the start of the type */
	protected static final String TEXT_TYPE = " TEXT";
	protected static final String NUMBER_TYPE = " INTEGER";
	/** A separator between fields declarations */
	protected static final String COMMA_SEP = ",";
    
    public static final String ORDER_ASCENDING = " ASC";
    public static final String ORDER_DESCENDING = " DESC";
    
	/**
	 * Executes the creation SQL responsible for creating this table.
	 * @param db
	 */
	protected abstract void create(SQLiteDatabase db);
	
	/**
	 * Invoked when the database version has been raised and an upgrade occurred.
	 * @param db
	 * @param newVersion
	 */
	protected abstract void upgrade(SQLiteDatabase db, int newVersion);
	
	/**
	 * @return All rows for this table.
	 */
	protected Cursor getAllRows() {
		return DatabaseManager.getInstance()
				.getWritableDatabase().query(getTableName(), getProjection(), null, null, null, null, null);
	}
	
	/**
	 * Clears all table data.
	 */
	public void clearAll() {
		DatabaseManager.getInstance()
			.getWritableDatabase().delete(getTableName(), null, null);
	}
	
	/**
	 * @return The name of table to be used in SQL statements.
	 */
	protected abstract String getTableName();
	
	/**
	 * @return Array of projections used when selecting rows.
	 */
	protected abstract String[] getProjection();
	
}
