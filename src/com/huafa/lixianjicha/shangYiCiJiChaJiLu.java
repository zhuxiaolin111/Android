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
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.alibaba.fastjson.JSON;
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

public class shangYiCiJiChaJiLu extends baseActivty {




    private ListView show_data_list;
    private Cursor cursor;

    //提交全部数据按钮
    private Button tijiaoquanbushuju,qingkongquanbu;
    //查询上一次稽查记录
    private Button shangyicijichajilu;

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
    private tijiao_result_model mTijiao_result_model;
    SimpleAdapter simpleadapter;
    ListView list;
    List listItems;
    Map<String, String> item;
    @Override
    protected void initContentView(Bundle savedInstanceState) {
        // super.onCreate(savedInstanceState);
        setContentView(R.layout.shngyicijichajilu);
        // 启动服务
        Intent intent = new Intent(this, LocationService.class);
        startService(intent);

        //实体化DBManager
        mgr = new DBManager(this);
        final Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.what==1){

                }
            }
        };
     final Thread thread=new Thread(new Runnable() {
         @Override
         public void run() {
             Message message=new Message();
             message.what=1;
               handler.sendMessage(message);
         }
     });


        tijiaoquanbushuju = (Button) findViewById(R.id.tijiaoquanbushuju_btn);
         qingkongquanbu= (Button) findViewById(R.id.qingkongquanbu_btn);
        qingkongquanbu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(shangYiCiJiChaJiLu.this)
                        .setTitle("温馨提示")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setMessage("所有稽查记录数据将不可恢复！")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                listItems.removeAll(listItems);
                                simpleadapter.notifyDataSetChanged();
                                list.setAdapter(simpleadapter);
                                mgr.delete();
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();


            }
        });
        //设置提交数据按钮的点击事件
        tijiaoshuju();
        //取得每行的集合cursor
        cursor = mgr.search("isflag", "shifoujicha");
        //将查询出来的数据存进该页的ListView中
        put_data_into_listview();

    }



    //设置本页的ListView
    private void put_data_into_listview() {

        listItems = new ArrayList<>();
        //光标逐个下移,遍历Cursor,并将数据存入ListView
        while (cursor.moveToNext()) {
           item = new HashMap<>();

            item.put("xingming", cursor.getString(cursor.getColumnIndex("Operator")));
            item.put("type", cursor.getString(cursor.getColumnIndex("Type")));
            item.put("item", cursor.getString(cursor.getColumnIndex("Item")));
            item.put("roomid", cursor.getString(cursor.getColumnIndex("RoomID")));
            listItems.add(item);
        }
        simpleadapter = new SimpleAdapter(this, listItems, R.layout.show_data,
                new String[]{"xingming", "type", "item", "roomid"},
                new int[]{R.id.xingming_data, R.id.type_data, R.id.item_data, R.id.roomid_data});
        list = (ListView) findViewById(R.id.zhanshitijiaoshuju_list);
        list.setAdapter(simpleadapter);
    }

    //通过接口上传数据
    public void shangchuanshuju() {
        Cursor c = mgr.search("isflag", "shifoujicha");
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
                        .add("Company", getString(R.string.测试))
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
                        .add("Longitude", String.valueOf(Common.latitude))
                        .add("Latitude", String.valueOf(Common.longitude))
                        .add("Operator",operator_arrayList.get(i))
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
                        String str = response.body().string();
                        mTijiao_result_model = JSON.parseObject(str, tijiao_result_model.class);
                        int    result = mTijiao_result_model.getData().size();
                        for (int i = 0; i < result; i++) {
                            // jichaxiangmu_arr.add(mTijiao_result_model.getData().get(i).getCPeccancyname());
                        }
                        System.out.println(str);


                    }
                });
            }
            new AlertDialog.Builder(shangYiCiJiChaJiLu.this)
                    .setTitle("温馨提示")
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setMessage("数据提交成功！")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //销毁页面
                            finish();
                        }
                    })
                    .show();
        } else {
            new AlertDialog.Builder(shangYiCiJiChaJiLu.this)
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
                    new AlertDialog.Builder(shangYiCiJiChaJiLu.this)
                            .setTitle("温馨提示")
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .setMessage("重新提交将覆盖上一次稽查记录（所有稽查记录将不可恢复）！！！")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //提交数据
                                    shangchuanshuju();

                                    //将数据库中的已提交的数据的"shifoujicha"项的"isfilag"值改为"yes"
                                    mgr.update();
                                }
                            })
                            .setNegativeButton("取消", null)
                            .show();


                 /*   //弹出提示框,提示是否提交数据
                    new AlertDialog.Builder(Show_Tijiao_Data.this)
                            .setTitle("注意")
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .setMessage("是否确定提交数据（提交后该列表中将不再显示已提交的数据，如要更改已提交数据，请重新稽查并再次点击提交）")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setNegativeButton("取消", null)
                            .show();*/

                } else {
                    new AlertDialog.Builder(shangYiCiJiChaJiLu.this)
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
        ConnectivityManager connectivity = (ConnectivityManager) shangYiCiJiChaJiLu.this
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
