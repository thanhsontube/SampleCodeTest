package son.nt.en.hellochao;

import java.util.List;

import son.nt.en.base.IAudioFile;

/**
 * Created by sonnt on 7/14/16.
 */
public class HelloChaoSentences implements IAudioFile
{
    public String text;
    public String audio;
    public String translate;

    public HelloChaoSentences() {
    }

    public HelloChaoSentences(String text, String audio, String translate) {
        this.text = text;
        this.audio = audio;
        this.translate = translate;
    }

    public String getText() {
        return text;
    }

    public String getAudio() {
        return audio;
    }

    public String getTranslate() {
        return translate;
    }

    @Override
    public String getTitle() {
        return text;
    }

    @Override
    public String getLinkMp3() {
        return audio;
    }

    @Override
    public String getImage() {
        return null;
    }

    @Override
    public String getDescription() {
        return translate;
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
