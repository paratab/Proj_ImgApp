package bolona_pig.proj_imgapp.ObjectClass;

/**
 * Created by DreamMii on 5/1/2559.
 */
public class User {

    public String username, password, name, nationId, email, telephone, imagePath;


    public User(String username, String password, String name, String nationId, String email, String telephone, String imagePath) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.nationId = nationId;
        this.email = email;
        this.telephone = telephone;
        this.imagePath = imagePath;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.name = "";
        this.nationId = "";
        this.email = "";
        this.telephone = "";
        this.imagePath = "";
    }

}
