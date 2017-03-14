package com.huafa.lixianjicha;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.huafa.model.tijiao_result_model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by chensiqi on 2016/11/8.
 * 在这个页面进行数据上传
 */

public class Show_Tijiao_Data extends baseActivty {
  /*  Handler handler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                for (int i = 0; i < result_arr.size() ; i++) {
                String s=result_arr.get(i);
                    if(!s.equals(0)){
                        Toast.makeText(Show_Tijiao_Data.this, "第"+i+1+"条数据提交失败", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    };*/

    private tijiao_result_model mTijiao_result_model;
    private int result_i;
    private String result_str;
    private ArrayList<String> result_arr;

    private ListView show_data_list;
    private Cursor cursor;

    //提交全部数据按钮
    private Button tijiaoquanbushuju;

    private DBManager mgr;

    //存放获取经纬度方法的字符串
    private String locationProvider;


    //存放从数据库中取出数据的ArrayList
    private ArrayList<String> roomid_arrayList = new ArrayList<>();
    private ArrayList<String> type_arrayList = new ArrayList<>();
    private ArrayList<String> item_arrayList = new ArrayList<>();
    private ArrayList<String> remark_arrayList = new ArrayList<>();
    private ArrayList<String> image1_arrayList = new ArrayList<>();
    private ArrayList<String> image2_arrayList = new ArrayList<>();
    private ArrayList<String> image3_arrayList = new ArrayList<>();
    private ArrayList<String> operator_arrayList = new ArrayList<>();
    LocationManager locationManager;


    @Override
    protected void initContentView(Bundle savedInstanceState) {
        // super.onCreate(savedInstanceState);
        setContentView(R.layout.show_tijiao_data);

        //实体化DBManager
        mgr = new DBManager(this);
        // 启动服务
        Intent intent = new Intent(this, LocationService.class);
        startService(intent);


        tijiaoquanbushuju = (Button) findViewById(R.id.tijiaoquanbushuju_btn);

        //设置提交数据按钮的点击事件
        tijiaoshuju();

        //取得每行的集合cursor
        //cursor为所有"shifoujicha"的值为"no"的行
        cursor = mgr.search("no", "shifoujicha");

        //将查询出来的数据存进该页的ListView中
        put_data_into_listview();

    }


    //设置本页的ListView
    private void put_data_into_listview() {
        List<Map<String, String>> listItems = new ArrayList<>();
        //光标逐个下移,遍历Cursor,并将数据存入ListView
        while (cursor.moveToNext()) {
            Map<String, String> item = new HashMap<>();

            item.put("xingming", cursor.getString(cursor.getColumnIndex("Operator")));
            item.put("type", cursor.getString(cursor.getColumnIndex("Type")));
            item.put("item", cursor.getString(cursor.getColumnIndex("Item")));
            item.put("roomid", cursor.getString(cursor.getColumnIndex("RoomID")));
            listItems.add(item);
        }
        SimpleAdapter simpleadapter = new SimpleAdapter(this, listItems, R.layout.show_data,
                new String[]{"xingming", "type", "item", "roomid"},
                new int[]{R.id.xingming_data, R.id.type_data, R.id.item_data, R.id.roomid_data});
        final ListView list = (ListView) findViewById(R.id.zhanshitijiaoshuju_list);
        list.setAdapter(simpleadapter);
    }


