package bolona_pig.proj_imgapp.ObjectClass;

import java.io.Serializable;

/**
 * Created by DreamMii on 17/1/2559.
 */
public class Notice implements Serializable {

    public int id;
    public String lnName, lnBirthDate, lnPlace, lnLostDate, lnDetail, lnAdder, lnPhone;

    public Notice(String lnName, String lnBirthDate, String lnPlace, String lnLostDate, String lnDetail, String lnAdder, String lnPhone) {
        this.lnName = lnName;
        this.lnBirthDate = lnBirthDate;
        this.lnPlace = lnPlace;
        this.lnLostDate = lnLostDate;
        this.lnDetail = lnDetail;
        this.lnAdder = lnAdder;
        this.lnPhone = lnPhone;
    }

    public Notice(int id, String lnName, String lnBirthDate, String lnPlace, String lnLostDate, String lnDetail, String lnAdder, String lnPhone) {
        this.id = id;
        this.lnName = lnName;
        this.lnBirthDate = lnBirthDate;
        this.lnPlace = lnPlace;
        this.lnLostDate = lnLostDate;
        this.lnDetail = lnDetail;
        this.lnAdder = lnAdder;
        this.lnPhone = lnPhone;
    }

    public void setNoticeId(int id) {
        this.id = id;
    }

}
