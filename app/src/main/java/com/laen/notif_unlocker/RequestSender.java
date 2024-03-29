package com.laen.notif_unlocker;


import android.content.Context;
import android.media.AudioManager;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
public class RequestSender {
    public String token;
    public String url;
    public RequestSender(String token, String uri){
        this.token = token;
        this.url = uri;
    }
    public String CreateFile(String name) throws IOException {
        String toReturn = "";
        URL murl = new URL(url);
        HttpURLConnection client = (HttpURLConnection) murl.openConnection();
        client.setRequestMethod("POST");
        client.setRequestProperty("Authorization", "Bearer " + token);
        client.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        client.setRequestProperty("Accept", "application/json; charset=UTF-8");
        client.setDoOutput(true);
        client.setDoInput(true);
        String toSend = "{\"name\":\""+name+"\"}";
        Log.i("", "CreateFile: URL CREATED" + murl);
        try {
            client.getOutputStream().write(toSend.getBytes(StandardCharsets.UTF_8), 0, toSend.getBytes(StandardCharsets.UTF_8).length);
        }catch(Exception e){
            Log.e("TAG", "CreateFile: ", e);
        }
        Log.i("TAG", "CreateFile: " + client.getResponseCode() + toSend);
        BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));

        String responseLine;
        while ((responseLine = reader.readLine()) != null)
        {
            String[] splitted = responseLine.split("\"");
            if (splitted.length > 1 && splitted[1].equals("id")) {
                Log.i(name, "CreateFile: " + splitted[3]);
                toReturn = splitted[3];
            }
        }
        return toReturn;
    }
    public static String getWholeData(InputStream stream) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader ISR = new InputStreamReader(stream);
        int c;
        while ((c = ISR.read()) != -1){
            sb.append((char)c);
        }
        ISR.close();
        stream.close();
        return sb.toString();
    }
    public String getFileData(String name) throws IOException{
        String toReturn = "";
        URL murl = new URL(url + name + "?alt=media");
        HttpURLConnection client = (HttpURLConnection) murl.openConnection();
        client.setRequestMethod("GET");
        client.setRequestProperty("Authorization", "Bearer " + token);
        client.setDoInput(true);

        int responseCode = client.getResponseCode();
        String dataToRead = getWholeData(client.getInputStream());
        Log.i("", "read file " + murl + " data = " + dataToRead);
        return dataToRead;
    }
}
