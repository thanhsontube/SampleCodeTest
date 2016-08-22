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
                Elements data = items.get(0).getElementsByAttributeValue("class", "raw-menu");
                Logger.debug(TAG, ">>>" + "loadDaily:" + data.size());
                for (Element e : data.get(0).getAllElements()) {
                    String link = e.getElementsByAttribute("href").attr("href");
                    Logger.debug(TAG, ">>>" + "e:" + link);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }
    }


}
