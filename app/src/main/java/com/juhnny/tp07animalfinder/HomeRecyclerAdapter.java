package com.juhnny.tp07animalfinder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.juhnny.tp07animalfinder.databinding.FragmentHomeTab1Binding;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class HomeRecyclerAdapter extends RecyclerView.Adapter<HomeRecyclerAdapter.VH> {

    ArrayList<Item> items;
    Context context;

    int deviceWidth;
    int deviceHeight;

    public HomeRecyclerAdapter(Context context, ArrayList<Item> items) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.recycler_main, parent, false);

        //Display display = getWindowManager().getDefaultDisplay();  // in Activity
        /* getActivity().getWindowManager().getDefaultDisplay() */ // in Fragment
        Display display = ((Activity)context).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getRealSize(size); // or getSize(size)
        deviceWidth = size.x;
        deviceHeight = size.y;

        //가로,세로 dp 구하는 법
//        DisplayMetrics metrics = new DisplayMetrics();
//        display.getMetrics(metrics);
//
//        float density = ((Activity)context).getResources().getDisplayMetrics().density;
//        float dpWidth = metrics.widthPixels / density;
//        float dpHeight = metrics.heightPixels / density;
//
//        Log.d("myTag", "density:"+density+", dpWidth:"+dpWidth+", dpHeight:"+dpHeight);

        return new VH(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Item item = items.get(position);

        //각 View에 item의 데이터 연결
        //ImageView 설정
        new Thread(new Runnable() {
            @Override
            public void run() {
                ImageView iv = holder.ivImage;
                iv.setVisibility(View.INVISIBLE);
                String imgSrc = item.imageSrc;

                try {
                    //TODO 프로그레스바 추가
                    URL imgUrl = new URL(imgSrc);
                    InputStream inputStream = imgUrl.openStream();
                    Bitmap bm = BitmapFactory.decodeStream(inputStream);
                    Activity activity = (Activity) context;
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            ImageView iv2 = holder.ivImage2;

                            int width = bm.getWidth();
                            int height = bm.getHeight();
                            if(height > width) { //사진의 높이가 너비보다 크면
                                iv.setAdjustViewBounds(true);
                                iv.getLayoutParams().width = RecyclerView.LayoutParams.MATCH_PARENT;
                                iv.getLayoutParams().height = RecyclerView.LayoutParams.WRAP_CONTENT;
                                iv.requestLayout();
                                iv.setImageBitmap(bm);
//                                holder.tvHappenDate.setText("height>=width "+deviceHeight+"/"+width+"/"+height
//                                        +" //M"+iv.getMaxHeight()+"/"+iv.getHeight());
                            } else { //너비가 높이보다 크면
                                iv.setAdjustViewBounds(false);
                                iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                iv.getLayoutParams().width = deviceWidth/2;
                                iv.getLayoutParams().height = deviceWidth/2;
                                iv.requestLayout();
                                iv.setImageBitmap(bm);
//                                holder.tvHappenDate.setText("width>height "+deviceWidth+"/"+width+"/"+height
//                                +" //M"+iv.getMaxHeight()+"/"+iv.getHeight());
                            }
                            iv.setVisibility(View.VISIBLE);
                        }
                    });
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        holder.tvHappenDate.setText(item.happenDate);
        holder.tvHappenPlace.setText(item.happenPlace);
//        Toast.makeText(context, "onBind()", Toast.LENGTH_SHORT).show();

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, HomeItemActivity.class);
            intent.putExtra("items", items);
            intent.putExtra("position", position);
            context.startActivity(intent);
        });
    }//onBindViewHolder

    @Override
    public int getItemCount() {
//        Toast.makeText(context, "itemCount: "+items.size(), Toast.LENGTH_SHORT).show();
        return items.size();
    }

    class VH extends RecyclerView.ViewHolder{ //////////////

        ImageView ivImage;
//        ImageView ivImage2;
        TextView tvHappenDate;
        TextView tvHappenPlace;

        public VH(@NonNull View itemView) {
            super(itemView);
            this.ivImage = itemView.findViewById(R.id.iv_image);
//            this.ivImage2 = itemView.findViewById(R.id.iv_image2);
            this.tvHappenDate = itemView.findViewById(R.id.tv_happendate);
            this.tvHappenPlace = itemView.findViewById(R.id.tv_happenplace);


        }
    }/////////////////////


}
