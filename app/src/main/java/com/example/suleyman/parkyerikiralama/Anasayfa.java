package com.example.suleyman.parkyerikiralama;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.suleyman.parkyerikiralama.pojos.Koordinat;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Anasayfa extends AppCompatActivity implements OnMapReadyCallback {
    int fiyat_son = 0;
    List<Koordinat> konumListesi;
    GoogleMap haritam;
    Button yol_tarifi_button;
    // yol tarifi için kullanılacak marker
    Marker guncel_marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean var_mi = false;

        // apinin olup olmadığının kontrolü
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        if (api.isGooglePlayServicesAvailable(this) == ConnectionResult.SUCCESS)
            var_mi = true;
        else if (api.isUserResolvableError(api.isGooglePlayServicesAvailable(this)))
            api.getErrorDialog(this, api.isGooglePlayServicesAvailable(this), 0).show();
        else
            Toast.makeText(this, "Api Yok", Toast.LENGTH_LONG).show();
        // api varsa activity yükeniyor
        if(var_mi) {
            setContentView(R.layout.activity_anasayfa);
            MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapFrgmnt);
            mapFragment.getMapAsync(this);
        }

        // shared preferencese kaydedilen url'e göre
        // konum listesinin doldurulumu

        konumListesi = konumListGetir();

        yol_tarifi_button = (Button)findViewById(R.id.anasayfa_yol_tarifi_btn);
        yol_tarifi_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder aBuilder = new AlertDialog.Builder(Anasayfa.this);
                aBuilder.setMessage("Yol tarifi alacak mısınız ?");
                aBuilder.setCancelable(false).setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                }).setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LatLng guncel_marker_position = guncel_marker.getPosition();
                        /*
                        Intent ıntent = new Intent(getApplicationContext(),YolTarifiActivity.class);
                        ıntent.putExtra("enlem",guncel_marker_position.latitude);
                        ıntent.putExtra("boylam",guncel_marker_position.longitude);
                        startActivity(ıntent);
                        */
                        SharedPreferences pr1 = getSharedPreferences(Variables.latlang_pref,MODE_PRIVATE);
                        SharedPreferences.Editor ed1 = pr1.edit();

                        ed1.clear();
                        ed1.commit();

                        ed1.putString("enlem", String.valueOf(guncel_marker_position.latitude));
                        ed1.putString("boylam",String.valueOf(guncel_marker_position.longitude));
                        ed1.commit();

                        Intent ıntent = new Intent(getApplicationContext(),YolTarifiActivity.class);
                        startActivity(ıntent);

                    }
                });
                AlertDialog alertDialog = aBuilder.create();
                alertDialog.setTitle("Emin misiniz ?");
                alertDialog.show();
            }
        });
        hesapla1();
    }

    private void hesapla1() {
        SharedPreferences preferences_common = getSharedPreferences(Variables.common_pref,MODE_PRIVATE);

        String bas_saat = preferences_common.getString("baslangic_saat","");
        String bit_saat = preferences_common.getString("bitis_saat","");

        int saat = Integer.parseInt(bit_saat.trim().split(":")[0])-Integer.parseInt(bas_saat.trim().split(":")[0]);
        double dakika = Integer.parseInt(bit_saat.trim().split(":")[1]) - Integer.parseInt(bas_saat.trim().split(":")[1]);
        dakika *=0.01;


        if(hesapla(saat + dakika) != -1) {
            fiyat_son = hesapla(saat + dakika);
        }
    }

    private List<Koordinat> konumListGetir() {
        List<Koordinat> l = new ArrayList<>();
        l.clear();

        // url çekiliyor.
        SharedPreferences preferences = getSharedPreferences(Variables.common_pref, MODE_PRIVATE);
        String url = preferences.getString("url","");
        String sonuc = "";
        // web servise bağlanan metod
        try {
            sonuc = new ExtractData().execute(url).get();
        }catch(Exception e){}

        // parçalanıp liste kaydediliyor.
        l = Variables.parcala(sonuc);
        return l;
    }

        // OnMapReadyCallback methodunun implements ettiği metod
        @Override
        public void onMapReady(GoogleMap googleMap) {
            haritam = googleMap;

            // harita boş değilse
            if(haritam != null) {
                haritam.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                    @Override
                    public void onMarkerDragStart(Marker marker) {
                    }

                    @Override
                    public void onMarkerDrag(Marker marker) {
                    }

                    @Override
                    public void onMarkerDragEnd(Marker marker) {
                        Geocoder gc = new Geocoder(Anasayfa.this);
                        LatLng ll = marker.getPosition();
                        try {
                            List<Address> addressLists = gc.getFromLocation(ll.latitude, ll.longitude, 1);
                            Address address = addressLists.get(0);
                            marker.setTitle(address.getLocality());
                            marker.showInfoWindow();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                // marker tıklanması veren method
                haritam.setOnMarkerClickListener(click);

                // yukarıda yüklenen konum listesini haritaya ekleyen metod
                Geocoder gc = new Geocoder(Anasayfa.this);
                goToLocationZoom(Double.parseDouble(konumListesi.get(0).getEnlem()),
                        Double.parseDouble(konumListesi.get(0).getBoylam()),18);
                List<Address> list = null;
                try {
                    // konum listesi boyutunca dönen method
                    for(int i=0;i<konumListesi.size();i++) {
                        LatLng ll = new LatLng(Double.parseDouble(konumListesi.get(i).getEnlem()),
                                Double.parseDouble(konumListesi.get(i).getBoylam()));
                        list = gc.getFromLocation(ll.latitude,ll.longitude,1);
                        Address address = list.get(0);
                        setMarker(address,ll.latitude,ll.longitude,konumListesi.get(i).getParkKodu());
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                }

            }
        }

        public void setMarker(Address address, double lt, double lg, String kod) {
            MarkerOptions markerOptions = new MarkerOptions().
                    title(address.getLocality()).
                    draggable(true).
                    icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).
                    position(new LatLng(lt,lg)).
                    snippet(""+kod);
            haritam.addMarker(markerOptions);
        }

        // yukarıda çağrılan marker click metodu
        GoogleMap.OnMarkerClickListener click = new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker1) {
                guncel_marker = marker1;
                AlertDialog.Builder aBuilder = new AlertDialog.Builder(Anasayfa.this);
                aBuilder.setMessage(fiyat_son+"Tl'ye Kiralayacak mısınız ?");
                aBuilder.setCancelable(false).setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                }).setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // aynı park alanını birden fazla defa kiralamaya çalışmıyorsa
                        if(denetle()==0)
                        {
                            // kiralama başarılı ise
                            boolean kiralandi_mi = kirala(marker1);
                            if(kiralandi_mi) {
                                Toast.makeText(getApplicationContext(), "Kiralama\nBaşarılı", Toast.LENGTH_LONG).show();
                                yol_tarifi_button.setEnabled(true);

                                SharedPreferences common_pref = getSharedPreferences(Variables.common_pref,MODE_PRIVATE);

                                Intent bildirimIntent = new Intent("bildirim_intent");
                                bildirimIntent.putExtra("tarih",common_pref.getString("tarih",""));
                                bildirimIntent.putExtra("bitis_saat",common_pref.getString("bitis_saat",""));
                                sendBroadcast(bildirimIntent);
                            }
                        }
                        else {
                            Toast.makeText(getApplicationContext(),
                                    "Aynı Zaman Dilimi İçinde \nZaten Bir Park Alanı Kiralamışsınız", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                AlertDialog alertDialog = aBuilder.create();
                alertDialog.setTitle("Emin misiniz ?");
                alertDialog.show();
                return true;
            }
        };

        private boolean kirala(Marker m) {
            SharedPreferences preferences_common = getSharedPreferences(Variables.common_pref,MODE_PRIVATE);
            SharedPreferences preferences_giris = getSharedPreferences(Variables.giris_pref,MODE_PRIVATE);

            String bas_saat = preferences_common.getString("baslangic_saat","");
            String bit_saat = preferences_common.getString("bitis_saat","");
            String enlem = String.valueOf(m.getPosition().latitude);
            String boylam = String.valueOf(m.getPosition().longitude);

            int konum_id;

            String url = Variables.http+Variables.konumIdDon + "?enlem="+enlem.trim()+"&boylam="+boylam.trim();
            String sonuc1 = "";
            try {
                sonuc1 = new ExtractData().execute(url).get();
            } catch(Exception e){}

            sonuc1 = sonuc1.trim();

            if(!sonuc1.trim().equals("-1"))
            {
                konum_id = Integer.parseInt(sonuc1.trim());
                int saat = Integer.parseInt(bit_saat.trim().split(":")[0])-Integer.parseInt(bas_saat.trim().split(":")[0]);
                double dakika = Integer.parseInt(bit_saat.trim().split(":")[1]) - Integer.parseInt(bas_saat.trim().split(":")[1]);
                dakika *=0.01;

                int fyt = 0;
                if(hesapla(saat + dakika) != -1) {
                    fyt = hesapla(saat + dakika);
                }

                String url1 = Variables.http+Variables.kirala+"?musteri_id="+preferences_giris.getInt(Variables.KULLANICI_ID,-1)+
                        "&konum_id="+konum_id+"&lokasyon_id="+preferences_common.getInt("lokasyon_id",-1)+
                        "&tarih="+preferences_common.getString("tarih","")+"&bas_saat="+bas_saat+
                        "&bit_saat="+bit_saat+"&fiyat="+fyt;

                String sonuc2 = "";
                try {
                    sonuc2 = new ExtractData().execute(url1).get();
                }catch (Exception e){
                    sonuc2 = e.getMessage();
                }

                sonuc2 = sonuc2.trim();
                if(sonuc2.contains("ama"))
                {
                    String url_bakiye_azalt = Variables.http+Variables.bakiyeAzalt+
                            "?musteri_id="+preferences_giris.getInt(Variables.KULLANICI_ID,-1)
                            +"&bakiye="+fyt;
                    String s = "";
                    try {
                        s = new ExtractData().execute(url_bakiye_azalt).get();
                    }catch (Exception e){}

                    s = s.trim();
                    if(s.contains("tr"))
                    {
                        return true;
                    }
                    else if(s.contains("fal") || s.isEmpty())
                    {
                        return false;
                    }
                }
            }
            else
            {
                Toast.makeText(getApplicationContext(),"hata..!! "+sonuc1,Toast.LENGTH_LONG).show();
                return false;
            }
            return false;
        }

    // kiralanmak istenen alan bir daha kiralanmak isterse hata veren method
    public int denetle()
    {
        SharedPreferences preferences_common = getSharedPreferences(Variables.common_pref,MODE_PRIVATE);
        SharedPreferences preferences_giris = getSharedPreferences(Variables.giris_pref,MODE_PRIVATE);

        String url = Variables.http+Variables.kiralamaKontrol+"?tarih="+preferences_common.getString("tarih","")+
                "&musteri_id="+preferences_giris.getInt(Variables.KULLANICI_ID,-1)+
                "&bas_saat="+preferences_common.getString("baslangic_saat","")+
                "&bit_saat="+preferences_common.getString("bitis_saat","");
        String s = "";
        try {
            s = new ExtractData().execute(url).get();
        }catch (Exception e){}
        s = s.trim();

        return Integer.parseInt(s);
    }

    private void goToLocationZoom(double latitude, double lontitude, int zoom) {
        LatLng ll = new LatLng(latitude, lontitude);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(ll, zoom);
        haritam.animateCamera(cameraUpdate);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_cikis)
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
        return super.onOptionsItemSelected(item);
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