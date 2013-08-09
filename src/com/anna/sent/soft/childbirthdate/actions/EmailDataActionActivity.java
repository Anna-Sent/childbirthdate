package com.anna.sent.soft.childbirthdate.actions;

import com.anna.sent.soft.utils.UserEmailFetcher;

public class EmailDataActionActivity extends EmailActionActivity {
	@Override
	protected String getEmail() {
		return UserEmailFetcher.getEmail(this);
	}

	@Override
	protected String getText() {
		// TODO
		return "data";
	}
}
