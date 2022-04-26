package com.juhnny.tp07animalfinder;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.juhnny.tp07animalfinder.databinding.FragmentHomeTab1Binding;

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

public class HomeTab1Fragment extends Fragment {

    MainActivity mainActivity;
    FragmentHomeTab1Binding binding;
    HomeRecyclerAdapter recyclerAdapter;
    ArrayAdapter sidoAdapter;
    ArrayAdapter sigunguAdapter;

    //시도 데이터 및 스피너
    LinkedHashMap<String, String> sidoMap = new LinkedHashMap<>();
    String[] sidoArray;

    //시군구 데이터 및 스피너
    ArrayList<String> sigunguNameList = new ArrayList<>();
    ArrayList<String> sigunguCodeList = new ArrayList<>();

    //보호소 데이터 및 스피너

    //유기동물 공고 리스트
    ArrayList<Item> items = new ArrayList<>();

    //요청 항목
    String endPoint = "http://apis.data.go.kr/1543061/abandonmentPublicSrvc";
    String serviceKey = "FMtuTQzDbxryseH1K1cpxwKYGtYU6oBMY1h5gA69zTMDYVgNnE73nrqDE6TLStN2ZX3rrzdZ1st%2BXfk6nzNVjg%3D%3D";
    int numOfRows = 100;
    String sidoCode = "";
    String sigunguCode = "";

    int itemCount = 0;
    String errMsg;
    String totalCount;

    //Activity의 onCreate() 이후
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e("myTag", "HomeTab1Fragment onCreateView()");
        mainActivity = (MainActivity) getActivity();
        binding = FragmentHomeTab1Binding.inflate(inflater);
        return binding.getRoot();
    }//onCreateView

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e("myTag", "HomeTab1Fragment onViewCreated()");

        //시도 스피너
        sidoMap.put("전국", "");
//        sidoArray = getResources().getStringArray(R.array.sido);
//        sidoAdapter = ArrayAdapter.createFromResource(this, R.array.sido, R.layout.spinner_sido); //데이터로 arrays.xml을 사용할 때
        String[] tempArray = new String[sidoMap.size()];
        sidoMap.keySet().toArray(tempArray);
        sidoArray = tempArray;
//        sidoAdapter = new ArrayAdapter(this, R.layout.spinner_sido, sidoArray);

        Log.e("myAdapter", "new ArrayAdapter connected");
        sidoAdapter = new SidoAdapter(mainActivity, R.layout.spinner_sido, sidoArray);
