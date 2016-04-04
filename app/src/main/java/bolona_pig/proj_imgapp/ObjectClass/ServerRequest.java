package bolona_pig.proj_imgapp.ObjectClass;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import bolona_pig.proj_imgapp.CallBack.GetBooleanCallBack;
import bolona_pig.proj_imgapp.CallBack.GetClueCallback;
import bolona_pig.proj_imgapp.CallBack.GetItemCallback;
import bolona_pig.proj_imgapp.CallBack.GetNoticeCallBack;
import bolona_pig.proj_imgapp.CallBack.GetUserCallBack;
import bolona_pig.proj_imgapp.R;

/**
 * Created by DreamMii on 5/1/2559.
 */
public class ServerRequest {

    public static final String ADDRESS = "http://www.surawit-sj.xyz";
    ProgressDialog progressDialog;
    Context context;

    public ServerRequest(Context context) {
        this.context = context;
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle(R.string.processing);
        progressDialog.setMessage("กรุณารอซักครู่...");
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

    public void storeClueDataInBG(Clue info, GetClueCallback seenInfoCallback) {
        progressDialog.show();
        new StoreClueDataAsyncTask(info, seenInfoCallback).execute();
    }

    public void fetchClueDataInBG(int infoId, GetClueCallback seenInfoCallback) {
        progressDialog.show();
        new FetchClueDataAsyncTask(infoId, seenInfoCallback).execute();
    }

    public void fetchNoticeItemGridInBG(int offset, GetItemCallback itemCallback) {
        new FetchNoticeItemGridAsyncTask(offset, itemCallback).execute();
    }

    public void fetchUserNoticeListInBG(User user, GetItemCallback itemCallback) {
        new FetchUserNoticeListAsyncTask(user, itemCallback).execute();
    }

    public void fetchUserClueListInBG(User user, GetItemCallback itemCallback) {
        new FetchUserClueListAsyncTask(user, itemCallback).execute();
    }

    public void checkUsernameExisted(String username, GetBooleanCallBack stringCallBack) {
        new CheckUsernameExistInBG(username, stringCallBack).execute();
    }

    public void updateNoticeStatus(int id, String username, GetBooleanCallBack booleanCallBack) {
        progressDialog.show();
        new UpdateNoticeStatusAsyncTask(id, username, booleanCallBack).execute();
    }

    public void customSearchInBG(String sex, int minAge, int maxAge, GetItemCallback itemCallback) {
        progressDialog.show();
        new CustomSearchAsyncTask(sex, minAge, maxAge, itemCallback).execute();
    }

    public void checkUserNoticeNumberInBG(String username, GetBooleanCallBack booleanCallback) {
        new CheckUserNoticeNumberAsyncTask(username, booleanCallback).execute();
    }

    public void storeGCMTokenInBG(String token, String username, GetBooleanCallBack booleanCallback) {
        new StoreGCMTokenAsyncTask(token, username, booleanCallback).execute();
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public class StoreUserDataAsyncTask extends AsyncTask<Void, Void, User> {

        User user;
        GetUserCallBack userCallBack;
        HttpRequest httpRequest;
        String resultStr;

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
            dataToSend.put("imageString", user.imagePath);

            User returnUser = null;

            try {

                if (!isNetworkAvailable()) {
                    resultStr = context.getResources().getString(R.string.errorNotConnectInternet);
                    return null;
                }

                int resultConnection = httpRequest.makeHttpRequest(dataToSend, ADDRESS + "/StoreUserData.php");
                if (resultConnection != 200) {
                    resultStr = context.getResources().getString(R.string.errorCannotConnectToServer);
                    return null;
                }

                String line = httpRequest.getReturnString();
                Log.i("custom_check", line);

                JSONObject jObj = new JSONObject(line);

                if (jObj.length() != 0) {
                    int resultDBConnection = jObj.getInt("resultDBConnection");
                    int resultStoreUserData = jObj.getInt("resultStoreUserData");
                    int resultStoreUserImage = jObj.getInt("resultStoreUserImage");

                    if (resultDBConnection == 1 && resultStoreUserData == 1 && resultStoreUserImage == 1) {
                        returnUser = user;
                    } else if (resultDBConnection == 0)
                        resultStr = context.getResources().getString(R.string.errorCannotConnectToServer);
                        // wait for check username flag
                    else if (resultStoreUserData == 0)
                        resultStr = context.getResources().getString(R.string.errorUsernameExisted);
                }
            } catch (Exception e) {
                Log.e("custom_check", e.toString());
            }


            return returnUser;
        }

        @Override
        protected void onPostExecute(User returnUser) {
            progressDialog.dismiss();
            userCallBack.done(returnUser, resultStr);
            super.onPostExecute(returnUser);
        }
    }

    public class FetchUserDataAsyncTask extends AsyncTask<Void, Void, User> {
        User user;
        GetUserCallBack userCallBack;
        HttpRequest httpRequest;
        String resultStr;

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

                if (!isNetworkAvailable()) {
                    resultStr = context.getResources().getString(R.string.errorNotConnectInternet);
                    return null;
                }

                int resultConnection = httpRequest.makeHttpRequest(dataToSend, ADDRESS + "/FetchUserData.php");
                if (resultConnection != 200) {
                    resultStr = context.getResources().getString(R.string.errorCannotConnectToServer);
                    return null;
                }

                String line = httpRequest.getReturnString();
                Log.i("custom_check", line);

                JSONObject jObj = new JSONObject(line);

                if (jObj.length() != 0) {
                    int resultDBConnection = jObj.getInt("resultDBConnection");
                    int resultFetchUserData = jObj.getInt("resultFetchUserData");
                    int resultFetchUserImage = jObj.getInt("resultFetchUserImage");
                    int resultUserData = jObj.getInt("resultUserData");
                    if (resultDBConnection == 1 && resultFetchUserData == 1 && resultFetchUserImage == 1 && resultUserData == 1) {
                        String name = jObj.getString("name");
                        String nationId = jObj.getString("nationId");
                        String email = jObj.getString("email");
                        String telephone = jObj.getString("telephone");
                        String imagePath = ADDRESS + jObj.getString("imagePath");
                        boolean isAdmin = jObj.getBoolean("isAdmin");

                        returnUser = new User(user.username, user.password, name, nationId, email, telephone, imagePath);
                        if (isAdmin) returnUser.setAsAdmin();
                    } else if (resultDBConnection == 0)
                        resultStr = context.getResources().getString(R.string.errorCannotConnectToServer);
                    else if (resultUserData == 0)
                        resultStr = context.getResources().getString(R.string.errorUserOrPassIncorrect);
                    else
                        resultStr = context.getResources().getString(R.string.errorSystemWorkingIncorrectly);
                }

            } catch (Exception e) {
                Log.i("custom_check", e.toString());
            }

            return returnUser;
        }

