package son.nt.en.service.notification;

import son.nt.en.base.IAudioFile;

/**
 * Created by sonnt on 7/28/16.
 */
public interface INotification {

    void setData(IAudioFile currentItem);

    void doPause();

    void doPlay();

    void doDetach ();
}
