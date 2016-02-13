package bolona_pig.proj_imgapp.ObjectClass;

import java.io.Serializable;

/**
 * Created by DreamMii on 17/1/2559.
 */
public class Notice implements Serializable {

    public int id;
    public String lnName, lnBirthDate, lnPlace, lnLostDate, lnDetail, lnAdder, lnPhone, imagePath;

    public Notice(int id, String lnName, String lnBirthDate, String lnPlace, String lnLostDate, String lnDetail, String lnAdder, String lnPhone, String imagePath) {
        this.id = id;
        this.lnName = lnName;
        this.lnBirthDate = lnBirthDate;
        this.lnPlace = lnPlace;
        this.lnLostDate = lnLostDate;
        this.lnDetail = lnDetail;
        this.lnAdder = lnAdder;
        this.lnPhone = lnPhone;
        this.imagePath = imagePath;
    }


}
