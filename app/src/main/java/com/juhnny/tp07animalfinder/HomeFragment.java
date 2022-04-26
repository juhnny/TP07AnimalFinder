package com.juhnny.tp07animalfinder;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.juhnny.tp07animalfinder.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    MainActivity mainActivity;
    FragmentHomeBinding binding;
    HomePagerAdapter homePagerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainActivity = (MainActivity) getActivity();
        binding = FragmentHomeBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MainActivity mainActivity = (MainActivity) getActivity();
        //툴바 설정
//        mainActivity.setSupportActionBar(binding.toolbar);

        //페이저 설정
        homePagerAdapter = new HomePagerAdapter(mainActivity);
        binding.pager.setAdapter(homePagerAdapter);

        //탭 설정, 페이저 연결
        String[] tabTitle = {"설정 1", "설정 2", "설정 3"};
        new TabLayoutMediator(binding.layoutTab, binding.pager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                //페이지 개수만큼 이 메소드가 발동함
                //첫번재 파라미터 tab: 자동으로 만들어질 Tab 객체
                //두번째 파라미터 position: Tab이 만들어질 위치
//                tab.setText("TAB" + (position+1));
                tab.setText(tabTitle[position]);
            }
        }).attach();





    }
}
