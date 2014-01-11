package com.example.likeRSS.forNet;

import android.os.AsyncTask;
import android.util.Log;
import com.example.likeRSS.RssSingleItem;
import com.example.likeRSS.activities.RSSListActivity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MyAsyncTask extends AsyncTask<String, String, ArrayList<RssSingleItem>> {

    public ArrayList<RssSingleItem> rssArrayList = null;

    @Override
    protected ArrayList<RssSingleItem> doInBackground(String... arg) {
        RssSingleItem elementRss = new RssSingleItem();

        rssArrayList = new ArrayList<RssSingleItem>();
        HttpResponse localHttpResponse;
        try {
            localHttpResponse = new DefaultHttpClient().execute(new HttpGet("http://newsru.com/plain/rss/txt_all.xml"));

            Log.d(RSSListActivity.LOG_ID, "in responce local connect");
            if (localHttpResponse.getStatusLine().getStatusCode() == 200) {
                try {
                    Log.d(RSSListActivity.LOG_ID, "in responce local connect");
                    URL url = new URL(arg[0]);
                    HttpURLConnection urlConnection = null;
                    urlConnection = (HttpURLConnection) url.openConnection();

                    urlConnection.connect();

                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder builder = factory.newDocumentBuilder();
                    Document doc = builder.parse(urlConnection.getInputStream());


                    NodeList nodes = doc.getElementsByTagName("item");
                    for (int i = 0; i < nodes.getLength(); i++) {
                        Element element = (Element) nodes.item(i);
                        NodeList title = element.getElementsByTagName("title");
                        NodeList category = element.getElementsByTagName("category");
                        NodeList description = element.getElementsByTagName("description");


                        Element eTitle = (Element) title.item(0);
                        Element eDescription = (Element) description.item(0);
                        Element eCategory = (Element) category.item(0);


                        String STitle = eTitle.getTextContent();
                        String SCategory = eCategory.getTextContent();
                        String SDescription = eDescription.getTextContent();


                        elementRss = new RssSingleItem(STitle, SDescription, SCategory);
                        rssArrayList.add(elementRss);
                        // RSSListNewsFragment.rssItems.add(elementRss);
                    }
                    Log.d(RSSListActivity.LOG_ID, "in try code =200 ");
                } catch (MalformedURLException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (SAXException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            } else rssArrayList = null;

            Log.d(RSSListActivity.LOG_ID, "in responce");
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            Log.d(RSSListActivity.LOG_ID, "in exception");
        }

        Log.d(RSSListActivity.LOG_ID, "in return ArrayList");

        return rssArrayList;
    }

    @Override
    protected void onPostExecute(ArrayList<RssSingleItem> s) {

    }
}
