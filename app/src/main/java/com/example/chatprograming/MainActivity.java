package com.example.chatprograming;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chatprograming.Adapter.FragmentsAdapter;
import com.example.chatprograming.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding; //* bildirme işlevi
    FirebaseAuth mAuth; //* kullanıcı doğrulama firebase kütüphanesi


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityMainBinding.inflate(getLayoutInflater()); //* doldurucuyu inflate
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();
        binding.viewPager.setAdapter(new FragmentsAdapter(getSupportFragmentManager())); //üsteki fragmentleri yani durum arama ve sohbetler sekmesini bağlıyoruz
        binding.tabLayaout.setupWithViewPager(binding.viewPager); //* yükleme işlemi yapıldı üsteki sekmelerin bildirme işleminden sonra



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.settings:
                //Toast.makeText(this, "ayarlara tıklandı", Toast.LENGTH_SHORT).show();

                Intent intent2 = new Intent(MainActivity.this,SettingsActivity.class);
                startActivity(intent2);
                break;
            case R.id.groupChat:
                //Toast.makeText(this, "Grup sohbeti başladı", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(MainActivity.this,GroupCheatActivity.class);
                startActivity(intent1);
                break;
            case R.id.Logout:
                mAuth.signOut(); //* çıkış yapılıyor mevcut bilgilerden firebasin kütüphanesinden
                //* fakat şöyle bir sorun var anlık olarak yansıtmıyor uygulamayı kapatıp açtığında giriş sayfasına atıyor çözümünüde yapıcağız
                //* yani yeterli değil
                Intent intent = new Intent(MainActivity.this, SignlnActivity.class);
                startActivity(intent); //* burada çıkış gerçekleşti intent aracılığıyla
                break;






        }

        return super.onOptionsItemSelected(item);
    }
}