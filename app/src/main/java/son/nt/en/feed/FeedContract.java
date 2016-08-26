package son.nt.en.feed;


import java.util.List;

import rx.Single;
import rx.SingleSubscriber;
import son.nt.en.base.IBasePresenter;
import son.nt.en.elite.EliteDto;
import son.nt.en.esl.EslDailyDto;
import son.nt.en.hellochao.HelloChaoSentences;

/**
 * Created by sonnt on 7/14/16.
 */
public interface FeedContract {
    interface View {


        void setEliteData(List<EliteDto> eliteDtos);

        void setDailyHelloChao(List<HelloChaoSentences> helloChaoSentences);

        void setEslData(List<EslDailyDto> eslDailyDtos);
    }

    interface Presenter extends IBasePresenter {
        void onStop();

    }

    interface IRepository {

        void getElite (SingleSubscriber<List<EliteDto>> getElites);

        Single<List<HelloChaoSentences>> getDailyHelloChao();

        void getESL(SingleSubscriber<List<EslDailyDto>> getEsl);
    }
}
