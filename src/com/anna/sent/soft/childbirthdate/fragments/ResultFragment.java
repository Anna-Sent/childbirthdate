package com.anna.sent.soft.childbirthdate.fragments;

import java.util.Calendar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.anna.sent.soft.childbirthdate.R;
import com.anna.sent.soft.childbirthdate.pregnancy.Pregnancy;
import com.anna.sent.soft.childbirthdate.pregnancy.PregnancyCalculator;
import com.anna.sent.soft.childbirthdate.shared.Data;
import com.anna.sent.soft.childbirthdate.shared.DataClient;
import com.anna.sent.soft.utils.DateUtils;
import com.anna.sent.soft.utils.StateSaverFragment;

public abstract class ResultFragment extends StateSaverFragment implements
		DataClient, OnClickListener {
	public ResultFragment() {
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (container == null) {
			// We have different layouts, and in one of them this
			// fragment's containing frame doesn't exist. The fragment
			// may still be created from its saved state, but there is
			// no reason to try to create its view hierarchy because it
			// won't be displayed. Note this is not needed -- we could
			// just run the code below, where we would create and return
			// the view hierarchy; it would just never be used.
			return null;
		}

		View v = inflater.inflate(getLayoutResourceId(), container, false);
		return v;
	}

	private Data mData = null;

	@Override
	public void setData(Data data) {
		mData = data;
	}

	private TableLayout table;

	private Calendar currentDate = Calendar.getInstance();

	private View mSelectedRow = null;

	@SuppressWarnings("deprecation")
	@Override
	public void onClick(View v) {
		if (v instanceof TableRow) {
			if (mSelectedRow != null) {
				mSelectedRow.setBackgroundDrawable(null);
			}

			v.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.bg_selected_view));
			mSelectedRow = v;
		}
	}

	@Override
	public void setViews(Bundle savedInstanceState) {
		table = (TableLayout) getActivity().findViewById(getTableId());
		currentDate.setTimeInMillis(System.currentTimeMillis());
	}

	@Override
	public void restoreState(Bundle state) {
	}

	@Override
	public void saveState(Bundle state) {
	}

	protected abstract int getLayoutResourceId();

	protected abstract int getTableId();

	protected abstract String getResult(Pregnancy p);

	protected abstract String getMessage(Pregnancy p);

	@Override
	public void onResume() {
		super.onResume();
		table.removeAllViews();
		String[] methodNames = getResources().getStringArray(
				R.array.methodNames);
		for (int i = 0; i < mData.byMethod().length; ++i) {
			if (mData.byMethod()[i]) {
				Pregnancy pregnancy = PregnancyCalculator.Factory.get(mData,
						i + 1);
				pregnancy.setCurrentPoint(currentDate);
				View row = getActivity().getLayoutInflater().inflate(
						R.layout.result_row, null);
				TextView textView = (TextView) row.findViewById(R.id.textView);
				textView.setText(methodNames[i]);
				TextView result = (TextView) row.findViewById(R.id.result);
				TextView message = (TextView) row.findViewById(R.id.message);
				table.addView(row);
				row.setOnClickListener(this);

				result.setText(getResult(pregnancy));
				String msg = getMessage(pregnancy);
				if (msg != null && !msg.equals("")) {
					message.setText(msg);
				} else {
					message.setVisibility(View.GONE);
				}
			}
		}
	}

	public static final class ResultEcdFragment extends ResultFragment {
		@Override
		protected String getResult(Pregnancy p) {
			Calendar end = p.getEndPoint();
			return DateUtils.toString(getActivity(), end);
		}

		@Override
		protected String getMessage(Pregnancy p) {
			return null;
		}

		@Override
		protected int getTableId() {
			return R.id.tableEcd;
		}

		@Override
		protected int getLayoutResourceId() {
			return R.layout.result_ecd;
		}
	}

	public static final class ResultEgaFragment extends ResultFragment {
		@Override
		protected String getResult(Pregnancy p) {
			if (p.isCorrect()) {
				return p.getInfo(getActivity());
			} else {
				return getString(R.string.errorIncorrectGestationalAge);
			}
		}

		@Override
		protected String getMessage(Pregnancy p) {
			if (p.isCorrect()) {
				return p.getAdditionalInfo(getActivity());
			} else {
				Calendar start = p.getStartPoint();
				if (p.getCurrentPoint().before(start)) {
					return getString(R.string.errorIncorrectCurrentDateSmaller);
				} else {
					return getString(R.string.errorIncorrectCurrentDateGreater);
				}
			}
		}

		@Override
		protected int getTableId() {
			return R.id.tableEga;
		}

		@Override
		protected int getLayoutResourceId() {
			return R.layout.result_ega;
		}
	}
}
