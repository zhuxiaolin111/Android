package com.huafa.lixianjicha;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;


/**
 * Created by apple on 16/10/28.
 * 底部弹出框的页面
 */

public class FloatBottomActivity extends Activity {

    private Button paizhaobtn;
    private Button xiangcebtn;
    private Button quxiaobtn;
    private String fanhuitemptupian = "0";
    private String pic;
    private String fileName;
    private File file;
    private Uri u;
    private String localTempImgDir;
    private String localTempImgFileName;

    @Override
    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alert_dialog);

        paizhaobtn = (Button) findViewById(R.id.btn_take_photo);
        xiangcebtn = (Button) findViewById(R.id.btn_pick_photo);
        quxiaobtn = (Button) findViewById(R.id.btn_cancel);

        localTempImgFileName = DateFormat.format("yyyyMMdd_hhmmss",Calendar.getInstance(Locale.CHINA)) + ".jpg";
        localTempImgDir = "/sdcard/Image/";

        paizhaobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(intent, 2);

                String sdStatus = Environment.getExternalStorageState();
                if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
                    Log.i("TestFile",
                            "SD card is not avaiable/writeable right now.");
                    return;
                }
                try {
                    File dir=new File(Environment.getExternalStorageDirectory() + "/"+localTempImgDir);
                    if(!dir.exists())dir.mkdirs();

                    Intent intent=new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    File f=new File(dir, localTempImgFileName);//localTempImgDir和localTempImageFileName是自己定义的名字
                    Uri u=Uri.fromFile(f);
                    intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, u);
                    startActivityForResult(intent, 2);
                } catch (ActivityNotFoundException e) {
// TODO Auto-generated catch block
                    Toast.makeText(FloatBottomActivity.this, "没有找到储存目录",Toast.LENGTH_LONG).show();
                }
            }
        });

        xiangcebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);

            }
        });
        quxiaobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    @SuppressLint("SdCardPath")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //获取图片路径
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
                String[] filePathColumns = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);

                if (c.moveToFirst()){
                    int column_index = c.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    String imagePath = c.getString(column_index);
//                    将生成的图片回传
                    Intent intent = new Intent();
                    intent.putExtra("result", imagePath);
                    setResult(1001, intent);
                }
                finish();
                c.close();

            }


        //获取拍照的图片的回调方法
        if (resultCode == Activity.RESULT_OK) {
            if (data == null){
                //由于指定了目标uri，存储在目标uri，intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                // 通过目标uri，找到图片
                // 对图片的缩放处理
                // 操作
                File f=new File(Environment.getExternalStorageDirectory() +localTempImgDir+localTempImgFileName);
                try {
                        Uri uri = Uri.parse(android.provider.MediaStore.Images.Media.insertImage(getContentResolver(),
                                        f.getAbsolutePath(), null, null));
                        Intent intent = new Intent();
                        intent.setData(uri);
                        setResult(1002, intent);
                        finish();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return true;
    }
}
