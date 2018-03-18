package com.example.android.quakereport;

import android.text.TextUtils;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static com.example.android.quakereport.EarthquakeActivity.LOG_TAG;

public final class QueryUtils {
    private QueryUtils() {
    }
    private static URL createUrl(String web){
        URL url = null;
        try {
            url=new URL(web);
        }catch (MalformedURLException e){
            e.printStackTrace();
        }
        return url;
    }

    public static List<Earthquake> extractEarthquakes(String urlStr) throws IOException {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<Earthquake> earthquakes = new ArrayList<Earthquake>();
        URL url = createUrl(urlStr);
        String s = null;
        try {
            s = makeHttpRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(s)){
            return null;
        }
        try {
                JSONObject reader = new JSONObject(s);
                JSONArray arr = reader.getJSONArray("features");
                for (int i=0;i<arr.length();i++){
                    JSONObject obj = arr.getJSONObject(i);
                    JSONObject a = obj.getJSONObject("properties");
                    double mag = a.getDouble("mag");
                    String place =a.getString("place");
                    long time =a.getLong("time");
                    String webUrl = a.getString("url");
                    earthquakes.add(new Earthquake(mag,place,time,webUrl));
                }

        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }
        return earthquakes;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String JsonResponse = "";
        HttpURLConnection httpURLConnection=null;
        InputStream inputStream=null;
        if(url==null){
            return JsonResponse;
        }
        try {
            httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setConnectTimeout(8000);
            httpURLConnection.setReadTimeout(5000);
            httpURLConnection.connect();
            if(httpURLConnection.getResponseCode()==200){
                inputStream=httpURLConnection.getInputStream();
                JsonResponse = extractJsonResponse(inputStream);
            }else{
                Log.e(LOG_TAG,"response code"+httpURLConnection.getResponseCode());
            }
        }catch (IOException e){
            Log.e(LOG_TAG,"IO exception occured");
        }finally {
            if(httpURLConnection!=null){
                httpURLConnection.disconnect();
            }
            if (inputStream!=null){
                inputStream.close();
            }
        }
        return JsonResponse;
    }


    private static String extractJsonResponse(InputStream inputStream)throws IOException{
        StringBuilder stringBuilder = new StringBuilder();
        if(inputStream!=null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("utf-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while (line!=null){
                stringBuilder.append(line);
                line = bufferedReader.readLine();
            }
        }
        return stringBuilder.toString();
    }
}