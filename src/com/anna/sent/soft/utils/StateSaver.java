package com.anna.sent.soft.utils;

import android.os.Bundle;

public interface StateSaver {
	public void setViews();

	public void restoreState(Bundle state);

	public void saveState(Bundle state);
}
