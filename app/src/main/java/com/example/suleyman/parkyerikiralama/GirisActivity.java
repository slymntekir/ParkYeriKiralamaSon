package com.example.suleyman.parkyerikiralama;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


// projenin giriş ekranı
public class GirisActivity extends AppCompatActivity
{
    EditText kullaniciAdi,sifre;
    // web servisin url'si
    String URL = Variables.http+Variables.sorguUrl;

    // mevcut kullanıcıya ait bilgilerin tutulması için tanımlanan
    // paylaşım kaynağı
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris);

        // id eşleştirmeleri
        kullaniciAdi = (EditText) findViewById(R.id.edtGiris_kullaniciAdi);
        sifre = (EditText)findViewById(R.id.edtGiris_sifre);

        // preferences tanımının referansa bağlanması
        preferences = getSharedPreferences(Variables.giris_pref,MODE_PRIVATE);
        editor = preferences.edit();
    }

    // giriş yapılacak butonun metodu
    public void girisYap(View view)
    {
        // kullanıcı adı ve şifre edittext'ten çekiliyor.
        String kul_adi = kullaniciAdi.getText().toString();
        String sifr = sifre.getText().toString();

        // url'e değiskenler gönderiliyor.
        String url = URL +"?kullanici_adi="+kul_adi+"&sifre="+sifr;
        String sonuc = "";

        // asynctask sınıfına bağlanılıyor.
        // okunan sonuç,sonuc değişkenine aktarılıyor.
        try {
            sonuc = new ExtractData().execute(url).get();
        }catch(Exception e) {
            sonuc = "-1";
        }

        // sonuc değişkeni -1 değilse öyle bir kullanıcı var demektir.
        if(!sonuc.trim().equals("-1"))
        {
            editor.clear();
            editor.commit();

            int id = Integer.parseInt(sonuc.trim());

            // kullanıcıya ait bilgiler paylaşım kaynaklarına
            // kaydediliyor sonra lazım olacak
            editor.putInt(Variables.KULLANICI_ID,id);
            editor.putString("kullanici_adi",kul_adi);
            editor.putString("sifre", sifr);
            editor.commit();

            /*
            StringBuilder sb = new StringBuilder();
            sb.append("kullanici_id").append(" : ").append(preferences.getInt("kullanici_id", -1));
            sb.append("\n").append("kullanici_adi").append(" : ").append(preferences.getString("kullanici_adi", ""));
            sb.append("\n").append("sifre").append(" : ").append(preferences.getString("sifre", "")).append("\n------\n");

            Toast.makeText(getApplicationContext(), sb.toString(), Toast.LENGTH_LONG).show();
            */

            Toast.makeText(getApplicationContext(),"Lütfen Bekleyin..\nYönlendiriliyorsunuz..",Toast.LENGTH_SHORT).show();

            new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(2000);
                }
                catch(Exception e){}
                finally {
                    Intent ıntent = new Intent(getApplicationContext(),SecimActivity.class);
                    startActivity(ıntent);
                }
            }}).start();
        }

        // eğer sonuç -1 ise öyle bir kullanıcı yok demektir.
        else if(sonuc.trim().equals("-1")) {
            Toast.makeText(getApplicationContext(), "giriş bilgileri yanlış...", Toast.LENGTH_SHORT).show();
        }
    }

    // kullanıcı ilk defa uygulamaya girdiyse kayıt olması için
    // bu method yönlendiriyor.
    public void kayıtOl(View view) {
        //startActivity(new Intent(getApplicationContext(),Anasayfa.class));
        Intent ıntent = new Intent(GirisActivity.this, KayitActivity.class);
        startActivity(ıntent);
    }
}