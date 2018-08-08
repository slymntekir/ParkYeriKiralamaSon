package com.example.suleyman.parkyerikiralama;

import com.example.suleyman.parkyerikiralama.pojos.Kiralanmis;
import com.example.suleyman.parkyerikiralama.pojos.Koordinat;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by suleyman on 14.3.2017.
 */

public class Variables
{
    public static final String giris_pref = "giris";
    public static final String common_pref = "common";
    public static final String latlang_pref = "latlang_pref";
    //public static final String common_txt = "metin.txt";
   // public static final String mainUrl = "http://10.0.2.2:8084/ParkYeriKiralamaRest/parkyeri/databases/";
    public static final String http = "http://10.0.2.2:8084";
    public static final String sorguUrl = "/ParkYeriKiralamaRest/parkyeri/databases/idsorgula";
    public static final String kayıtUrl = "/ParkYeriKiralamaRest/parkyeri/databases/musteriekle";
    public static final String bakiyeSorgu = "/ParkYeriKiralamaRest/parkyeri/databases/bakiyeSorgula";
    public static final String tlYukle = "/ParkYeriKiralamaRest/parkyeri/databases/tlYukle";
    public static final String lokasyonYukle ="/ParkYeriKiralamaRest/parkyeri/databases/lokasyonYukle";
    public static final String lokasyonIdDon ="/ParkYeriKiralamaRest/parkyeri/databases/lokasyonIdDon";
    public static final String plakaSorgu ="/ParkYeriKiralamaRest/parkyeri/databases/plakaSorgu";
    public static final String koordinatDon = "/ParkYeriKiralamaRest/parkyeri/databases/koordinatDon";
    public static final String konumIdDon = "/ParkYeriKiralamaRest/parkyeri/databases/konumIdDon";
    public static final String kirala = "/ParkYeriKiralamaRest/parkyeri/databases/kirala";
    public static final String kiralamaKontrol = "/ParkYeriKiralamaRest/parkyeri/databases/kiralamaKontrol";
    public static final String kiralanmisAlanlar = "/ParkYeriKiralamaRest/parkyeri/databases/kiralanmaBilgileri";
    public static final String kullaniciGuncelle = "/ParkYeriKiralamaRest/parkyeri/databases/kullaniciGuncelle";
    public static final String TlYukle = "/ParkYeriKiralamaRest/parkyeri/databases/TlYukle";
    public static final String bakiyeAzalt = "/ParkYeriKiralamaRest/parkyeri/databases/bakiyeAzalt";

    public static final String KULLANICI_ID = "kullanici_id";

    public static List<Koordinat> parcala(String girdi)
    {
        List<Koordinat> list = new ArrayList<>();
        list.clear();
        try {
            JSONArray jsonArray = new JSONArray(girdi);
            for(int i=0;i<jsonArray.length();i++)
            {
                Koordinat k = new Koordinat();
                JSONObject konumList = (JSONObject)jsonArray.get(i);
                k.setEnlem(konumList.getString("enlem"));
                k.setBoylam(konumList.getString("boylam"));
                k.setZoom(Integer.parseInt(konumList.getString("zoom")));
                k.setParkKodu(konumList.getString("parkKodu"));

                list.add(k);
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<Kiralanmis> parcala_kira(String girdi)
    {
        List<Kiralanmis> kiraList = new ArrayList<>();
        kiraList.clear();
        try {
            JSONArray jsonArray = new JSONArray(girdi);
            for(int i=0;i<jsonArray.length();i++)
            {
                Kiralanmis k = new Kiralanmis();
                JSONObject kiralist = (JSONObject)jsonArray.get(i);
                k.setEnlem(Float.parseFloat(kiralist.getString("enlem")));
                k.setBoylam(Float.parseFloat(kiralist.getString("boylam")));
                k.setPark_kodu(Integer.parseInt(kiralist.getString("park_kodu")));
                k.setSehir(kiralist.getString("sehir"));
                k.setLokasyon_adi(kiralist.getString("lokasyon_adi"));
                k.setTarih(Date.valueOf(kiralist.getString("tarih")));
                k.setBaslangic_saat(Time.valueOf(kiralist.getString("baslangic_saat")));
                k.setBitis_saat(Time.valueOf(kiralist.getString("bitis_saat")));
                k.setFiyat(Integer.parseInt(kiralist.getString("fiyat")));
                kiraList.add(k);
            }
        }catch (Exception e){
        }
        return kiraList;
    }
}