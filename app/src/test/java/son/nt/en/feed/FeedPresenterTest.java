package son.nt.en.feed;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.subscriptions.CompositeSubscription;
import son.nt.en.RxSchedulersOverrideRule;
import son.nt.en.elite.EliteDto;
import son.nt.en.esl.EslDailyDto;
import son.nt.en.hellochao.HelloChaoSentences;
import son.nt.en.utils.CompositeSubs;

/**
 * Created by sonnt on 8/26/16.
 * {@link FeedPresenter}
 */
public class FeedPresenterTest {

    @Rule
    public final RxSchedulersOverrideRule mOverrideSchedulersRule = new RxSchedulersOverrideRule();
    @Mock
    FeedContract.View mView;

    @Mock
    FeedContract.IRepository mRepository;


    @Mock
    CompositeSubs mCompositeSubs;
    @Captor
    ArgumentCaptor<Subscriber<List<EslDailyDto>>> mSubscriberArgumentCaptor;

    @Captor
    ArgumentCaptor<Observer<List<EliteDto>>> captorElites;

    List<HelloChaoSentences> helloChaoSentences;

    CompositeSubscription mCompositeSubscription = new CompositeSubscription();

    FeedPresenter mPresenter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mPresenter = new FeedPresenter(mRepository, mView);
        helloChaoSentences = createHcs();
        Mockito.when(mRepository.getDailyHelloChao()).thenReturn(Observable.just(helloChaoSentences));
    }

    @Test
    public void testOnStartHC() throws Exception {
//        List<HelloChaoSentences> helloChaoSentences = createHcs();
//        Mockito.when(mRepository.getDailyHelloChao()).thenReturn(Observable.just(helloChaoSentences));
        mPresenter.onStart();
        Mockito.verify(mView).setDailyHelloChao(helloChaoSentences);
    }

    @Test
    public void testOnStartEsl() {
        List<EslDailyDto> esls  = createEsls();
        mPresenter.onStart();
        Mockito.verify(mRepository).getESL(mSubscriberArgumentCaptor.capture());
        mSubscriberArgumentCaptor.getValue().onNext(esls);
        Mockito.verify(mView).setEslData(esls);
    }

    @Test
    public void testOnStartElite() {
        List<EliteDto> elietes  = createElites();
        mPresenter.onStart();
        Mockito.verify(mRepository).getElite(captorElites.capture());
        captorElites.getValue().onNext(elietes);
        Mockito.verify(mView).setEliteData(elietes);
    }


    @Test
    public void testOnStop() throws Exception {
        mPresenter.onStop();
//        Mockito.verify(mCompositeSubscription).unsubscribe();
//        mSubscription.unsubscribe();
        Mockito.verify(mCompositeSubs).removeAll();



    }

    private List<EliteDto> createElites ()
    {
        EliteDto eslDailyDto = new EliteDto();
        return Collections.singletonList(eslDailyDto);
    }
    private List<EslDailyDto> createEsls ()
    {
        EslDailyDto eslDailyDto = new EslDailyDto();
        return Collections.singletonList(eslDailyDto);
    }

    private List<HelloChaoSentences> createHcs() {
        HelloChaoSentences helloChaoSentences = new HelloChaoSentences();
        return Collections.singletonList(helloChaoSentences);
    }
}