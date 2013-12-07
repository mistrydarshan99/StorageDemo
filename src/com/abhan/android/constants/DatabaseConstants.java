package com.abhan.android.constants;

public class DatabaseConstants {
	public static final String DATABASE_NAME = "Demo.db";
	public static final String TABLE_NAME = "AbhanAndroid";
	public static final int DATABASE_VERSION = 2;

	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_IMAGE = "image";
	public static final String COLUMN_VERSION = "version";
	public static final String COLUMN_ORDER = "sortorder";

	private static final String COLUMN_INTEGER = " INTEGER";
	private static final String COLUMN_TEXT = " TEXT";
	private static final String COLUMN_BLOB = " BLOB";
	private static final String COLUMN_REAL = " REAL";

	private static final String COMMA = ",";
	private static final String SPACE = " ";
	private static final String PRIMARY_KEY = "PRIMARY KEY";
	private static final String AUTO_INCREMENT = "AUTOINCREMENT";
	private static final String OPENING = "(";
	private static final String CLOSING = ")";

	/*private static final String NOT_NULL = "NOT NULL";
	private static final String DEFAULT_INTEGER = "DEFAULT 0";
	private static final String DEFAULT_TEXT = "DEFAULT \'\'";*/

	private static final String CRATE_TABLE = "CREATE TABLE IF NOT EXISTS ";
	private static final String DROP_TABLE = "DROP TABLE IF EXISTS ";

	public static final String DROP_TABLE_ABHANANDROID = DROP_TABLE
			+ TABLE_NAME;
	public static final String CREATE_TABLE_ABHANANDROID = CRATE_TABLE
			+ TABLE_NAME + OPENING + COLUMN_ID + COLUMN_INTEGER + SPACE
			+ PRIMARY_KEY + SPACE + AUTO_INCREMENT + COMMA + SPACE
			+ COLUMN_NAME + COLUMN_TEXT + COMMA + SPACE
			+ COLUMN_IMAGE + COLUMN_BLOB + COMMA + SPACE + COLUMN_VERSION
			+ COLUMN_REAL + COMMA + SPACE + COLUMN_ORDER + COLUMN_INTEGER
			+ CLOSING;
	public static final String ALTER_TABLE_ABHANANDROID = "ALTER TABLE "
			+ TABLE_NAME + " RENAME TO " + TABLE_NAME + "_Obsolete";
	public static final String INSERT_ALL_DATA = "INSERT INTO " + TABLE_NAME
			+ OPENING + COLUMN_ID + COMMA + SPACE + COLUMN_NAME + COMMA + SPACE
			+ COLUMN_IMAGE + COMMA + SPACE + COLUMN_VERSION + COMMA + SPACE
			+ COLUMN_ORDER + CLOSING + " SELECT " + COLUMN_ID + COMMA
			+ SPACE + COLUMN_NAME + COMMA + SPACE + COLUMN_IMAGE + COMMA
			+ SPACE + COLUMN_VERSION + COMMA + SPACE + COLUMN_ORDER + " FROM "
			+ TABLE_NAME + "_Obsolete";
}
