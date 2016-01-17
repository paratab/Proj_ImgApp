package bolona_pig.proj_imgapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by DreamMii on 5/1/2559.
 */
public class ServerRequest {

    ProgressDialog progressDialog;
    public static final int CONNECTION_TIME = 1000 * 15;
    public static final String ADDRESS = "http://www.surawit-sj.xyz/";

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

        public StoreUserDataAsyncTask(User user, GetUserCallBack userCallBack) {
            this.user = user;
            this.userCallBack = userCallBack;
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


            String encodeData = getEncodeData(dataToSend);
            BufferedReader reader = null; // Read some data from server
            User returnUser = null;

            try {
                URL url = new URL(ADDRESS + "Register.php");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                con.setRequestMethod("POST");

                con.setDoOutput(true);

                OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());

                writer.write(encodeData);

                writer.flush();


                //----------Read Output from Post---------
                StringBuilder strb = new StringBuilder();
                reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                String line;
                while ((line = reader.readLine()) != null) {
                    strb.append(line + "\n");
                }
                line = strb.toString();

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
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close(); // Close Reader
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return returnUser;
        }

        private String getEncodeData(Map<String, String> data) {
            StringBuilder sb = new StringBuilder();
            for (String key : data.keySet()) {
                String value = null;
                try {
                    value = URLEncoder.encode(data.get(key), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                if (sb.length() > 0)
                    sb.append("&");

                sb.append(key + "=" + value);
            }
            return sb.toString();
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

        public FetchUserDataAsyncTask(User user, GetUserCallBack userCallBack) {
            this.user = user;
            this.userCallBack = userCallBack;
        }

        @Override
        public User doInBackground(Void... params) {
            Map<String, String> dataToSend = new HashMap<>();
            dataToSend.put("username", user.username);
            dataToSend.put("password", user.password);

            String encodeData = getEncodeData(dataToSend);
            BufferedReader reader = null; // Read some data from server

            User returnUser = null;

            try {
                URL url = new URL(ADDRESS + "FetchUserData.php");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                con.setRequestMethod("POST");
                con.setDoOutput(true);
                OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
                writer.write(encodeData);
                writer.flush();

                StringBuilder strb = new StringBuilder();
                reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                String line;
                while ((line = reader.readLine()) != null) {
                    strb.append(line + "\n");
                }
                line = strb.toString();

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
            } finally {
                if (reader != null) {
                    try {
                        reader.close(); // Close Reader
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return returnUser;
        }

        private String getEncodeData(Map<String, String> data) {
            StringBuilder sb = new StringBuilder();
            for (String key : data.keySet()) {
                String value = null;
                try {
                    value = URLEncoder.encode(data.get(key), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                if (sb.length() > 0)
                    sb.append("&");

                sb.append(key + "=" + value);
            }
            return sb.toString();
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

        public UpdateUserDataAsyncTask(User user, GetUserCallBack userCallBack) {
            this.user = user;
            this.userCallBack = userCallBack;
        }

        @Override
        public User doInBackground(Void... params) {
            Map<String, String> dataToSend = new HashMap<>();
            dataToSend.put("username", user.username);
            dataToSend.put("password", user.password);
            dataToSend.put("name", user.name);
            //dataToSend.put("nationId",user.nationId);
            dataToSend.put("email", user.email);
            dataToSend.put("telephone", user.telephone);

            String encodeData = getEncodeData(dataToSend);
            BufferedReader reader = null; // Read some data from server

            User returnUser = null;

            try {
                URL url = new URL(ADDRESS + "UpdateUserData.php");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                con.setRequestMethod("POST");
                con.setDoOutput(true);
                OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
                writer.write(encodeData);
                writer.flush();

                StringBuilder strb = new StringBuilder();
                reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                String line;
                while ((line = reader.readLine()) != null) {
                    strb.append(line + "\n");
                }
                line = strb.toString();

                Log.i("custom_check", line);

                JSONObject jObj = new JSONObject(line);

                if (jObj.length() != 0) {
                    String name = jObj.getString("name");
                    //String nationId = jObj.getString("nationId");
                    String email = jObj.getString("email");
                    String telephone = jObj.getString("telephone");

                    returnUser = new User(user.username, user.password, name, user.nationId, email, telephone);
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close(); // Close Reader
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return returnUser;
        }

        private String getEncodeData(Map<String, String> data) {
            StringBuilder sb = new StringBuilder();
            for (String key : data.keySet()) {
                String value = null;
                try {
                    value = URLEncoder.encode(data.get(key), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                if (sb.length() > 0)
                    sb.append("&");

                sb.append(key + "=" + value);
            }
            return sb.toString();
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

        public UpdateUserPasswordAsyncTask(User user, String newPassword, GetUserCallBack userCallBack) {
            this.user = user;
            this.userCallBack = userCallBack;
            this.newPassword = newPassword;
        }

        @Override
        public User doInBackground(Void... params) {
            Map<String, String> dataToSend = new HashMap<>();
            dataToSend.put("username", user.username);
            dataToSend.put("password", user.password);
            dataToSend.put("newPassword", newPassword);

            String encodeData = getEncodeData(dataToSend);
            BufferedReader reader = null; // Read some data from server

            User returnUser = null;

            try {
                URL url = new URL(ADDRESS + "UpdateUserPassword.php");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                con.setRequestMethod("POST");
                con.setDoOutput(true);
                OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
                writer.write(encodeData);
                writer.flush();

                StringBuilder strb = new StringBuilder();
                reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                String line;
                while ((line = reader.readLine()) != null) {
                    strb.append(line + "\n");
                }
                line = strb.toString();
                Log.i("custom_check", line);

                JSONObject jObj = new JSONObject(line);

                if (jObj.length() != 0) {
                    String password = jObj.getString("password");

                    returnUser = new User(user.username, password, user.name, user.nationId, user.email, user.telephone);
                }

            } catch (Exception e) {
                Log.i("custom_check", e.toString());
            } finally {
                if (reader != null) {
                    try {
                        reader.close(); // Close Reader
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return returnUser;
        }

        private String getEncodeData(Map<String, String> data) {
            StringBuilder sb = new StringBuilder();
            for (String key : data.keySet()) {
                String value = null;
                try {
                    value = URLEncoder.encode(data.get(key), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                if (sb.length() > 0)
                    sb.append("&");

                sb.append(key + "=" + value);
            }
            return sb.toString();
        }

        @Override
        protected void onPostExecute(User returnData) {
            progressDialog.dismiss();
            userCallBack.done(returnData);
            super.onPostExecute(returnData);

        }
    }

    protected String makeConnection(String encodeData, String urlFile) {

        BufferedReader reader = null; // Read some data from server
        String line = "";

        try {
            URL url = new URL(ADDRESS + urlFile);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("POST");
            con.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
            writer.write(encodeData);
            writer.flush();

            StringBuilder strb = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));


            while ((line = reader.readLine()) != null) {
                strb.append(line + "\n");
            }
            line = strb.toString();

            Log.i("custom_check", "The values received in the store part are as follows:");
            Log.i("custom_check", line);


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close(); // Close Reader
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return line;
    }
}
