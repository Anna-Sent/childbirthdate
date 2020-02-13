package com.anna.sent.soft.childbirthdate.actions;

import com.anna.sent.soft.childbirthdate.R;
import com.anna.sent.soft.childbirthdate.data.DataImpl;
import com.anna.sent.soft.childbirthdate.pregnancy.Pregnancy;
import com.anna.sent.soft.childbirthdate.pregnancy.PregnancyCalculator;
import com.anna.sent.soft.childbirthdate.utils.DateUtils;
import com.anna.sent.soft.utils.UserEmailFetcher;

public class EmailDataActionActivity extends EmailActionActivity {
    @Override
    protected String getEmail() {
        return UserEmailFetcher.getEmail(this);
    }

    @Override
    protected String getText() {
        DataImpl data = new DataImpl(this);
        data.update();
        StringBuilder result = new StringBuilder();
        String[] strings1 = getResources().getStringArray(R.array.MethodNames);
        String[] strings2 = data.getStrings2();
        boolean[] byMethod = data.byMethod();
        for (int i = 0; i < byMethod.length; ++i) {
            if (byMethod[i]) {
                Pregnancy p = PregnancyCalculator.Factory.get(data, i + 1);
                if (p != null) {
                    result.append(strings1[i]);
                    result.append(": ");
                    result.append(strings2[i]);
                    result.append("\n\t");
                    String ecd = DateUtils.toString(this, p.getEndPoint());
                    result.append(getString(R.string.ecdIs, ecd));
                    result.append("\n\n");
                }
            }
        }

        return result.toString();
    }
}