    //通过接口上传数据
    public void shangchuanshuju() {
        //搜索所有"shifoujicha"值是"no"的
        Cursor c = mgr.search("no", "shifoujicha");
        while (c.moveToNext()) {
            roomid_arrayList.add(c.getString(c.getColumnIndex("RoomID")));
            type_arrayList.add(c.getString(c.getColumnIndex("Type")));
            item_arrayList.add(c.getString(c.getColumnIndex("Item")));
            remark_arrayList.add(c.getString(c.getColumnIndex("Remark")));
            image1_arrayList.add(c.getString(c.getColumnIndex("media1")));
            image2_arrayList.add(c.getString(c.getColumnIndex("media2")));
            image3_arrayList.add(c.getString(c.getColumnIndex("media3")));
            operator_arrayList.add(c.getString(c.getColumnIndex("Operator")));
        }
        //发送上传请求
        if (c.getCount() != 0) {

            for (int i = 0; i < c.getCount(); i++) {
                OkHttpClient okhttpclient = new OkHttpClient();
                String ext1 = "";
                String ext2 = "";
                String ext3 = "";
                if (image1_arrayList.get(i) != "") {
                    ext1 = "jpg";
                }
                if (image2_arrayList.get(i) != "") {
                    ext2 = "jpg";
                }
                if (image3_arrayList.get(i) != "") {
                    ext3 = "jpg";
                }
                RequestBody formbody = new FormBody.Builder()
                        .add("method", "JC_WebSupervise")
                        .add("Company", getString(R.string.公司名称))
                        .add("RoomID", roomid_arrayList.get(i))
                        .add("Num", "")
                        .add("Type", type_arrayList.get(i))
                        .add("Item", item_arrayList.get(i))
                        .add("Remark", remark_arrayList.get(i))
                        .add("media1", "")
                        .add("media2", "")
                        .add("media3", "")
                        .add("Image1", image1_arrayList.get(i))
                        .add("image2", image2_arrayList.get(i))
                        .add("image3", image3_arrayList.get(i))
                        .add("ext1", ext1)
                        .add("ext2", ext2)
                        .add("ext3", ext3)
                        .add("Latitude", String.valueOf(Common.latitude))
                        .add("Longitude", String.valueOf(Common.longitude))
                        .add("Operator", operator_arrayList.get(i))
                        .build();
                Request request = new Request.Builder()
                        .url(getString(R.string.上传数据接口))
                        .post(formbody)
                        .build();
                okhttp3.Call call = okhttpclient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(okhttp3.Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(okhttp3.Call call, Response response) throws IOException {
                        result_str = response.body().string();
                        Log.d("result",result_str);
               /*         mTijiao_result_model = JSON.parseObject(result_str, tijiao_result_model.class);
                        result_i = mTijiao_result_model.getData().size();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                for (int i = 0; i < result_i; i++) {
                                    result_arr.add(mTijiao_result_model.getData().get(i).getResult_code());
                                    Log.d("result", result_str);
                                }
                                //做一个Handle,已在数据请求完以后更新4个显示基础数据的按钮
                                Message message = new Message();
                                message.what = 1;
                                handler.sendEmptyMessage(1);
                            }
                        });*/

                    }
                });
            }
            new AlertDialog.Builder(Show_Tijiao_Data.this)
                    .setTitle("温馨提示")
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setMessage("数据提交成功！")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //销毁页面
                            //将数据库中的已提交的数据的"shifoujicha"项的"no"值改为"yes"
                            mgr.update_isflag();
                            finish();
                        }
                    })
                    .show();
        } else {
            new AlertDialog.Builder(Show_Tijiao_Data.this)
                    .setTitle("温馨提示")
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setMessage("无稽查数据提交！")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //销毁页面
                            finish();
                        }
                    })
                    .setNegativeButton("取消", null)
                    .show();

        }
    }

    //设置提交数据的点击事件
    public void tijiaoshuju() {
        tijiaoquanbushuju.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkAvailable(this)) {
                    //获取当前经纬度坐标
                    //   get_jingdu_And_Weidu();
                    //提交数据
                    shangchuanshuju();
                } else {
                    new AlertDialog.Builder(Show_Tijiao_Data.this)
                            .setTitle("注意")
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .setMessage("请到网络信号良好的区域提交数据！")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //销毁页面
                                    finish();
                                }
                            })
                            .setNegativeButton("取消", null)
                            .show();
                }
            }
        });
    }

    //判断是否有网络连接
    public boolean isNetworkAvailable(View.OnClickListener context) {
        ConnectivityManager connectivity = (ConnectivityManager) Show_Tijiao_Data.this
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    // 当前所连接的网络可用
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return true;
    }

}
