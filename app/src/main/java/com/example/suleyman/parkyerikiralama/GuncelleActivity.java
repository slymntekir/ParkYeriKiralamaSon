package com.example.suleyman.parkyerikiralama;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class GuncelleActivity extends AppCompatActivity {
    EditText edtGuncelle_tc,edtGuncelle_ad,edtGuncelle_soyad,
            edtGuncelle_email,edtGuncelle_adres,edtGuncelle_kullaniciAdi,
            edtGuncelle_sifre,edtGuncelle_sifre2,edtGuncelle_plaka,edtGuncelle_telNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guncelle);

        tanimlamalar();
    }

    private void tanimlamalar() {
        edtGuncelle_tc = (EditText)findViewById(R.id.edtGuncelle_tc);
        edtGuncelle_ad = (EditText)findViewById(R.id.edtGuncelle_ad);
        edtGuncelle_soyad = (EditText) findViewById(R.id.edtGuncelle_soyad);
        edtGuncelle_email = (EditText)findViewById(R.id.edtGuncelle_email);
        edtGuncelle_adres = (EditText) findViewById(R.id.edtGuncelle_adres);
        edtGuncelle_kullaniciAdi = (EditText)findViewById(R.id.edtGuncelle_kullaniciAdi);
        edtGuncelle_sifre = (EditText)findViewById(R.id.edtGuncelle_sifre);
        edtGuncelle_sifre2 = (EditText) findViewById(R.id.edtGuncelle_sifre2);
        edtGuncelle_plaka = (EditText) findViewById(R.id.edtGuncelle_plaka);
        edtGuncelle_telNo = (EditText)findViewById(R.id.edtGuncelle_telNo);
    }

    public void guncelle(View view) {
        // atamalar
        String tcNo = edtGuncelle_tc.getText().toString();
        String ad = edtGuncelle_ad.getText().toString();
        String soyad = edtGuncelle_soyad.getText().toString();
        String email = edtGuncelle_email.getText().toString();
        String adres = edtGuncelle_adres.getText().toString();
        String kullaniciAdi = edtGuncelle_kullaniciAdi.getText().toString();
        String sifre = edtGuncelle_sifre.getText().toString();
        String sifre2 = edtGuncelle_sifre2.getText().toString();
        String plaka = edtGuncelle_plaka.getText().toString();
        String telNo = edtGuncelle_telNo.getText().toString();

        //kayıt olunması için tüm alanların dolu olması gerekli
        if (!tcNo.isEmpty() &&
                !ad.isEmpty() &&
                !soyad.isEmpty() &&
                !email.isEmpty() &&
                !adres.isEmpty() &&
                !kullaniciAdi.isEmpty() &&
                !sifre.isEmpty() &&
                !sifre2.isEmpty() &&
                !plaka.isEmpty() &&
                !telNo.isEmpty()
                )
        {
            SharedPreferences preferences = getSharedPreferences(Variables.giris_pref,MODE_PRIVATE);

            if(!sifre.equals(sifre2))
                Toast.makeText(getApplicationContext(), "Lütfen şifreleri kontrol ediniz", Toast.LENGTH_SHORT).show();
            else
            {
                String guncelle_url = Variables.http+Variables.kullaniciGuncelle+"?kullanici_id="+preferences.getInt(Variables.KULLANICI_ID,-1)
                        +"&tc_no="+tcNo+"&adi="+ad+"&soyadi="+soyad+"&arac_plaka="+plaka+"&kul_adi="+kullaniciAdi
                        +"&sifre="+sifre+"&email="+email+"&tel_no="+telNo+"&adres="+telNo;
                String sonuc = "";
                try {
                    sonuc = new ExtractData().execute(guncelle_url).get();
                }catch (Exception e){
                    sonuc = "hayir";
                }
                sonuc = sonuc.trim();

                if(sonuc.equals("tamam")) {
                    Toast.makeText(getApplicationContext(),"Guncelleme İşlemi Başarılı\n"+
                            "Lütfen Bekleyin",Toast.LENGTH_LONG).show();

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
                else if(sonuc.isEmpty() || sonuc.contains("ayi") || !sonuc.equals("tamam"))
                    Toast.makeText(getApplicationContext(),"Guncelleme İşlemi Olurken Hata Oldu "+sonuc,Toast.LENGTH_LONG).show();
            }
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
}