//        sidoAdapter = new ArrayAdapter(mainActivity, R.layout.spinner_sido, sidoArray);
        binding.spinnerHomeSido.setAdapter(sidoAdapter);

        binding.spinnerHomeSido.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            //spinnerSido 선택 시 그 시도에 맞게 다시 로드
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                Toast.makeText(mainActivity, "sido "+position, Toast.LENGTH_SHORT).show();
                //Spinner는 시작하자마자 한번 자동으로 선택된다는 걸 주의
                //선택된 시도의 코드 저장
                //시작하면 첫 항목인 "전국" 자동선택

                //항목이 선택되면
                String sidoName = sidoArray[position];
                sidoCode = sidoMap.get(sidoName);
                //시군구 스피너 새로고침
                loadSigungu();
                binding.spinnerHomeSigungu.setEnabled(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });//시도 스피너


        //시군구 스피너
        sigunguNameList.add("전체");
        sigunguCodeList.add("");
        sigunguAdapter = new ArrayAdapter(mainActivity, R.layout.spinner_sigungu, sigunguNameList);
        binding.spinnerHomeSigungu.setAdapter(sigunguAdapter);
        binding.spinnerHomeSigungu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                Toast.makeText(mainActivity, "sigungu "+position, Toast.LENGTH_SHORT).show();
                sigunguCode = sigunguCodeList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });//시군구 스피너

        //검색 버튼 설정
        binding.btnSearch.setOnClickListener(view1 -> {
//            sortData(sidoMap);
            loadData();
        });

        //Recycler와 Adapter 연결부터 하자. Adapter 필요
        recyclerAdapter = new HomeRecyclerAdapter(mainActivity, items);
        binding.recycler.setAdapter(recyclerAdapter);

        //데이터 로드
        loadSido();
        loadData(); //onCreate()에 두는 게 맞나?

    }//onViewCreated
    //Activity의 onStart() 이전

    String sidoNameTemp;
    String sidoCodeTemp;

    void loadSido(){
        new Thread(){

            @Override
            public void run() {
                try {
                    Log.e("myTag10", "loadSido() starts");
                    URL url = new URL(endPoint + "/sido" +
                            "?serviceKey=" + serviceKey +
                            "&numOfRows="+ 30);
                    InputStream inputStream = url.openStream();
                    InputStreamReader reader = new InputStreamReader(inputStream);
                    XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
                    parser.setInput(reader);

                    int eventType = parser.getEventType();
                    while(eventType != XmlPullParser.END_DOCUMENT){
                        switch (eventType){
                            case XmlPullParser.START_DOCUMENT:
                                break;
                            case XmlPullParser.START_TAG:
                                if(parser.getName().equals("orgCd")){ //시도 코드
                                    parser.next();
                                    sidoCodeTemp = parser.getText();
                                } else if(parser.getName().equals("orgdownNm")){ //시도 이름
                                    parser.next();
                                    sidoNameTemp = parser.getText();
                                }
                                break;
                            case XmlPullParser.END_TAG:
                                if(parser.getName().equals("item")){
                                    Log.e("myTag11",  sidoNameTemp + sidoCodeTemp);
                                    sidoMap.put(sidoNameTemp, sidoCodeTemp);
                                }
                                Log.e("myTag12", sidoMap.size()+"");

                                break;
                        }
                        eventType = parser.next();
                    }//while
                    reader.close();

                    String[] tempArray = new String[sidoMap.size()];
                    sidoMap.keySet().toArray(tempArray);
                    sidoArray = tempArray;
                    Log.e("myTag13", sidoMap.toString());
                    Log.e("myTag13", sidoArray.length+"");

                    mainActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            sidoAdapter = new ArrayAdapter(mainActivity, R.layout.spinner_sido, sidoArray);
                            binding.spinnerHomeSido.setAdapter(sidoAdapter);
                        }
                    });
                    Log.e("myTag15", "loadSido() ends");
                    //////시군구 리스트를 리셋하고 로드, 스피너 새로고침/////
                    loadSigungu();

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }//try-catch
                catch (IOException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }//loadSido()


    String sigunguNameTemp;
    String sigunguCodeTemp;

    void loadSigungu(){
        sigunguNameList.clear();
        sigunguNameList.add("전체");
        sigunguCodeList.clear();
        sigunguCodeList.add("");
        new Thread(){

            @Override
            public void run() {
                try {
                    Log.e("myTag20", "loadSigungu() starts");
                    URL url = new URL(endPoint + "/sigungu" +
                            "?serviceKey=" + serviceKey +
                            "&numOfRows="+ 30
                            + (!(sidoCode.equals(""))? "&upr_cd=" + sidoCode : ""));
                    Log.e("myUrl20", url.toString());
                    InputStream inputStream = url.openStream();
                    InputStreamReader reader = new InputStreamReader(inputStream);
                    XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
                    parser.setInput(reader);

                    int eventType = parser.getEventType();
                    while(eventType != XmlPullParser.END_DOCUMENT){
                        switch (eventType){
                            case XmlPullParser.START_DOCUMENT:
                                break;
                            case XmlPullParser.START_TAG:
                                if(parser.getName().equals("orgCd")){ //시군구 코드
                                    parser.next();
                                    sigunguCodeTemp = parser.getText();
                                } else if(parser.getName().equals("orgdownNm")){ //시군구 이름
                                    parser.next();
                                    sigunguNameTemp = parser.getText();
                                }
                                break;
                            case XmlPullParser.END_TAG:
                                if(parser.getName().equals("item")){
                                    Log.e("myTag21",  sigunguNameTemp + sigunguCodeTemp);
                                    sigunguNameList.add(sigunguNameTemp);
                                    sigunguCodeList.add(sigunguCodeTemp);
                                }
                                Log.e("myTag22", sigunguNameList.size()+"");
                                break;
                        }
                        eventType = parser.next();
                    }//while
                    reader.close();

                    mainActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            sigunguAdapter = new ArrayAdapter(mainActivity, R.layout.spinner_sigungu, sigunguNameList);
                            binding.spinnerHomeSigungu.setAdapter(sigunguAdapter);
                        }
                    });
                    Log.e("myTag15", "loadSigungu() ends");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }//try-catch
                catch (IOException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }//loadSigungu


    void loadData(){
        Log.e("myTag00", "loadData() starts"+sidoCode+"/");
        //Permission added
        items.clear();
        recyclerAdapter.notifyDataSetChanged();

        new Thread(){
            @Override
            public void run() {
                //데이터 읽어오기
                //URL -> Stream -> Parser -> 커스텀 파싱 작업
                try {
                    Log.e("myTag01", "loadData() starts"+sidoCode+"/");
                    String urlStr = endPoint + "/abandonmentPublic"
                            +"?serviceKey="+serviceKey
                            +"&numOfRows="+numOfRows
                            + (!(sidoCode.equals(""))? "&upr_cd="+sidoCode : "")
                            + (!(sigunguCode.equals(""))? "&org_cd="+sigunguCode : ""); //urlStr이 final이라 if 사용 불가, 삼항연산자 사용

                    URL url = new URL(urlStr);
                    Log.i("myUrl", url.toString());
                    InputStream inputStream = url.openStream();
                    InputStreamReader reader = new InputStreamReader(inputStream);

                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                    XmlPullParser parser = factory.newPullParser();
                    parser.setInput(reader);

                    Item item = null;

                    int eventType = parser.getEventType();
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        switch (eventType){
                            case XmlPullParser.START_DOCUMENT:
                                //프로그레스바 놓자

                                break;
                            case XmlPullParser.START_TAG:
                                String tagName = parser.getName();
                                if(tagName.equals("item")){
                                    item = new Item();
                                } else if(tagName.equals("happenDt")){
                                    parser.next();
                                    if(item!=null) item.happenDate = parser.getText();
                                } else if(tagName.equals("happenPlace")){
                                    parser.next();
                                    if(item!=null) item.happenPlace = parser.getText();
                                } else if(tagName.equals("popfile")){
                                    parser.next();
                                    if(item!=null) item.imageSrc = parser.getText();
                                } else if(tagName.equals("errMsg")){
                                    parser.next();
                                    errMsg = parser.getText();
                                } else if(tagName.equals("totalCount")){
                                    parser.next();
                                    totalCount = parser.getText();
                                }
                                break;
                            case XmlPullParser.END_TAG:
                                if(parser.getName().equals("item")){
                                    if(item!=null) items.add(item);
                                    itemCount++;
                                }
                                break;
                        }
                        eventType = parser.next();
                    }//while
                    reader.close();
//                    items.clear();

                    mainActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(items.size()==1){
                                binding.recycler.setVisibility(View.GONE);
                                binding.tvNoresult.setVisibility(View.VISIBLE);
                            } else {
                                binding.recycler.setVisibility(View.VISIBLE);
                                binding.tvNoresult.setVisibility(View.GONE);
                            }
                            recyclerAdapter.notifyDataSetChanged();
                        }
                    });
                    Log.e("myTag", "loadData() ends");

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }
                //parser, 그러니까 Stream 안 닫아도 되나?
            }
        }.start();
    }
}
