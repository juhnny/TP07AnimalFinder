package com.juhnny.tp07animalfinder;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.juhnny.tp07animalfinder.databinding.FragmentMapBinding;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MapFragment extends Fragment {

    MainActivity mainActivity;
    FragmentMapBinding binding;
    ShelterAdapter shelterAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMapBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        G.shelters.add(new Shelter("보호소1"));
        G.shelters.add(new Shelter("보호소1"));
        G.shelters.add(new Shelter("보호소1"));
        G.shelters.add(new Shelter("보호소1"));
        G.shelters.add(new Shelter("보호소1"));
        G.shelters.add(new Shelter("보호소1"));
        G.shelters.add(new Shelter("보호소1"));
        G.shelters.add(new Shelter("보호소1"));
        G.shelters.add(new Shelter("보호소1"));
        G.shelters.add(new Shelter("보호소1"));
        G.shelters.add(new Shelter("보호소1"));
        G.shelters.add(new Shelter("보호소1"));

        mainActivity = (MainActivity) getActivity();
//        mainActivity.setSupportActionBar(binding.toolbar);

        shelterAdapter = new ShelterAdapter(mainActivity);
        binding.recycler.setAdapter(shelterAdapter);

        loadShelter();

    }//onViewCreated()

    void loadShelter(){
        new Thread(() -> {
            try {
                URL url = new URL("http://www.naver.com");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                
                InputStream is = connection.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader reader = new BufferedReader(isr);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

}
