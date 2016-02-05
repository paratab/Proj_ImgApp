package bolona_pig.proj_imgapp.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import bolona_pig.proj_imgapp.CallBack.GetNoticeCallBack;
import bolona_pig.proj_imgapp.CallBack.GetSeenInfoCallback;
import bolona_pig.proj_imgapp.CallBack.GetUserCallBack;
import bolona_pig.proj_imgapp.ObjectClass.HttpRequest;
import bolona_pig.proj_imgapp.ObjectClass.Notice;
import bolona_pig.proj_imgapp.ObjectClass.SeenInfo;
import bolona_pig.proj_imgapp.ObjectClass.User;

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

    public void storeNoticeDataInBG(Notice notice, GetNoticeCallBack noticeCallBack) {
        progressDialog.show();
        new StoreNoticeDataAsyncTask(notice, noticeCallBack).execute();
    }

    public void fetchNoticeDataInBG(int noticeId, GetNoticeCallBack noticeCallBack) {
        progressDialog.show();
        new FetchNoticeDataAsyncTask(noticeId, noticeCallBack).execute();
    }

    public void updateNoticeDataInBG(Notice notice, GetNoticeCallBack noticeCallBack) {
        progressDialog.show();
        new UpdateNoticeDataAsyncTask(notice, noticeCallBack).execute();
    }

    public void storeSeenInfoDataInBG(SeenInfo info, GetSeenInfoCallback seenInfoCallback) {
        progressDialog.show();
        new StoreSeenInfoDataAsyncTask(info, seenInfoCallback).execute();
    }

    public void fetchSeenInfoDataInBG(int infoId, GetSeenInfoCallback seenInfoCallback) {
        progressDialog.show();
        new FetchSeenInfoDataAsyncTask(infoId, seenInfoCallback).execute();
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

                String line = httpRequest.makeHttpRequest(dataToSend, ADDRESS + "StoreUserData.php");
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

    public class StoreNoticeDataAsyncTask extends AsyncTask<Void, Void, Notice> {

        Notice notice;
        GetNoticeCallBack noticeCallBack;
        HttpRequest httpRequest;

        public StoreNoticeDataAsyncTask(Notice notice, GetNoticeCallBack noticeCallBack) {
            this.notice = notice;
            this.noticeCallBack = noticeCallBack;
            httpRequest = new HttpRequest();
        }


        @Override
        protected Notice doInBackground(Void... params) {

            Map<String, String> dataToSend = new HashMap<>();
            dataToSend.put("lnName", notice.lnName);
            dataToSend.put("lnBirthDate", notice.lnBirthDate);
            dataToSend.put("lnPlace", notice.lnPlace);
            dataToSend.put("lnLostDate", notice.lnLostDate);
            dataToSend.put("lnDetail", notice.lnDetail);
            dataToSend.put("lnAdder", notice.lnAdder);
            //dataToSend.put("lnPhone", notice.lnPhone);

            try {

                String line = httpRequest.makeHttpRequest(dataToSend, ADDRESS + "StoreNoticeData.php");
                Log.i("custom_check", line);

                JSONObject jObj = new JSONObject(line);

                if (jObj.length() != 0) {
                    String error = jObj.getString("error");
                    String lnName = jObj.getString("lnName");
                    String lnBirthDate = jObj.getString("lnBirthDate");
                    String lnPlace = jObj.getString("lnPlace");
                    String lnLostDate = jObj.getString("lnLostDate");
                    String lnDetail = jObj.getString("lnDetail");
                    String lnAdder = jObj.getString("lnAdder");
                    String lnPhone = jObj.getString("lnPhone");
                    int lnId = jObj.getInt("lnId");

                    if (error.equals("null")) {
                        notice = new Notice(lnId, lnName, lnBirthDate, lnPlace, lnLostDate, lnDetail, lnAdder, lnPhone);
                    } else {
                        notice = null;
                    }
                }
            } catch (Exception e) {
                Log.e("custom_check", e.toString());
            }
            return notice;
        }

        @Override
        protected void onPostExecute(Notice returnNotice) {
            progressDialog.dismiss();
            noticeCallBack.done(returnNotice);
            super.onPostExecute(returnNotice);
        }
    }

    public class FetchNoticeDataAsyncTask extends AsyncTask<Void, Void, Notice> {
        Notice notice;
        GetNoticeCallBack noticeCallBack;
        HttpRequest httpRequest;
        int noticeId;

        public FetchNoticeDataAsyncTask(int noticeId, GetNoticeCallBack noticeCallBack) {
            this.noticeId = noticeId;
            this.noticeCallBack = noticeCallBack;
            httpRequest = new HttpRequest();
        }

        @Override
        public Notice doInBackground(Void... params) {
            Map<String, String> dataToSend = new HashMap<>();
            dataToSend.put("noticeId", noticeId + "");

            notice = null;

            try {

                String line = httpRequest.makeHttpRequest(dataToSend, ADDRESS + "FetchNoticeData.php");
                Log.i("custom_check", line);

                JSONObject jObj = new JSONObject(line);

                if (jObj.length() != 0) {
                    String error = jObj.getString("error");
                    String lnName = jObj.getString("lnName");
                    String lnBirthDate = jObj.getString("lnBirthDate");
                    String lnPlace = jObj.getString("lnPlace");
                    String lnLostDate = jObj.getString("lnLostDate");
                    String lnDetail = jObj.getString("lnDetail");
                    String lnAdder = jObj.getString("lnAdder");
                    String lnPhone = jObj.getString("lnPhone");
                    int lnId = jObj.getInt("lnId");

                    if (error.equals("null")) {
                        notice = new Notice(lnId, lnName, lnBirthDate, lnPlace, lnLostDate, lnDetail, lnAdder, lnPhone);
                    } else {
                        notice = null;
                    }
                }

            } catch (Exception e) {
                Log.i("custom_check", e.toString());
            }

            return notice;
        }

        @Override
        protected void onPostExecute(Notice returnData) {
            progressDialog.dismiss();
            noticeCallBack.done(returnData);
            super.onPostExecute(returnData);

        }
    }

    public class UpdateNoticeDataAsyncTask extends AsyncTask<Void, Void, Notice> {
        Notice notice;
        GetNoticeCallBack noticeCallBack;
        HttpRequest httpRequest;

        public UpdateNoticeDataAsyncTask(Notice notice, GetNoticeCallBack noticeCallBack) {
            this.notice = notice;
            this.noticeCallBack = noticeCallBack;
            httpRequest = new HttpRequest();
        }

        @Override
        public Notice doInBackground(Void... params) {

            Map<String, String> dataToSend = new HashMap<>();
            dataToSend.put("lnName", notice.lnName);
            dataToSend.put("lnBirthDate", notice.lnBirthDate);
            dataToSend.put("lnPlace", notice.lnPlace);
            dataToSend.put("lnLostDate", notice.lnLostDate);
            dataToSend.put("lnDetail", notice.lnDetail);
            dataToSend.put("noticeId", notice.id + "");

            notice = null;

            try {

                String line = httpRequest.makeHttpRequest(dataToSend, ADDRESS + "UpdateNoticeData.php");
                Log.i("custom_check", line);

                JSONObject jObj = new JSONObject(line);

                if (jObj.length() != 0) {
                    String error = jObj.getString("error");
                    String lnName = jObj.getString("lnName");
                    String lnBirthDate = jObj.getString("lnBirthDate");
                    String lnPlace = jObj.getString("lnPlace");
                    String lnLostDate = jObj.getString("lnLostDate");
                    String lnDetail = jObj.getString("lnDetail");
                    String lnAdder = jObj.getString("lnAdder");
                    String lnPhone = jObj.getString("lnPhone");
                    int lnId = jObj.getInt("lnId");

                    if (error.equals("null")) {
                        notice = new Notice(lnId, lnName, lnBirthDate, lnPlace, lnLostDate, lnDetail, lnAdder, lnPhone);
                    } else {
                        notice = null;
                    }
                }

            } catch (Exception e) {
                Log.i("custom_check", e.toString());
            }

            return notice;
        }

        @Override
        protected void onPostExecute(Notice returnData) {
            progressDialog.dismiss();
            noticeCallBack.done(returnData);
            super.onPostExecute(returnData);

        }
    }

    public class StoreSeenInfoDataAsyncTask extends AsyncTask<Void, Void, SeenInfo> {

        SeenInfo seenInfo;
        GetSeenInfoCallback seenInfoCallback;
        HttpRequest httpRequest;

        public StoreSeenInfoDataAsyncTask(SeenInfo seenInfo, GetSeenInfoCallback seenInfoCallback) {
            this.seenInfo = seenInfo;
            this.seenInfoCallback = seenInfoCallback;
            httpRequest = new HttpRequest();
        }

        @Override
        protected SeenInfo doInBackground(Void... params) {

            Map<String, String> dataToSend = new HashMap<>();
            dataToSend.put("seenDate", seenInfo.seenDate);
            dataToSend.put("seenPlace", seenInfo.seenPlace);
            dataToSend.put("seenDetail", seenInfo.seenDetail);
            dataToSend.put("seenAdder", seenInfo.seenAdder);

            try {

                String line = httpRequest.makeHttpRequest(dataToSend, ADDRESS + "StoreSeenInfoData.php");
                Log.i("custom_check", line);

                JSONObject jObj = new JSONObject(line);

                if (jObj.length() != 0) {
                    String error = jObj.getString("error");
                    String seenDate = jObj.getString("seenDate");
                    String seenPlace = jObj.getString("seenPlace");
                    String seenDetail = jObj.getString("seenDetail");
                    String seenAdder = jObj.getString("seenAdder");
                    String seenPhone = jObj.getString("seenPhone");
                    int seenId = jObj.getInt("seenId");

                    if (error.equals("null")) {
                        seenInfo = new SeenInfo(seenId, seenDate, seenPlace, seenDetail, seenAdder, seenPhone);
                    } else {
                        seenInfo = null;
                    }
                }
            } catch (Exception e) {
                Log.e("custom_check", e.toString());
            }
            return seenInfo;
        }

        @Override
        protected void onPostExecute(SeenInfo returnInfo) {
            progressDialog.dismiss();
            seenInfoCallback.done(returnInfo);
            super.onPostExecute(returnInfo);
        }
    }

    public class FetchSeenInfoDataAsyncTask extends AsyncTask<Void, Void, SeenInfo> {
        GetSeenInfoCallback seenInfoCallback;
        HttpRequest httpRequest;
        int infoId;
        SeenInfo seenInfo;

        public FetchSeenInfoDataAsyncTask(int infoId, GetSeenInfoCallback seenInfoCallback) {
            this.infoId = infoId;
            this.seenInfoCallback = seenInfoCallback;
            httpRequest = new HttpRequest();
        }

        @Override
        public SeenInfo doInBackground(Void... params) {
            Map<String, String> dataToSend = new HashMap<>();
            dataToSend.put("infoId", infoId + "");

            seenInfo = null;

            try {

                String line = httpRequest.makeHttpRequest(dataToSend, ADDRESS + "FetchSeenInfoData.php");
                Log.i("custom_check", line);

                JSONObject jObj = new JSONObject(line);

                if (jObj.length() != 0) {
                    String error = jObj.getString("error");
                    String seenDate = jObj.getString("seenDate");
                    String seenPlace = jObj.getString("seenPlace");
                    String seenDetail = jObj.getString("seenDetail");
                    String seenAdder = jObj.getString("seenAdder");
                    String seenPhone = jObj.getString("seenPhone");
                    int seenId = jObj.getInt("seenId");

                    if (error.equals("null")) {
                        seenInfo = new SeenInfo(seenId, seenDate, seenPlace, seenDetail, seenAdder, seenPhone);
                    } else {
                        seenInfo = null;
                    }
                }

            } catch (Exception e) {
                Log.i("custom_check", e.toString());
            }

            return seenInfo;
        }

        @Override
        protected void onPostExecute(SeenInfo seenInfo) {
            progressDialog.dismiss();
            seenInfoCallback.done(seenInfo);
            super.onPostExecute(seenInfo);

        }
    }
}
