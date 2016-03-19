package com.hackbulgaria.onebeerplease.smartalarm;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by boyko on 3/18/16.
 */
public class FileManager {

    public  static void writeToFile(Context context,Set<String> userData) {

       FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(new File(context.getFilesDir(),"userData.txt"));
            for (String data : userData) {
                try {
                    fileWriter.write(data + ", ");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }
    public static Set<String> readFile(File file){

        FileInputStream fin = null;
        String ret = null;
        try {
            fin = new FileInputStream(file);
             ret = convertStreamToString(fin);
            fin.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new HashSet<String>(Arrays.asList(ret.split(", ")));
    }
}
