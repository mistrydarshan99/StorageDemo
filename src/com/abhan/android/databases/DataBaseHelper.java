package com.abhan.android.databases;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import com.abhan.android.Abhan;
import com.abhan.android.constants.DatabaseConstants;

public class DataBaseHelper extends SQLiteOpenHelper {
	private static String DATABASE_PATH = "/data/data/YOUR_PACKAGE/databases/";
	private SQLiteDatabase sqLiteDataBase;
	private final Context context;

	public DataBaseHelper(Context context) {
		super(context, DatabaseConstants.DATABASE_NAME, null,
				DatabaseConstants.DATABASE_VERSION);
		DATABASE_PATH = "/data/data/" + context.getPackageName() + "/databases/";
		this.context = context;
	}

	public void createDataBase() throws IOException {
		boolean mDataBaseExist = checkDataBase();
		if (!mDataBaseExist) {
			this.getReadableDatabase();
			try {
				copyDataBase();
			} catch (IOException mIOException) {
				throw new Error("ErrorCopyingDataBase");
			}
		}
	}

	private boolean checkDataBase() {
		SQLiteDatabase mCheckDataBase = null;
		try {
			String mPath = DATABASE_PATH + DatabaseConstants.DATABASE_NAME;
			File pathFile = new File(mPath);
			if(pathFile.exists()) {
				mCheckDataBase = SQLiteDatabase.openDatabase(mPath, null,
						SQLiteDatabase.NO_LOCALIZED_COLLATORS);
			}
		} catch (SQLiteException mSQLiteException) {
			Abhan.abhanLog(5, mSQLiteException.toString());
		}

		if (mCheckDataBase != null) {
			mCheckDataBase.close();
		}
		return mCheckDataBase != null;
	}
	
	@SuppressWarnings("unused")
	private void copyDataBase() throws IOException {
		InputStream mInput = context.getAssets().open(
				DatabaseConstants.DATABASE_NAME);
		String outFileName = DATABASE_PATH + DatabaseConstants.DATABASE_NAME;
		OutputStream mOutput = new FileOutputStream(outFileName);
		byte[] mBuffer = new byte[1024];
		int mLength;
		while ((mLength = mInput.read(mBuffer)) > 0) {
			mOutput.write(mBuffer, 0, mLength);
		}
		mOutput.flush();
		mOutput.close();
		mInput.close();
	}

	public boolean openDataBase() throws SQLException {
		String mPath = DATABASE_PATH + DatabaseConstants.DATABASE_NAME;
		sqLiteDataBase = SQLiteDatabase.openDatabase(mPath, null,
				SQLiteDatabase.NO_LOCALIZED_COLLATORS);
		return sqLiteDataBase != null;
	}

	@Override
	public synchronized void close() {
		super.close();
		if(sqLiteDataBase != null) {
			sqLiteDataBase.close();
		}
		final int memoryAmountReleased = SQLiteDatabase.releaseMemory();
		Abhan.abhanLog(3, String.valueOf(memoryAmountReleased));
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DatabaseConstants.DROP_TABLE_ABHANANDROID);
		db.execSQL(DatabaseConstants.CREATE_TABLE_ABHANANDROID);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Abhan.abhanLog(3, "Method Call");
	}
	
	private void migrateDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {
		final int currentVersion = db.getVersion();
		Abhan.abhanLog(3, "currentVersion: " + currentVersion);
		Abhan.abhanLog(3, "oldVersion: " + oldVersion);
		Abhan.abhanLog(3, "newVersion: " + newVersion);
		
		if(currentVersion >= newVersion) {
			return;
		} else {
			db.beginTransaction();
			alterTable(db);
			db.setTransactionSuccessful();
			db.endTransaction();
			db.setVersion(newVersion);
		}
	}

	private void alterTable(SQLiteDatabase db) {
		db.execSQL(DatabaseConstants.ALTER_TABLE_ABHANANDROID);
		db.execSQL(DatabaseConstants.CREATE_TABLE_ABHANANDROID);
		db.execSQL(DatabaseConstants.INSERT_ALL_DATA);
		db.execSQL(DatabaseConstants.DROP_TABLE_ABHANANDROID + "_Obsolete");
	}
	
	
	
}