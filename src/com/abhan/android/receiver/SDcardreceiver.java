package com.abhan.android.receiver;

import com.abhan.android.Abhan;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class SDcardreceiver extends BroadcastReceiver {

	// private Context act;
	//
	// public SDcardreceiver(Context activity) {
	// act=activity;
	// }

	@Override
	public void onReceive(Context context, Intent intent) {
		Abhan.updateExternalStorageState();
	}

}
