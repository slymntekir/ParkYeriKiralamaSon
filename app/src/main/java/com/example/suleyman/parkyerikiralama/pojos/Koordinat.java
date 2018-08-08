package com.example.suleyman.parkyerikiralama.pojos;

/**
 * Created by suleyman on 4/4/2017.
 */

public class Koordinat {
    private int konum_id;
    private String enlem;
    private String boylam;
    private int zoom;
    private String parkKodu;

    public Koordinat() {
    }

    public Koordinat(int konum_id, String enlem, String boylam, int zoom, String parkKodu) {
        this.konum_id = konum_id;
        this.enlem = enlem;
        this.boylam = boylam;
        this.zoom = zoom;
        this.parkKodu = parkKodu;
    }

    public int getKonum_id() {
        return konum_id;
    }

    public void setKonum_id(int konum_id) {
        this.konum_id = konum_id;
    }

    public String getEnlem() {
        return enlem;
    }

    public void setEnlem(String enlem) {
        this.enlem = enlem;
    }

    public String getBoylam() {
        return boylam;
    }

    public void setBoylam(String boylam) {
        this.boylam = boylam;
    }

    public int getZoom() {
        return zoom;
    }

    public void setZoom(int zoom) {
        this.zoom = zoom;
    }

    public String getParkKodu() {
        return parkKodu;
    }

    public void setParkKodu(String parkKodu) {
        this.parkKodu = parkKodu;
    }

    @Override
    public String toString() {
        return "Koordinat{" +
                "konum_id=" + konum_id +
                ", enlem='" + enlem + '\'' +
                ", boylam='" + boylam + '\'' +
                ", zoom=" + zoom +
                ", parkKodu='" + parkKodu + '\'' +
                '}';
    }
}
