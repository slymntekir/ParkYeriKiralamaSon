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
import android.widget.EditText;
import android.widget.Toast;

public class TlYukle extends AppCompatActivity {
    int miktar;
    EditText edt_tl_yukle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tl_yukle);

        edt_tl_yukle = (EditText) findViewById(R.id.edtTlKodu);
    }

    public void tlYUKLE(View view)
    {
        final SharedPreferences preferences = getSharedPreferences(Variables.giris_pref,MODE_PRIVATE);
        final String tl_kodu = edt_tl_yukle.getText().toString();

        // dialog penceresi oluşturuluyor
        AlertDialog.Builder aBuilder = new AlertDialog.Builder(TlYukle.this);
        aBuilder.setMessage("Yükleyecek misiniz ?");
        aBuilder.setCancelable(false).setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        }).setPositiveButton("Evet", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                String url = Variables.http+Variables.TlYukle+"?kullanici_id="+preferences.getInt(Variables.KULLANICI_ID,-1)+
                        "&tl_kodu="+tl_kodu;
                String sonuc = "";
                try {
                    sonuc = new ExtractData().execute(url).get();
                }catch (Exception e){}

                sonuc = sonuc.trim();

                if(sonuc.contains("ama"))
                {
                    Toast.makeText(getApplicationContext(),"Talep Edilen Tutar Hattınıza Yüklendi",Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(getApplicationContext(),"Lütfen Doğru Yazdığınızdan Emin Olunuz.! "+sonuc,Toast.LENGTH_LONG).show();
            }
        });
        AlertDialog alertDialog = aBuilder.create();
        alertDialog.setTitle("Emin misiniz ?");
        alertDialog.show();
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