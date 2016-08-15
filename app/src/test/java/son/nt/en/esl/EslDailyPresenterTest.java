package son.nt.en.esl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.Scheduler;
import rx.Subscription;
import rx.android.plugins.RxAndroidPlugins;
import rx.android.plugins.RxAndroidSchedulersHook;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

/**
 * Created by sonnt on 8/1/16.
 */
public class EslDailyPresenterTest
{

//    @Rule
//    public final RxSchedulersOverrideRule mOverrideSchedulersRule = new RxSchedulersOverrideRule();

    @Mock
    EslDailyContract.View                               mView;

    @Mock
    FireBaseRepository                                  mRepository;

    @Captor
    private ArgumentCaptor<Observer<List<EslDailyDto>>> observerArgumentCaptor;

    @Mock
    Observer<List<EslDailyDto>>                         observer;

    PublishSubject<List<EslDailyDto>> publishSubject2;

    @Mock
    Subscription subscription;

    @Mock
    Subscription subscription2;

    EslDailyPresenter                                   mPresenter;

    @Before
    public void setUp() throws Exception
    {
        MockitoAnnotations.initMocks(this);
        RxAndroidPlugins.getInstance().registerSchedulersHook(new RxAndroidSchedulersHook() {
            @Override
            public Scheduler getMainThreadScheduler() {
                return Schedulers.immediate();
            }
        });




        publishSubject2 = PublishSubject.create();
//
//        subscription2 = publishSubject2
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(observer);
        mPresenter = new EslDailyPresenter(mView, mRepository, publishSubject2);
    }

    @Test
    public void testOnStart() throws Exception
    {
        List<EslDailyDto> eslDailyDtos = new ArrayList<>();
        eslDailyDtos.add(new EslDailyDto());
        eslDailyDtos.add(new EslDailyDto());
        eslDailyDtos.add(new EslDailyDto());
        eslDailyDtos.add(new EslDailyDto());

        mPresenter.onStart();
        Mockito.verify(mRepository).getData();
//        Mockito.verify(mRepository).getData(observerArgumentCaptor.capture());
//        observerArgumentCaptor.getValue().onNext(eslDailyDtos);
//        observerArgumentCaptor.getValue().onError(new Throwable());
//        Mockito.verify(mView).resultSearch(eslDailyDtos);

    }

    @Test
    public void testAfterTextChanged() throws Exception
    {

        List<EslDailyDto> eslDailyDtos = new ArrayList<>();
        eslDailyDtos.add(new EslDailyDto());
        eslDailyDtos.add(new EslDailyDto());
        eslDailyDtos.add(new EslDailyDto());
        eslDailyDtos.add(new EslDailyDto());

        Mockito.when(mRepository.doSearch3(Mockito.anyString())).thenReturn(eslDailyDtos);
        mPresenter.afterTextChanged("qwe");
        Mockito.verify(mView).setVisibility(true);
        Mockito.verify(mView).resultSearch(eslDailyDtos);

    }

    @Test
    public void testOnStart1() throws Exception {

    }

    @Test
    public void testOnDestroy() throws Exception {
        mPresenter.onDestroy();
        Mockito.verify(subscription).unsubscribe();
        Mockito.verify(subscription2).unsubscribe();

    }

    @Test
    public void testAfterTextChanged1() throws Exception {
        mPresenter.afterTextChanged("123");
        Mockito.verify(mView).setVisibility(true);

    }
}