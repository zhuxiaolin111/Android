package com.huafa.lixianjicha;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.huafa.model.Buliding_info_model;
import com.huafa.model.Jicha_leibie_model;
import com.huafa.model.Jicha_xiangmu_model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class Building_Info_Activity extends baseActivty {

    android.os.Handler handler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        zonghushu.setText(String.valueOf(s));
                        //停供数
                        for (int i = 0; i < s; i++) {
                            if ("当年停".equals(buliding_info_model.getData().get(i).getC_Mark_Now())) {
                                jinniantinggong_Num += 1;
                            }
                        }
                        jin_nian_tinggongshu.setText(String.valueOf(jinniantinggong_Num));
                        for (int i = 0; i < s; i++) {
                            if ("当年开".equals(buliding_info_model.getData().get(i).getC_Mark_Now())) {
                                jinnianhuifugongre_Num += 1;
                            }
                        }
                        jin_nian_huifugongreshu.setText(String.valueOf(jinnianhuifugongre_Num));

                    }
                });
            }
            if (msg.what == 2) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        jing_nian_tinggongshu.setText(String.valueOf(wangniantinggong_Num));
                    }
                });
            }
        }
    };

    private Button tijiaoquanbushujuBtn;

    //获取h5页面传递过来的数据
    private String str1,str2;

    //红绿灯按钮的flag
    private int flag_hong = 0;
    private int flag_lan = 0;
    private int flag_huang = 0;


    //定义两次点击间隙时间
    private long mExitTime;


    //更新楼座内客户信息所需数据

    private ArrayList<String> bianhao_arr;
    private ArrayList<String> xingming_arr;
    private ArrayList<String> zhuangtai_arr;
    private ArrayList<Double> mianji_arr;
    private ArrayList<String> qiankuan_arr;
    private ArrayList<String> fangjianhao_arr;
    private ArrayList<String> youwubianhua_arr;

    //看往年数据按钮
    //看总计按钮
    private Button wangnian_tinggong;
    private Button zongji;

    //稽查项目数据字符串
    private String jicha_xiangmu_str;
    //稽查类别数据字符串
    private String jicha_leibie_str;


    //网络下载的楼座用户总数据
    private Buliding_info_model buliding_info_model;
    private int s = 0;
    private int jinniantinggong_Num;
    private int jinnianhuifugongre_Num;
    private int wangniantinggong_Num;

    //4个显示基础数据的按钮
    private TextView zonghushu;
    private TextView jin_nian_tinggongshu;
    private TextView jin_nian_huifugongreshu;
    private TextView jing_nian_tinggongshu;


    //用户数据字符串
    private String yonghushuju_Str;
    private String wangnianyonghushuju_Str;

    //往年数据下载
    private int wangnianting_s;
    private ArrayList<String> wangniantinggong_arr;
    private Buliding_info_model wangniantinggong_model;

    //稽查项目请求出来的数据
    private int jichaxiangmu_s;
    private ArrayList<String> jichaxiangmu_arr;
    private Jicha_xiangmu_model jicha_xiangmu_model;

    //稽查类别请求出来的数据
    private int jichaleibie_s;
    private ArrayList<String> jichaleibie_arr;
    private Jicha_leibie_model jicha_leibie_model;


    //显示listView的相关变量
    private List<Map<String, String>> listItems;
    private SimpleAdapter simpleadapter;
    private ListView list;


    //红绿灯按钮
    private Button tinggongBtn;
    private Button huifuBtn;
    private Button qiangzhihuifuduanguanBtn;

    //设置日历以取得当前月份和年份
    private java.util.Calendar c;
    private int mMonth;
    private int mYear;


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    /* 进度条 */
    private ProgressDialog myDialog;
    int count=0;//存储进度条进度
    @Override
    protected void initContentView(Bundle savedInstanceState) {
       // super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building__info_);

        rec();

        str1 = new String(this.getIntent().getStringExtra("buildid"));
        str2=new String(this.getIntent().getStringExtra("operator"));
        Common.operator=str2;

        //显示基础数据的4个按钮
        zonghushu = (TextView) findViewById(R.id.zonghushu);
        jin_nian_tinggongshu = (TextView) findViewById(R.id.jin_nian_tinggongshu);
        jin_nian_huifugongreshu = (TextView) findViewById(R.id.jin_nian_huifugongreshu);
        jing_nian_tinggongshu = (TextView) findViewById(R.id.jing_nian_tinggongshu);

        //红绿灯按钮
        tinggongBtn = (Button) findViewById(R.id.tinggongBtn);
        huifuBtn = (Button) findViewById(R.id.huifuBtn);
        qiangzhihuifuduanguanBtn = (Button) findViewById(R.id.qiangzhiuifuduanguanBtn);

        //设置三个按钮的点击事件
        honglvdeng_btn_click();

        //提交全部数据的按钮
        tijiaoquanbushujuBtn = (Button) findViewById(R.id.tijiaoquanbushuju);

        //往年停供和总计按钮初始化
        wangnian_tinggong = (Button) findViewById(R.id.wangnianshuju);
        zongji = (Button) findViewById(R.id.zongji);

        bianhao_arr = new ArrayList<>();
        xingming_arr = new ArrayList<>();
        zhuangtai_arr = new ArrayList<>();
        mianji_arr = new ArrayList<>();
        qiankuan_arr = new ArrayList<>();
        fangjianhao_arr = new ArrayList<>();
        youwubianhua_arr = new ArrayList<>();

        //初始化稽查项目和稽查类别arraylist按钮
        jichaxiangmu_arr = new ArrayList<>();
        jichaleibie_arr = new ArrayList<>();

        //初始化当前月份和年份
        c = java.util.Calendar.getInstance();
        mMonth = c.get(java.util.Calendar.MONTH) + 1;
        mYear = c.get(java.util.Calendar.YEAR);

        //提交全部数据按钮的点击事件
        tijiaoshuju();


        //往年停供的数据模型解析
        wangniantinggong_jiexi();
        //取得楼座中客户的个人缴费情况
        building_info_postAsynHttp();
        //取得稽查选项
        Jichaxiangmu_PostAsyncHttp();
        //取得稽查类别
        Jichaleibie_PostAsyncHttp();
        //往年停供和总计按钮点击事件
        wangniantinggong_zongji_click();

        //判断按钮是否可以点击(ListView为空时设为无法点击)


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }




    /**
     * 圆形进度条测试..
     */
    public void circle() {
       myDialog = new ProgressDialog(Building_Info_Activity.this); // 获取对象
        myDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // 设置样式为圆形样式
        myDialog.setTitle("友情提示"); // 设置进度条的标题信息
        myDialog.setMessage("数据加载中，请稍后..."); // 设置进度条的提示信息
        myDialog.setIcon(android.R.drawable.ic_dialog_info); // 设置进度条的图标
        myDialog.setIndeterminate(false); // 设置进度条是否为不明确
        myDialog.setCancelable(true); // 设置进度条是否按返回键取消

        // 为进度条添加确定按钮 ， 并添加单机事件
        myDialog.setButton("确定", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                myDialog.cancel(); // 撤销进度条
            }
        });

        myDialog.show(); // 显示进度条
    }

    /**
     * 矩形进度条测试...
     */
    public void rec() {

        myDialog = new ProgressDialog(Building_Info_Activity.this); // 得到一个对象
        myDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL); // 设置为矩形进度条
        myDialog.setTitle("提示");
        myDialog.setMessage("离线数据加载中，请稍后...");
        myDialog.setIcon(android.R.drawable.ic_dialog_info);
        myDialog.setIndeterminate(false); // 设置进度条是否为不明确
        myDialog.setCancelable(true);
        myDialog.setMax(100); // 设置进度条的最大值
        myDialog.setProgress(0); // 设置当前默认进度为 0
        myDialog.setSecondaryProgress(1000); // 设置第二条进度值为100

        // 为进度条添加取消按钮
        myDialog.setButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                myDialog.cancel();
            }
        });
        myDialog.show(); // 显示进度条
        new Thread() {
            public void run() {
                while (count <= 100) {
                    myDialog.setProgress(count+=2);
                    try {
                        Thread.sleep(100);  //暂停 0.1秒
                    } catch (Exception e) {
                        Log.i("msg","线程异常..");
                    }
                }
            }
        }.start();

    }

    //往年数据按钮点击事件
    public void wangniantinggong_zongji_click() {

        wangnian_tinggong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(Building_Info_Activity.this, Wangnian_shuju.class);
                intent.putExtra("wangnian_shuju_json_str", wangnianyonghushuju_Str);
                intent.putExtra("wangnian_jicha_xiangmu_str", jichaxiangmu_arr);
                intent.putExtra("wangnian_jicha_leibie_str", jichaleibie_arr);
                startActivity(intent);

            }
        });


        zongji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(Building_Info_Activity.this, Zongji_List.class);
                intent.putExtra("wangnian_shuju_json_str", wangnianyonghushuju_Str);
                intent.putExtra("wangnian_jicha_xiangmu_str", jichaxiangmu_arr);
                intent.putExtra("wangnian_jicha_leibie_str", jichaleibie_arr);
                //今年的数据
                intent.putExtra("jinnian_shuju_json_str", yonghushuju_Str);
                startActivity(intent);
            }
        });
    }


    //提交全部数据的点击事件
    public void tijiaoshuju() {

        tijiaoquanbushujuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Building_Info_Activity.this, Show_Tijiao_Data.class);
                Building_Info_Activity.this.startActivity(intent);
            }
        });
    }

    //判断是否有网络连接
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
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


    //往年停供数数据下载
    public void wangniantinggong_jiexi() {
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("method", "JC_GetBuildingRoomList")
                .add("Company", getString(R.string.测试))
                .add("BuildingID", str1)
                .add("QueryType", "")
                .add("ChargeYear", String.valueOf(mYear - 1) + "-" + String.valueOf(mYear))
                .build();
        Request request = new Request.Builder()
                .url(getString(R.string.往年停供数数据下载接口))
                .post(formBody)
                .build();

        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                wangnianyonghushuju_Str = response.body().string();
                wangniantinggong_model = JSON.parseObject(wangnianyonghushuju_Str, Buliding_info_model.class);

                wangnianting_s = wangniantinggong_model.getData().size();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < wangnianting_s; i++) {
                            if ("停".equals(wangniantinggong_model.getData().get(i).getC_Mark()) &&
                                    "无变化".equals(wangniantinggong_model.getData().get(i).getC_Mark_Now())) {
                                wangniantinggong_Num += 1;
                            }
                        }
                    }
                });
                Message message = new Message();
                message.what = 2;
                handler.sendEmptyMessage(2);
            }
        });
    }

    //稽查项目数据下载
    public void Jichaxiangmu_PostAsyncHttp() {
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("method", "JC_GetSuperviseItem")
                .add("Company", getString(R.string.测试))
                .build();
        Request request = new Request.Builder()
                .url(getString(R.string.稽查项目数据下载接口))
                .post(formBody)
                .build();

        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                jicha_xiangmu_str = response.body().string();
                jicha_xiangmu_model = JSON.parseObject(jicha_xiangmu_str, Jicha_xiangmu_model.class);
                jichaxiangmu_s = jicha_xiangmu_model.getData().size();
                for (int i = 0; i < jichaxiangmu_s; i++) {
                    jichaxiangmu_arr.add(jicha_xiangmu_model.getData().get(i).getCPeccancyname());
                }


            }
        });
    }


    //稽查类别数据下载
    public void Jichaleibie_PostAsyncHttp() {
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("method", "JC_GetSuperviseType")
                .add("Company", getString(R.string.测试))
                .build();
        Request request = new Request.Builder()
                .url(getString(R.string.稽查类别数据下载接口))
                .post(formBody)
                .build();

        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                jicha_leibie_str = response.body().string();
                jicha_leibie_model = JSON.parseObject(jicha_leibie_str, Jicha_leibie_model.class);
                jichaleibie_s = jicha_leibie_model.getData().size();
                for (int i = 0; i < jichaleibie_s; i++) {
                    jichaleibie_arr.add(jicha_leibie_model.getData().get(i).getCPeccancytype());
                }


            }
        });
    }


    //点击更改背景图片方法
    private void setBackgroundOfVersion(View view, Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            //Android系统大于等于API16，使用setBackground
            view.setBackground(drawable);
        } else {
            //Android系统小于API16，使用setBackground
            view.setBackgroundDrawable(drawable);
        }
    }


    //红绿灯按钮的点击事件
    public void honglvdeng_btn_click() {

        tinggongBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag_hong += 1;
                flag_huang = 0;
                flag_lan = 0;
                if (flag_hong % 2 == 1) {
                    //设置点击时的按钮的背景图片
                    Resources res = getResources();
                    Drawable drawable = res.getDrawable(R.drawable.click_ef674d);
                    setBackgroundOfVersion(tinggongBtn, drawable);

                    //同时将另外两个按钮变回点击前的按钮
                    //将停供（蓝色）按钮改回原来的颜色
                    Drawable drawableL = res.getDrawable(R.drawable.normol_84a2d4);
                    setBackgroundOfVersion(huifuBtn, drawableL);
                    //将恢复（黄色）按钮改回原来的颜色
                    Drawable drawableY = res.getDrawable(R.drawable.fadd67);
                    setBackgroundOfVersion(qiangzhihuifuduanguanBtn, drawableY);


                    listItems = new ArrayList<>();
                    int ss = 0;
                    for (int i = 0; i < s; i++) {
                        if ("当年停".equals(buliding_info_model.getData().get(i).getC_Mark_Now())) {
                            Map<String, String> item = new HashMap<>();

                            item.put("bianhao", bianhao_arr.get(i));
                            item.put("xingming", xingming_arr.get(i));
                            item.put("famenzhuangtai", zhuangtai_arr.get(i));
                            item.put("mianji", mianji_arr.get(i).toString());
                            item.put("qianfeijine", qiankuan_arr.get(i));
                            item.put("fangjianhao", fangjianhao_arr.get(i));
                            item.put("youwubianhua", youwubianhua_arr.get(i));
                            listItems.add(item);
                            ss += 1;

                        }
                    }
                    simpleadapter = new SimpleAdapter(Building_Info_Activity.this, listItems, R.layout.user_info_new,
                            new String[]{"bianhao", "xingming", "famenzhuangtai", "mianji",
                                    "qianfeijine", "fangjianhao", "youwubianhua"},
                            new int[]{R.id.bianhao, R.id.xingming, R.id.famenzhuangtai, R.id.mianji,
                                    R.id.qianfeijine, R.id.fangjianhao, R.id.youwubianhua});
                    list = (ListView) findViewById(R.id.userinfolistview);
                    //变换item的颜色
                    list.setBackgroundColor(Color.parseColor("#e47762"));

                    list.setAdapter(simpleadapter);
                    list_click(list);
                }
                if (flag_hong % 2 == 0) {
                    building_info_get_check_list();
                    Resources res = getResources();
                    Drawable drawable = res.getDrawable(R.drawable.ef674d);
                    setBackgroundOfVersion(tinggongBtn, drawable);
                }


            }
        });


        huifuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag_lan += 1;
                flag_hong = 0;
                flag_huang = 0;
                if (flag_lan % 2 == 1) {
                    //设置点击时的按钮的背景图片
                    Resources res = getResources();
                    Drawable drawable = res.getDrawable(R.drawable.click_84a2d4);
                    setBackgroundOfVersion(huifuBtn, drawable);

                    //同时将另外两个按钮变回点击前的按钮
                    //将停供（红色）按钮改回原来的颜色
                    Drawable drawableH = res.getDrawable(R.drawable.ef674d);
                    setBackgroundOfVersion(tinggongBtn, drawableH);
                    //将恢复（黄色）按钮改回原来的颜色
                    Drawable drawableY = res.getDrawable(R.drawable.fadd67);
                    setBackgroundOfVersion(qiangzhihuifuduanguanBtn, drawableY);

                    listItems = new ArrayList<>();
                    int ss = 0;
                    for (int i = 0; i < s; i++) {
                        if ("当年开".equals(buliding_info_model.getData().get(i).getC_Mark_Now())) {
                            Map<String, String> item = new HashMap<>();

                            item.put("bianhao", bianhao_arr.get(i));
                            item.put("xingming", xingming_arr.get(i));
                            item.put("famenzhuangtai", zhuangtai_arr.get(i));
                            item.put("mianji", mianji_arr.get(i).toString());
                            item.put("qianfeijine", qiankuan_arr.get(i));
                            item.put("fangjianhao", fangjianhao_arr.get(i));
                            item.put("youwubianhua", youwubianhua_arr.get(i));
                            listItems.add(item);
                            ss += 1;

                        }
                    }
                    simpleadapter = new SimpleAdapter(Building_Info_Activity.this, listItems, R.layout.user_info_new,
                            new String[]{"bianhao", "xingming", "famenzhuangtai", "mianji",
                                    "qianfeijine", "fangjianhao", "youwubianhua"},
                            new int[]{R.id.bianhao, R.id.xingming, R.id.famenzhuangtai, R.id.mianji,
                                    R.id.qianfeijine, R.id.fangjianhao, R.id.youwubianhua});
                    list = (ListView) findViewById(R.id.userinfolistview);
                    //变换item的颜色
                    list.setBackgroundColor(Color.parseColor("#84a2d4"));
                    list.setAdapter(simpleadapter);
                    list_click(list);
                }
                if (flag_lan % 2 == 0) {
                    building_info_get_check_list();
                    Resources resources = getResources();
                    Drawable drawable = resources.getDrawable(R.drawable.normol_84a2d4);
                    setBackgroundOfVersion(huifuBtn, drawable);
                }
            }
        });


        qiangzhihuifuduanguanBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                flag_huang += 1;
                flag_hong = 0;
                flag_lan = 0;
                if (flag_huang % 2 == 1) {
                    //设置点击时的按钮的背景图片
                    Resources res = getResources();
                    Drawable drawable = res.getDrawable(R.drawable.click_fadd67);
                    setBackgroundOfVersion(qiangzhihuifuduanguanBtn, drawable);

                    //同时将另外两个按钮变回点击前的按钮
                    //将停供（红色）按钮改回原来的颜色
                    Drawable drawableH = res.getDrawable(R.drawable.ef674d);
                    setBackgroundOfVersion(tinggongBtn, drawableH);
                    //将恢复（蓝色）按钮改回原来的颜色
                    Drawable drawableL = res.getDrawable(R.drawable.normol_84a2d4);
                    setBackgroundOfVersion(huifuBtn, drawableL);

                    listItems = new ArrayList<>();
                    int ss = 0;

                    for (int i = 0; i < s; i++) {
                        if (buliding_info_model.getData().get(i).getM_Total() > 1 && mMonth >= 11) {
                            Map<String, String> item = new HashMap<>();

                            item.put("bianhao", bianhao_arr.get(i));
                            item.put("xingming", xingming_arr.get(i));
                            item.put("famenzhuangtai", zhuangtai_arr.get(i));
                            item.put("mianji", mianji_arr.get(i).toString());
                            item.put("qianfeijine", qiankuan_arr.get(i));
                            item.put("fangjianhao", fangjianhao_arr.get(i));
                            item.put("youwubianhua", youwubianhua_arr.get(i));
                            listItems.add(item);
                            ss += 1;

                        }
                    }
                    simpleadapter = new SimpleAdapter(Building_Info_Activity.this, listItems, R.layout.user_info_new,
                            new String[]{"bianhao", "xingming", "famenzhuangtai", "mianji",
                                    "qianfeijine", "fangjianhao", "youwubianhua"},
                            new int[]{R.id.bianhao, R.id.xingming, R.id.famenzhuangtai, R.id.mianji,
                                    R.id.qianfeijine, R.id.fangjianhao, R.id.youwubianhua});
                    list = (ListView) findViewById(R.id.userinfolistview);
                    LinearLayout li = (LinearLayout) findViewById(R.id.listView_item);

                    //变换item的颜色
                    list.setBackgroundColor(Color.parseColor("#ecd475"));
                    //改变item字体颜色

                    list.setAdapter(simpleadapter);
                    list_click(list);
                }
                if (flag_huang % 2 == 0) {
                    building_info_get_check_list();
                    Resources resources = getResources();
                    Drawable drawable = resources.getDrawable(R.drawable.fadd67);
                    setBackgroundOfVersion(qiangzhihuifuduanguanBtn, drawable);
                }
            }
        });
    }


    //封装的list点击事件
    public void list_click(ListView listView) {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                HashMap item = (HashMap) adapterView.getItemAtPosition(i);
                final String roomIDStr = String.valueOf(item.get("fangjianhao").toString());//get每一行的数据的名字
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i1 = 0; i1 < s; i1++) {
                            if (String.valueOf(buliding_info_model.getData().get(i1).getI_RoomID()).equals(roomIDStr)) {
                                Intent intent = new Intent();
                                intent.setClass(Building_Info_Activity.this, User_Info.class);
                                intent.putExtra("fangjianxinxi", buliding_info_model.getData().get(i1).getC_EnrolAddress());
                                intent.putExtra("fangjianbianhao", buliding_info_model.getData().get(i1).getC_RoomNum());
                                intent.putExtra("yezhumingcheng", buliding_info_model.getData().get(i1).getC_OwnerName());
                                intent.putExtra("yonghukahao", buliding_info_model.getData().get(i1).getC_CardNum());
                                intent.putExtra("lianxifangshi", buliding_info_model.getData().get(i1).getC_Touch1());
                                intent.putExtra("gongnuanmianji", String.valueOf(buliding_info_model.getData().get(i1).getM_UseArea()));
                                intent.putExtra("jianzhumianji", String.valueOf(buliding_info_model.getData().get(i1).getM_Area()));
                                intent.putExtra("fangwuchaoxiang", buliding_info_model.getData().get(i1).getC_Direction());
                                intent.putExtra("yewuzhuangtai", buliding_info_model.getData().get(i1).getC_Mark());
                                intent.putExtra("jichajilu", buliding_info_model.getData().get(i1).getPeccantCount());
                                intent.putExtra("roomid", String.valueOf(buliding_info_model.getData().get(i1).getI_RoomID()));
                                intent.putExtra("operator",str2);
                                //发送稽查项目和稽查类别数据
                                intent.putExtra("jichaxiangmu", jichaxiangmu_arr);
                                intent.putExtra("jichaleibie", jichaleibie_arr);

                                startActivity(intent);
                                return;
                            }
                        }
                    }
                });

            }
        });
    }


    //楼座内客户信息数据下载
    public void building_info_postAsynHttp() {
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("method", "JC_GetBuildingRoomList")
                .add("Company", getString(R.string.测试))
                .add("BuildingID", str1)
                .add("QueryType", "")
                .build();
        Request request = new Request.Builder()
                .url(getString(R.string.楼座内客户信息数据下载接口))
                .post(formBody)
                .build();

        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                yonghushuju_Str = response.body().string();
                buliding_info_model = JSON.parseObject(yonghushuju_Str, Buliding_info_model.class);
                s = buliding_info_model.getData().size();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < s; i++) {
                            bianhao_arr.add(buliding_info_model.getData().get(i).getC_RoomNum());
                            xingming_arr.add(buliding_info_model.getData().get(i).getC_OwnerName());
                            zhuangtai_arr.add(buliding_info_model.getData().get(i).getC_Mark());
                            mianji_arr.add(buliding_info_model.getData().get(i).getM_Area());
                            qiankuan_arr.add(String.valueOf(buliding_info_model.getData().get(i).getM_Total()));
                            fangjianhao_arr.add(String.valueOf(buliding_info_model.getData().get(i).getI_RoomID()));
                            youwubianhua_arr.add(buliding_info_model.getData().get(i).getC_Mark_Now());
                        }
                        building_info_get_check_list();
                        //做一个Handle,已在数据请求完以后更新4个显示基础数据的按钮
                        Message message = new Message();
                        message.what = 1;
                        handler.sendEmptyMessage(1);
                    }
                });

            }
        });
    }

    //楼座内全部客户信息在列表中显示的方法
    public void building_info_get_check_list() {
        listItems = new ArrayList<>();
        for (int i = 0; i < s; i++) {
            Map<String, String> item = new HashMap<>();

            item.put("bianhao", bianhao_arr.get(i));
            item.put("xingming", xingming_arr.get(i));
            item.put("famenzhuangtai", zhuangtai_arr.get(i));
            item.put("mianji", mianji_arr.get(i).toString());
            item.put("qianfeijine", qiankuan_arr.get(i));
            item.put("fangjianhao", fangjianhao_arr.get(i));
            item.put("youwubianhua", youwubianhua_arr.get(i));
            listItems.add(item);

        }
        simpleadapter = new SimpleAdapter(this, listItems, R.layout.user_info_new,
                new String[]{"bianhao", "xingming", "famenzhuangtai",
                        "mianji", "qianfeijine", "fangjianhao", "youwubianhua"},
                new int[]{R.id.bianhao, R.id.xingming, R.id.famenzhuangtai, R.id.mianji,
                        R.id.qianfeijine, R.id.fangjianhao, R.id.youwubianhua});
        list = (ListView) findViewById(R.id.userinfolistview);
        //变换item的颜色
        list.setBackgroundColor(Color.parseColor("#aaaaaa"));
        list.setAdapter(simpleadapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                HashMap item = (HashMap) adapterView.getItemAtPosition(i);
                final String roomIDStr = String.valueOf(item.get("fangjianhao").toString());//get每一行的数据的名字
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i1 = 0; i1 < s; i1++) {
                            if (String.valueOf(buliding_info_model.getData().get(i1).getI_RoomID()).equals(roomIDStr)) {
                                Intent intent = new Intent();
                                intent.setClass(Building_Info_Activity.this, User_Info.class);
                                intent.putExtra("fangjianxinxi", buliding_info_model.getData().get(i1).getC_EnrolAddress());
                                intent.putExtra("fangjianbianhao", buliding_info_model.getData().get(i1).getC_RoomNum());
                                intent.putExtra("yezhumingcheng", buliding_info_model.getData().get(i1).getC_OwnerName());
                                intent.putExtra("yonghukahao", buliding_info_model.getData().get(i1).getC_CardNum());
                                intent.putExtra("lianxifangshi", buliding_info_model.getData().get(i1).getC_Touch1());
                                intent.putExtra("gongnuanmianji", String.valueOf(buliding_info_model.getData().get(i1).getM_UseArea()));
                                intent.putExtra("jianzhumianji", String.valueOf(buliding_info_model.getData().get(i1).getM_Area()));
                                intent.putExtra("fangwuchaoxiang", buliding_info_model.getData().get(i1).getC_Direction());
                                intent.putExtra("yewuzhuangtai", buliding_info_model.getData().get(i1).getC_Mark());
                                intent.putExtra("jichajilu", buliding_info_model.getData().get(i1).getPeccantCount());
                                intent.putExtra("roomid", String.valueOf(buliding_info_model.getData().get(i1).getI_RoomID()));

                                //发送稽查项目和稽查类别数据
                                intent.putExtra("jichaxiangmu", jichaxiangmu_arr);
                                intent.putExtra("jichaleibie", jichaleibie_arr);

                                startActivity(intent);
                                return;
                            }
                        }
                    }
                });

            }
        });
    }

    //点击两次退出离线环境


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 3500) {
                Toast.makeText(this, "再次点击将离开离线稽查环境", Toast.LENGTH_LONG).show();
                mExitTime = System.currentTimeMillis();

            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Building_Info_ Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }


}