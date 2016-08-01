package son.nt.en.esl;

import java.util.List;

import son.nt.en.base.IAudioFile;

/**
 * Created by sonnt on 7/15/16.
 */
public class EslDailyDto implements IAudioFile {
    public String homeGroup;
    public String homeTitle;
    public String homeHref;
    public String homeQuizLink;
    public String homeMp3;
    public String homeDescription;
    public String homeImage;
    public String homeFullText;

    public EslDailyDto() {
    }

    public String getHomeGroup() {
        return homeGroup;
    }

    public void setHomeGroup(String homeGroup) {
        this.homeGroup = homeGroup;
    }

    public String getHomeTitle() {
        return homeTitle;
    }

    public void setHomeTitle(String homeTitle) {
        this.homeTitle = homeTitle;
    }

    public String getHomeHref() {
        return homeHref;
    }

    public void setHomeHref(String homeHref) {
        this.homeHref = homeHref;
    }

    public String getHomeQuizLink() {
        return homeQuizLink;
    }

    public void setHomeQuizLink(String homeQuizLink) {
        this.homeQuizLink = homeQuizLink;
    }

    public String getHomeMp3() {
        return homeMp3;
    }

    public void setHomeMp3(String homeMp3) {
        this.homeMp3 = homeMp3;
    }

    public String getHomeDescription() {
        return homeDescription;
    }

    public void setHomeDescription(String homeDescription) {
        this.homeDescription = homeDescription;
    }

    public String getHomeImage() {
        return homeImage;
    }

    public void setHomeImage(String homeImage) {
        this.homeImage = homeImage;
    }

    public String getHomeFullText() {
        return homeFullText;
    }

    public void setHomeFullText(String homeFullText) {
        this.homeFullText = homeFullText;
    }

    @Override
    public String getTitle() {
        return homeTitle;
    }

    @Override
    public String getLinkMp3() {
        return homeMp3;
    }

    @Override
    public String getImage() {
        return homeImage;
    }

    @Override
    public String getDescription() {
        return homeDescription;
    }

    @Override
    public List<String> getGroup() {
        return null;
    }

    @Override
    public Long getDuration() {
        return null;
    }

    @Override
    public List<String> getTags() {
        return null;
    }
}
