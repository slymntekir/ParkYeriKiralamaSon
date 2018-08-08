package com.example.suleyman.parkyerikiralama.pojos;

import java.sql.Date;
import java.sql.Time;

/**
 * Created by suleyman on 5/8/2017.
 */

public class Kiralanmis {
    private float enlem;
    private float boylam;
    private int park_kodu;
    private String sehir;
    private String lokasyon_adi;
    private Date tarih;
    private Time baslangic_saat;
    private Time bitis_saat;
    private int fiyat;

    public Kiralanmis() {
        super();
    }

    public Kiralanmis(float enlem, float boylam, int park_kodu, String sehir, String lokasyon_adi,
                      Date tarih, Time baslangic_saat, Time bitis_saat, int fiyat) {
        this.enlem = enlem;
        this.boylam = boylam;
        this.park_kodu = park_kodu;
        this.sehir = sehir;
        this.lokasyon_adi = lokasyon_adi;
        this.tarih = tarih;
        this.baslangic_saat = baslangic_saat;
        this.bitis_saat = bitis_saat;
        this.fiyat = fiyat;
    }

    public float getEnlem() {
        return enlem;
    }

    public void setEnlem(float enlem) {
        this.enlem = enlem;
    }

    public float getBoylam() {
        return boylam;
    }

    public void setBoylam(float boylam) {
        this.boylam = boylam;
    }

    public int getPark_kodu() {
        return park_kodu;
    }

    public void setPark_kodu(int park_kodu) {
        this.park_kodu = park_kodu;
    }

    public String getSehir() {
        return sehir;
    }

    public void setSehir(String sehir) {
        this.sehir = sehir;
    }

    public String getLokasyon_adi() {
        return lokasyon_adi;
    }

    public void setLokasyon_adi(String lokasyon_adi) {
        this.lokasyon_adi = lokasyon_adi;
    }

    public Date getTarih() {
        return tarih;
    }

    public void setTarih(Date tarih) {
        this.tarih = tarih;
    }

    public Time getBaslangic_saat() {
        return baslangic_saat;
    }

    public void setBaslangic_saat(Time baslangic_saat) {
        this.baslangic_saat = baslangic_saat;
    }

    public Time getBitis_saat() {
        return bitis_saat;
    }

    public void setBitis_saat(Time bitis_saat) {
        this.bitis_saat = bitis_saat;
    }

    public int getFiyat() {
        return fiyat;
    }

    public void setFiyat(int fiyat) {
        this.fiyat = fiyat;
    }

    @Override
    public String toString() {
        return ""+"Enlem : " + enlem +
                "\nBoylam : " + boylam +
                "\nPark Kodu : " + park_kodu +
                "\nSehir : " + sehir +
                "\nLokasyon Adi : " + lokasyon_adi+
                "\nTarih : " + tarih +
                "\nBaslangıç Saati : " + baslangic_saat +
                "\nBitiş Saati : " + bitis_saat +
                "\nFiyatı : " + fiyat+
                "\n-----------------\n";
    }
}
