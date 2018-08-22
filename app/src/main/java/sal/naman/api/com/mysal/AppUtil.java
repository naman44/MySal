package sal.naman.api.com.mysal;

import android.content.Context;
import android.telephony.TelephonyManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Naman-PC on 16-06-2018.
 */

public class AppUtil {

    public static String dateFormat = "yyyy/MM/dd";
    public static String timeFormat="HH:mm";
    public static String startingTime = "09:00";
    public static String endTime = "17:30";

    public static String formatDate(Date date) {
        SimpleDateFormat df = new SimpleDateFormat(dateFormat, getLocale());
        return df.format(date);
    }

    public static Date formatDateFromString(String date) throws ParseException{
        SimpleDateFormat df = new SimpleDateFormat(dateFormat, getLocale());
        return df.parse(date);
    }

    public static Date formatTimeFromString(String time) throws ParseException{
        SimpleDateFormat df = new SimpleDateFormat(timeFormat, getLocale());
        return df.parse(time);
    }

    public static String formatTime(String time){
        SimpleDateFormat df = new SimpleDateFormat(timeFormat, getLocale());
        return df.format(time);
    }

    public static String getMonthName(int month){
        String monthName;
        switch (month){
            case 0:
                monthName = "January";
                break;
            case 1:
                monthName = "February";
                break;
            case 2:
                monthName =  "March";
                break;
            case 3:
                monthName = "April";
                break;
            case 4:
                monthName = "May";
                break;
            case 5:
                monthName = "June";
                break;
            case 6:
                monthName = "July";
                break;
            case 7:
                monthName = "August";
                break;
            case 8:
                monthName = "September";
                break;
            case 9:
                monthName = "October";
                break;
            case 10:
                monthName = "November";
                break;
            case 11:
                monthName = "December";
                break;
            default:
                monthName = "Not Found";
                break;
        }
        return monthName;
    }

    public static String[] calculateDate(int month){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH , 1);
        String firstDate = AppUtil.formatDate(cal.getTime());
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DATE));
        String lastDate = AppUtil.formatDate(cal.getTime());
        return new String[]{firstDate, lastDate};
    }

    private static Locale getLocale(){
        return Locale.getDefault();
    }

    private static Locale getLocale(Context context){
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String country = null;
        if(tm != null)
            country = tm.getNetworkCountryIso();
        if(country != null && !country.isEmpty()) {
            return new Locale("en", country);
        }
        else return Locale.getDefault();
    }

    private static String getCurrencySymbol(Context context){
        Locale locale = getLocale(context);
        if(locale != null){
            Currency currency = Currency.getInstance(getLocale(context));
            return currency.getSymbol();
        }
        else
            return "";
    }

    public static String getAmountWithSymbol(Context context, double amount){
        return getCurrencySymbol(context) + amount;
    }

    public static double getAmountSansSymbol(Context context, String amount){
        amount = amount.replace(getCurrencySymbol(context), "");
        return Double.parseDouble(amount);
    }
}
