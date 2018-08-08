package com.example.suleyman.parkyerikiralama;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by suleyman on 8.3.2017.
 */


// bu sınıfın görevi gelen url'e yardımcı thread üzerinden
// bağlanıp sonucu döndürmek
// yani asynctask yapısı
public class ExtractData extends AsyncTask<String,Void,String>
{
    @Override
    public String doInBackground(String... adres)
    {
        String response = "";
        // bağlantının yapılması için httpUrlConnection sınıfından örnek
        // tanımlanıyor.
        HttpURLConnection httpURLConnection = null;
        try
        {
            // parametre olarak gelen url
            // URL sınıfına aktarılıyor.
            URL url = new URL(adres[0]);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            // bağlantı başarılı olduğu durum
            if(httpURLConnection.getResponseCode()==HttpURLConnection.HTTP_OK)
            {
                // verilerin çekilmesi süreci
                InputStream in = new BufferedInputStream(httpURLConnection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder sb = new StringBuilder();

                String line = "";
                try
                {
                    while((line = reader.readLine()) != null) {
                        sb.append(line).append('\n');
                    }
                    in.close();
                }catch (IOException e){
                }
                response = sb.toString();
            }
        }catch (Exception e)
        {
            Log.d("Async",e.getMessage());
        }
        // onPostExecute methoduna gönderilen String değeri
        return response;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}