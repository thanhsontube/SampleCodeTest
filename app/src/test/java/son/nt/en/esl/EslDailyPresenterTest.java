package son.nt.en.esl;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import son.nt.en.RxSchedulersOverrideRule;

/**
 * Created by sonnt on 8/1/16.
 */
public class EslDailyPresenterTest
{

    @Rule
    public final RxSchedulersOverrideRule mOverrideSchedulersRule = new RxSchedulersOverrideRule();

    @Mock
    EslDailyContract.View                               mView;
    @Mock
    FireBaseRepository                                  mRepository;

    @Captor
    private ArgumentCaptor<Observer<List<EslDailyDto>>> observerArgumentCaptor;

    @Mock
    Observer<List<EslDailyDto>>                         observer;

    EslDailyPresenter                                   mPresenter;

    @Before
    public void setUp() throws Exception
    {
        MockitoAnnotations.initMocks(this);
        mPresenter = new EslDailyPresenter(mView, mRepository);
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
        Mockito.verify(mRepository).getData(observerArgumentCaptor.capture());
        observerArgumentCaptor.getValue().onNext(eslDailyDtos);
        Mockito.verify(mView).resultSearch(eslDailyDtos);

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

}