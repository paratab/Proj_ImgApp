package bolona_pig.proj_imgapp.ObjectClass;

/**
 * Created by DreamMii on 2/2/2559.
 */
public class SeenInfo {

    public int seenId;
    public String seenDate, seenPlace, seenDetail, seenAdder, seenPhone;
    public String imagePath;

    public SeenInfo(int seenId, String seenDate, String seenPlace, String seenDetail, String seenAdder, String seenPhone, String imagePath) {
        this.seenId = seenId;
        this.seenDate = seenDate;
        this.seenPlace = seenPlace;
        this.seenDetail = seenDetail;
        this.seenAdder = seenAdder;
        this.seenPhone = seenPhone;
        this.imagePath = imagePath;
    }

    public SeenInfo(int seenId, String seenDate, String seenPlace) {
        this.seenId = seenId;
        this.seenDate = seenDate;
        this.seenPlace = seenPlace;
    }

}
