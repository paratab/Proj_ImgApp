package bolona_pig.proj_imgapp.ObjectClass;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import bolona_pig.proj_imgapp.CallBack.GetItemCallback;
import bolona_pig.proj_imgapp.CallBack.GetNoticeCallBack;
import bolona_pig.proj_imgapp.CallBack.GetSeenInfoCallback;
import bolona_pig.proj_imgapp.CallBack.GetUserCallBack;

/**
 * Created by DreamMii on 5/1/2559.
 */
public class ServerRequest {

    public static final String ADDRESS = "http://www.surawit-sj.xyz";
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

    public void fetchNoticeItemGridInBG(int offset, GetItemCallback itemCallback) {
        progressDialog.show();
        new FetchNoticeItemGridAsyncTask(offset, itemCallback).execute();
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
            dataToSend.put("imageString", user.imagePath);

            User returnUser = null;

            try {

                String line = httpRequest.makeHttpRequest(dataToSend, ADDRESS + "/StoreUserData.php");
                Log.i("custom_check", line);

                JSONObject jObj = new JSONObject(line);

                if (jObj.length() != 0) {
                    int resultDetail = jObj.getInt("resultDetail");
                    int resultImage = jObj.getInt("resultImage");

                    if (resultDetail == 1 && resultImage == 1) returnUser = user;
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

                String line = httpRequest.makeHttpRequest(dataToSend, ADDRESS + "/FetchUserData.php");
                Log.i("custom_check", line);

                JSONObject jObj = new JSONObject(line);

                if (jObj.length() != 0) {
                    String name = jObj.getString("name");
                    String nationId = jObj.getString("nationId");
                    String email = jObj.getString("email");
                    String telephone = jObj.getString("telephone");
                    String imagePath = ADDRESS + jObj.getString("imagePath");

                    returnUser = new User(user.username, user.password, name, nationId, email, telephone, imagePath);
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
            dataToSend.put("imageString", user.imagePath);

            User returnUser = null;

            try {
                String line = httpRequest.makeHttpRequest(dataToSend, ADDRESS + "/UpdateUserData.php");
                Log.i("custom_check", line);

                JSONObject jObj = new JSONObject(line);

                if (jObj.length() != 0) {
                    int result = jObj.getInt("result");
                    String imagePath = ADDRESS + jObj.getString("imagePath");
                    if (result == 1)
                        returnUser = new User(user.username, user.password, user.name, user.nationId, user.email, user.telephone, imagePath);
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
                String line = httpRequest.makeHttpRequest(dataToSend, ADDRESS + "/UpdateUserPassword.php");
                Log.i("custom_check", line);

                JSONObject jObj = new JSONObject(line);

                if (jObj.length() != 0) {
                    int result = jObj.getInt("result");

                    if (result == 1)
                        returnUser = new User(user.username, newPassword, user.name, user.nationId, user.email, user.telephone, user.imagePath);
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
            dataToSend.put("imageString", notice.imagePath);

            Notice returnNotice = null;

            try {

                String line = httpRequest.makeHttpRequest(dataToSend, ADDRESS + "/StoreNoticeData.php");
                Log.i("custom_check", line);

                JSONObject jObj = new JSONObject(line);

                if (jObj.length() != 0) {
                    int lnId = jObj.getInt("lnId");
                    int resultNoticeCheck = jObj.getInt("resultNoticeCheck");
                    int resultNoticeAdd = jObj.getInt("resultNoticeAdd");
                    int resultImage = jObj.getInt("resultImage");

                    if (resultNoticeCheck == 1 && resultNoticeAdd == 1 && resultImage == 1) {
                        returnNotice = new Notice(lnId, notice.lnName, notice.lnBirthDate, notice.lnPlace, notice.lnLostDate, notice.lnDetail, notice.lnAdder, notice.lnPhone, "");
                    } else {

                    }
                }
            } catch (Exception e) {
                Log.e("custom_check", e.toString());
            }
            return returnNotice;
        }

        @Override
        protected void onPostExecute(Notice returnNotice) {
            progressDialog.dismiss();
            noticeCallBack.done(returnNotice);
            super.onPostExecute(returnNotice);
        }
    }

    public class FetchNoticeDataAsyncTask extends AsyncTask<Void, Void, Notice> {

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

            Notice returnNotice = null;

            try {

                String line = httpRequest.makeHttpRequest(dataToSend, ADDRESS + "/FetchNoticeData.php");
                Log.i("custom_check", line);

                JSONObject jObj = new JSONObject(line);

                if (jObj.length() != 0) {
                    String lnName = jObj.getString("lnName");
                    String lnBirthDate = jObj.getString("lnBirthDate");
                    String lnPlace = jObj.getString("lnPlace");
                    String lnLostDate = jObj.getString("lnLostDate");
                    String lnDetail = jObj.getString("lnDetail");
                    String lnAdder = jObj.getString("lnAdder");
                    String lnPhone = jObj.getString("lnPhone");
                    String imagePath = ADDRESS + jObj.getString("imagePath");
                    int lnId = jObj.getInt("lnId");
                    int resultNotice = jObj.getInt("resultNotice");
                    int resultImage = jObj.getInt("resultImage");

                    if (resultImage == 1 && resultNotice == 1)
                        returnNotice = new Notice(lnId, lnName, lnBirthDate, lnPlace, lnLostDate, lnDetail, lnAdder, lnPhone, imagePath);
                }

            } catch (Exception e) {
                Log.i("custom_check", e.toString());
            }

            return returnNotice;
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
            dataToSend.put("imageString", notice.imagePath);

            Notice returnNotice = null;

            try {

                String line = httpRequest.makeHttpRequest(dataToSend, ADDRESS + "/UpdateNoticeData.php");
                Log.i("custom_check", line);

                JSONObject jObj = new JSONObject(line);

                if (jObj.length() != 0) {
                    int resultImage = jObj.getInt("resultImage");
                    int resultUpdate = jObj.getInt("resultUpdate");
                    String imagePath = ADDRESS + jObj.getString("imagePath");

                    if (resultUpdate == 1 && resultImage == 1)
                        returnNotice = new Notice(notice.id, notice.lnName, notice.lnBirthDate, notice.lnPlace, notice.lnLostDate, notice.lnDetail, notice.lnAdder, notice.lnPhone, imagePath);

                }

            } catch (Exception e) {
                Log.i("custom_check", e.toString());
            }

            return returnNotice;
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
            dataToSend.put("imageString", seenInfo.imagePath);

            SeenInfo returnSeenInfo = null;

            try {

                String line = httpRequest.makeHttpRequest(dataToSend, ADDRESS + "/StoreSeenInfoData.php");
                Log.i("custom_check", line);

                JSONObject jObj = new JSONObject(line);

                if (jObj.length() != 0) {
                    int seenId = jObj.getInt("seenId");
                    int resultInfo = jObj.getInt("resultInfo");
                    int resultImage = jObj.getInt("resultImage");

                    if (resultImage == 1 && resultInfo == 1)
                        returnSeenInfo = new SeenInfo(seenId, seenInfo.seenDate, seenInfo.seenPlace, seenInfo.seenDetail, seenInfo.seenAdder, seenInfo.seenPhone, "");

                }
            } catch (Exception e) {
                Log.e("custom_check", e.toString());
            }
            return returnSeenInfo;
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

            SeenInfo returnSeenInfo = null;

            try {

                String line = httpRequest.makeHttpRequest(dataToSend, ADDRESS + "/FetchSeenInfoData.php");
                Log.i("custom_check", line);

                JSONObject jObj = new JSONObject(line);

                if (jObj.length() != 0) {
                    String seenDate = jObj.getString("seenDate");
                    String seenPlace = jObj.getString("seenPlace");
                    String seenDetail = jObj.getString("seenDetail");
                    String seenAdder = jObj.getString("seenAdder");
                    String seenPhone = jObj.getString("seenPhone");
                    String imagePath = ADDRESS + jObj.getString("imagePath");
                    int seenId = jObj.getInt("seenId");
                    int resultInfo = jObj.getInt("resultInfo");
                    int resultImage = jObj.getInt("resultImage");

                    if (resultInfo == 1 && resultImage == 1)
                        returnSeenInfo = new SeenInfo(seenId, seenDate, seenPlace, seenDetail, seenAdder, seenPhone, imagePath);
                }

            } catch (Exception e) {
                Log.i("custom_check", e.toString());
            }

            return returnSeenInfo;
        }

        @Override
        protected void onPostExecute(SeenInfo seenInfo) {
            progressDialog.dismiss();
            seenInfoCallback.done(seenInfo);
            super.onPostExecute(seenInfo);

        }
    }

    public class FetchNoticeItemGridAsyncTask extends AsyncTask<Void, Void, ArrayList<NoticeItem>> {
        GetItemCallback itemCallback;
        HttpRequest httpRequest;
        ArrayList<NoticeItem> noticeItems;
        int offset;

        public FetchNoticeItemGridAsyncTask(int offset, GetItemCallback itemCallback) {
            this.itemCallback = itemCallback;
            httpRequest = new HttpRequest();
            this.offset = offset;
            noticeItems = new ArrayList<>();
        }

        @Override
        public ArrayList<NoticeItem> doInBackground(Void... params) {
            Map<String, String> dataToSend = new HashMap<>();
            dataToSend.put("offset", offset + "");

            try {

                String line = httpRequest.makeHttpRequest(dataToSend, ADDRESS + "/FetchNoticeItemGrid.php");
                Log.i("custom_check", line);

                JSONObject jObj = new JSONObject(line);

                if (jObj.length() != 0) {
                    JSONArray noticeArray = jObj.getJSONArray("gridItem");
                    for (int i = 0; i < noticeArray.length(); i++) {
                        JSONObject item = noticeArray.getJSONObject(i);
                        int id = item.getInt("lnId");
                        String imagePath = item.getString("imgURL");
                        String textLine1 = item.getString("lnName");
                        String textLine2 = item.getString("lnLostDate");
                        Log.d("custom_check", id + " , " + imagePath + " , " + textLine1 + " , " + textLine2);
                        noticeItems.add(new NoticeItem(id, imagePath, textLine1, textLine2));
                    }
                }

            } catch (Exception e) {
                Log.i("custom_check", e.toString());
            }

            return noticeItems;
        }

        @Override
        protected void onPostExecute(ArrayList<NoticeItem> noticeItems) {
            progressDialog.dismiss();
            itemCallback.done(noticeItems);
            super.onPostExecute(noticeItems);

        }
    }
}