        @Override
        protected void onPostExecute(User returnData) {
            progressDialog.dismiss();
            userCallBack.done(returnData, resultStr);
            super.onPostExecute(returnData);

        }
    }

    public class UpdateUserDataAsyncTask extends AsyncTask<Void, Void, User> {
        User user;
        GetUserCallBack userCallBack;
        HttpRequest httpRequest;
        String resultStr;

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
            dataToSend.put("imageString", user.imagePath);

            User returnUser = null;

            try {

                if (!isNetworkAvailable()) {
                    resultStr = context.getResources().getString(R.string.errorNotConnectInternet);
                    return null;
                }

                int resultConnection = httpRequest.makeHttpRequest(dataToSend, ADDRESS + "/UpdateUserData.php");
                if (resultConnection != 200) {
                    resultStr = context.getResources().getString(R.string.errorCannotConnectToServer);
                    return null;
                }

                String line = httpRequest.getReturnString();
                Log.i("custom_check", line);

                JSONObject jObj = new JSONObject(line);

                if (jObj.length() != 0) {
                    int resultDBConnection = jObj.getInt("resultDBConnection");
                    int resultUpdateUserData = jObj.getInt("resultUpdateUserData");
                    int resultUpdateUserImage = jObj.getInt("resultUpdateUserImage");
                    int resultFetchUserData = jObj.getInt("resultFetchUserData");

                    if (resultDBConnection == 1 && resultUpdateUserData == 1 && resultUpdateUserImage == 1 && resultFetchUserData == 1) {
                        String imagePath = ADDRESS + jObj.getString("imagePath");

                        returnUser = new User(user.username, user.password, user.name, user.nationId, user.email, user.telephone, imagePath);
                        if (user.isAdmin()) returnUser.setAsAdmin();
                    } else if (resultDBConnection == 0)
                        resultStr = context.getResources().getString(R.string.errorCannotConnectToServer);
                    else
                        resultStr = context.getResources().getString(R.string.errorSystemWorkingIncorrectly);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return returnUser;
        }

        @Override
        protected void onPostExecute(User returnData) {
            progressDialog.dismiss();
            userCallBack.done(returnData, resultStr);
            super.onPostExecute(returnData);

        }
    }

    public class UpdateUserPasswordAsyncTask extends AsyncTask<Void, Void, User> {
        User user;
        GetUserCallBack userCallBack;
        String newPassword;
        HttpRequest httpRequest;
        String resultStr;

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

                if (!isNetworkAvailable()) {
                    resultStr = context.getResources().getString(R.string.errorNotConnectInternet);
                    return null;
                }

                int resultConnection = httpRequest.makeHttpRequest(dataToSend, ADDRESS + "/UpdateUserPassword.php");
                if (resultConnection != 200) {
                    resultStr = context.getResources().getString(R.string.errorCannotConnectToServer);
                    return null;
                }

                String line = httpRequest.getReturnString();
                Log.i("custom_check", line);

                JSONObject jObj = new JSONObject(line);

                if (jObj.length() != 0) {
                    int resultDBConnection = jObj.getInt("resultDBConnection");
                    int resultUpdateUserPassword = jObj.getInt("resultUpdateUserPassword");

                    if (resultDBConnection == 0)
                        resultStr = context.getResources().getString(R.string.errorCannotConnectToServer);
                    else if (resultUpdateUserPassword == 0)
                        resultStr = context.getResources().getString(R.string.errorSystemWorkingIncorrectly);
                    else if (resultDBConnection == 1 && resultUpdateUserPassword == 1) {
                        returnUser = new User(user.username, newPassword, user.name, user.nationId, user.email, user.telephone, user.imagePath);
                        if (user.isAdmin()) returnUser.setAsAdmin();
                    }
                }

            } catch (Exception e) {
                Log.i("custom_check", e.toString());
            }

            return returnUser;
        }

        @Override
        protected void onPostExecute(User returnData) {
            progressDialog.dismiss();
            userCallBack.done(returnData, resultStr);
            super.onPostExecute(returnData);

        }
    }

    public class StoreNoticeDataAsyncTask extends AsyncTask<Void, Void, Notice> {

        Notice notice;
        GetNoticeCallBack noticeCallBack;
        HttpRequest httpRequest;
        String resultStr;

        public StoreNoticeDataAsyncTask(Notice notice, GetNoticeCallBack noticeCallBack) {
            this.notice = notice;
            this.noticeCallBack = noticeCallBack;
            httpRequest = new HttpRequest();
        }


        @Override
        protected Notice doInBackground(Void... params) {

            Map<String, String> dataToSend = new HashMap<>();
            dataToSend.put("name", notice.name);
            dataToSend.put("sex", notice.sex);
            dataToSend.put("birthDate", notice.birthDate);
            dataToSend.put("lostPlace", notice.lostPlace);
            dataToSend.put("lostDate", notice.lostDate);
            dataToSend.put("detail", notice.detail);
            dataToSend.put("username", notice.adderUsername);
            dataToSend.put("imageString", notice.imagePath);

            Notice returnNotice = null;

            try {

                if (!isNetworkAvailable()) {
                    resultStr = context.getResources().getString(R.string.errorNotConnectInternet);
                    return null;
                }

                int resultConnection = httpRequest.makeHttpRequest(dataToSend, ADDRESS + "/StoreNoticeData.php");
                if (resultConnection != 200) {
                    resultStr = context.getResources().getString(R.string.errorCannotConnectToServer);
                    return null;
                }

                String line = httpRequest.getReturnString();
                Log.i("custom_check", line);

                JSONObject jObj = new JSONObject(line);

                if (jObj.length() != 0) {
                    int resultDBConnection = jObj.getInt("resultDBConnection");
                    int resultStoreNoticeData = jObj.getInt("resultStoreNoticeData");
                    int resultStoreNoticeImage = jObj.getInt("resultStoreNoticeImage");

                    if (resultDBConnection == 1 && resultStoreNoticeData == 1 && resultStoreNoticeImage == 1) {
                        int id = jObj.getInt("id");
                        returnNotice = new Notice(id);
                    } else if (resultDBConnection == 0)
                        resultStr = context.getResources().getString(R.string.errorCannotConnectToServer);
                    else
                        resultStr = context.getResources().getString(R.string.errorSystemWorkingIncorrectly);
                }
            } catch (Exception e) {
                Log.e("custom_check", e.toString());
            }
            return returnNotice;
        }

        @Override
        protected void onPostExecute(Notice returnNotice) {
            progressDialog.dismiss();
            noticeCallBack.done(returnNotice, resultStr);
            super.onPostExecute(returnNotice);
        }
    }

    public class FetchNoticeDataAsyncTask extends AsyncTask<Void, Void, Notice> {

        GetNoticeCallBack noticeCallBack;
        HttpRequest httpRequest;
        int noticeId;
        String resultStr;

        public FetchNoticeDataAsyncTask(int noticeId, GetNoticeCallBack noticeCallBack) {
            this.noticeId = noticeId;
            this.noticeCallBack = noticeCallBack;
            httpRequest = new HttpRequest();
        }

        @Override
        public Notice doInBackground(Void... params) {
            Map<String, String> dataToSend = new HashMap<>();
            dataToSend.put("id", noticeId + "");

            Notice returnNotice = null;

            try {

                if (!isNetworkAvailable()) {
                    resultStr = context.getResources().getString(R.string.errorNotConnectInternet);
                    return null;
                }

                int resultConnection = httpRequest.makeHttpRequest(dataToSend, ADDRESS + "/FetchNoticeData.php");
                if (resultConnection != 200) {
                    resultStr = context.getResources().getString(R.string.errorCannotConnectToServer);
                    return null;
                }

                String line = httpRequest.getReturnString();
                Log.i("custom_check", line);

                JSONObject jObj = new JSONObject(line);

                if (jObj.length() != 0) {
                    int resultDBConnection = jObj.getInt("resultDBConnection");
                    int resultFetchNoticeData = jObj.getInt("resultFetchNoticeData");
                    int resultFetchNoticeImage = jObj.getInt("resultFetchNoticeImage");

                    if (resultDBConnection == 1 && resultFetchNoticeData == 1 && resultFetchNoticeImage == 1) {
                        String name = jObj.getString("name");
                        String sex = jObj.getString("sex");
                        String birthDate = jObj.getString("birthDate");
                        String lostPlace = jObj.getString("lostPlace");
                        String lostDate = jObj.getString("lostDate");
                        String detail = jObj.getString("detail");
                        String adderUsername = jObj.getString("adderUsername");
                        String adderName = jObj.getString("adderName");
                        String telephone = jObj.getString("telephone");
                        String imagePath = ADDRESS + jObj.getString("imagePath");
                        int id = jObj.getInt("id");

                        returnNotice = new Notice(id, name, sex, birthDate, lostPlace, lostDate, detail, adderUsername, adderName, telephone, imagePath);
                    } else if (resultDBConnection == 0)
                        resultStr = context.getResources().getString(R.string.errorCannotConnectToServer);
                    else
                        resultStr = context.getResources().getString(R.string.errorSystemWorkingIncorrectly);
                }
            } catch (Exception e) {
                Log.i("custom_check", e.toString());
            }
            return returnNotice;
        }

        @Override
        protected void onPostExecute(Notice returnData) {
            progressDialog.dismiss();
            noticeCallBack.done(returnData, resultStr);
            super.onPostExecute(returnData);

        }
    }

    public class UpdateNoticeDataAsyncTask extends AsyncTask<Void, Void, Notice> {
        Notice notice;
        GetNoticeCallBack noticeCallBack;
        HttpRequest httpRequest;
        String resultStr;

        public UpdateNoticeDataAsyncTask(Notice notice, GetNoticeCallBack noticeCallBack) {
            this.notice = notice;
            this.noticeCallBack = noticeCallBack;
            httpRequest = new HttpRequest();
        }

        @Override
        public Notice doInBackground(Void... params) {

            Map<String, String> dataToSend = new HashMap<>();
            dataToSend.put("id", notice.id + "");
            dataToSend.put("name", notice.name);
            dataToSend.put("sex", notice.sex);
            dataToSend.put("birthDate", notice.birthDate);
            dataToSend.put("lostPlace", notice.lostPlace);
            dataToSend.put("lostDate", notice.lostDate);
            dataToSend.put("detail", notice.detail);
            dataToSend.put("adderUsername", notice.adderUsername);
            dataToSend.put("imageString", notice.imagePath);

            Notice returnNotice = null;

            try {

                if (!isNetworkAvailable()) {
                    resultStr = context.getResources().getString(R.string.errorNotConnectInternet);
                    return null;
                }

                int resultConnection = httpRequest.makeHttpRequest(dataToSend, ADDRESS + "/UpdateNoticeData.php");
                if (resultConnection != 200) {
                    resultStr = context.getResources().getString(R.string.errorCannotConnectToServer);
                    return null;
                }

                String line = httpRequest.getReturnString();
                Log.i("custom_check", line);

                JSONObject jObj = new JSONObject(line);

                if (jObj.length() != 0) {
                    int resultDBConnection = jObj.getInt("resultDBConnection");
                    int resultFetchNoticeData = jObj.getInt("resultUpdateNoticeData");
                    int resultFetchNoticeImage = jObj.getInt("resultUpdateNoticeImage");

                    if (resultDBConnection == 1 && resultFetchNoticeData == 1 && resultFetchNoticeImage == 1) {
                        String imagePath = ADDRESS + jObj.getString("imagePath");
                        returnNotice = new Notice(notice.id, notice.name, notice.sex, notice.birthDate, notice.lostPlace, notice.lostDate, notice.detail, notice.adderUsername, notice.adderName, notice.telephone, imagePath);
                    } else if (resultDBConnection == 0)
                        resultStr = context.getResources().getString(R.string.errorCannotConnectToServer);
                    else
                        resultStr = context.getResources().getString(R.string.errorSystemWorkingIncorrectly);
                }
            } catch (Exception e) {
                Log.i("custom_check", e.toString());
            }
            return returnNotice;
        }

        @Override
        protected void onPostExecute(Notice returnData) {
            progressDialog.dismiss();
            noticeCallBack.done(returnData, resultStr);
            super.onPostExecute(returnData);

        }
    }

    public class StoreClueDataAsyncTask extends AsyncTask<Void, Void, Clue> {

        Clue clueData;
        GetClueCallback seenInfoCallback;
        HttpRequest httpRequest;
        String resultStr;

        public StoreClueDataAsyncTask(Clue clueData, GetClueCallback seenInfoCallback) {
            this.clueData = clueData;
            this.seenInfoCallback = seenInfoCallback;
            httpRequest = new HttpRequest();
        }

        @Override
        protected Clue doInBackground(Void... params) {

            Map<String, String> dataToSend = new HashMap<>();
            dataToSend.put("sex", clueData.sex);
            dataToSend.put("seenDate", clueData.seenDate);
            dataToSend.put("seenPlace", clueData.seenPlace);
            dataToSend.put("detail", clueData.detail);
            dataToSend.put("adderUsername", clueData.adderUsername);
            dataToSend.put("imageString", clueData.imagePath);

            Clue returnClueData = null;

            try {

                if (!isNetworkAvailable()) {
                    resultStr = context.getResources().getString(R.string.errorNotConnectInternet);
                    return null;
                }

                int resultConnection = httpRequest.makeHttpRequest(dataToSend, ADDRESS + "/StoreClueData.php");
                if (resultConnection != 200) {
                    resultStr = context.getResources().getString(R.string.errorCannotConnectToServer);
                    return null;
                }

                String line = httpRequest.getReturnString();
                Log.i("custom_check", line);

                JSONObject jObj = new JSONObject(line);

                if (jObj.length() != 0) {
                    int resultDBConnection = jObj.getInt("resultDBConnection");
                    int resultStoreClueData = jObj.getInt("resultStoreClueData");
                    int resultStoreClueImage = jObj.getInt("resultStoreClueImage");

                    if (resultDBConnection == 1 && resultStoreClueData == 1 && resultStoreClueImage == 1) {
                        int id = jObj.getInt("id");
                        returnClueData = new Clue(id);
                    } else if (resultDBConnection == 0)
                        resultStr = context.getResources().getString(R.string.errorCannotConnectToServer);
                    else
                        resultStr = context.getResources().getString(R.string.errorSystemWorkingIncorrectly);
                }
            } catch (Exception e) {
                Log.e("custom_check", e.toString());
            }
            return returnClueData;
        }

        @Override
        protected void onPostExecute(Clue returnInfo) {
            progressDialog.dismiss();
            seenInfoCallback.done(returnInfo, resultStr);
            super.onPostExecute(returnInfo);
        }
    }

    public class FetchClueDataAsyncTask extends AsyncTask<Void, Void, Clue> {
        GetClueCallback seenInfoCallback;
        HttpRequest httpRequest;
        int clueId;
        String resultStr;

        public FetchClueDataAsyncTask(int clueId, GetClueCallback seenInfoCallback) {
            this.clueId = clueId;
            this.seenInfoCallback = seenInfoCallback;
            httpRequest = new HttpRequest();
        }

        @Override
        public Clue doInBackground(Void... params) {
            Map<String, String> dataToSend = new HashMap<>();
            dataToSend.put("id", clueId + "");

            Clue returnClueData = null;

            try {

                if (!isNetworkAvailable()) {
                    resultStr = context.getResources().getString(R.string.errorNotConnectInternet);
                    return null;
                }

                int resultConnection = httpRequest.makeHttpRequest(dataToSend, ADDRESS + "/FetchClueData.php");
                if (resultConnection != 200) {
                    resultStr = context.getResources().getString(R.string.errorCannotConnectToServer);
                    return null;
                }

                String line = httpRequest.getReturnString();
                Log.i("custom_check", line);

                JSONObject jObj = new JSONObject(line);

                if (jObj.length() != 0) {
                    int resultDBConnection = jObj.getInt("resultDBConnection");
                    int resultStoreClueData = jObj.getInt("resultFetchClueData");
                    int resultStoreClueImage = jObj.getInt("resultFetchClueImage");

                    if (resultDBConnection == 1 && resultStoreClueData == 1 && resultStoreClueImage == 1) {
                        String sex = jObj.getString("sex");
                        String seenDate = jObj.getString("seenDate");
                        String seenPlace = jObj.getString("seenPlace");
                        String detail = jObj.getString("detail");
                        String adderUsername = jObj.getString("adderUsername");
                        String adderName = jObj.getString("adderName");
                        String telephone = jObj.getString("telephone");
                        String imagePath = ADDRESS + jObj.getString("imagePath");
                        int id = jObj.getInt("id");
                        returnClueData = new Clue(id, sex, seenDate, seenPlace, detail, adderUsername, adderName, telephone, imagePath);
                    } else if (resultDBConnection == 0)
                        resultStr = context.getResources().getString(R.string.errorCannotConnectToServer);
                    else
                        resultStr = context.getResources().getString(R.string.errorSystemWorkingIncorrectly);
                }

            } catch (Exception e) {
                Log.i("custom_check", e.toString());
            }

            return returnClueData;
        }

        @Override
        protected void onPostExecute(Clue seenInfo) {
            progressDialog.dismiss();
            seenInfoCallback.done(seenInfo, resultStr);
            super.onPostExecute(seenInfo);
        }
    }

    public class FetchNoticeItemGridAsyncTask extends AsyncTask<Void, Void, ArrayList<GridItem>> {
        GetItemCallback itemCallback;
        HttpRequest httpRequest;
        ArrayList<GridItem> noticeItems;
        int offset;
        String resultStr;

        public FetchNoticeItemGridAsyncTask(int offset, GetItemCallback itemCallback) {
            this.itemCallback = itemCallback;
            httpRequest = new HttpRequest();
            this.offset = offset;
            noticeItems = new ArrayList<>();
        }

        @Override
        public ArrayList<GridItem> doInBackground(Void... params) {
            Map<String, String> dataToSend = new HashMap<>();
            dataToSend.put("offset", offset + "");

            try {

                if (!isNetworkAvailable()) {
                    resultStr = context.getResources().getString(R.string.errorNotConnectInternet);
                    return noticeItems;
                }

                int resultConnection = httpRequest.makeHttpRequest(dataToSend, ADDRESS + "/FetchNoticeItemGrid.php");
                if (resultConnection != 200) {
                    resultStr = context.getResources().getString(R.string.errorCannotConnectToServer);
                    return noticeItems;
                }

                String line = httpRequest.getReturnString();
                Log.i("custom_check", line);

                JSONObject jObj = new JSONObject(line);

                if (jObj.length() != 0) {
                    int resultDBConnection = jObj.getInt("resultDBConnection");
                    int resultFetchNoticeGridData = jObj.getInt("resultFetchNoticeGridData");

                    if (resultDBConnection == 1 && resultFetchNoticeGridData == 1) {
                        JSONArray noticeArray = jObj.getJSONArray("gridItem");
                        GridItem item;
                        if (noticeArray.length() != 0) {
                            for (int i = 0; i < noticeArray.length(); i++) {
                                JSONObject items = noticeArray.getJSONObject(i);
                                int id = items.getInt("id");
                                String name = items.getString("name");
                                String birthDate = items.getString("birthDate");
                                String imagePath = ADDRESS + items.getString("imagePath");
                                item = new GridItem(id, name, birthDate, imagePath);
                                noticeItems.add(item);
                            }
                        } else {
                            resultStr = context.getResources().getString(R.string.errorNoNoticeExist);
                        }
                    } else if (resultDBConnection == 0)
                        resultStr = context.getResources().getString(R.string.errorCannotConnectToServer);
                    else
                        resultStr = context.getResources().getString(R.string.errorSystemWorkingIncorrectly);
                }
            } catch (Exception e) {
                Log.i("custom_check", e.toString());
            }

            return noticeItems;
        }

        @Override
        protected void onPostExecute(ArrayList<GridItem> noticeItems) {
            itemCallback.done(noticeItems, resultStr);
            super.onPostExecute(noticeItems);

        }
    }

    public class FetchUserNoticeListAsyncTask extends AsyncTask<Void, Void, ArrayList<GridItem>> {
        GetItemCallback itemCallback;
        HttpRequest httpRequest;
        ArrayList<GridItem> noticeItems;
        User user;
        String resultStr;

        public FetchUserNoticeListAsyncTask(User user, GetItemCallback itemCallback) {
            this.itemCallback = itemCallback;
            httpRequest = new HttpRequest();
            noticeItems = new ArrayList<>();
            this.user = user;
        }

        @Override
        public ArrayList<GridItem> doInBackground(Void... params) {
            Map<String, String> dataToSend = new HashMap<>();
            dataToSend.put("username", user.username);

            try {

                if (!isNetworkAvailable()) {
                    resultStr = context.getResources().getString(R.string.errorNotConnectInternet);
                    return null;
                }

                int resultConnection = httpRequest.makeHttpRequest(dataToSend, ADDRESS + "/FetchUserNoticeList.php");
                if (resultConnection != 200) {
                    resultStr = context.getResources().getString(R.string.errorCannotConnectToServer);
                    return null;
                }

                String line = httpRequest.getReturnString();
                Log.i("custom_check", line);

                JSONObject jObj = new JSONObject(line);
                if (jObj.length() != 0) {
                    int resultDBConnection = jObj.getInt("resultDBConnection");
                    int resultFetchUserNoticeListData = jObj.getInt("resultFetchUserNoticeListData");

                    if (resultDBConnection == 1 && resultFetchUserNoticeListData == 1) {
                        JSONArray noticeArray = jObj.getJSONArray("gridItem");
                        GridItem item;
                        if (noticeArray.length() != 0) {
                            for (int i = 0; i < noticeArray.length(); i++) {
                                JSONObject items = noticeArray.getJSONObject(i);
                                int id = items.getInt("id");
                                String lnName = items.getString("name");
                                String lnBirthDate = items.getString("lostDate");
                                String imagePath = ADDRESS + items.getString("imagePath");
                                item = new GridItem(id, lnName, lnBirthDate, imagePath);
                                noticeItems.add(item);
                            }
                        } else {
                            resultStr = context.getResources().getString(R.string.errorNoNoticeExist);
                        }
                    } else if (resultDBConnection == 0)
                        resultStr = context.getResources().getString(R.string.errorCannotConnectToServer);
                    else if (resultFetchUserNoticeListData == 0)
                        resultStr = context.getResources().getString(R.string.errorSystemWorkingIncorrectly);
                }
            } catch (Exception e) {
                Log.i("custom_check", e.toString());
            }

            return noticeItems;
        }

        @Override
        protected void onPostExecute(ArrayList<GridItem> noticeItems) {
            itemCallback.done(noticeItems, resultStr);
            super.onPostExecute(noticeItems);

        }
    }

    public class FetchUserClueListAsyncTask extends AsyncTask<Void, Void, ArrayList<GridItem>> {
        GetItemCallback itemCallback;
        HttpRequest httpRequest;
        ArrayList<GridItem> noticeItems;
        User user;
        String resultStr;

        public FetchUserClueListAsyncTask(User user, GetItemCallback itemCallback) {
            this.itemCallback = itemCallback;
            httpRequest = new HttpRequest();
            noticeItems = new ArrayList<>();
            this.user = user;
        }

        @Override
        public ArrayList<GridItem> doInBackground(Void... params) {
            Map<String, String> dataToSend = new HashMap<>();
            dataToSend.put("username", user.username);

            try {

                if (!isNetworkAvailable()) {
                    resultStr = context.getResources().getString(R.string.errorNotConnectInternet);
                    return null;
                }

                int resultConnection = httpRequest.makeHttpRequest(dataToSend, ADDRESS + "/FetchUserClueList.php");
                if (resultConnection != 200) {
                    resultStr = context.getResources().getString(R.string.errorCannotConnectToServer);
                    return null;
                }

                String line = httpRequest.getReturnString();
                Log.i("custom_check", line);

                JSONObject jObj = new JSONObject(line);
                if (jObj.length() != 0) {
                    int resultDBConnection = jObj.getInt("resultDBConnection");
                    int resultFetchUserClueListData = jObj.getInt("resultFetchUserClueListData");
                    if (resultDBConnection == 1 && resultFetchUserClueListData == 1) {
                        JSONArray noticeArray = jObj.getJSONArray("gridItem");
                        GridItem item;
                        if (noticeArray.length() != 0) {
                            for (int i = 0; i < noticeArray.length(); i++) {
                                JSONObject items = noticeArray.getJSONObject(i);
                                int id = items.getInt("id");
                                String place = items.getString("seenPlace");
                                String date = items.getString("seenDate");
                                String imagePath = ADDRESS + items.getString("imagePath");
                                item = new GridItem(id, place, date, imagePath);
                                noticeItems.add(item);
                            }
                        } else {
                            resultStr = context.getResources().getString(R.string.errorNoClueExist);
                        }
                    } else if (resultDBConnection == 0)
                        resultStr = context.getResources().getString(R.string.errorCannotConnectToServer);
                    else
                        resultStr = context.getResources().getString(R.string.errorSystemWorkingIncorrectly);
                }
            } catch (Exception e) {
                Log.i("custom_check", e.toString());
            }

            return noticeItems;
        }

        @Override
        protected void onPostExecute(ArrayList<GridItem> noticeItems) {
            itemCallback.done(noticeItems, resultStr);
            super.onPostExecute(noticeItems);
        }
    }

    public class CheckUsernameExistInBG extends AsyncTask<Void, Void, Boolean> {
        GetBooleanCallBack booleanCallBack;
        HttpRequest httpRequest;
        String username;
        String resultStr;

        public CheckUsernameExistInBG(String username, GetBooleanCallBack booleanCallBack) {
            this.booleanCallBack = booleanCallBack;
            httpRequest = new HttpRequest();
            this.username = username;
        }

        @Override
        public Boolean doInBackground(Void... params) {
            Map<String, String> dataToSend = new HashMap<>();
            dataToSend.put("username", this.username);

            Boolean isUsernameAvailable = null;

            try {

                if (!isNetworkAvailable()) {
                    resultStr = context.getResources().getString(R.string.errorNotConnectInternet);
                    return null;
                }

                int resultConnection = httpRequest.makeHttpRequest(dataToSend, ADDRESS + "/CheckUsernameExist.php");
                if (resultConnection != 200) {
                    resultStr = context.getResources().getString(R.string.errorCannotConnectToServer);
                    return null;
                }

                String line = httpRequest.getReturnString();
                Log.i("custom_check", line);

                JSONObject jObj = new JSONObject(line);
                if (jObj.length() != 0) {
                    int resultDBConnection = jObj.getInt("resultDBConnection");
                    int resultUsernameCheck = jObj.getInt("resultUsernameCheck");

                    if (resultDBConnection == 1 && resultUsernameCheck == 1) {
                        isUsernameAvailable = jObj.getBoolean("isUsernameAvailable");
                    } else if (resultDBConnection == 0)
                        resultStr = context.getResources().getString(R.string.errorCannotConnectToServer);
                    else
                        resultStr = context.getResources().getString(R.string.errorSystemWorkingIncorrectly);
                }
            } catch (Exception e) {
                Log.i("custom_check", e.toString());
            }

            return isUsernameAvailable;
        }

        @Override
        protected void onPostExecute(Boolean isUsernameAvailable) {
            booleanCallBack.done(isUsernameAvailable, resultStr);
            super.onPostExecute(isUsernameAvailable);
        }
    }

    public class UpdateNoticeStatusAsyncTask extends AsyncTask<Void, Void, Boolean> {
        GetBooleanCallBack booleanCallBack;
        HttpRequest httpRequest;
        int id;
        String resultStr, username;

        public UpdateNoticeStatusAsyncTask(int id, String username, GetBooleanCallBack booleanCallBack) {
            this.booleanCallBack = booleanCallBack;
            httpRequest = new HttpRequest();
            this.id = id;
            this.username = username;
        }

        @Override
        public Boolean doInBackground(Void... params) {
            Map<String, String> dataToSend = new HashMap<>();
            dataToSend.put("username", username);
            dataToSend.put("id", this.id + "");

            Boolean isUpdateSuccess = null;

            try {

                if (!isNetworkAvailable()) {
                    resultStr = context.getResources().getString(R.string.errorNotConnectInternet);
                    return null;
                }

                int resultConnection = httpRequest.makeHttpRequest(dataToSend, ADDRESS + "/UpdateNoticeStatus.php");
                if (resultConnection != 200) {
                    resultStr = context.getResources().getString(R.string.errorCannotConnectToServer);
                    return null;
                }

                String line = httpRequest.getReturnString();
                Log.i("custom_check", line);

                JSONObject jObj = new JSONObject(line);
                if (jObj.length() != 0) {
                    int resultDBConnection = jObj.getInt("resultDBConnection");
                    int resultUpdateStatus = jObj.getInt("resultUpdateStatus");
                    int resultNoticeStatus = jObj.getInt("resultNoticeStatus");

                    if (resultDBConnection == 1 && resultUpdateStatus == 1 && resultNoticeStatus == 1) {
                        isUpdateSuccess = true;
                    } else if (resultDBConnection == 0)
                        resultStr = context.getResources().getString(R.string.errorCannotConnectToServer);
                    else
                        resultStr = context.getResources().getString(R.string.errorSystemWorkingIncorrectly);
                }
            } catch (Exception e) {
                Log.i("custom_check", e.toString());
            }

            return isUpdateSuccess;
        }

        @Override
        protected void onPostExecute(Boolean isUpdateSuccess) {
            progressDialog.dismiss();
            booleanCallBack.done(isUpdateSuccess, resultStr);
            super.onPostExecute(isUpdateSuccess);
        }
    }

    public class CustomSearchAsyncTask extends AsyncTask<Void, Void, ArrayList<GridItem>> {
        GetItemCallback itemCallback;
        HttpRequest httpRequest;
        int minAge, maxAge;
        String resultStr, sex;
        ArrayList<GridItem> noticeItems;

        public CustomSearchAsyncTask(String sex, int minAge, int maxAge, GetItemCallback itemCallback) {
            this.itemCallback = itemCallback;
            noticeItems = new ArrayList<>();
            httpRequest = new HttpRequest();
            this.minAge = minAge;
            this.maxAge = maxAge;
            this.sex = sex;
        }

        @Override
        public ArrayList<GridItem> doInBackground(Void... params) {
            Map<String, String> dataToSend = new HashMap<>();
            dataToSend.put("sex", sex);
            dataToSend.put("minAge", minAge + "");
            dataToSend.put("maxAge", maxAge + "");

            if (sex.equals(context.getResources().getString(R.string.sexBoth))) {
                dataToSend.put("mode", "2");
            } else if (minAge == 0 && maxAge == 0) {
                dataToSend.put("mode", "1");
            } else {
                dataToSend.put("mode", "3");
            }

            try {

                if (!isNetworkAvailable()) {
                    resultStr = context.getResources().getString(R.string.errorNotConnectInternet);
                    return noticeItems;
                }

                int resultConnection = httpRequest.makeHttpRequest(dataToSend, ADDRESS + "/NoticeCustomSearch.php");
                if (resultConnection != 200) {
                    resultStr = context.getResources().getString(R.string.errorCannotConnectToServer);
                    return noticeItems;
                }

                String line = httpRequest.getReturnString();
                Log.i("custom_check", line);

                JSONObject jObj = new JSONObject(line);

                if (jObj.length() != 0) {
                    int resultDBConnection = jObj.getInt("resultDBConnection");
                    int resultFetchNoticeData = jObj.getInt("resultFetchNoticeData");

                    if (resultDBConnection == 1 && resultFetchNoticeData == 1) {
                        JSONArray noticeArray = jObj.getJSONArray("gridItem");
                        GridItem item;
                        if (noticeArray.length() != 0) {
                            for (int i = 0; i < noticeArray.length(); i++) {
                                JSONObject items = noticeArray.getJSONObject(i);
                                int id = items.getInt("id");
                                String name = items.getString("name");
                                String birthDate = items.getString("birthDate");
                                String imagePath = ADDRESS + items.getString("imagePath");
                                item = new GridItem(id, name, birthDate, imagePath);
                                noticeItems.add(item);
                            }
                        } else {
                            resultStr = context.getResources().getString(R.string.noNoticeSameWithSearch);
                        }
                    } else if (resultDBConnection == 0)
                        resultStr = context.getResources().getString(R.string.errorCannotConnectToServer);
                    else
                        resultStr = context.getResources().getString(R.string.errorSystemWorkingIncorrectly);
                }
            } catch (Exception e) {
                Log.i("custom_check", e.toString());
            }

            return noticeItems;
        }

        @Override
        protected void onPostExecute(ArrayList<GridItem> returnArray) {
            progressDialog.dismiss();
            itemCallback.done(returnArray, resultStr);
            super.onPostExecute(returnArray);
        }
    }

    public class CheckUserNoticeNumberAsyncTask extends AsyncTask<Void, Void, Boolean> {
        GetBooleanCallBack booleanCallBack;
        HttpRequest httpRequest;
        String username;
        String resultStr;

        public CheckUserNoticeNumberAsyncTask(String username, GetBooleanCallBack booleanCallBack) {
            this.booleanCallBack = booleanCallBack;
            httpRequest = new HttpRequest();
            this.username = username;
        }

        @Override
        public Boolean doInBackground(Void... params) {
            Map<String, String> dataToSend = new HashMap<>();
            dataToSend.put("username", this.username);

            Boolean isAbleToCreateNotice = null;

            try {

                if (!isNetworkAvailable()) {
                    resultStr = context.getResources().getString(R.string.errorNotConnectInternet);
                    return null;
                }

                int resultConnection = httpRequest.makeHttpRequest(dataToSend, ADDRESS + "/CheckUserNoticeNumber.php");
                if (resultConnection != 200) {
                    resultStr = context.getResources().getString(R.string.errorCannotConnectToServer);
                    return null;
                }

                String line = httpRequest.getReturnString();
                Log.i("custom_check", line);

                JSONObject jObj = new JSONObject(line);
                if (jObj.length() != 0) {
                    int resultDBConnection = jObj.getInt("resultDBConnection");
                    int resultUserNoticeCheck = jObj.getInt("resultUserNoticeCheck");

                    if (resultDBConnection == 1 && resultUserNoticeCheck == 1) {
                        isAbleToCreateNotice = jObj.getBoolean("isAbleToCreateNotice");
                        if (!isAbleToCreateNotice)
                            resultStr = context.getString(R.string.errorNoticeOverThree);
                    } else if (resultDBConnection == 0)
                        resultStr = context.getResources().getString(R.string.errorCannotConnectToServer);
                    else
                        resultStr = context.getResources().getString(R.string.errorSystemWorkingIncorrectly);
                }
            } catch (Exception e) {
                Log.i("custom_check", e.toString());
            }

            return isAbleToCreateNotice;
        }

        @Override
        protected void onPostExecute(Boolean isAbleToCreateNotice) {
            progressDialog.dismiss();
            booleanCallBack.done(isAbleToCreateNotice, resultStr);
            super.onPostExecute(isAbleToCreateNotice);
        }
    }

    public class StoreGCMTokenAsyncTask extends AsyncTask<Void, Void, Boolean> {
        GetBooleanCallBack booleanCallBack;
        HttpRequest httpRequest;
        String token, username;
        String resultStr;

        public StoreGCMTokenAsyncTask(String token, String username, GetBooleanCallBack booleanCallBack) {
            this.booleanCallBack = booleanCallBack;
            httpRequest = new HttpRequest();
            this.token = token;
            this.username = username;
        }

        @Override
        public Boolean doInBackground(Void... params) {
            Map<String, String> dataToSend = new HashMap<>();
            dataToSend.put("token", this.token);
            dataToSend.put("username", this.username);
            String result = "GCM Register fail!1";

            Boolean isConnected = null;

            try {

                if (!isNetworkAvailable()) {
                    resultStr = result;
                    return null;
                }

                int resultConnection = httpRequest.makeHttpRequest(dataToSend, ADDRESS + "/StoreGCMToken.php");
                if (resultConnection != 200) {
                    resultStr = result;
                    return null;
                }

                String line = httpRequest.getReturnString();
                Log.i("custom_check", line);

                JSONObject jObj = new JSONObject(line);
                if (jObj.length() != 0) {
                    int resultDBConnection = jObj.getInt("resultDBConnection");
                    int resultTokenCheck = jObj.getInt("resultTokenCheck");
                    int resultStoreToken = jObj.getInt("resultStoreToken");

                    if (resultDBConnection == 1 && resultTokenCheck == 1 && resultStoreToken == 1) {
                        isConnected = true;
                    } else
                        resultStr = result;
                }
            } catch (Exception e) {
                Log.i("custom_check", e.toString());
            }

            return isConnected;
        }

        @Override
        protected void onPostExecute(Boolean isConnected) {
            progressDialog.dismiss();
            booleanCallBack.done(isConnected, resultStr);
            super.onPostExecute(isConnected);
        }
    }


}
