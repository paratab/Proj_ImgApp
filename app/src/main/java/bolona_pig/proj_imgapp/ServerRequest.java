package bolona_pig.proj_imgapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by DreamMii on 5/1/2559.
 */
public class ServerRequest {

    public static final String ADDRESS = "http://www.surawit-sj.xyz/";
    ProgressDialog progressDialog;

    public ServerRequest(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Processing");
        progressDialog.setMessage("Please wait....");
    }

    public void storeUserDataInBG(User user, GetUserCallBack userCallBack) {
        progressDialog.show();
        new StoreUserDataAsyncTask(user, userCallBack).execute();
    }

    public void fetchUserDataInBG(User user, GetUserCallBack userCallBack) {
        progressDialog.show();
        new FetchUserDataAsyncTask(user, userCallBack).execute();
    }

    public void updateUserDataInBG(User user, GetUserCallBack userCallBack) {
        progressDialog.show();
        new UpdateUserDataAsyncTask(user, userCallBack).execute();
    }

    public void updateUserPasswordInBG(User user, String newPassword, GetUserCallBack userCallBack) {
        progressDialog.show();
        new UpdateUserPasswordAsyncTask(user, newPassword, userCallBack).execute();
    }

    public class StoreUserDataAsyncTask extends AsyncTask<Void, Void, User> {

        User user;
        GetUserCallBack userCallBack;
        HttpRequest httpRequest;

        public StoreUserDataAsyncTask(User user, GetUserCallBack userCallBack) {
            this.user = user;
            this.userCallBack = userCallBack;
            httpRequest = new HttpRequest();
        }

        @Override
        protected User doInBackground(Void... params) {

            Map<String, String> dataToSend = new HashMap<>();
            dataToSend.put("username", user.username);
            dataToSend.put("password", user.password);
            dataToSend.put("name", user.name);
            dataToSend.put("nationId", user.nationId);
            dataToSend.put("email", user.email);
            dataToSend.put("telephone", user.telephone);

            User returnUser = null;

            try {

                String line = httpRequest.makeHttpRequest(dataToSend, ADDRESS + "Register.php");
                Log.i("custom_check", line);

                JSONObject jObj = new JSONObject(line);

                if (jObj.length() != 0) {
                    String name = jObj.getString("name");
                    String nationId = jObj.getString("nationId");
                    String email = jObj.getString("email");
                    String telephone = jObj.getString("telephone");

                    returnUser = new User(user.username, user.password, name, nationId, email, telephone);
                }
            } catch (Exception e) {
                Log.e("custom_check", e.toString());
            }


            return returnUser;
        }

        @Override
        protected void onPostExecute(User returnUser) {
            progressDialog.dismiss();
            userCallBack.done(returnUser);
            super.onPostExecute(returnUser);
        }
    }

    public class FetchUserDataAsyncTask extends AsyncTask<Void, Void, User> {
        User user;
        GetUserCallBack userCallBack;
        HttpRequest httpRequest;

        public FetchUserDataAsyncTask(User user, GetUserCallBack userCallBack) {
            this.user = user;
            this.userCallBack = userCallBack;
            httpRequest = new HttpRequest();
        }

        @Override
        public User doInBackground(Void... params) {
            Map<String, String> dataToSend = new HashMap<>();
            dataToSend.put("username", user.username);
            dataToSend.put("password", user.password);

            User returnUser = null;

            try {

                String line = httpRequest.makeHttpRequest(dataToSend, ADDRESS + "FetchUserData.php");
                Log.i("custom_check", line);

                JSONObject jObj = new JSONObject(line);

                if (jObj.length() != 0) {
                    String name = jObj.getString("name");
                    String nationId = jObj.getString("nationId");
                    String email = jObj.getString("email");
                    String telephone = jObj.getString("telephone");

                    returnUser = new User(user.username, user.password, name, nationId, email, telephone);
                }

            } catch (Exception e) {
                Log.i("custom_check", e.toString());
            }

            return returnUser;
        }

        @Override
        protected void onPostExecute(User returnData) {
            progressDialog.dismiss();
            userCallBack.done(returnData);
            super.onPostExecute(returnData);

        }
    }

    public class UpdateUserDataAsyncTask extends AsyncTask<Void, Void, User> {
        User user;
        GetUserCallBack userCallBack;
        HttpRequest httpRequest;

        public UpdateUserDataAsyncTask(User user, GetUserCallBack userCallBack) {
            this.user = user;
            this.userCallBack = userCallBack;
            httpRequest = new HttpRequest();
        }

        @Override
        public User doInBackground(Void... params) {
            Map<String, String> dataToSend = new HashMap<>();
            dataToSend.put("username", user.username);
            dataToSend.put("password", user.password);
            dataToSend.put("name", user.name);
            dataToSend.put("email", user.email);
            dataToSend.put("telephone", user.telephone);

            User returnUser = null;

            try {
                String line = httpRequest.makeHttpRequest(dataToSend, ADDRESS + "UpdateUserData.php");
                Log.i("custom_check", line);

                JSONObject jObj = new JSONObject(line);

                if (jObj.length() != 0) {
                    String name = jObj.getString("name");
                    String email = jObj.getString("email");
                    String telephone = jObj.getString("telephone");

                    returnUser = new User(user.username, user.password, name, user.nationId, email, telephone);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return returnUser;
        }

        @Override
        protected void onPostExecute(User returnData) {
            progressDialog.dismiss();
            userCallBack.done(returnData);
            super.onPostExecute(returnData);

        }
    }

    public class UpdateUserPasswordAsyncTask extends AsyncTask<Void, Void, User> {
        User user;
        GetUserCallBack userCallBack;
        String newPassword;
        HttpRequest httpRequest;

        public UpdateUserPasswordAsyncTask(User user, String newPassword, GetUserCallBack userCallBack) {
            this.user = user;
            this.userCallBack = userCallBack;
            this.newPassword = newPassword;
            httpRequest = new HttpRequest();
        }

        @Override
        public User doInBackground(Void... params) {
            Map<String, String> dataToSend = new HashMap<>();
            dataToSend.put("username", user.username);
            dataToSend.put("password", user.password);
            dataToSend.put("newPassword", newPassword);

            User returnUser = null;

            try {
                String line = httpRequest.makeHttpRequest(dataToSend, ADDRESS + "UpdateUserPassword.php");
                Log.i("custom_check", line);

                JSONObject jObj = new JSONObject(line);

                if (jObj.length() != 0) {
                    String password = jObj.getString("password");

                    returnUser = new User(user.username, password, user.name, user.nationId, user.email, user.telephone);
                }

            } catch (Exception e) {
                Log.i("custom_check", e.toString());
            }

            return returnUser;
        }

        @Override
        protected void onPostExecute(User returnData) {
            progressDialog.dismiss();
            userCallBack.done(returnData);
            super.onPostExecute(returnData);

        }
    }

}
