package com.example.suleyman.parkyerikiralama;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

// park yeri aramak için veya parkkontor yüklemek için
// kullanılacak aktivity
public class SecimActivity extends AppCompatActivity {
    TextView tv_bakiye;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secim);

        tv_bakiye = (TextView) findViewById(R.id.tv_bakiye);

        SharedPreferences preferences = getSharedPreferences(Variables.giris_pref,MODE_PRIVATE);
        int id = preferences.getInt(Variables.KULLANICI_ID,-1);
        String url = Variables.http+Variables.bakiyeSorgu+"?id="+id;
        try {
            tv_bakiye.setText("Güncel Bakiyeniz : "+new ExtractData().execute(url).get().trim());
        }catch (Exception e){}
    }

    // tl yüklenmesi için uygun sayfaya yönlendiren method
    public void tlYukle(View view) {
        Toast.makeText(this,"Lütfen bekleyin\nSayfaya yönlendiriliyorsunuz...",Toast.LENGTH_SHORT).show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try
                {
                    Thread.sleep(2000);
                }catch (Exception e){
                }
                finally {
                    Intent ı = new Intent(SecimActivity.this,TlYukle.class);
                    startActivity(ı);
                }
            }
        }).start();
    }

    // park yeri aramak için kullanılan method
    public void parkYeriAra(View view) {
        try {
            //kullanıcının bakiyesi yeterli mi yoksa değil mi ona bakılıyor.
            SharedPreferences preferences = getSharedPreferences(Variables.giris_pref,MODE_PRIVATE);
            int id = preferences.getInt(Variables.KULLANICI_ID,-1);

            // sorgu web servise gönderiliyor.
            String url = null;
            if(id != -1)
                url = Variables.http+Variables.bakiyeSorgu+"?id="+id;

            // sonuç çekiliyor.
            String sonuc;
            try {
                sonuc = new ExtractData().execute(url).get();
            }catch(Exception e){
                sonuc = "-2";
            }
            sonuc = sonuc.trim();

            //  bakiye değişkene aktarılıyor.
            // sonra kontrol ediliyor
            int bakiye = Integer.parseInt(sonuc);
            boolean durum = !sonuc.trim().equals("-2") && !sonuc.trim().equals("-1");

            // bakiye sıfırsa uyarı veriliyor.
            if(durum && sonuc.trim().equals("0"))
            {
                Toast.makeText(getApplicationContext(),"Lütfen Bakiyenizi\nKontrol Ediniz...",Toast.LENGTH_LONG).show();
            }
            // bakiye varsa arama kısmına yönlendiriliyor.
            else if(bakiye > 0)
            {
                Intent ıntent = new Intent(getApplicationContext(),FiltreliAra.class);
                startActivity(ıntent);
            }
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id==R.id.action_cikis)
        {
            SharedPreferences preferences = getSharedPreferences(Variables.giris_pref,MODE_PRIVATE);
            SharedPreferences preferences1 = getSharedPreferences(Variables.common_pref,MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            SharedPreferences.Editor editor1 = preferences1.edit();

            editor.clear();
            editor.commit();
            editor1.clear();
            editor1.commit();

            Toast.makeText(getApplicationContext(),"Çıkış Yapılıyor.",Toast.LENGTH_LONG).show();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(2000);
                    }catch (Exception e){}
                }
            }).start();

            Intent ı = new Intent(getApplicationContext(),GirisActivity.class);
            startActivity(ı);
        }
        return true;
    }

    public void kiralanmisYerleriGoster(View view) {
        try {
            startActivity(new Intent(getApplicationContext(),KiralanmisYerler.class));
        }catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    public void bilgileriGuncelle(View view) {
        Intent ı = new Intent(getApplicationContext(),GuncelleActivity.class);
        startActivity(ı);
    }
}
