package son.nt.en.chat;

import son.nt.en.base.IBasePresenter;

/**
 * Created by sonnt on 7/13/16.
 */
public interface ChatContract
{
    interface View
    {

        void userDoNotLogin(String s);

        void clearMessage(String s);
    }

    interface Presenter extends IBasePresenter
    {

        void sendMessage(String s);
    }

}
