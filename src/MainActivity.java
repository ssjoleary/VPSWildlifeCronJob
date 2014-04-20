import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity {

    public static void main(String[] args) {
        System.out.println("Debug: Starting...");
        RssSightingAsyncTask myTask = new RssSightingAsyncTask();
        myTask.doInBackground("http://www.iwdg.ie/_customphp/iscope/rss_sightings.php");
    }

    private static class RssSightingAsyncTask  {
        RSSParser rssParser = new RSSParser();
        List<RSSItem> rssItems = new ArrayList<RSSItem>();

        protected String doInBackground(String... urls) {
            String url = urls[0];
            // list of rss items
            rssItems = rssParser.getRSSFeedItems(url);

            Document doc;//Jsoup.parse(rssItems.get(0).getLink());
            try {
                for(RSSItem rssItem: rssItems) {
                    doc = Jsoup.connect(rssItem.getLink())
                            .userAgent("Mozilla/5.0 Gecko/20100101 Firefox/21.0")
                            .timeout(0)
                            .get();

                    Element id = doc.select("html body#bd.fs3 div#ja-wrapper div#ja-containerwrap-fr div#ja-containerwrap2 div#ja-container div#ja-container2.clearfix div#ja-mainbody-fr.clearfix div#ja-contentwrap div#ja-content div#k2Container.itemView div.itemBody div.itemFullText div table.table1 tbody tr td").get(0);
                    System.out.println("Debug: " + id.ownText());

                    if(!checkSightingExists(id.ownText())){
                        Element name = doc.select("html body#bd.fs3 div#ja-wrapper div#ja-containerwrap-fr div#ja-containerwrap2 div#ja-container div#ja-container2.clearfix div#ja-mainbody-fr.clearfix div#ja-contentwrap div#ja-content div#k2Container.itemView div.itemBody div.itemFullText div table.table1 tbody tr td").get(30);
                        System.out.println("Debug: " + name.ownText());

                        Element species = doc.select("html body#bd.fs3 div#ja-wrapper div#ja-containerwrap-fr div#ja-containerwrap2 div#ja-container div#ja-container2.clearfix div#ja-mainbody-fr.clearfix div#ja-contentwrap div#ja-content div#k2Container.itemView div.itemBody div.itemFullText div table.table1 tbody tr td a").first();
                        Element date = doc.select("html body#bd.fs3 div#ja-wrapper div#ja-containerwrap-fr div#ja-containerwrap2 div#ja-container div#ja-container2.clearfix div#ja-mainbody-fr.clearfix div#ja-contentwrap div#ja-content div#k2Container.itemView div.itemBody div.itemFullText div table.table1 tbody tr td").get(5);
                        Element lat = doc.select("html body#bd.fs3 div#ja-wrapper div#ja-containerwrap-fr div#ja-containerwrap2 div#ja-container div#ja-container2.clearfix div#ja-mainbody-fr.clearfix div#ja-contentwrap div#ja-content div#k2Container.itemView div.itemBody div.itemFullText div table.table1 tbody tr td").get(18);
                        Element lng = doc.select("html body#bd.fs3 div#ja-wrapper div#ja-containerwrap-fr div#ja-containerwrap2 div#ja-container div#ja-container2.clearfix div#ja-mainbody-fr.clearfix div#ja-contentwrap div#ja-content div#k2Container.itemView div.itemBody div.itemFullText div table.table1 tbody tr td").get(19);
                        Element location = doc.select("html body#bd.fs3 div#ja-wrapper div#ja-containerwrap-fr div#ja-containerwrap2 div#ja-container div#ja-container2.clearfix div#ja-mainbody-fr.clearfix div#ja-contentwrap div#ja-content div#k2Container.itemView div.itemBody div.itemFullText div table.table1 tbody tr td").get(3);
                        Element animals = doc.select("html body#bd.fs3 div#ja-wrapper div#ja-containerwrap-fr div#ja-containerwrap2 div#ja-container div#ja-container2.clearfix div#ja-mainbody-fr.clearfix div#ja-contentwrap div#ja-content div#k2Container.itemView div.itemBody div.itemFullText div table.table1 tbody tr td").get(7);
                        if(species == null) {
                            Element nametwo = doc.select("html body#bd.fs3 div#ja-wrapper div#ja-containerwrap-fr div#ja-containerwrap2 div#ja-container div#ja-container2.clearfix div#ja-mainbody-fr.clearfix div#ja-contentwrap div#ja-content div#k2Container.itemView div.itemBody div.itemFullText div table.table1 tbody tr td").get(29);
                            System.out.println("Debug: " + nametwo.ownText());

                            Element speciesText = doc.select("html body#bd.fs3 div#ja-wrapper div#ja-containerwrap-fr div#ja-containerwrap2 div#ja-container div#ja-container2.clearfix div#ja-mainbody-fr.clearfix div#ja-contentwrap div#ja-content div#k2Container.itemView div.itemBody div.itemFullText div table.table1 tbody tr td").get(2);
                            Element latText = doc.select("html body#bd.fs3 div#ja-wrapper div#ja-containerwrap-fr div#ja-containerwrap2 div#ja-container div#ja-container2.clearfix div#ja-mainbody-fr.clearfix div#ja-contentwrap div#ja-content div#k2Container.itemView div.itemBody div.itemFullText div table.table1 tbody tr td").get(16);
                            Element lngText = doc.select("html body#bd.fs3 div#ja-wrapper div#ja-containerwrap-fr div#ja-containerwrap2 div#ja-container div#ja-container2.clearfix div#ja-mainbody-fr.clearfix div#ja-contentwrap div#ja-content div#k2Container.itemView div.itemBody div.itemFullText div table.table1 tbody tr td").get(17);

                            //Sighting sighting = new Sighting(Integer.parseInt(id.ownText()), speciesText.ownText(), date.ownText(), latText.ownText(), lngText.ownText(), location.ownText(), animals.ownText(), nametwo.ownText());
                            //wildlifeDB.insertInfoRssSighting(sighting);
                            System.out.println("Debug: " + "Sighting added: species = null");
                            System.out.println("Species: " + speciesText.ownText());
                            System.out.println("Lat, Lng: " + latText.ownText() + ", " + lngText.ownText());
                            System.out.println("Location: " + location.ownText());
                            System.out.println("----------------");
                        } else {
                            //Sighting sighting = new Sighting(Integer.parseInt(id.ownText()), species.ownText(), date.ownText(), lat.ownText(), lng.ownText(), location.ownText(), animals.ownText(), name.ownText());
                            //wildlifeDB.insertInfoRssSighting(sighting);
                            System.out.println("Debug: " + "Sighting added: species = null");
                            System.out.println("Species: " + species.ownText());
                            System.out.println("Lat, Lng: " + lat.ownText() + ", " + lng.ownText());
                            System.out.println("Location: " + location.ownText());
                            System.out.println("----------------");
                            System.out.println("Debug: " + "Sighting added: species != null");

                        }
                    } else {
                        System.out.println("Debug: " + "Sighting not added");
                        return null;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        private boolean checkSightingExists(String s) {

            return false;
        }
    }
}
