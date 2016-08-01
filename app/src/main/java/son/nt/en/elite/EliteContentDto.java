package son.nt.en.elite;

/**
 * Created by sonnt on 7/20/16.
 */
public class EliteContentDto {
    public String id;
    public String content;

    public EliteContentDto() {
    }

    public EliteContentDto(String id, String content) {
        this.id = id;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
