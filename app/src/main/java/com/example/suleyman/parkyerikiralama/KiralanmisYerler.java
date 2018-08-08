package com.example.suleyman.parkyerikiralama;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.suleyman.parkyerikiralama.pojos.Kiralanmis;

import java.util.ArrayList;
import java.util.List;

public class KiralanmisYerler extends AppCompatActivity {
    ListView kiralanmis_listview;
    List<Kiralanmis> kiraliListe;
    private String[] NAMES = new String[]{"Ahmet","Hasan","Veli","Kenan"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kiralanmis_yerler);

        kiralanmis_listview = (ListView) findViewById(R.id.kiralanmis_listview);
        kiraliListe = listDon();

        final String[] kiralanmisListeSon = new String[kiraliListe.size()];
        for (int i = 0; i < kiraliListe.size(); i++) {
            kiralanmisListeSon[i] = kiraliListe.get(i).toString();
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                (this,R.layout.kiralanmis,kiralanmisListeSon);
        kiralanmis_listview.setAdapter(arrayAdapter);

        kiralanmis_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int p, long id) {
                // sonradan eklenen
                final int position = p;

                AlertDialog.Builder aBuilder = new AlertDialog.Builder(KiralanmisYerler.this);
                aBuilder.setMessage("Ne Yapmak İstersiniz..!!");
                aBuilder.setCancelable(true).setNegativeButton("Kapat", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                }).setPositiveButton("Navigasyon", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        SharedPreferences pr1 = getSharedPreferences(Variables.latlang_pref,MODE_PRIVATE);
                        SharedPreferences.Editor ed1 = pr1.edit();

                        ed1.clear();
                        ed1.commit();

                        ed1.putString("enlem", String.valueOf(kiraliListe.get(position).getEnlem()));
                        ed1.putString("boylam",String.valueOf(kiraliListe.get(position).getBoylam()));
                        ed1.commit();

                        Intent ıntent = new Intent(getApplicationContext(),YolTarifiActivity.class);
                        startActivity(ıntent);
                    }
                })/*.setPositiveButton("Kiralama İşlemini İptal Et", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),"İptal Ediliyor",Toast.LENGTH_LONG).show();
                    }
                })*/;
                AlertDialog alertDialog = aBuilder.create();
                alertDialog.setTitle("Emin misiniz ?");
                alertDialog.show();
            }
        });
    }

    // sıkıntı yok
    public List<Kiralanmis> listDon()
    {
        List<Kiralanmis> kira_liste = new ArrayList<>();
        kira_liste.clear();
        SharedPreferences preferences = getSharedPreferences(Variables.giris_pref,MODE_PRIVATE);
        String url = Variables.http+Variables.kiralanmisAlanlar+
                "?kullanici_id="+
                preferences.getInt(Variables.KULLANICI_ID,-1);
        String sonuc = "";
        try {
            sonuc = new ExtractData().execute(url).get();
        }catch (Exception e) {
            sonuc = "[]";
        }
        if(!sonuc.trim().equals("[]"))
        {
            kira_liste = Variables.parcala_kira(sonuc);
        }
        return kira_liste;
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
