package com.anna.sent.soft.childbirthdate.actions;

import com.anna.sent.soft.childbirthdate.R;

public class EmailSupportActionActivity extends EmailActionActivity {
	@Override
	protected String getEmail() {
		return getString(R.string.supportEmail);
	}

	@Override
	protected String getText() {
		return null;
	}
}
