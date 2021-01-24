package com.tws.courier.domain.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.RawRes;

import com.tws.courier.domain.models.OrderValidation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The type Utils.
 */
public class Utils {

   // String MOBILE_NUMBER_PATTERN = "^[6-9]{1}\\d{9}$";
    private static String MOBILE_NUMBER_PATTERN= "^[6-9]{1}\\d{9}$";
    private static final Pattern m_no_pattern = Pattern.compile(MOBILE_NUMBER_PATTERN);


    /**
     * Is network connected available boolean.
     *
     * @param context the context
     * @return the boolean
     */
    public static boolean isNetworkConnectedAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }

    /**
     * Is valid email boolean.
     *
     * @param email the email
     * @return the boolean
     */
    public static boolean isValidEmail(String email) {
        if (TextUtils.isEmpty(email)) return false;
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /**
     * Is valid phone number boolean.
     *
     * @param number the number
     * @return the boolean
     */
    public static boolean isValidPhoneNumber(String number) {
        if (TextUtils.isEmpty(number)) return false;
        return Patterns.PHONE.matcher(number).matches();
//        return ((number.length() == 10 && !number.startsWith("0", 0)) || number.length() > 10)
// && Patterns.PHONE.matcher(number.trim()).matches();
    }
    public static boolean isNumber(String input)
    {
        //String input = "34s";
        if(input.matches("^\\d+(\\.\\d+)?"))
        {
            //okay
            return true;
        } else {
            // not okay !
            return false;
        }
    }

    public static boolean isMobileNumber(final String mobileNumber)
    {
        Matcher matcher = m_no_pattern.matcher(mobileNumber);
        return matcher.matches();
    }

    /**
     * Close key board.
     *
     * @param context the context
     */
    public static void closeKeyBoard(Context context) {
        if (context instanceof Activity) {
            View view = ((Activity) context).getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    /**
     * Close key board.
     *
     * @param context the context
     * @param view    the view
     */
    public static void closeKeyBoard(Context context, View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * Gets rounded corner bitmap.
     *
     * @param bitmap the bitmap
     * @param pixels the pixels
     * @return the rounded corner bitmap
     */
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    /**
     * Gets bitmap from drawable.
     *
     * @param drawable the drawable
     * @return the bitmap from drawable
     */
    public static Bitmap getBitmapFromDrawable(Drawable drawable) {
        if (drawable == null) {
            return null;
        }

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        try {
            Bitmap bitmap;
            int COLORDRAWABLE_DIMENSION = 2;
            final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;
            if (drawable instanceof ColorDrawable) {
                bitmap = Bitmap.createBitmap(COLORDRAWABLE_DIMENSION, COLORDRAWABLE_DIMENSION, BITMAP_CONFIG);
            } else {
                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), BITMAP_CONFIG);
            }

            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Print key hash.
     *
     * @param context the context
     */
    public static void printKeyHash(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Logger.print("Hash Key : " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
        } catch (Exception e) {
        }
    }

    /**
     * Read json file string.
     *
     * @param context  the context
     * @param jsonFile the json file
     * @return the string
     */
    public static String readJsonFile(Context context, @RawRes int jsonFile) {
        InputStream is = context.getResources().openRawResource(jsonFile);
        String output = null;
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
            output = writer.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return output;
    }

    /* Checks if external storage is available for read and write */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public static void copyFile(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        try {
            OutputStream out = new FileOutputStream(dst);
            try {
                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            } finally {
                out.close();
            }
        } finally {
            in.close();
        }
    }

    public static int convertSpToPx(Context context, float sp) {
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
        return px;
    }


    public static int convertDpToPx(Context context, int dps) {
        DisplayMetrics metrics = new DisplayMetrics();
        if (context instanceof Activity) {
            ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
            return (int) (metrics.density * dps);
        } else
            return 0;
    }

    public static void openAppInPlaystore(Context context) {
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=".concat(context.getPackageName())));
        context.startActivity(intent);
    }




    public static String getAreaType(int journeyType) {
        String type="";
        if(journeyType==0)
        {
            type="local";
        }
        else if(journeyType==1)
        {
            type="local";
        }
        else if(journeyType==2)
        {
            type="national";
        }
        else if(journeyType==3)
        {
            type="international";
        }
        return type;
    }

    public static String getVehicleType(int journeyType,String domestic) {
String type="";
        if(journeyType==0)
        {
            type="bike";
        }
        else if(journeyType==1)
        {
            type="truck";
        }
        else if(journeyType==2)
        {

            if(domestic.equalsIgnoreCase("domesticTruck"))
            {
                type="truck";
            }
            else if(domestic.equalsIgnoreCase("domesticTrain"))
            {
                type="train";
            }
            else if(domestic.equalsIgnoreCase("domesticFlight"))
            {
                type="flight";
            }

        }
        else if(journeyType==3)
        {
            type="flight";
        }
        return type;
    }
}
