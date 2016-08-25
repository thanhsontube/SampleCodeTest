package son.nt.en.jsoup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.os.AsyncTask;

import son.nt.en.MsConst;
import son.nt.en.utils.Logger;

/**
 * Created by sonnt on 8/22/16.
 */
public class JsoupLoader {

    public static final String TAG = JsoupLoader.class.getSimpleName();

    public static void loadDaily() {
        LoadData loadData = new LoadData();
        loadData.execute();
    }

    private static class LoadData extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {

                Document document = Jsoup.connect(MsConst.HELLO_CHAO_THU_THACH_TRONG_NGAY).get();
                Elements items = document.getElementsByAttributeValue("class", "box shadow light callout");
                Elements data = items.get(0).getElementsByClass("raw-menu");

                for (Element e : data.get(0).getAllElements()) {
                    if (e.nodeName().equals("li")) {
                        String link = e.getElementsByAttribute("href").attr("href");
                        String textEng = e.getAllElements().get(1).text();
                        String textVi = e.getAllElements().get(0).text().replace(textEng, "");
                        Logger.debug(TAG, ">>>" + "link:" + link + "  ;textVi:" + textVi + ";textEng:" + textEng);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }
    }


}
