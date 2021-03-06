package bolona_pig.proj_imgapp.ObjectClass;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by DreamMii on 5/1/2559.
 */
public class UserLocalStore {

    public static final String SP_NAME = "userDetail";
    SharedPreferences userLocalDB;

    public UserLocalStore(Context context) {
        userLocalDB = context.getSharedPreferences(SP_NAME, 0);
    }

    public void storeUserData(User user) {
        SharedPreferences.Editor spEdt = userLocalDB.edit();
        spEdt.putString("username", user.username);
        spEdt.putString("password", user.password);
        spEdt.putString("name", user.name);
        spEdt.putString("nationId", user.nationId);
        spEdt.putString("email", user.email);
        spEdt.putString("telephone", user.telephone);
        spEdt.putString("imagePath", user.imagePath);
        spEdt.putBoolean("isAdmin", user.isAdmin());
        spEdt.apply();
    }

    public User getLoggedInUser() {
        String username = userLocalDB.getString("username", "");
        String password = userLocalDB.getString("password", "");
        String name = userLocalDB.getString("name", "");
        String nationId = userLocalDB.getString("nationId", "");
        String email = userLocalDB.getString("email", "");
        String telephone = userLocalDB.getString("telephone", "");
        String imagePath = userLocalDB.getString("imagePath", "");
        Boolean isAdmin = userLocalDB.getBoolean("isAdmin", false);

        User user = new User(username, password, name, nationId, email, telephone, imagePath);
        if (isAdmin) user.setAsAdmin();

        return user;
    }

    public void setUserLoggedIn(Boolean loggedIn) {
        SharedPreferences.Editor spEdt = userLocalDB.edit();
        spEdt.putBoolean("loggedIn", loggedIn);
        spEdt.apply();
    }

    public void clearUserData() {
        SharedPreferences.Editor spEdt = userLocalDB.edit();
        spEdt.clear();
        spEdt.apply();
    }

    public boolean getLoggedInStatus() {
        return userLocalDB.getBoolean("loggedIn", false);
    }
}
