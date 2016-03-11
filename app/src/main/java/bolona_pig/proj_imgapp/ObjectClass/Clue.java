package bolona_pig.proj_imgapp.ObjectClass;

/**
 * Created by DreamMii on 2/2/2559.
 */
public class Clue {

    public int id;
    public String seenDate, seenPlace, detail, adderName, telephone;
    public String imagePath, sex, adderUsername;

    public Clue(int id, String sex, String seenDate, String seenPlace, String detail,
                String adderUsername, String adderName, String telephone, String imagePath) {
        this.id = id;
        this.sex = sex;
        this.seenDate = seenDate;
        this.seenPlace = seenPlace;
        this.detail = detail;
        this.adderUsername = adderUsername;
        this.adderName = adderName;
        this.telephone = telephone;
        this.imagePath = imagePath;
    }

    public Clue(int id, String date, String place) {
        this.id = id;
        this.seenDate = date;
        this.seenPlace = place;
        this.sex = "";
        this.detail = "";
        this.adderUsername = "";
        this.adderName = "";
        this.telephone = "";
        this.imagePath = "";
    }

    public Clue(int id) {
        this.id = id;
        this.seenDate = "";
        this.seenPlace = "";
        this.sex = "";
        this.detail = "";
        this.adderUsername = "";
        this.adderName = "";
        this.telephone = "";
        this.imagePath = "";
    }

}
