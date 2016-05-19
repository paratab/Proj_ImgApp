package bolona_pig.proj_imgapp.ObjectClass;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import bolona_pig.proj_imgapp.CallBack.GetBooleanCallBack;

/**
 * Created by DreamMii on 17/1/2559.
 */
public class MidModule {

    public MidModule() {
    }

    public boolean isValidUsername(String username) {
        Pattern emailPattern = Pattern.compile("^[a-zA-Z0-9_-]{3,15}$");
        Matcher m = emailPattern.matcher(username);
        return m.matches();
    }

    public boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public String getSHA1Hash(String str) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            messageDigest.update(str.getBytes("UTF-8"));
            byte[] bytes = messageDigest.digest();
            StringBuilder buffer = new StringBuilder();
            for (byte b : bytes) {
                buffer.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
            }
            return buffer.toString();
        } catch (Exception ignored) {
            Log.e("custom_check", ignored.toString());
            return null;
        }
    }

    public void printError(Context context, CharSequence text) {
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    public String bitmapToString(Bitmap in) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        in.compress(Bitmap.CompressFormat.JPEG, 80, bytes);
        return Base64.encodeToString(bytes.toByteArray(), Base64.DEFAULT);
    }

    public Bitmap stringToBitmap(String in) {
        byte[] bytes = Base64.decode(in, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    public void showAlertDialog(String resultString, Context context) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setTitle("ข้อผิดพลาด");
        dialogBuilder.setMessage(resultString);
        dialogBuilder.setPositiveButton("รับทราบ", null);
        dialogBuilder.show();
    }

    public Bitmap resizeBitmapFromURI(Context c, Uri imageURI) {
        try {
            int inWidth = 0, inHeight = 0;
            InputStream inputStream = c.getContentResolver().openInputStream(imageURI);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(inputStream, null, options);
            //inputStream.close();
            inputStream = null;

            inWidth = options.outWidth;
            inHeight = options.outHeight;

            int dstWidth = 2048;
            int dstHeight = 1152;

//            if((Float.parseFloat(inWidth+"")/inHeight) == 4.0/3){
//                 dstWidth = 2048;
//                 dstHeight = 1536;
//            }else if((Float.parseFloat(inWidth+"")/inHeight) == 16.0/9){
//                dstWidth = 2560;
//                dstHeight = 1536;
//            }else{
//                dstWidth = inWidth;
//                dstHeight = inHeight;
//            }

            inputStream = c.getContentResolver().openInputStream(imageURI);
            options = new BitmapFactory.Options();
            options.inSampleSize = Math.max(inWidth / dstWidth, inHeight / dstHeight);
            Bitmap roughBitmap = BitmapFactory.decodeStream(inputStream, null, options);
            Log.i("custom_check", "resizeBitmapFromURI Before: W" + roughBitmap.getWidth() + ",H" + roughBitmap.getHeight());
            if (inHeight * inWidth <= dstHeight * dstWidth) return roughBitmap;
            Matrix m = new Matrix();
            RectF inRect = new RectF(0, 0, roughBitmap.getWidth(), roughBitmap.getHeight());
            RectF outRect = new RectF(0, 0, dstWidth, dstHeight);
            m.setRectToRect(inRect, outRect, Matrix.ScaleToFit.CENTER);
            float[] values = new float[9];
            m.getValues(values);

            Bitmap resizedBitmap = Bitmap.createScaledBitmap(roughBitmap, (int) (roughBitmap.getWidth() * values[0]), (int) (roughBitmap.getHeight() * values[4]), true);
            Log.i("custom_check", "resizeBitmapFromURI After: W" + resizedBitmap.getWidth() + ",H" + resizedBitmap.getHeight());

            return resizedBitmap;

        } catch (Exception e) {
            Log.e("custom_check", e.toString());
        }

        return null;
    }

    public void decodeLatLng(Context c, String latlng, GetBooleanCallBack booleanCallBack) {
        new ReverseGeoCodingTask(c, latlng, booleanCallBack).execute();
    }

    private class ReverseGeoCodingTask extends AsyncTask<Void, Void, Boolean> {
        LatLng latlng;
        GetBooleanCallBack booleanCallback;
        String str;
        Context context;

        public ReverseGeoCodingTask(Context c, String latlngStr, GetBooleanCallBack booleanCallback) {
            latlngStr = latlngStr.substring(11, latlngStr.length() - 1);
            String[] latlong = latlngStr.split(",");
            double latitude = Double.parseDouble(latlong[0]);
            double longitude = Double.parseDouble(latlong[1]);
            this.latlng = new LatLng(latitude, longitude);
            this.booleanCallback = booleanCallback;
            this.context = c;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            Locale.setDefault(new Locale("th_TH"));

            Geocoder geocoder = new Geocoder(context);
            List<Address> addresses = null;

            try {
                addresses = geocoder.getFromLocation(latlng.latitude, latlng.longitude, 1);
            } catch (IOException e) {
                Log.e("custom_check", e.toString());
            }

            if (addresses != null && addresses.size() > 0) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    if (returnedAddress.getMaxAddressLineIndex() == (i - 1)) {
                        strReturnedAddress.append(returnedAddress.getAddressLine(i));
                    } else {
                        strReturnedAddress.append(returnedAddress.getAddressLine(i)).append(",");
                    }
                }
                str = "สถานที่โดยประมาณ : " + strReturnedAddress.toString();
                //Log.e("custom_check", str);
            }

            return Boolean.TRUE;
        }

        @Override
        protected void onPostExecute(Boolean bool) {
            booleanCallback.done(bool, str);
            super.onPostExecute(bool);
        }
    }
}
