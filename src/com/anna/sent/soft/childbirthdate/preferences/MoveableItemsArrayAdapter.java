package com.anna.sent.soft.childbirthdate.preferences;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.anna.sent.soft.childbirthdate.R;

public class MoveableItemsArrayAdapter extends ArrayAdapter<String> implements
		OnClickListener {
	private static final String TAG = "moo";
	@SuppressWarnings("unused")
	private static final boolean DEBUG = false;
	@SuppressWarnings("unused")
	private static final boolean DEBUG_CREATION = false;

	private String wrapMsg(String msg) {
		return getClass().getSimpleName() + ": " + msg;
	}

	@SuppressWarnings("unused")
	private void log(String msg, boolean scenario) {
		if (scenario) {
			Log.d(TAG, wrapMsg(msg));
		}
	}

	private List<Object> mValues;

	public List<Object> getValues() {
		return mValues;
	}

	private static class ViewHolder {
		private int position;
	}

	public MoveableItemsArrayAdapter(Context context, List<Object> values) {
		super(context, R.layout.dialog_list_item);
		mValues = values;
	}

	@Override
	public int getCount() {
		return mValues.size();
	}

	@Override
	public View getView(int position, View contentView, ViewGroup viewGroup) {
		View view;

		view = getItem(contentView, R.id.lastItem, R.layout.dialog_list_item);

		ViewHolder viewHolder = (ViewHolder) view.getTag();
		viewHolder.position = position;

		TextView tv = (TextView) view.findViewById(R.id.textViewItem);
		tv.setText(mValues.get(position).toString());

		Button buttonUp = (Button) view.findViewById(R.id.buttonUp);
		buttonUp.setTag(viewHolder);
		buttonUp.setOnClickListener(this);

		Button buttonDown = (Button) view.findViewById(R.id.buttonDown);
		buttonDown.setTag(viewHolder);
		buttonDown.setOnClickListener(this);

		Button buttonDelete = (Button) view.findViewById(R.id.buttonDelete);
		buttonDelete.setTag(viewHolder);
		buttonDelete.setOnClickListener(this);

		return view;
	}

	private View getItem(View contentView, int id, int resourceId) {
		View view;
		ViewHolder viewHolder;
		if (contentView == null || contentView.getId() != id) {
			LayoutInflater layoutInflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = layoutInflater.inflate(resourceId, null);
			viewHolder = new ViewHolder();
			view.setTag(viewHolder);
		} else {
			view = contentView;
		}

		return view;
	}

	public void addItem(Object object) {
		mValues.add(object);
		notifyDataSetChanged();
	}

	public void upItem(int position) {
		if (position >= mValues.size()) {
			return;
		}

		Object object = mValues.get(position);
		if (position > 0) {
			mValues.remove(position);
			mValues.add(position - 1, object);
			notifyDataSetChanged();
		}
	}

	public void downItem(int position) {
		if (position < 0) {
			return;
		}

		Object object = mValues.get(position);
		if (position < mValues.size() - 1) {
			mValues.remove(position);
			mValues.add(position + 1, object);
			notifyDataSetChanged();
		}
	}

	public void removeItem(int position) {
		mValues.remove(position);
		notifyDataSetChanged();
	}

	@Override
	public void onClick(View v) {
		ViewHolder viewHolder = (ViewHolder) v.getTag();
		if (viewHolder != null) {
			int position = viewHolder.position;
			switch (v.getId()) {
			case R.id.buttonUp:
				upItem(position);
				break;
			case R.id.buttonDown:
				downItem(position);
				break;
			case R.id.buttonDelete:
				removeItem(position);
				break;
			}
		}
	}
}