package son.nt.en.feed;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.Single;
import rx.SingleSubscriber;
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
    ArgumentCaptor<SingleSubscriber<List<EslDailyDto>>> mSubscriberArgumentCaptor;

    @Captor
    ArgumentCaptor<SingleSubscriber<List<EliteDto>>> captorElites;

    @Captor
    ArgumentCaptor<Single<List<HelloChaoSentences>>> captorHc;

    List<HelloChaoSentences> helloChaoSentences;

    FeedPresenter mPresenter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mPresenter = new FeedPresenter(mRepository, mView, mCompositeSubs);
        helloChaoSentences = createHcs();
        Mockito.when(mRepository.getDailyHelloChao()).thenReturn(Single.just(helloChaoSentences));
    }

    @Test
    public void testOnStartHC() throws Exception {
        mPresenter.onStart();
        Mockito.verify(mView).setDailyHelloChao(helloChaoSentences);
    }

    @Test
    public void testOnStartHCFail() throws Exception {
        Mockito.when(mRepository.getDailyHelloChao()).thenReturn(Single.error(new Throwable()));
        mPresenter.onStart();
        Mockito.verify(mView).setDailyHelloChao(new ArrayList<>());
    }

    @Test
    public void testOnStartEsl() {
        List<EslDailyDto> esls  = createEsls();
        mPresenter.onStart();
        Mockito.verify(mRepository).getESL(mSubscriberArgumentCaptor.capture());
        mSubscriberArgumentCaptor.getValue().onSuccess(esls);
        Mockito.verify(mView).setEslData(esls);
    }

    @Test
    public void testOnStartEslError() {
        mPresenter.onStart();
        Mockito.verify(mRepository).getESL(mSubscriberArgumentCaptor.capture());
        mSubscriberArgumentCaptor.getValue().onError(new Throwable());
        Mockito.verify(mView).setEslData(new ArrayList<>());
    }

    @Test
    public void testOnStartEliteOnSuccess() {
        List<EliteDto> elites = createElites();
        mPresenter.onStart();
        Mockito.verify(mRepository).getElite(captorElites.capture());
        captorElites.getValue().onSuccess(elites);
        Mockito.verify(mView).setEliteData(elites);
    }

    @Test
    public void testOnStartEliteOnError() {
        mPresenter.onStart();
        Mockito.verify(mRepository).getElite(captorElites.capture());
        captorElites.getValue().onError(new Throwable());
        Mockito.verify(mView).setEliteData(new ArrayList<>());
    }


    @Test
    public void testOnStop() throws Exception {
        mPresenter.onStop();
        Mockito.verify(mCompositeSubs).removeAll();
    }

    private List<EliteDto> createElites() {
        EliteDto eslDailyDto = new EliteDto();
        return Collections.singletonList(eslDailyDto);
    }

    private List<EslDailyDto> createEsls() {
        EslDailyDto eslDailyDto = new EslDailyDto();
        return Collections.singletonList(eslDailyDto);
    }

    private List<HelloChaoSentences> createHcs() {
        HelloChaoSentences helloChaoSentences = new HelloChaoSentences();
        return Collections.singletonList(helloChaoSentences);
    }
}