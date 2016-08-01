package son.nt.en.service;

/**
 * Created by sonnt on 5/26/16.
 */
public class GoPlayer {
    public static final int DO_PLAY = 1;
    public static final int DO_PAUSE = 2;
    public int command;
    public int pos;
    public String title;
    public String des;
    public String image;

    public GoPlayer(int command) {
        this.command = command;
    }

    public GoPlayer(int command, int pos) {
        this.command = command;
        this.pos = pos;
    }

    public GoPlayer(int command, String title, String des, String image) {
        this.command = command;
        this.title = title;
        this.des = des;
        this.image = image;
    }
}
