package bolona_pig.proj_imgapp;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.security.MessageDigest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by DreamMii on 17/1/2559.
 */
public class SecureModule {

    public SecureModule() {
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

}
