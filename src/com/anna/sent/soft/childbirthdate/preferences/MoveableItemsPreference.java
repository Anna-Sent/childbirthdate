package com.anna.sent.soft.childbirthdate.preferences;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ListView;

import com.anna.sent.soft.childbirthdate.R;
import com.anna.sent.soft.childbirthdate.age.ISetting;
import com.anna.sent.soft.childbirthdate.age.LocalizableObject;
import com.anna.sent.soft.childbirthdate.age.SettingsParser;

public abstract class MoveableItemsPreference extends DialogPreference
		implements OnClickListener {
	private static final String TAG = "moo";
	private static final boolean DEBUG = false;

	private String wrapMsg(String msg) {
		return getClass().getSimpleName() + ": " + msg;
	}

	@SuppressWarnings("unused")
	private void log(String msg) {
		if (DEBUG) {
			Log.d(TAG, wrapMsg(msg));
		}
	}

	private String mValue;
	private MoveableItemsArrayAdapter mAdapter;

	public MoveableItemsPreference(Context context) {
		this(context, null);
	}

	public MoveableItemsPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		mValue = getDefaultValue();
		setDialogLayoutResource(R.layout.dialog_list);
		setPositiveButtonText(android.R.string.ok);
		setNegativeButtonText(android.R.string.cancel);
		setDialogIcon(null);
	}

	@Override
	protected void onSetInitialValue(boolean restore, Object defaultValue) {
		setValue(restore ? getPersistedString(getDefaultValue())
				: (String) defaultValue);
	}

	@Override
	protected Object onGetDefaultValue(TypedArray a, int index) {
		return a.getString(index);
	}

	@Override
	protected void onBindDialogView(View view) {
		super.onBindDialogView(view);

		// get inflater
		LayoutInflater inflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		// inflate footer
		View footer = inflater.inflate(R.layout.dialog_list_last_item, null);

		// get button 'add' from footer
		Button buttonAdd = (Button) footer.findViewById(R.id.buttonAdd);
		buttonAdd.setOnClickListener(this);

		// inflate view 'add'
		View viewAdd = inflater.inflate(getAddLayoutResourceId(), null);

		// find parent of view 'add' from footer
		ViewGroup viewAddParent = (ViewGroup) footer
				.findViewById(R.id.lastItem);

		// add view 'add' to parent
		viewAddParent.addView(viewAdd, new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

		// get list view
		ListView listView = (ListView) view.findViewById(R.id.listView);

		listView.setFooterDividersEnabled(true);

		// add footer to list view
		listView.addFooterView(footer);

		// and then setup adapter
		mAdapter = new MoveableItemsArrayAdapter(getContext(), toList(mValue));
		listView.setAdapter(mAdapter);

		setupViewAdd(viewAdd);
	}

	protected abstract String getDefaultValue();

	protected abstract int getAddLayoutResourceId();

	protected abstract void setupViewAdd(View viewAdd);

	@Override
	protected void onDialogClosed(boolean positiveResult) {
		super.onDialogClosed(positiveResult);

		if (positiveResult) {
			List<LocalizableObject> list = mAdapter.getValues();
			setValue(toString(list));
		}
	}

	private void setValue(String value) {
		if (!value.equals(mValue)) {
			mValue = value;
			persistString(value);
			notifyChanged();
		}
	}

	private List<LocalizableObject> toList(String str) {
		List<ISetting> source = SettingsParser.loadList(str, getElement());
		List<LocalizableObject> destination = new ArrayList<LocalizableObject>();

		for (int i = 0; i < source.size(); ++i) {
			destination.add(source.get(i));
		}

		return destination;
	}

	protected abstract ISetting getElement();

	private String toString(List<LocalizableObject> list) {
		List<LocalizableObject> source = list;
		List<ISetting> destination = new ArrayList<ISetting>();

		for (int i = 0; i < source.size(); ++i) {
			destination.add((ISetting) source.get(i));
		}

		return SettingsParser.saveList(destination);
	}

	@Override
	protected Parcelable onSaveInstanceState() {
		final Parcelable superState = super.onSaveInstanceState();

		/*
		 * if (isPersistent()) { return superState; }
		 */

		final SavedState myState = new SavedState(superState);
		myState.value = saveAddValue();
		return myState;
	}

	protected abstract String saveAddValue();

	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		if (state == null || !state.getClass().equals(SavedState.class)) {
			super.onRestoreInstanceState(state);
			return;
		}

		SavedState myState = (SavedState) state;
		super.onRestoreInstanceState(myState.getSuperState());
		restoreAddValue(myState.value);
	}

	protected abstract void restoreAddValue(String value);

	private static class SavedState extends BaseSavedState {
		public String value;

		public SavedState(Parcelable superState) {
			super(superState);
		}

		public SavedState(Parcel source) {
			super(source);
			value = source.readString();
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			super.writeToParcel(dest, flags);
			dest.writeString(value);
		}

		@SuppressWarnings("unused")
		public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
			@Override
			public SavedState createFromParcel(Parcel in) {
				return new SavedState(in);
			}

			@Override
			public SavedState[] newArray(int size) {
				return new SavedState[size];
			}
		};
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.buttonAdd:
			addItem(mAdapter);
			break;
		}
	}

	protected abstract void addItem(MoveableItemsArrayAdapter adapter);
}