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
import android.view.View;
import android.widget.ListView;

import com.anna.sent.soft.childbirthdate.R;

public class SickListAgePreference extends DialogPreference {
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

	private String mValue = SickListConstants.Days.DEFAULT_VALUE;

	private static List<Object> toList(String value) {
		List<Object> result = new ArrayList<Object>();
		String[] items = value.split(",");

		for (int i = 0; i < items.length; ++i) {
			if (!items[i].equals("")) {
				try {
					int item = Integer.parseInt(items[i]);
					result.add(item);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
			}
		}

		return result;
	}

	private static String toString(List<Integer> values) {
		String result = "";
		for (int i = 0; i < values.size(); ++i) {
			result += values.get(i) + ",";
		}

		return result;
	}

	public SickListAgePreference(Context context) {
		this(context, null);
	}

	public SickListAgePreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		setDialogLayoutResource(R.layout.dialog_sick_list_age);
		setPositiveButtonText(android.R.string.ok);
		setNegativeButtonText(android.R.string.cancel);
		setDialogIcon(null);
	}

	@Override
	protected void onSetInitialValue(boolean restore, Object defaultValue) {
		setValue(restore ? getPersistedString(SickListConstants.Days.DEFAULT_VALUE)
				: (String) defaultValue);
	}

	@Override
	protected Object onGetDefaultValue(TypedArray a, int index) {
		return a.getString(index);
	}

	@Override
	protected void onBindDialogView(View view) {
		super.onBindDialogView(view);

		ListView listView = (ListView) view.findViewById(R.id.listView);
		listView.setAdapter(new MoveableItemsArrayAdapter(getContext(),
				toList(SickListConstants.Days.DEFAULT_VALUE)));
	}

	public String getValue() {
		return mValue;
	}

	public void setValue(String value) {
		if (value.equals(mValue)) {
			mValue = value;
			persistString(value);
			notifyChanged();
		}
	}

	public List<Object> getValueAsList() {
		return toList(mValue);
	}

	@Override
	protected void onDialogClosed(boolean positiveResult) {
		super.onDialogClosed(positiveResult);

		if (positiveResult) {
			/*
			 * int numberPickerValue = mNumberPicker.getValue(); if
			 * (callChangeListener(numberPickerValue)) {
			 * setValue(numberPickerValue); mNumberPicker = null; }
			 */
		}
	}

	@Override
	protected Parcelable onSaveInstanceState() {
		// log("save");
		final Parcelable superState = super.onSaveInstanceState();

		/*
		 * if (isPersistent()) { return superState; }
		 */

		final SavedState myState = new SavedState(superState);
		/*
		 * if (mNumberPicker != null) { myState.value =
		 * mNumberPicker.getValue(); // log("save " + myState.value); }
		 */

		return myState;
	}

	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		// log("restore");
		if (state == null || !state.getClass().equals(SavedState.class)) {
			// log("restore " + state == null ? "null" :
			// state.getClass().getName());
			super.onRestoreInstanceState(state);
			return;
		}

		SavedState myState = (SavedState) state;
		super.onRestoreInstanceState(myState.getSuperState());
		/*
		 * if (mNumberPicker != null) { mNumberPicker.setValue(myState.value);
		 * // log("restore " + myState.value); }
		 */
	}

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
}