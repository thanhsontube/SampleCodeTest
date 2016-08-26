package son.nt.en.feed;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import son.nt.en.FireBaseConstant;
import son.nt.en.MsConst;
import son.nt.en.elite.EliteDto;
import son.nt.en.esl.EslDailyDto;
import son.nt.en.hellochao.HelloChaoSentences;
import son.nt.en.utils.Logger;

/**
 * Created by sonnt on 8/22/16.
 */
public class FeedRepository implements FeedContract.IRepository {


    public static final String TAG = FeedRepository.class.getSimpleName();
    Observer<List<EliteDto>> subscriberElite;
    Observer<List<EslDailyDto>> subscriberEsl;

    DatabaseReference mDatabaseReference;

    @Inject
    public FeedRepository(DatabaseReference databaseReference) {
        mDatabaseReference = databaseReference;
    }

    @Override
    public void getElite(Observer<List<EliteDto>> listObserver) {
        this.subscriberElite = listObserver;

        Query query = mDatabaseReference.child(FireBaseConstant.TABLE_ELITE_DAILY).limitToFirst(5);
        query.addListenerForSingleValueEvent(mEliteValueEventListener);
    }

    @Override
    public void getESL(Subscriber<List<EslDailyDto>> subscriberEsl) {
        this.subscriberEsl = subscriberEsl;
        Query query = mDatabaseReference.child(FireBaseConstant.TABLE_ESL_DAILY).limitToFirst(5);
        query.addListenerForSingleValueEvent(mESlValueEventListener);
    }

    @Override
    public Observable<List<HelloChaoSentences>> getDailyHelloChao() {

        return Observable.create((Observable.OnSubscribe<List<HelloChaoSentences>>) subscriber -> {
            try {

                Document document = Jsoup.connect(MsConst.HELLO_CHAO_THU_THACH_TRONG_NGAY).get();
                Elements items = document.getElementsByAttributeValue("class", "box shadow light callout");
                Elements data = items.get(0).getElementsByClass("raw-menu");

                List<HelloChaoSentences> helloChaoSentences = new ArrayList<>();
                HelloChaoSentences helloChaoSentence;
                for (Element e : data.get(0).getAllElements()) {
                    if (e.nodeName().equals("li")) {
                        String link = e.getElementsByAttribute("href").attr("href");
                        String textEng = e.getAllElements().get(1).text();
                        String textVi = e.getAllElements().get(0).text().replace(textEng, "");
                        Logger.debug(TAG, ">>>" + "link:" + link + " ;textVi:" + textVi + ";textEng:" + textEng);
                        helloChaoSentence = new HelloChaoSentences(textEng, link, textVi);
                        helloChaoSentences.add(helloChaoSentence);
                    }
                }
                subscriber.onNext(helloChaoSentences);
                subscriber.onCompleted();

            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }


    ValueEventListener mESlValueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            List<EslDailyDto> list = new ArrayList<>();
            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                EslDailyDto post = postSnapshot.getValue(EslDailyDto.class);
                list.add(post);
            }

            subscriberEsl.onNext(list);

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            List<EliteDto> list = new ArrayList<>();
            subscriberElite.onNext(list);
        }
    };


    ValueEventListener mEliteValueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            List<EliteDto> list = new ArrayList<>();
            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                EliteDto post = postSnapshot.getValue(EliteDto.class);
                list.add(post);
            }

            subscriberElite.onNext(list);

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            List<EliteDto> list = new ArrayList<>();
            subscriberElite.onNext(list);
        }
    };


}
