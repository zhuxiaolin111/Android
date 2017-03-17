package com.huafa.lixianjicha;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.alibaba.fastjson.JSON;
import com.huafa.model.Buliding_info_model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chensiqi on 2016/11/15.z
  */

public  class Wangnian_shuju extends baseActivty {

    private String wangnian_shuju_json_str;
    private Buliding_info_model wangnian_model;
    private int s = 0;
    private int jishu_s;

    private ArrayList<String> jichaxiangmu_arr,jichaleibie_arr;

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        //super.onCreate(savedInstanceState);
        setContentView(R.layout.wangnian_shuju);

        Intent intent = getIntent();

        wangnian_shuju_json_str = intent.getStringExtra("wangnian_shuju_json_str");
        jichaxiangmu_arr = intent.getStringArrayListExtra("wangnian_jicha_xiangmu_str");
        jichaleibie_arr = intent.getStringArrayListExtra("wangnian_jicha_leibie_str");

        //解析json字符串
        jiexi_json_str();


    }
    public void jiexi_json_str(){
        wangnian_model = JSON.parseObject(wangnian_shuju_json_str,Buliding_info_model.class);
        set_wangnian_listView();
    }

    public void set_wangnian_listView(){
        List<Map<String,String>> listItem = new ArrayList<>();
        s = wangnian_model.getData().size();
        if(s>1){
            for (int i = 0; i < s; i++) {

                if ("停".equals(wangnian_model.getData().get(i).getC_Mark()) &&
                        "无变化".equals(wangnian_model.getData().get(i).getC_Mark_Now())) {
                    Map<String,String> item = new HashMap<>();
                    item.put("bianhao", wangnian_model.getData().get(i).getC_RoomNum());
                    item.put("xingming", wangnian_model.getData().get(i).getC_OwnerName());
                    item.put("famenzhuangtai", wangnian_model.getData().get(i).getC_Mark());
                    item.put("mianji", String.valueOf(wangnian_model.getData().get(i).getM_Area()));
                    item.put("qianfeijine", String.valueOf(wangnian_model.getData().get(i).getM_Total()));
                    item.put("fangjianhao",String.valueOf(wangnian_model.getData().get(i).getI_RoomID()));
                    item.put("youwubianhua",wangnian_model.getData().get(i).getC_Mark_Now());

                    listItem.add(item);
                    jishu_s += 1;
                }
            }

        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(this,listItem,R.layout.user_info_new,
                new String[]{"bianhao", "xingming", "famenzhuangtai",
                        "mianji", "qianfeijine","fangjianhao","youwubianhua"},
                new int[]{R.id.bianhao, R.id.xingming, R.id.famenzhuangtai, R.id.mianji,
                        R.id.qianfeijine,R.id.fangjianhao,R.id.youwubianhua});
        final ListView listView = (ListView) findViewById(R.id.wangnianshuju_list);
        //变换item的颜色
        listView.setBackgroundColor(Color.parseColor("#ef674d"));
        listView.setAdapter(simpleAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                HashMap item = (HashMap)adapterView.getItemAtPosition(i);
                final String roomIDStr =String.valueOf(item.get("fangjianhao").toString());//get每一行的数据的名字
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i1 = 0; i1 < s; i1++) {
                            if (String.valueOf(wangnian_model.getData().get(i1).getI_RoomID()).equals(roomIDStr)){
                                Intent intent = new Intent();
                                intent.setClass(Wangnian_shuju.this,User_Info.class);
                                intent.putExtra("fangjianxinxi", wangnian_model.getData().get(i1).getC_EnrolAddress());
                                intent.putExtra("fangjianbianhao", wangnian_model.getData().get(i1).getC_RoomNum());
                                intent.putExtra("yezhumingcheng", wangnian_model.getData().get(i1).getC_OwnerName());
                                intent.putExtra("yonghukahao", wangnian_model.getData().get(i1).getC_CardNum());
                                intent.putExtra("lianxifangshi", wangnian_model.getData().get(i1).getC_Touch1());
                                intent.putExtra("gongnuanmianji", String.valueOf(wangnian_model.getData().get(i1).getM_UseArea()));
                                intent.putExtra("jianzhumianji", String.valueOf(wangnian_model.getData().get(i1).getM_Area()));
                                intent.putExtra("fangwuchaoxiang", wangnian_model.getData().get(i1).getC_Direction());
                                intent.putExtra("yewuzhuangtai", wangnian_model.getData().get(i1).getC_Mark());
                                intent.putExtra("jichajilu", wangnian_model.getData().get(i1).getPeccantCount());
                                intent.putExtra("roomid",String.valueOf(wangnian_model.getData().get(i1).getI_RoomID()));

                                //发送稽查项目和稽查类别数据
                                intent.putExtra("jichaxiangmu",jichaxiangmu_arr);
                                intent.putExtra("jichaleibie",jichaleibie_arr);

                                startActivity(intent);
                                return;
                            }
                        }
                    }
                });

            }
        });

    }



}
