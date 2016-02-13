package bolona_pig.proj_imgapp.ObjectClass;

/**
 * Created by DreamMii on 13/2/2559.
 */
public class GridItem {
    public int id;
    public String image;
    public String title1;
    public String title2;

    public GridItem(int id, String title1, String title2, String image) {
        this.id = id;
        this.title1 = title1;
        this.title2 = title2;
        this.image = image;
    }
}