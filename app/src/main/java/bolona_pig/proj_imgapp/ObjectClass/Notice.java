package bolona_pig.proj_imgapp.ObjectClass;

import java.io.Serializable;

/**
 * Created by DreamMii on 17/1/2559.
 */
public class Notice implements Serializable {

    public int id;
    public String name, birthDate, lostPlace, lostDate,
            detail, adderName, telephone, imagePath, sex, adderUsername;

    public Notice(int id, String name, String sex, String birthDate, String lostPlace, String lostDate,
                  String detail, String adderUsername, String adderName,
                  String telephone, String imagePath) {
        this.id = id;
        this.name = name;
        this.sex = sex;
        this.birthDate = birthDate;
        this.lostPlace = lostPlace;
        this.lostDate = lostDate;
        this.detail = detail;
        this.adderUsername = adderUsername;
        this.adderName = adderName;
        this.telephone = telephone;
        this.imagePath = imagePath;
    }

    public Notice(int id) {
        this.id = id;
        this.name = "";
        this.sex = "";
        this.birthDate = "";
        this.lostPlace = "";
        this.lostDate = "";
        this.detail = "";
        this.adderUsername = "";
        this.adderName = "";
        this.telephone = "";
        this.imagePath = "";
    }


}
