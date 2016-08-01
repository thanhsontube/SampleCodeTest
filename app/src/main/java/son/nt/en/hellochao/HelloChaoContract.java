package son.nt.en.hellochao;

import java.util.List;

import son.nt.en.base.BasePresenter;

/**
 * Created by sonnt on 7/14/16.
 */
public interface HelloChaoContract {
    interface View {

        void resultSearch(List<HelloChaoSentences> list);
    }

    interface Presenter extends BasePresenter
    {

        void doSearch(String keyword);

        void setData(List<HelloChaoSentences> list);
    }
}
