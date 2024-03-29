package com.laen.notif_unlocker.storage;

import android.content.Context;

import com.laen.notif_unlocker.MainActivity;
import com.laen.notif_unlocker.R;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class AppData {
    public static void storeData(String googleFileId, String notif_data, Context con) throws IOException {
        File file_dir = con.getFilesDir();
        File storage = new File(file_dir, MainActivity.mMainActivity.res.getString(R.string.storage_filename));
        if (!storage.exists()){
            storage.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(storage);
        String dataToWrite = googleFileId + ";" + notif_data;
        byte[] data = dataToWrite.getBytes(StandardCharsets.UTF_8);
        fos.write(data);
        fos.close();
    }
    public static void storeData(String googleFileId, Context con) throws IOException {
        storeData(googleFileId, "", con);
    }
    public static String[] getStoredData(Context con) throws IOException {
        File file_dir = con.getFilesDir();
        File storage = new File(file_dir, MainActivity.mMainActivity.res.getString(R.string.storage_filename));
        String[] toReturn = new String[2];
        if (!storage.exists())
        {
            throw new IOException("file not created");
        }
        FileInputStream fis = new FileInputStream(storage);
        int availibleSize = fis.available();
        byte[] data = new byte[availibleSize];
        fis.read(data);
        String fullData = new String(data, StandardCharsets.UTF_8);
        StringBuilder googleFileId = new StringBuilder();
        StringBuilder notifData = new StringBuilder();
        boolean hasFoundSemi = false;
        for (int i = 0; i < fullData.length(); i++){
            if (!hasFoundSemi)
            {
                if (fullData.charAt(i) == ';')
                {
                    hasFoundSemi = true;
                    continue;
                }
                googleFileId.append(fullData.charAt(i));
            }
            else
            {
                notifData.append(fullData.charAt(i));
            }
        }
        toReturn[0] = googleFileId.toString();
        toReturn[1] = notifData.toString();
        fis.close();
        return toReturn;
    }
}
