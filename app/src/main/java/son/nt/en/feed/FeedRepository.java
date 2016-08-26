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

import rx.Single;
import rx.SingleSubscriber;
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
    SingleSubscriber<List<EliteDto>> subscriberElite;
    SingleSubscriber<List<EslDailyDto>> subscriberEsl;

    DatabaseReference mDatabaseReference;

    @Inject
    public FeedRepository(DatabaseReference databaseReference) {
        mDatabaseReference = databaseReference;
    }

    @Override
    public void getElite(SingleSubscriber<List<EliteDto>> getElites) {
        this.subscriberElite = getElites;

        Query query = mDatabaseReference.child(FireBaseConstant.TABLE_ELITE_DAILY).limitToFirst(5);
        query.addListenerForSingleValueEvent(mEliteValueEventListener);
    }

    @Override
    public void getESL(SingleSubscriber<List<EslDailyDto>> subscriberEsl) {
        this.subscriberEsl = subscriberEsl;

        Query query = mDatabaseReference.child(FireBaseConstant.TABLE_ESL_DAILY).limitToFirst(5);
        query.addListenerForSingleValueEvent(mESlValueEventListener);
    }

    @Override
    public Single<List<HelloChaoSentences>> getDailyHelloChao() {

        return Single.create(new Single.OnSubscribe<List<HelloChaoSentences>>() {
            @Override
            public void call(SingleSubscriber<? super List<HelloChaoSentences>> singleSubscriber) {
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

                    singleSubscriber.onSuccess(helloChaoSentences);

                } catch (Exception e) {
                    singleSubscriber.onError(e);
                }
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
            subscriberEsl.onSuccess(list);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            subscriberEsl.onError(new Throwable(databaseError.getMessage()));
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
            subscriberElite.onSuccess(list);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            subscriberElite.onError(new Exception((databaseError.toException())));
        }
    };


}
