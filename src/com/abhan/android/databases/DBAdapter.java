package com.abhan.android.databases;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.abhan.android.Abhan;
import com.abhan.android.constants.DatabaseConstants;
import com.abhan.android.models.AbhanAndroid;

public class DBAdapter {
	private final Context mContext;
	private SQLiteDatabase mDb;
	private final DataBaseHelper mDbHelper;
	
	public DBAdapter(Context context) {
		this.mContext = context;
		mDbHelper = new DataBaseHelper(mContext);
	}

	public DBAdapter createDatabase() throws SQLException {
		try {
			mDbHelper.createDataBase();
		} catch (IOException mIOException) {
			throw new Error("UnableToCreateDatabase");
		}
		return this;
	}

	public DBAdapter open() throws SQLException {
		try {
			mDbHelper.openDataBase();
			mDbHelper.close();
			mDb = mDbHelper.getReadableDatabase();
		} catch (SQLException mSQLException) {
			Abhan.abhanLog(5, mSQLException.toString());
			throw mSQLException;
		}
		return this;
	}

	public void close() {
		mDbHelper.close();
	}

	public long insertRecord(final AbhanAndroid abhanAndroid) {
		long inserted = -1;
		ContentValues contentValues = new ContentValues();
		contentValues
				.put(DatabaseConstants.COLUMN_NAME, abhanAndroid.getName());
		if (abhanAndroid.getImage() != null) {
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			abhanAndroid.getImage().compress(Bitmap.CompressFormat.PNG, 100,
					byteArrayOutputStream);
			contentValues.put(DatabaseConstants.COLUMN_IMAGE,
					byteArrayOutputStream.toByteArray());
		}
		contentValues.put(DatabaseConstants.COLUMN_VERSION,
				abhanAndroid.getVersion());
		contentValues.put(DatabaseConstants.COLUMN_ORDER,
				abhanAndroid.getOrder());
		inserted = mDb
				.insert(DatabaseConstants.TABLE_NAME, null, contentValues);
		return inserted;
	}

	public int updateRecord(final AbhanAndroid abhanAndroid, final int abhanId) {
		int updated = -1;
		ContentValues contentValues = new ContentValues();
		contentValues
				.put(DatabaseConstants.COLUMN_NAME, abhanAndroid.getName());
		if (abhanAndroid.getImage() != null) {
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			abhanAndroid.getImage().compress(Bitmap.CompressFormat.PNG, 100,
					byteArrayOutputStream);
			contentValues.put(DatabaseConstants.COLUMN_IMAGE,
					byteArrayOutputStream.toByteArray());
		}
		contentValues.put(DatabaseConstants.COLUMN_VERSION,
				abhanAndroid.getVersion());
		contentValues.put(DatabaseConstants.COLUMN_ORDER,
				abhanAndroid.getOrder());
		final String where = DatabaseConstants.COLUMN_ID + "=?";
		final String[] whereArgs = new String[] { String.valueOf(abhanId) };
		updated = mDb.update(DatabaseConstants.TABLE_NAME, contentValues,
				where, whereArgs);
		return updated;
	}

	public AbhanAndroid getRecord(final int abhanId) {
		AbhanAndroid abhanAndroid = null;
		Cursor cursor = null;
		final String selection = DatabaseConstants.COLUMN_ID + "=?";
		final String[] selectionArgs = new String[] { String.valueOf(abhanId) };
		final String[] columns = new String[] { DatabaseConstants.COLUMN_ID,
				DatabaseConstants.COLUMN_NAME, DatabaseConstants.COLUMN_IMAGE,
				DatabaseConstants.COLUMN_VERSION,
				DatabaseConstants.COLUMN_ORDER };
		cursor = mDb.query(DatabaseConstants.TABLE_NAME, columns, selection,
				selectionArgs, null, null, null);
		if (cursor != null && cursor.getCount() > 0) {
			abhanAndroid = new AbhanAndroid();
			final int id = cursor.getInt(cursor
					.getColumnIndex(DatabaseConstants.COLUMN_ID));
			final String name = cursor.getString(cursor
					.getColumnIndex(DatabaseConstants.COLUMN_NAME));
			final byte[] blob = cursor.getBlob(cursor
					.getColumnIndex(DatabaseConstants.COLUMN_IMAGE));
			final Bitmap image = BitmapFactory.decodeByteArray(blob, 0,
					blob.length);
			final double version = cursor.getDouble(cursor
					.getColumnIndex(DatabaseConstants.COLUMN_VERSION));
			final int order = cursor.getInt(cursor
					.getColumnIndex(DatabaseConstants.COLUMN_ORDER));
			abhanAndroid.setId(id);
			abhanAndroid.setName(name);
			abhanAndroid.setImage(image);
			abhanAndroid.setVersion(version);
			abhanAndroid.setOrder(order);
		}
		cursor.close();
		return abhanAndroid;
	}

	public int deleteRecord(final int abhanId) {
		int deleted = -1;
		final String where = DatabaseConstants.COLUMN_ID + "=?";
		final String[] whereArgs = new String[] { String.valueOf(abhanId) };
		deleted = mDb.delete(DatabaseConstants.TABLE_NAME, where, whereArgs);
		return deleted;
	}
	
	public Cursor listData() {
		Cursor c = mDb.rawQuery("SELECT * FROM VCSDemo", null);
		if (c != null && c.getCount() > 0) {
			c.moveToFirst();
		}
		return c;
	}
}