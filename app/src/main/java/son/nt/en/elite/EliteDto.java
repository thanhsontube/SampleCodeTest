package son.nt.en.elite;

import org.parceler.Parcel;

/**
 * Created by sonnt on 7/20/16.
 */
@Parcel
public class EliteDto {
    public String id;
    public String title;
    public String image;
    public String linkDetail;
    public String des;
    public String authPic;
    public String authName;
    public long publishTime;
    public String content;


    public EliteDto() {
    }

    public EliteDto(String id, String title, String image, String linkDetail, String des, String authPic, String authName, long publishTime, String content) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.linkDetail = linkDetail;
        this.des = des;
        this.authPic = authPic;
        this.authName = authName;
        this.publishTime = publishTime;
        this.content = content;
    }


}
