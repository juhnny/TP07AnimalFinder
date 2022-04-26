package com.juhnny.tp07animalfinder;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.juhnny.tp07animalfinder.databinding.FragmentMyinfoBinding;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MyinfoFragment extends Fragment {

    MainActivity mainActivity;
    FragmentMyinfoBinding b;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        b = FragmentMyinfoBinding.inflate(inflater);
        return b.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mainActivity = (MainActivity) getActivity();
        mainActivity.setSupportActionBar(b.toolbar);

        //전단지 비율 조절 - A4 비율
        Display display = mainActivity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        b.layoutPoster.getLayoutParams().width = (int)(size.x * 0.95);
        b.layoutPoster.getLayoutParams().height = (int)(size.x * 0.95 * 1.414);

        //입력된 항목 반영
        b.ivFirst.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            resultLauncher1.launch(intent);
        });

        b.ivSecond.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            resultLauncher2.launch(intent);
        });

        b.etTitle.addTextChangedListener(new MyTextWatcher(b.tvPosterTitle));

        b.etAnimalType.addTextChangedListener(new MyTextWatcher2(b.tvTypeEtc));
        b.etSex.addTextChangedListener(new MyTextWatcher2(b.tvTypeEtc));
        b.etAge.addTextChangedListener(new MyTextWatcher2(b.tvTypeEtc));
        b.etColor.addTextChangedListener(new MyTextWatcher2(b.tvTypeEtc));

        b.etDate.addTextChangedListener(new MyTextWatcher3(b.tvDatePlace));
        b.etPlace.addTextChangedListener(new MyTextWatcher3(b.tvDatePlace));

        b.etDetail.addTextChangedListener(new MyTextWatcher(b.tvDetail));
        b.etExtra.addTextChangedListener(new MyTextWatcher(b.tvExtra));
        b.etAppeal1.addTextChangedListener(new MyTextWatcher(b.tvAppeal1));
        b.etAppeal2.addTextChangedListener(new MyTextWatcher(b.tvAppeal2));

        b.btnSavePoster.setOnClickListener(v -> savePoster());

        //권한 획득
//        mainActivity.onRequestPermissionsResult(); //오버라이드를 액티비티가 아니라 프래그먼트 여기저기서 하면 충돌하지 싶네.. 맞나?
        //어차피 액티비티 클래스 밖에서는 상속 관계가 아니고서는 오버라이딩을 할 수가 없구나.
        //request는 각 프래그먼트에서 하고 onRequestPermissionsResult()만 액티비티에 몰아서 처리하는 식으로 해보자.
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            //API29, Q버전 이상부터는 공용 외부저장소에 쓰기 권한 없이 쓰기 가능. 읽을 땐 Media Store가 알아서 권한 물어보는 듯
        } else {
            String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if(mainActivity.checkSelfPermission(permissions[0]) == PackageManager.PERMISSION_DENIED){
            mainActivity.requestPermissions(permissions, mainActivity.REQUEST_CODE_EXTERNAL_STORAGE);
            }
        }

    }


    ActivityResultLauncher resultLauncher1 = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
       if(result.getResultCode() == mainActivity.RESULT_OK){
           Intent intent = result.getData();
           Uri imgUri = intent.getData();
           b.ivFirst.setScaleType(ImageView.ScaleType.CENTER_CROP);
           Glide.with(mainActivity).load(imgUri).into(b.ivFirst);
       }
    });

    ActivityResultLauncher resultLauncher2 = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if(result.getResultCode() == mainActivity.RESULT_OK){
            Intent intent = result.getData();
            Uri imgUri = intent.getData();
            Glide.with(mainActivity).load(imgUri).into(b.ivSecond);
        }
    });

    void savePoster(){
        //뷰 캡쳐
        View targetView = b.layoutPoster;
        targetView.destroyDrawingCache();
        targetView.buildDrawingCache();
        Bitmap bitmap = targetView.getDrawingCache();
        //저장 경로
        OutputStream outputStream = null;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            Toast.makeText(mainActivity, "Q버전 이상입니다.", Toast.LENGTH_SHORT).show();
            ContentResolver resolver = mainActivity.getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, System.currentTimeMillis()+".jpg");
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);

            //상대경로인 Uri 사용 - 예)content://media/external/images/media/34
            Uri imgUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            Toast.makeText(mainActivity, "Uri: "+imgUri.toString(), Toast.LENGTH_SHORT).show();
            try {
                outputStream = resolver.openOutputStream(imgUri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            //아마 퍼미션 필요할 것
            //절대 경로를 사용
            Toast.makeText(mainActivity, "Q버전 미만입니다.", Toast.LENGTH_SHORT).show();
            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath(); //File의 경로 String으로 받기
            //파일 이름
            String fileName = "IMG_" + new SimpleDateFormat("yyyyMMddhhmmss", Locale.KOREA).format(new Date()) + ".png";
            File imageFile = new File(path, fileName);

            try {
                outputStream = new FileOutputStream(imageFile);
            } catch (FileNotFoundException e) {
                Toast.makeText(mainActivity, "error:"+e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        try {
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast.makeText(mainActivity, "Saved the poster", Toast.LENGTH_SHORT).show();
    }

    class MyTextWatcher implements TextWatcher{

        TextView targetTextView;

        public MyTextWatcher(TextView textView) {
            this.targetTextView = textView;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            targetTextView.setText(charSequence.toString());
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }

    class MyTextWatcher2 extends MyTextWatcher{

        public MyTextWatcher2(TextView targetTextView) {
            super(targetTextView);
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String strTypeEtc = b.etAnimalType.getText().toString() +"/ "
                    + b.etSex.getText().toString() +"/ "
                    + b.etAge.getText().toString() +"/ "
                    + b.etColor.getText().toString();
            targetTextView.setText(strTypeEtc);
        }
    }

    class MyTextWatcher3 extends MyTextWatcher{

        public MyTextWatcher3(TextView textView) {
            super(textView);
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String str = b.etDate.getText().toString() + ", "
                    + b.etPlace.getText().toString();
            targetTextView.setText(str);
        }
    }

}
