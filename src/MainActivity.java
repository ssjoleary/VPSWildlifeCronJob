import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;


import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.io.InputStream;


public class MainActivity {

    public static void main(String[] args) {
        System.out.println("Debug: Starting...");
        RssSightingAsyncTask myTask = new RssSightingAsyncTask();
        myTask.doInBackground("http://www.iwdg.ie/_customphp/iscope/rss_sightings.php");
    }

    private static class RssSightingAsyncTask  {
        RSSParser rssParser = new RSSParser();
        List<RSSItem> rssItems = new ArrayList<RSSItem>();
        List<Sighting> mySightings = new ArrayList<Sighting>();


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

                            Sighting sighting = new Sighting(Integer.parseInt(id.ownText()), speciesText.ownText(), date.ownText(), latText.ownText(), lngText.ownText(), location.ownText(), animals.ownText(), nametwo.ownText());
                            mySightings.add(sighting);
                            //wildlifeDB.insertInfoRssSighting(sighting);
                            System.out.println("Debug: " + "Sighting added: species = null");
                            System.out.println("Species: " + speciesText.ownText());
                            System.out.println("Lat, Lng: " + latText.ownText() + ", " + lngText.ownText());
                            System.out.println("Location: " + location.ownText());
                            System.out.println("----------------");
                        } else {
                            Sighting sighting = new Sighting(Integer.parseInt(id.ownText()), species.ownText(), date.ownText(), lat.ownText(), lng.ownText(), location.ownText(), animals.ownText(), name.ownText());
                            mySightings.add(sighting);
                            //wildlifeDB.insertInfoRssSighting(sighting);
                            System.out.println("Debug: " + "Sighting added: species = null");
                            System.out.println("Species: " + species.ownText());
                            System.out.println("Lat, Lng: " + lat.ownText() + ", " + lng.ownText());
                            System.out.println("Location: " + location.ownText());
                            System.out.println("----------------");
                            System.out.println("Debug: " + "Sighting added: species != null");

                        }
                            for(Sighting subSighting: mySightings) {
                                showSuccess(POST("http://fyp-irish-wildlife.herokuapp.com/sightings/postsighting/", subSighting));
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
        public static String POST(String url, Sighting sightingSubmit){
            InputStream inputStream;
            String result = "";
            try {

                // 1. create HttpClient
                HttpClient httpclient = new DefaultHttpClient();

                // 2. make POST request to the given URL
                HttpPost httpPost = new HttpPost(url);

                String json;

                // 3. build jsonObject
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("species", sightingSubmit.getSpecies());
                jsonObject.put("date", sightingSubmit.getDate());
                jsonObject.put("lat", sightingSubmit.getSightingLat());
                jsonObject.put("lng", sightingSubmit.getSightingLong());
                jsonObject.put("location", sightingSubmit.getLocation());
                jsonObject.put("animals", sightingSubmit.getAnimals());
                jsonObject.put("name", sightingSubmit.getName());
                jsonObject.put("imageurl", sightingSubmit.getImgUrlString());

                // 4. convert JSONObject to JSON to String
                json = jsonObject.toString();
                // ** Alternative way to convert Person object to JSON string usin Jackson Lib
                // ObjectMapper mapper = new ObjectMapper();
                // json = mapper.writeValueAsString(searchActivity);

                // 5. set json to StringEntity
                StringEntity se = new StringEntity(json);

                // 6. set httpPost Entity
                httpPost.setEntity(se);

                // 7. Set some headers to inform server about the type of the content
                httpPost.setHeader("Accept", "application/json");
                httpPost.setHeader("Content-type", "application/json");

                // 8. Execute POST request to the given URL
                HttpResponse httpResponse = httpclient.execute(httpPost);

                // 9. receive response as inputStream
                inputStream = httpResponse.getEntity().getContent();

                // 10. convert inputstream to string
                if(inputStream != null) {
                    System.out.println("Debug: SubmitActivity: POST: inputStream != null");
                    result = convertInputStreamToString(inputStream);
                }
                else {
                    System.out.println("Debug: SubmitActivity: POST: inputStream = null: did not work");
                    result = "Did not work!";
                }

            } catch (Exception e) {
                System.out.println("Debug: InputStream" + e.getLocalizedMessage());
            }

            // 11. return result
            return result;
        }

        private static String convertInputStreamToString(InputStream inputStream) throws IOException {
            BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
            String line;
            StringBuilder result = new StringBuilder();
            while((line = bufferedReader.readLine()) != null) {
                result.append(line);
            }

            inputStream.close();
            System.out.println("Debug: SubmitActivity: convertInputStreamToString: result.To.String(): " + result.toString());

            return result.toString();

        }

        private void showSuccess(String result) {
            //generateNoteOnSD("ErrorHtml", result);
            Object obj = JSONValue.parse(result);
            JSONArray json;
            try {
                json = (JSONArray)obj;
                for (int i = 0; i < json.size(); i++) {
                    JSONObject c;
                    try {
                        c = (JSONObject)json.get(i);

                        //String success = c.get("success");
                        System.out.println("Debug: isSuccessful: " + c.get(i));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
