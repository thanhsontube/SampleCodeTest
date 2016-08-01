package son.nt.en.debug;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import son.nt.en.FireBaseConstant;
import son.nt.en.R;
import son.nt.en.elite.EliteContentDto;
import son.nt.en.elite.EliteDto;
import son.nt.en.esl.EslDailyDto;
import son.nt.en.hellochao.HelloChaoSentences;
import son.nt.en.utils.DataTimesUtil;
import son.nt.en.utils.Logger;

public class DebugActivity extends AppCompatActivity implements View.OnClickListener
{

    private static final String TAG = DebugActivity.class.getSimpleName();
    DatabaseReference           mFirebaseDatabaseReference;
    List<EliteDto>              eliteDtos;
    List<EliteContentDto>              eliteContent;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);
        findViewById(R.id.readfile).setOnClickListener(this);
        findViewById(R.id.readesl).setOnClickListener(this);
        findViewById(R.id.elite).setOnClickListener(this);
        findViewById(R.id.elite_content).setOnClickListener(this);
        findViewById(R.id.elite_mixup).setOnClickListener(this);
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.readfile:
            {
                //                test();
                break;
            }

            case R.id.readesl:
            {

                //                readFileESL();
                break;
            }
            case R.id.elite:
            {
//                                new LoadJsoup().execute();


                loadContent();
                break;
            }
            case R.id.elite_content:
            {
                loadListE();
                break;
            }
            case R.id.elite_mixup:
            {
//                mixup();

//                loadContent();
                break;
            }
        }
    }

    private void loadListE()
    {
        try
        {
            mFirebaseDatabaseReference.child(FireBaseConstant.TABLE_ELITE_Content)
                    .addValueEventListener(valueEventListenerContent);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void mixup()
    {
        for (int i = 0; i < eliteDtos.size(); i++)
        {
            eliteDtos.get(i).content = eliteContent.get(i).getContent();
        }

        uploadEliteWithContent(eliteDtos);


    }


    private void loadContent()
    {
        try
        {
            mFirebaseDatabaseReference.child(FireBaseConstant.TABLE_ELITE_DAILY)
                            .addValueEventListener(valueEventListenerIndex);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    ValueEventListener valueEventListenerContent = new ValueEventListener()
    {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot)
        {
            eliteContent = new ArrayList<>();
            for (DataSnapshot postSnapshot : dataSnapshot.getChildren())
            {
                EliteContentDto post = postSnapshot.getValue(EliteContentDto.class);
                eliteContent.add(post);
            }
            Logger.debug(TAG, ">>>" + "list valueEventListenerContent:" + eliteContent.size());

        }

        @Override
        public void onCancelled(DatabaseError databaseError)
        {

        }
    };

    ValueEventListener valueEventListenerIndex = new ValueEventListener()
    {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot)
        {
            eliteDtos = new ArrayList<>();
            for (DataSnapshot postSnapshot : dataSnapshot.getChildren())
            {
                EliteDto post = postSnapshot.getValue(EliteDto.class);
                eliteDtos.add(post);
            }
            Logger.debug(TAG, ">>>" + "list valueEventListenerIndex:" + eliteDtos.size());
        }

        @Override
        public void onCancelled(DatabaseError databaseError)
        {

        }
    };

    private void readFile()
    {
        try
        {
            String path = Environment.getExternalStorageDirectory() + "/" + "00_backup" + "/" + "hello.txt";
            BufferedReader br = new BufferedReader(new FileReader(new File(path)));
            String line;
            HelloChaoSentences helloChaoSentences = null;
            List<HelloChaoSentences> list = new ArrayList<>();
            int i = 0;

            while ((line = br.readLine()) != null)
            {
                i++;
                if (i == 1)
                {
                    helloChaoSentences = new HelloChaoSentences();
                    helloChaoSentences.text = line;
                }
                if (i == 2)
                {
                    helloChaoSentences.translate = line;
                }
                if (i == 3)
                {
                    helloChaoSentences.audio = line;
                }
                if (i == 4)
                {
                    list.add(helloChaoSentences);
                    i = 0;

                }
            }
            br.close();
            Logger.debug(TAG, ">>>" + "List:" + list.size());
            HelloChaoSentences t1 = list.get(10);
            Logger.debug(TAG, ">>>" + "text:" + t1.text + ";trans:" + t1.translate + ";au:" + t1.audio);
            upload1(list);

        }
        catch (Exception e)
        {
            e.printStackTrace();

        }
    }

    private void upload1(List<HelloChaoSentences> list)
    {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        for (HelloChaoSentences s : list)
        {

            databaseReference.child("HelloChaoSentences").push().setValue(s)
                            .addOnCompleteListener(new OnCompleteListener<Void>()
                            {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    Logger.debug(TAG, ">>>" + "onComplete");

                                }
                            }).addOnFailureListener(new OnFailureListener()
                            {
                                @Override
                                public void onFailure(@NonNull Exception e)
                                {
                                    Logger.debug(TAG, ">>>" + "fail:" + e);

                                }
                            });

        }

    }

    private void readFileESL()
    {
        try
        {
            String path = Environment.getExternalStorageDirectory() + "/" + "00_backup" + "/" + "esl.txt";
            BufferedReader br = new BufferedReader(new FileReader(new File(path)));
            String line;
            EslDailyDto helloChaoSentences = null;
            List<EslDailyDto> list = new ArrayList<>();
            int i = 0;

            while ((line = br.readLine()) != null)
            {
                i++;
                if (i == 1)
                {
                    helloChaoSentences = new EslDailyDto();
                    helloChaoSentences.homeGroup = line;
                }
                if (i == 2)
                {
                    helloChaoSentences.homeTitle = line;
                }
                if (i == 3)
                {
                    helloChaoSentences.homeHref = line;
                }
                if (i == 4)
                {
                    helloChaoSentences.homeQuizLink = line;
                }
                if (i == 5)
                {
                    helloChaoSentences.homeMp3 = line;
                }
                if (i == 6)
                {
                    helloChaoSentences.homeDescription = line;
                }
                if (i == 7)
                {
                    helloChaoSentences.homeImage = line;
                }

                if (i == 8)
                {
                    list.add(helloChaoSentences);
                    i = 0;

                }
            }
            br.close();
            Logger.debug(TAG, ">>>" + "List:" + list.size());
            //            for (EslDailyDto d : list)
            //            {
            //            }
            //            EslDailyDto t1 = list.get(120);
            //            Logger.debug(TAG, ">>>" + "text:" + t1.homeDescription + ";trans:" + t1.homeTitle + ";au:" + t1.homeFullText);
            //
            //            EslDailyDto t2 = list.get(12);
            //            Logger.debug(TAG, ">>>" + "text:" + t2.homeDescription + ";trans:" + t2.homeTitle + ";au:" + t2.homeFullText);
            uploadESL(list);

        }
        catch (Exception e)
        {
            e.printStackTrace();

        }
    }

    private void uploadESL(List<EslDailyDto> list)
    {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        for (EslDailyDto s : list)
        {

            databaseReference.child("EslDaiLy").push().setValue(s).addOnCompleteListener(new OnCompleteListener<Void>()
            {
                @Override
                public void onComplete(@NonNull Task<Void> task)
                {
                    Logger.debug(TAG, ">>>" + "onComplete");

                }
            }).addOnFailureListener(new OnFailureListener()
            {
                @Override
                public void onFailure(@NonNull Exception e)
                {
                    Logger.debug(TAG, ">>>" + "fail:" + e);

                }
            });

        }

    }

    private void uploadElite(List<EliteDto> list)
    {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        for (EliteDto s : list)
        {

            databaseReference.child("EliteDaily").push().setValue(s)
                            .addOnCompleteListener(new OnCompleteListener<Void>()
                            {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    Logger.debug(TAG, ">>>" + "onComplete");

                                }
                            }).addOnFailureListener(new OnFailureListener()
                            {
                                @Override
                                public void onFailure(@NonNull Exception e)
                                {
                                    Logger.debug(TAG, ">>>" + "fail:" + e);

                                }
                            });

        }

    }

    private void uploadEliteWithContent(List<EliteDto> list)
    {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        for (EliteDto s : list)
        {

            databaseReference.child("Elite").push().setValue(s)
                    .addOnCompleteListener(new OnCompleteListener<Void>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            Logger.debug(TAG, ">>>" + "onComplete");

                        }
                    }).addOnFailureListener(new OnFailureListener()
            {
                @Override
                public void onFailure(@NonNull Exception e)
                {
                    Logger.debug(TAG, ">>>" + "fail:" + e);

                }
            });

        }

    }

    private void uploadEliteContent(List<EliteContentDto> list)
    {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        for (EliteContentDto s : list)
        {

            databaseReference.child("EliteContent").push().setValue(s)
                    .addOnCompleteListener(new OnCompleteListener<Void>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            Logger.debug(TAG, ">>>" + "onComplete");

                        }
                    }).addOnFailureListener(new OnFailureListener()
            {
                @Override
                public void onFailure(@NonNull Exception e)
                {
                    Logger.debug(TAG, ">>>" + "fail:" + e);

                }
            });

        }

    }

    private class LoadJsoup extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... params)
        {
            try
            {

                List<EliteDto> listCheck;
                List<EliteDto> totalList = new ArrayList<>();
                for (int i = 1; i < 400; i++)
                {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("http://elitedaily.com/topic/love/page/");
                    listCheck = getList(stringBuilder.append(i).toString());

                    Logger.debug(TAG, ">>>" + "listCheck:" + listCheck.size());
                    if (listCheck.isEmpty())
                    {
                        break;
                    }

                    totalList.addAll(listCheck);

                }
                Logger.debug(TAG, ">>>" + "Done....:" + totalList.size());
                //                uploadElite(totalList);

                //                Document document = Jsoup.connect("http://elitedaily.com/topic/love/page/1").get();
                //                Logger.debug(TAG, ">>>" + "title:" + document.title());
                //                Elements items = document.getElementsByAttributeValue("class", "activity-item");
                //                Logger.debug(TAG, ">>>" + "items:" + items.size());
                //                List<EliteDto> lists = new ArrayList<>();
                //                EliteDto eliteDto;
                //                for (Element e : items)
                //                {
                //                    String id = e.getElementsByAttribute("data-post-id").attr("data-post-id");
                //                    String image = e.getElementsByAttribute("src").attr("src");
                //                    String linkDetail = e.getElementsByAttribute("data-ev-val").attr("data-ev-val");
                //                    String title = e.getElementsByAttributeValue("data-ev-loc", "taxonomy-page-horizontal").text();
                //                    String des = e.getElementsByAttributeValue("class", "custom-article-excerpt").text();
                //
                //                    Element au = e.getElementsByAttributeValue("class", "row article-meta").get(0)
                //                                    .getElementsByAttributeValue("class", "article-meta__author-avatar-wrapper").get(0);
                //                    Logger.debug(TAG, ">>>" + "au:" + au.getAllElements().size());
                //                    String authPic = au.getAllElements().get(1).attr("data-lazy-src");
                //                    String authName = au.getAllElements().get(1).attr("alt");
                //                    Logger.debug(TAG, ">>>" + "authName:" + authName + " ;link:" + authPic);
                //                    eliteDto = new EliteDto(id, title, image, linkDetail, des, authPic, authName, "");
                //                    lists.add(eliteDto);
                //                }

            }
            catch (Exception e)
            {
                Logger.error(TAG, ">>> Error:" + "Error :" + e);
            }

            return null;
        }
    }

    synchronized private List<EliteDto> getList(String link)
    {
        Logger.debug(TAG, ">>>" + "getList:" + link);
        List<EliteDto> lists = new ArrayList<>();
        try
        {
            Document document = Jsoup.connect(link).get();
            //            Logger.debug(TAG, ">>>" + "title:" + document.title());
            Elements items = document.getElementsByAttributeValue("class", "activity-item");
            //            Logger.debug(TAG, ">>>" + "items:" + items.size());

            EliteDto eliteDto;
            for (Element e : items)
            {
                String id = e.getElementsByAttribute("data-post-id").attr("data-post-id");
                String image = e.getElementsByAttribute("src").attr("src");
                String linkDetail = e.getElementsByAttribute("data-ev-val").attr("data-ev-val");
                String title = e.getElementsByAttributeValue("data-ev-loc", "taxonomy-page-horizontal").text();
                Logger.debug(TAG, ">>>" + "title:" + title + ";linkDetail:" + linkDetail);
                String des = e.getElementsByAttributeValue("class", "custom-article-excerpt").text();

                Element au = e.getElementsByAttributeValue("class", "row article-meta").get(0)
                                .getElementsByAttributeValue("class", "article-meta__author-avatar-wrapper").get(0);
                String authPic = au.getAllElements().get(1).attr("data-lazy-src");
                String authName = au.getAllElements().get(1).attr("alt");
                //                Logger.debug(TAG, ">>>" + "authName:" + authName + " ;link:" + authPic);

                String publishData = e.getElementsByAttributeValue("itemprop", "datePublished").attr("datetime");
                //                Logger.debug(TAG, ">>>" + "publishData:" + publishData);
                eliteDto = new EliteDto(id, title, image, linkDetail, des, authPic, authName,
                                DataTimesUtil.convertDateToMilliseconds("yyyy-MM-dd'T'HH:mm:ss", publishData), "");
                lists.add(eliteDto);
            }
        }
        catch (Exception e)
        {
            Logger.error(TAG, ">>> Error:" + "getList:" + link + ";e:" + e.toString());
        }
        return lists;
    }

    private class LoadContent extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... params)
        {
            //            try
            //            {
            //                String link = "http://elitedaily.com/dating/guy-isnt-into-you-relationship-expert/1556469/";
            //                Document document = Jsoup.connect(link).get();
            //                Elements e = document.getElementsByAttributeValue("class", "entry-content");
            //                Logger.debug(TAG, ">>>" + "e:" + e.text());
            //
            //            }
            //            catch (Exception e)
            //            {
            //                Logger.error(TAG, ">>> Error:" + "Error :" + e);
            //            }
            int i = 1;
            List<EliteContentDto> eliteContentDtos = new ArrayList<>();
            EliteContentDto dto;
            for (EliteDto d : eliteDtos)
            {
                String text = loadContentData(d.linkDetail);
                dto = new EliteContentDto(d.id, text);
                eliteContentDtos.add(dto);

//                i++;
//                if (i == 5)
//                {
//                    break;
//                }

            }

            uploadEliteContent(eliteContentDtos);



            return null;
        }
    }

    synchronized private String  loadContentData(String link)
    {
        Logger.debug(TAG, ">>>" + "loadContentData:" + link);
        try
        {
            Document document = Jsoup.connect(link).get();
            Elements e = document.getElementsByAttributeValue("class", "entry-content");
            Logger.debug(TAG, ">>>" + "e:" + e.text());
            return e.text();
        }
        catch (Exception e)
        {
            Logger.error(TAG, ">>> Error:" + "loadContentData:" + link + ";e:" + e.toString());
        }
        return "";
    }

}
