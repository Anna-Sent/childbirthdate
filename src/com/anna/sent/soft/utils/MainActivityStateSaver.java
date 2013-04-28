package com.anna.sent.soft.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.anna.sent.soft.childbirthdate.shared.Shared;

public class MainActivityStateSaver {
	public static void save(Activity activity, Intent intent) {
		StateSaverActivity listener = (StateSaverActivity) activity;
		if (listener != null) {
			Bundle state = new Bundle();
			listener.saveState(state);
			intent.putExtra(Shared.ResultParam.EXTRA_MAIN_ACTIVITY_STATE, state);
		}
	}
}
