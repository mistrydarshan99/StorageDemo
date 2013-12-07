package com.abhan.android.views;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient.CustomViewCallback;
import android.widget.TextView;
import android.widget.Toast;

import com.abhan.android.Abhan;
import com.abhan.android.R;
import com.abhan.android.constants.AbhanConstants;
import com.abhan.android.databases.DBAdapter;
import com.abhan.android.receiver.SDcardreceiver;

public class AbhanActivity extends Activity {
	private boolean isRotated = false;
	private Abhan abhanapplication;
	private SDcardreceiver receiversdcad;

	private TextView pref;
	private DBAdapter db;

	private File folder;
	public static boolean fileexist_var;
	public static final String PREFS_NAME = "MyPrefsFile";
	public static final String internal_Filename = "MyDemoFile";
	public static final String MYEXTRNALDIR = "MyExternalFolder";
	public static final String MYEXTERNAL_FILE = "Myfile";
	private SharedPreferences persistant_storeage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_abhan);
		pref = (TextView) findViewById(R.id.textname);
		if (savedInstanceState != null) {
			isRotated = savedInstanceState.getBoolean(AbhanConstants.ABHAN_KEY,
					false);
		}
		Abhan.abhanLog(3, String.valueOf(isRotated));
		abhanapplication = (Abhan) getApplicationContext();

		// first type to call database
		Cursor c = abhanapplication.getDatabase().listData();

		// Second type to call database

		// db=new DBAdapter(this); db.open(); Cursor c=db.listData();
		// db.close();

		persistanet_storage();

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean(AbhanConstants.ABHAN_KEY, !isRotated);
		Abhan.abhanLog(3, "OnSaveInstanceState");
	}

	@Override
	protected void onStop() {
		super.onStop();
		Abhan.abhanLog(3, "OnStop");
	}

	public void persistanet_storage() {
		persistant_storeage = getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor Ed = persistant_storeage.edit();
		Ed.putBoolean("boolvalue", true);
		Ed.putFloat("floatvalue", 10.25f);
		Ed.putInt("intvalue", 1256);
		Ed.putLong("longvalue", 10L);
		Ed.putString("stringvalue", "Darshan");
		Ed.commit();
	}

	public void getsharepreferencevalue(View v) {
		persistant_storeage = getSharedPreferences(PREFS_NAME, 0);

		pref.setText(" Bool  "
				+ persistant_storeage.getBoolean("boolvalue", false)
				+ " \n Float  "
				+ persistant_storeage.getFloat("floatvalue", 0.0f)
				+ " \n Integer " + persistant_storeage.getInt("intvalue", 0)
				+ " \n Long " + persistant_storeage.getLong("longvalue", 0L)
				+ " \n String"
				+ persistant_storeage.getString("stringvalue", null));
	}

	public void internalFile(View v) {

		String string = "Hello world!";
		FileOutputStream outputStream;

		try {
			outputStream = openFileOutput(internal_Filename, this.MODE_PRIVATE);
			outputStream.write(string.getBytes());
			outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void readFile(View v) {

		boolean file_exist = new File(this.getFilesDir(), internal_Filename)
				.exists();
		if (file_exist) {

			try {
				// read file from internal storage
				BufferedReader bufferedReader = new BufferedReader(
						new FileReader(new File(getFilesDir() + File.separator
								+ internal_Filename)));

				String read;
				StringBuilder builder = new StringBuilder("");

				while ((read = bufferedReader.readLine()) != null) {
					builder.append(read);
				}
				abhanapplication.abhanLog(1, "Output" + builder.toString());
				pref.setText(builder.toString());
				bufferedReader.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			pref.setText("File is not exist");
		}
	}

	public void rawFile(View v) {
		try {
			// 1st way read file from raw resource
			InputStream fis = getResources().openRawResource(R.raw.staticfile);
			// byte[] b = new byte[fis.available()]; fis.read(b);
			// pref.setText(new String(b));

			// 2nd way read raw file different way
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					fis));
			String read;
			StringBuilder builder = new StringBuilder("");

			while ((read = reader.readLine()) != null) {
				builder.append(read);
			}
			pref.setText(builder.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean createExternalStoragePublicPicture(String filepath) {

		folder = new File(
				this.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),
				filepath);
		abhanapplication.abhanLog(3,
				"File path where dir create" + folder.toString());
		if (!folder.exists()) {
			if (!folder.mkdir()) {
				abhanapplication.abhanLog(
						3,
						"dir is not created"
								+ this.getExternalFilesDir(
										Environment.DIRECTORY_DOWNLOADS)
										.toString());
				return false;
			} else {
				abhanapplication.abhanLog(
						3,
						"dir is created"
								+ this.getExternalFilesDir(
										Environment.DIRECTORY_DOWNLOADS)
										.toString());
				return true;
			}
		} else {
			abhanapplication.abhanLog(3, "file is exist");
			return true;
		}

	}

	public void externalDir(View v) {

		if (abhanapplication.mExternalStorageAvailable) {

			if (fileexist_var) {
				boolean file_exist = new File(folder.toString(),
						MYEXTERNAL_FILE).exists();
				if (file_exist) {

					try {
						// read file from internal storage
						BufferedReader bufferedReader = new BufferedReader(
								new FileReader(new File(folder.toString(),
										MYEXTERNAL_FILE)));

						String read;
						StringBuilder builder = new StringBuilder("");

						while ((read = bufferedReader.readLine()) != null) {
							builder.append(read);
						}
						abhanapplication.abhanLog(1,
								"Output" + builder.toString());
						pref.setText(builder.toString());
						bufferedReader.close();

					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					pref.setText("File is not exist");
				}
			}
		} else {
			pref.setText("Cannot read because external storage is not available");
		}
	}

	public void externalstore(View v) {

		if (abhanapplication.mExternalStorageAvailable) {

			fileexist_var = createExternalStoragePublicPicture(MYEXTRNALDIR);

			if (fileexist_var) {

				String string = "Hello world from external dirctory!";

				try {
					// create a File object for the output file
					File outputFile = new File(folder.toString(),
							MYEXTERNAL_FILE);
					// now attach the OutputStream to the file object, instead
					// of a String representation
					FileOutputStream fos = new FileOutputStream(outputFile);
					fos.write(string.getBytes());
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				pref.setText("Folder is not created");
			}
		} else {
			pref.setText("External storage is not present");
		}
	}

}