package com.juhnny.tp07animalfinder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.juhnny.tp07animalfinder.databinding.ActivityItemHomeBinding;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class HomeItemActivity extends AppCompatActivity {

    ActivityItemHomeBinding binding;

    ArrayList<Item> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityItemHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        items = (ArrayList<Item>) intent.getSerializableExtra("items");
        int position = intent.getIntExtra("position", 0);

        Glide.with(this).load(items.get(position).imageSrc).override(Target.SIZE_ORIGINAL).into(binding.ivMain);

        //추가
        //외부 지도 앱으로 발견장소 주소 보내 검색하기
        //외부 지도 앱으로 보호소 주소 보내 검색하기
        //전화 앱으로 전화번호 보내기



    }
}