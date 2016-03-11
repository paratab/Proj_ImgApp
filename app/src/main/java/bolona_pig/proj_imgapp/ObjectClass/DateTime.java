package bolona_pig.proj_imgapp.ObjectClass;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;

import java.util.Arrays;
import java.util.Calendar;

import bolona_pig.proj_imgapp.CallBack.GetDateCallback;

/**
 * Created by DreamMii on 30/1/2559.
 */
public class DateTime {

    static String months[] = {null, "Jan", "Feb", "Mar", "Apr", "May",
            "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    Context context;
    GetDateCallback dateCallback;

    public DateTime(Context context) {
        this.context = context;
    }

    public String getCurrentDate() {
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        return "" + mDay + "-" + months[mMonth + 1] + "-" + mYear;
    }

    public String getAge(String date) {
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        String[] day = date.split("-");
        dob.set(Integer.parseInt(day[2]), getMonthInt(day[1]), Integer.parseInt(day[0]));

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        return (Integer.valueOf(age)).toString();
    }

    public int getMonthInt(String str) {
        return Arrays.asList(months).indexOf(str);
    }

    public void showDatePickup(GetDateCallback dateCallback) {
        this.dateCallback = dateCallback;
        Calendar c = Calendar.getInstance();
        int sYear = c.get(Calendar.YEAR);
        int sMonth = c.get(Calendar.MONTH);
        int sDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(context,
                new DateSetListener(), sYear, sMonth, sDay);
        dialog.show();
    }

    public class DateSetListener implements DatePickerDialog.OnDateSetListener {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            int mYear = year;
            int mMonth = monthOfYear;
            int mDay = dayOfMonth;

            StringBuilder date = new StringBuilder().append(mDay).append("-")
                    .append(months[mMonth + 1]).append("-").append(mYear);

            dateCallback.done(date.toString());
        }
    }
}