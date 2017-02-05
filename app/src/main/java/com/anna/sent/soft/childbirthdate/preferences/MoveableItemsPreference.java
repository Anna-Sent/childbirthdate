package com.anna.sent.soft.childbirthdate.preferences;

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
import com.anna.sent.soft.childbirthdate.utils.MyLog;

import java.util.List;

public abstract class MoveableItemsPreference extends DialogPreference
        implements OnClickListener {
    private String mValue;
    private MoveableItemsArrayAdapter mAdapter;
    private ListView mListView;

    protected MoveableItemsPreference(Context context) {
        this(context, null);
    }

    protected MoveableItemsPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        mValue = getDefaultValue();
        setDialogLayoutResource(R.layout.dialog_list);
        setPositiveButtonText(android.R.string.ok);
        setNegativeButtonText(android.R.string.cancel);
        setDialogIcon(null);
    }

    private String wrapMsg(String msg) {
        return getClass().getSimpleName() + ": " + msg;
    }

    private void log(String msg) {
        MyLog.getInstance().logcat(Log.DEBUG, wrapMsg(msg));
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

        // get button 'add'
        Button buttonAdd = (Button) view.findViewById(R.id.buttonAdd);
        buttonAdd.setOnClickListener(this);

        // inflate view 'add'
        View viewAdd = inflater.inflate(getAddLayoutResourceId(), null);

        // find parent of view 'add'
        ViewGroup viewAddParent = (ViewGroup) view.findViewById(R.id.lastItem);

        // add view 'add' to parent
        viewAddParent.addView(viewAdd, new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        // get list view
        mListView = (ListView) view.findViewById(R.id.listView);

        // and then setup adapter
        mAdapter = new MoveableItemsArrayAdapter(getContext(), toList(mValue));
        mListView.setAdapter(mAdapter);

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

    public String getValueToShow() {
        return getValueToShow(mValue);
    }

    public String getValueToShow(String value) {
        String result = "";

        List<LocalizableObject> list = toList(value);
        for (int i = 0; i < list.size(); ++i) {
            result += list.get(i).toString(getContext())
                    + (i == list.size() - 1 ? "" : "; ");
        }

        return result;
    }

    private List<LocalizableObject> toList(String str) {
        return SettingsParser.toList(str, getElement());
    }

    protected abstract ISetting getElement();

    private String toString(List<LocalizableObject> list) {
        return SettingsParser.toString(list);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        final Parcelable superState = super.onSaveInstanceState();

		/*
         * if (isPersistent()) { return superState; }
		 */

        final SavedState myState = new SavedState(superState);
        myState.value = saveAddValue();
        if (mAdapter != null) {
            myState.values = SettingsParser.toString(mAdapter.getValues()
            );
        } else {
            myState.values = null;
        }

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
        if (mAdapter != null) {
            mAdapter.setItems(SettingsParser.toList(myState.values,
                    getElement()));
        }
    }

    protected abstract void restoreAddValue(String value);

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonAdd:
                addItem(mAdapter);
                break;
        }
    }

    @SuppressWarnings("unused")
    private void scrollToBottom() {
        mListView.post(new Runnable() {
            @Override
            public void run() {
                mListView.setSelection(mAdapter.getCount() - 1);
            }
        });
    }

    protected abstract void addItem(MoveableItemsArrayAdapter adapter);

    private static class SavedState extends BaseSavedState {
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
        public String value;
        private String values;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        public SavedState(Parcel source) {
            super(source);
            value = source.readString();
            values = source.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeString(value);
            dest.writeString(values);
        }
    }
}
