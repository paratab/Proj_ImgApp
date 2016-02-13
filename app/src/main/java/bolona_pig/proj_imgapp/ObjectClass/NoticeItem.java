package bolona_pig.proj_imgapp.ObjectClass;

/**
 * Created by DreamMii on 10/2/2559.
 */
public class NoticeItem {

    private int id;
    private String image;
    private String titleLine1;
    private String titleLine2;

    public NoticeItem() {
        super();
    }

    public NoticeItem(int id, String image, String titleLine1, String titleLine2) {
        this.id = id;
        this.image = image;
        this.titleLine1 = titleLine1;
        this.titleLine2 = titleLine2;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitleLine1() {
        return titleLine1;
    }

    public void setTitleLine1(String titleLine1) {
        this.titleLine1 = titleLine1;
    }

    public String getTitleLine2() {
        return titleLine1;
    }

    public void setTitleLine2(String titleLine2) {
        this.titleLine2 = titleLine2;
    }

}
