package com.example.suleyman.parkyerikiralama;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class KayitActivity extends AppCompatActivity {
    // tanımlamalar
    EditText edt_tcno, edt_ad, edt_soyad, edt_email, edt_adres, edt_kullanicAdi, edt_sifre, edt_sifre2,edt_plaka,edt_telno;
    // web servisin kayıt url'si
    String KAYIT_URL = Variables.http+Variables.kayıtUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kayit);

        // tanımlamaların eşleştirilmesi
        edt_tcno = (EditText) findViewById(R.id.edtKayit_tc);
        edt_ad = (EditText) findViewById(R.id.edtKayit_ad);
        edt_soyad = (EditText) findViewById(R.id.edtKayit_soyad);
        edt_email = (EditText) findViewById(R.id.edtKayit_email);
        edt_adres = (EditText) findViewById(R.id.edtKayit_adres);
        edt_kullanicAdi = (EditText) findViewById(R.id.edtKayit_kullaniciAdi);
        edt_sifre = (EditText) findViewById(R.id.edtKayit_sifre);
        edt_sifre2 = (EditText) findViewById(R.id.edtKayit_sifre2);
        edt_plaka = (EditText) findViewById(R.id.edtKayit_plaka);
        edt_telno = (EditText) findViewById(R.id.edtKayit_telNo);
    }

    public void kaydet(View view) {
        // atamalar
        String tcNo = edt_tcno.getText().toString();
        String ad = edt_ad.getText().toString();
        String soyad = edt_soyad.getText().toString();
        String email = edt_email.getText().toString();
        String adres = edt_adres.getText().toString();
        String kullaniciAdi = edt_kullanicAdi.getText().toString();
        String sifre = edt_sifre.getText().toString();
        String sifre2 = edt_sifre2.getText().toString();
        String plaka = edt_plaka.getText().toString();
        String telNo = edt_telno.getText().toString();

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
            // plaka sorgusu yapılıyor var mı yok mu ?
            String url = Variables.http+Variables.plakaSorgu + "?plaka="+plaka;
            String plakaSonuc = null;
            try {
                plakaSonuc = new ExtractData().execute(url).get();
            }catch (Exception e) {}

            // boşluklar kesiliyor.
            plakaSonuc = plakaSonuc.trim();

            // şifre kontrolü yapılıyor
            if(!edt_sifre.getText().toString().equals(edt_sifre2.getText().toString()))
                Toast.makeText(getApplicationContext(), "Lütfen şifreleri kontrol ediniz", Toast.LENGTH_SHORT).show();

            // plaka yanlışsa yani varsa uyarı veriliyor.
            // yani daha önceden başka bir kullanıcı kaydetmiş ise
            if(plakaSonuc.equals("true"))
                Toast.makeText(getApplicationContext(),"Lütfen plakayı kontrol ediniz...",Toast.LENGTH_LONG).show();

            // plaka doğru ise yani eşsiz ise kayıt yapılıyor.
            else if(plakaSonuc.equals("false"))
            {
                //değerler restful web servise gönderiliyor.
                String kayit_url = KAYIT_URL +
                        "?tc_no=" + tcNo + "&ad=" + ad +
                        "&soyad=" + soyad + "&e_mail=" + email +
                        "&adres=" + adres + "&kullanici_adi=" + kullaniciAdi +
                        "&sifre=" + sifre + "&arac_plaka=" + plaka +
                        "&tel_no=" + telNo;

                // sonuc tanımlanıyor ve asynctask'a bağlanılıp sonuc alınıyor.
                String sonuc = "";
                try {
                    sonuc =new ExtractData().execute(kayit_url).get();
                }catch (Exception e) {
                    sonuc = "olumsuz";
                }

                sonuc = sonuc.trim();
                // kayıt başarılı ise servis tamam yanıtını veriyor
                if(sonuc.contains("tamam"))
                {
                    // ve giriş sayfasına yönlendiriliyor.
                    Toast.makeText(getApplicationContext(),"Başarıyla kayıt oldunuz.." +
                            "\nlütfen bekleyin..",Toast.LENGTH_LONG).show();

                    // giriş sayfasına yönlendiriliyor.
                    new Thread(new Runnable()
                    {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(3000);
                            }catch (Exception e) {}
                            finally {
                                Intent ıntent = new Intent(getApplicationContext(),GirisActivity.class);
                                startActivity(ıntent);
                            }
                        }
                    }).start();
                }
                else
                {
                    // bilgilerde yanlışlık varsa uyarı veriliyor
                    Toast.makeText(getApplicationContext(),"lütfen bilgileri kontrol ediniz..."+sonuc,Toast.LENGTH_LONG).show();
                }

            }
        }

        // alanlar eksik dolduruldu ise uyarı veriliyor.
        else {
            Toast.makeText(getApplicationContext(),"Lütfen tüm alanları doldurunuz..", Toast.LENGTH_SHORT).show();
        }
    }
}