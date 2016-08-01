package son.nt.en.base;

import java.util.List;

/**
 * Created by sonnt on 7/15/16.
 */
public interface IAudioFile
{
//    String itemId;
//
//    String title;
//    String link;
//    String image;
//    String group;
//
//    long duration;
//    String sub;
//
//    int totalLike;
//    int totalComments;
//
//    boolean isPlaying;
//    boolean isFavorite;
//    boolean isLiked;

    String getTitle();
    String getLinkMp3();
    String getImage();
    String getDescription();
    List<String> getGroup();
    Long getDuration();
//    boolean

    List<String> getTags();

}
