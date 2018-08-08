package com.example.suleyman.parkyerikiralama;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FiltreliAra extends AppCompatActivity {
    Spinner spn_lokasyon,spn_basSaat,spn_basDakika,spn_bitSaat,spn_bitDakika;
    DatePicker datePicker;
    String lokasyon_adi;
    int lokasyon_id;
    String URL = Variables.http+Variables.lokasyonYukle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtreli_ara);

        spn_lokasyon = (Spinner) findViewById(R.id.spn_lokasyon);
        datePicker = (DatePicker) findViewById(R.id.dt_picker);
        spn_basSaat = (Spinner)findViewById(R.id.spn_basSaat);
        spn_basDakika = (Spinner)findViewById(R.id.spn_basDakika);
        spn_bitSaat = (Spinner)findViewById(R.id.spn_bitSaat);
        spn_bitDakika = (Spinner) findViewById(R.id.spn_bitDakika);

        /*
        ArrayAdapter<String> saat_adapter = new ArrayAdapter<String>(this,R.layout.spinner_editing,
                getResources().getStringArray(R.array.saats));
        saat_adapter.setDropDownViewResource(R.layout.spinnerdrop_editing);

        ArrayAdapter<String> dakika_adapter = new ArrayAdapter<String>(this,R.layout.spinner_editing,
                getResources().getStringArray(R.array.dakikas));
        dakika_adapter.setDropDownViewResource(R.layout.spinnerdrop_editing);

        spn_basSaat.setAdapter(saat_adapter);
        spn_basDakika.setAdapter(dakika_adapter);
        spn_bitSaat.setAdapter(saat_adapter);
        spn_bitDakika.setAdapter(dakika_adapter);
        */

        // default tarih ekleniyor
        datePicker.init(2017,new Date().getMonth(),new Date().getDate(),new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {}});

        // çalışıyor 5/4/2017 15:32
        try
        {
            // liste oluşturuluyor
            List<String> lists = fillList();

            //liste adaptera bağlanılıyor.
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                    (this,R.layout.spinner_editing,lists);
            // açılır menu ayarlanıyor.
            arrayAdapter.setDropDownViewResource(R.layout.spinnerdrop_editing);
            // adapter spinner'e set ediliyor.
            spn_lokasyon.setAdapter(arrayAdapter);
        }
        catch(Exception e) {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    // çalışıyor 5/4/2017 15:32
    public List<String> fillList() {
        // web serviseten json array parse edilip liste atılıyor
        List<String> lists = new ArrayList<>();
        lists.clear();

        try
        {
            String lokasyonlar = new ExtractData().execute(URL).get();
            JSONArray lksynlr = new JSONArray(lokasyonlar);
            for(int i=0;i<lksynlr.length();i++)
            {
                // lokasyon adları listeye atılıyor.
                JSONObject jsonObject = (JSONObject)lksynlr.get(i);
                lists.add(jsonObject.getString("lokasyon_adi"));
            }
        }
        catch(Exception e){}
        return lists;
    }

    public void degiskenKaydet(int y,int a,int g)
    {
        try {
            int yil = y,ay = a,gun = g;
            String sonuc = "";
            String tarih = "";
            String baslangicSaat = "";
            String bitisSaat = "";
            // veritabanının anlayacağı şekilde tarih,saat bilgileri oluşturuluyor.
            tarih = "" + yil + "-" + ay + "-" + gun;
            baslangicSaat = spn_basSaat.getSelectedItem().toString().trim() + ":" + spn_basDakika.getSelectedItem().toString().trim() + ":00";
            bitisSaat = spn_bitSaat.getSelectedItem().toString().trim() + ":" + spn_bitDakika.getSelectedItem().toString().trim() + ":00";
            // seçili lokasyon alınıyor.
            lokasyon_adi = spn_lokasyon.getSelectedItem().toString().trim();
            //lokasyonun id'si çekiliyor.
            String url = Variables.http + Variables.lokasyonIdDon + "?lokasyon_adi=" + lokasyon_adi;
            //web servise bağlanılıyor.
            try {
                sonuc = new ExtractData().execute(url).get();
            } catch (Exception e) {
                sonuc = "-1";
            }

            if(sonuc.isEmpty())
            {
                if(lokasyon_adi.contains("fen"))
                    sonuc="2019";
                else if(lokasyon_adi.contains("Cami"))
                    sonuc="2023";
                else if(lokasyon_adi.contains("Bati"))
                    sonuc = "2027";
                else if(lokasyon_adi.contains("Bilgi"))
                    sonuc = "2029";
                else if(lokasyon_adi.contains("Hukuk"))
                    sonuc = "2030";
                else if(lokasyon_adi.equals("KYK"))
                    sonuc = "2031";
                else if(lokasyon_adi.contains("Tekno"))
                    sonuc = "2033";
                else if(lokasyon_adi.contains("Cadde"))
                    sonuc = "2034";
                else if(lokasyon_adi.contains("Oto"))
                    sonuc = "2035";
                else if(lokasyon_adi.contains("AVM"))
                    sonuc = "2037";
                else if(lokasyon_adi.contains("eledi"))
                    sonuc = "2039";
                else if(lokasyon_adi.contains("menEvi"))
                    sonuc = "2041";
                else if(lokasyon_adi.contains("evsBey"))
                    sonuc = "2042";
                else if(lokasyon_adi.contains("oyGa"))
                    sonuc = "2043";
                else if(lokasyon_adi.contains("ALT"))
                    sonuc = "2044";
            }
            sonuc = sonuc.trim();
            /*
            Toast.makeText(getApplicationContext(),url+"\n"+lokasyon_adi+"\n"+
                    sonuc+","+sonuc.length(),Toast.LENGTH_LONG).show();
            */

            // lokasyon id'si -1 değilse veya sıfırdan büyükse.
            // sharedpreferences'a kaydediliyor.
            if(!sonuc.trim().equals("-1") && Integer.parseInt(sonuc.trim()) > 0)
            {
                lokasyon_id = Integer.parseInt(sonuc.trim());
                SharedPreferences preferences = getSharedPreferences(Variables.common_pref, MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();

                editor.clear();
                editor.commit();

                // oluşturulan diğer bilgilerde daha sonra kullanılmak üzere
                // kaydediliyor.
                editor.putInt("lokasyon_id", lokasyon_id);
                editor.putString("baslangic_saat", baslangicSaat.trim());
                editor.putString("bitis_saat", bitisSaat.trim());
                editor.putString("tarih", tarih.trim());
                editor.commit();

                /*
                StringBuilder sb = new StringBuilder();
                sb.append(preferences.getInt("lokasyon_id", -1)).append("\n");
                sb.append(preferences.getString("baslangic_saat", "null")).append("\n");
                sb.append(preferences.getString("bitis_saat", "null")).append("\n");
                sb.append(preferences.getString("tarih", "2000-01-01")).append("\n");

                Toast.makeText(getApplicationContext(), sb.toString(), Toast.LENGTH_LONG).show();
                */
            }
            // lokasyon_id bulunamazsa
            else
                Toast.makeText(getApplicationContext(),"hata....!!!!",Toast.LENGTH_LONG).show();
        }catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),e.getMessage()+" ht",Toast.LENGTH_LONG).show();
        }
    }

    // park yeri arama işleminin yapıldığı method
    public void ara(View view)
    {
        try {
            // string değerlere aktarılıyor.
            String basSaat = spn_basSaat.getSelectedItem().toString();
            String bitSaat = spn_bitSaat.getSelectedItem().toString();
            String basDakika = spn_basDakika.getSelectedItem().toString();
            String bitDakika = spn_bitDakika.getSelectedItem().toString();

            // int'e çevrilimi
            int basSaat1, bitSaat1, basDakika1, bitDakika1;
            basSaat1 = Integer.parseInt(basSaat);
            bitSaat1 = Integer.parseInt(bitSaat);
            basDakika1 = Integer.parseInt(basDakika);
            bitDakika1 = Integer.parseInt(bitDakika);
            // baslangıc saati bitis saatinden ileride ise
            if (bitSaat1 - basSaat1 < 0)
                Toast.makeText(getApplicationContext(), "Bitis saati Başlangıç saatinden daha geride..", Toast.LENGTH_LONG).show();
                // en az 1 saat fark olması için
            else if (basSaat1 == bitSaat1)
                Toast.makeText(getApplicationContext(), "En az 1 saat olmak zorunda...", Toast.LENGTH_LONG).show();

                // saat farkı doğru ise
            else if(bitSaat1 - basSaat1 > 0 && bitDakika1 - basDakika1 >= 0)
            {
                // tarih bilgisi çekiliyor.
                int yıl = datePicker.getYear();
                int ay = datePicker.getMonth();
                int gun = datePicker.getDayOfMonth();

                Date date = new Date();
                if(Integer.parseInt(String.valueOf(yıl).substring(2)) < Integer.parseInt(String.valueOf(date.getYear()).substring(1)))
                    Toast.makeText(getApplicationContext(),"Lütfen Tarih Bilgisini Kontrol Ediniz..!!",Toast.LENGTH_LONG).show();
                else if(Integer.parseInt(String.valueOf(yıl).substring(2)) ==
                        Integer.parseInt(String.valueOf(date.getYear()).substring(1)) &&
                        ay < date.getMonth())
                {
                    Toast.makeText(getApplicationContext(),"Lütfen Tarih Bilgisini Kontrol Ediniz..!!",Toast.LENGTH_LONG).show();
                }
                else if(Integer.parseInt(String.valueOf(yıl).substring(2)) ==
                        Integer.parseInt(String.valueOf(date.getYear()).substring(1)) &&
                        ay == date.getMonth() &&
                        gun < date.getDate())
                {
                    Toast.makeText(getApplicationContext(),"Lütfen Tarih Bilgisini Kontrol Ediniz..!!",Toast.LENGTH_LONG).show();
                }
                else
                {
                    SharedPreferences pref_giris = getSharedPreferences(Variables.giris_pref,MODE_PRIVATE);

                    // sonradan eklenen
                    int fiyat = hesapla(bitSaat1-basSaat1 + (bitDakika1 - basDakika1)*0.01 );
                    String url_bakiye = Variables.http+Variables.bakiyeSorgu+"?id="+pref_giris.getInt(Variables.KULLANICI_ID,-1);

                    if(Integer.parseInt(new ExtractData().execute(url_bakiye).get().trim().toString()) >= fiyat)
                    {
                        ay = ay + 1;

                        yıl = Integer.parseInt(String.valueOf(yıl).trim());
                        ay = Integer.parseInt(String.valueOf(ay).trim());
                        gun = Integer.parseInt(String.valueOf(gun).trim());

                        // metod çağrımı
                        degiskenKaydet(yıl, ay, gun);

                        // uygun park yeri aramak için
                        // web servise bağlanan metod
                        sorguYap(yıl, ay, gun);

                        Toast.makeText(getApplicationContext(), "Lütfen Bekleyin\nSayfaya yönlendiriliyorsunuz...", Toast.LENGTH_LONG).show();

                        try {
                            // sayfa yönlendirimi
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Thread.sleep(2000);
                                    } catch (Exception e) {
                                    } finally {
                                        Intent ıntent = new Intent(getApplicationContext(), Anasayfa.class);
                                        startActivity(ıntent);
                                    }
                                }
                            }).start();
                        }catch(Exception e) {
                            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                    else
                        Toast.makeText(getApplicationContext(),"Bakiyeniz Yetersiz\nLütfen Tl Yükleyiniz..!!",Toast.LENGTH_LONG).show();

                }
            }
            // saatler uygun değilse
            else
                Toast.makeText(getApplicationContext(),"Lütfen saatleri\nkontrol ediniz", Toast.LENGTH_LONG).show();
        }catch(Exception e) {
            Toast.makeText(getApplicationContext(),e.getMessage()+" hata..!!",Toast.LENGTH_LONG).show();
        }
    }

    public void sorguYap(int yil,int ay,int gun)
    {
        // web servise yani veritabanına uygun değişken hazırlığı
        String tarih = ""+yil+"-"+ay+"-"+gun;
        String baslangicSaat = spn_basSaat.getSelectedItem().toString().trim()+":"+spn_basDakika.getSelectedItem().toString().trim()+":00";
        String bitisSaat = spn_bitSaat.getSelectedItem().toString().trim()+":"+spn_bitDakika.getSelectedItem().toString().trim()+":00";

        tarih = tarih.trim();
        // uygun url hazırlanması
        String url = Variables.http+Variables.koordinatDon + "?lokasyon_id="+lokasyon_id+
                "&tarih"+tarih+"&bas_saat="+baslangicSaat+
                "&bit_saat="+bitisSaat;

        // url kaynaklarına kaydedidiliyor
        SharedPreferences preferences = getSharedPreferences(Variables.common_pref,MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("url",url);
        editor.commit();
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

    public int hesapla(double saat)
    {
        if(1 == saat)
            return 3;
        if(saat > 1 && saat <2)
            return  4;
        if(saat == 2)
            return 5;
        if(saat > 2 && saat <4)
            return 7;
        if(saat == 4)
            return 10;
        if(saat > 4 && saat <=6)
            return 11;
        if(saat > 6 && saat < 8)
            return 13;
        if(saat == 8)
            return 15;
        if(saat > 8 && saat < 14)
            return 16;
        if(saat >= 14 && saat < 24)
            return 18;
        if(saat==24)
            return 20;
        else
            return -1;
    }
}