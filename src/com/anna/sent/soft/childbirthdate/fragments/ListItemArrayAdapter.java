package com.anna.sent.soft.childbirthdate.fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.anna.sent.soft.childbirthdate.R;

public class ListItemArrayAdapter extends ArrayAdapter<String> implements
		OnClickListener {
	private String[] mStrings1;
	private String[] mStrings2;
	private ViewHolder[] mViewHolders;
	private int mCount;

	private static class ViewHolder {
		private CheckBox checkBox;
		private TextView text1;
		private TextView text2;
	}

	public ListItemArrayAdapter(Context context, String[] strings1,
			String[] strings2) {
		super(context, R.layout.list_item, R.id.text1, strings1);
		mStrings1 = strings1;
		mStrings2 = strings2;
		mCount = Math.min(mStrings1.length, mStrings2.length);
		mViewHolders = new ViewHolder[mCount];
	}

	@Override
	public int getCount() {
		return mCount;
	}

	@Override
	public View getView(int position, View contentView, ViewGroup viewGroup) {
		View view = null;
		ViewHolder viewHolder = null;

		if (contentView == null) {
			LayoutInflater layoutInflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = layoutInflater.inflate(R.layout.list_item, null);

			if (view != null) {
				viewHolder = new ViewHolder();
				viewHolder.checkBox = (CheckBox) view
						.findViewById(R.id.checkBox);
				viewHolder.text1 = (TextView) view.findViewById(R.id.text1);
				viewHolder.text2 = (TextView) view.findViewById(R.id.text2);
				// view.setTag(viewHolder);
				mViewHolders[position] = viewHolder;

				viewHolder.checkBox.setOnClickListener(this);
				viewHolder.checkBox.setTag(viewHolder);
				viewHolder.text1.setText(mStrings1[position]);
				viewHolder.text2.setText(mStrings2[position]);
				viewHolder.text2
						.setVisibility(viewHolder.checkBox.isChecked() ? View.VISIBLE
								: View.GONE);
			}
		} else {
			view = contentView;
			// viewHolder = (ViewHolder) contentView.getTag();
			viewHolder = mViewHolders[position];
		}

		return view;
	}

	@Override
	public void onClick(View v) {
		ViewHolder viewHolder = (ViewHolder) v.getTag();
		if (viewHolder != null) {
			viewHolder.text2
					.setVisibility(viewHolder.checkBox.isChecked() ? View.VISIBLE
							: View.GONE);
		}
	}

	public void updateValues(String[] strings2) {
		for (int i = 0; i < mCount && i < strings2.length; ++i) {
			mStrings2[i] = strings2[i];
			ViewHolder viewHolder = mViewHolders[i];
			if (viewHolder != null) {
				viewHolder.text2.setText(mStrings2[i]);
			}
		}
	}
}
