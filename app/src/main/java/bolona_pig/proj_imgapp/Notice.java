package bolona_pig.proj_imgapp;

/**
 * Created by DreamMii on 17/1/2559.
 */
public class Notice {

    int noticeId;
    String lp_name, lp_birthdate, lp_location, lp_date, lp_detail, ct_name, ct_phone;

    public Notice(String lp_name, String lp_birthdate, String lp_date, String lp_location, String lp_detail, String ct_name, String ct_phone) {
        this.lp_name = lp_name;
        this.lp_birthdate = lp_birthdate;
        this.lp_date = lp_date;
        this.lp_location = lp_location;
        this.lp_detail = lp_detail;
        this.ct_name = ct_name;
        this.ct_phone = ct_phone;
    }

    public void setNoticeId(int id) {
        this.noticeId = id;
    }

}
