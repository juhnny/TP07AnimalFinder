package com.juhnny.tp07animalfinder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationBarView;
import com.juhnny.tp07animalfinder.databinding.ActivityMainBinding;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FragmentManager fragmentManager;
    ArrayList<Fragment> fragments = new ArrayList<>();

    final int REQUEST_CODE_EXTERNAL_STORAGE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("myTag", "MainActivity onCreate()");
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        BottomSheetDialog를 써?

        //BNV 및 Fragment 설정
        fragmentManager = getSupportFragmentManager();
        fragments.add(new HomeFragment());
        fragments.add(null);
        fragments.add(null);
        fragmentManager.beginTransaction().add(R.id.container, fragments.get(0)).commit();

        binding.bnv.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction trans = fragmentManager.beginTransaction();
                if(fragments.get(0) != null) trans.hide(fragments.get(0));
                if(fragments.get(1) != null) trans.hide(fragments.get(1));
                if(fragments.get(2) != null) trans.hide(fragments.get(2));

                switch (item.getItemId()){
                    case R.id.menu_bnv_home:
                        trans.show(fragments.get(0));
                        break;
                    case R.id.menu_bnv_map:
                        if(fragments.get(1) == null) {
                            fragments.set(1, new MapFragment());
                            trans.add(R.id.container, fragments.get(1));
                        }
                        trans.show(fragments.get(1));
                        break;
                    case R.id.menu_bnv_myinfo:
                        if(fragments.get(2) == null) {
                            fragments.set(2, new MyinfoFragment());
                            trans.add(R.id.container, fragments.get(2));
                        }
                        trans.show(fragments.get(2));
                        break;
                }//switch
                trans.commit();
                return true;
            }
        });


    }

    @Override
    protected void onResume() { //Fragment의 onViewCreated 다음...이라고 인터넷에서 봤는데 실제로는 더 빨리 끝나네?
        super.onResume();
        Log.e("myTag", "MainActivity onResume");

    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        finish();
    }

    void sortData(LinkedHashMap<String, String> map){
        //어차피 날짜 순서 밖에 뒤집을 일이 없다. 이미 최신순으로 불러와져 있으니 역순으로 된 맵을 만들어서 보여주기만 하면 된다.
//        homeRecyclerAdapter = new HomeRecyclerAdapter(this, itemsReverse);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == REQUEST_CODE_EXTERNAL_STORAGE && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this, "외부저장소 권한 허용됨", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "외부저장소 권한 거부됨", Toast.LENGTH_SHORT).show();
        }
    }

}