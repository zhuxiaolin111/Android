package com.huafa.lixianjicha;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.huafa.model.SQLite_Jicha_Model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 16/10/28.
 * 详情页
 */

public class User_Info extends baseActivty {
    //图片
    Bitmap bm1, bm2, bm3;
    Bitmap bitmap2, bitmap1, bitmap3;
    ImageView imageView1;
    ImageView imageView2;
    ImageView imageView3;
    //新加的字符串组
    private String[] xinjiade_String;

    //压缩后的图片
    private Bitmap bm_yasuo;

    //返回的bae64编码
    private String return_base64_str;

    //SQLite数据库管理类的实体化
    private DBManager mgr;

    //图片按钮
    private Button btn1;
    private Button btn2;
    private Button btn3;

    //保存按钮
    private Button baocun;

    //上一页传递过来的数据
    private String fangjianxinxiStr, fangjianbianhaoStr, yezhumingchengStr, yonghukahaoStr, lianxifangshiStr, gongnuanmianjiStr,
            jianzhumianjiStr, fangwuchaoxiangStr, yewuzhuangtaiStr, jichajiluStr, roomid, Operator;

    //详情页显示用户信息的TextView
    private TextView fangjianxinxi, fangjianbianhao, yezhumingcheng, yonghukahao, lianxifangshi, gongnuanmianji, jianzhumianji,
            fangwuchaoxiang, yewuzhuangtai, jichajilu;

    //稽查项目和稽查类别按钮
    private Button jichaxiangmuBtn, jichaleibieBtn;


    //稽查项目和稽查类别的ArrayList
    private ArrayList<String> jichaxiangmu_arrayList, jichaleibie_arrayList;
    //新加的ArrayList
    private ArrayList<String> xinjiade_arrayList;

    //稽查项目和稽查类别的String[]
    private String[] jichaxiangmu_String, jichaleibie_String;

    //快速稽查按钮
    private Button kuaisubeizhuBtn;
    private EditText kuaisubeizhuText;

    //图片转码后的字符串
    private String bitmap1_zhuanma, bitmap2_zhuanma, bitmap3_zhuanma;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        //  super.onCreate(savedInstanceState);
        setContentView(R.layout.user_details_page);


        //将数据库管理类实体化
        mgr = new DBManager(this);

        //图片按钮
        btn1 = (Button) findViewById(R.id.tupian1);
        btn2 = (Button) findViewById(R.id.tupian2);
        btn3 = (Button) findViewById(R.id.tupian3);

        //保存按钮
        baocun = (Button) findViewById(R.id.baocun);

        //快速备注按钮
        kuaisubeizhuBtn = (Button) findViewById(R.id.kuaisubeizhu);
        //快速备注文字
        kuaisubeizhuText = (EditText) findViewById(R.id.kuaisubeizhuxinxi);
        kuaisubeizhuText.setInputType(InputType.TYPE_NULL);
        //用户详情TextView
        fangjianxinxi = (TextView) findViewById(R.id.fangwuxinxi);
        fangjianbianhao = (TextView) findViewById(R.id.fangjianbianhao);
        yezhumingcheng = (TextView) findViewById(R.id.yezhumingcheng);
        yonghukahao = (TextView) findViewById(R.id.yonghukahao);
        lianxifangshi = (TextView) findViewById(R.id.lianxifangshi);
        gongnuanmianji = (TextView) findViewById(R.id.gongnuanmianji);
        jianzhumianji = (TextView) findViewById(R.id.jianzhumianji);
        fangwuchaoxiang = (TextView) findViewById(R.id.fangwuchaoxiang);
        yewuzhuangtai = (TextView) findViewById(R.id.yewuzhuangtai);
        jichajilu = (TextView) findViewById(R.id.jichajilu);

        //稽查项目和稽查类别按钮
        jichaxiangmuBtn = (Button) findViewById(R.id.jichaxiangmu);
        jichaleibieBtn = (Button) findViewById(R.id.jichaleibie);

        //请求稽查项目的ArrayList中的值
        Intent intent = getIntent();
        jichaxiangmu_arrayList = intent.getStringArrayListExtra("jichaxiangmu");
        jichaleibie_arrayList = intent.getStringArrayListExtra("jichaleibie");
        //新加的ArrayList
        xinjiade_arrayList = new ArrayList<>();
        xinjiade_arrayList.add("关栓");
        xinjiade_arrayList.add("开栓");
        xinjiade_arrayList.add("窃热");

        //讲arraylist装维String[]
        jichaxiangmu_String = jichaxiangmu_arrayList.toArray(new String[jichaxiangmu_arrayList.size()]);
        jichaleibie_String = jichaleibie_arrayList.toArray(new String[jichaleibie_arrayList.size()]);
        //新加的ArrayList
        xinjiade_String = xinjiade_arrayList.toArray(new String[xinjiade_arrayList.size()]);

        //快速稽查按钮点击事件
        kuaisubeizhuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("".equals(jichaleibieBtn.getText())) {
                    if ("".equals(jichaxiangmuBtn.getText())) {
                        Toast.makeText(User_Info.this, "请选择稽查项目", Toast.LENGTH_SHORT).show();
                    }
                    if (jichaxiangmuBtn.getText() != "") {
                        Toast.makeText(User_Info.this, "请选择稽查类别", Toast.LENGTH_SHORT).show();
                    }
                } else if (jichaleibieBtn.getText() != "") {
                    if ("".equals(jichaxiangmuBtn.getText())) {
                        Toast.makeText(User_Info.this, "请选择稽查项目", Toast.LENGTH_SHORT).show();
                    } else {
                        kuaisubeizhuxuanxiang((String) jichaleibieBtn.getText());
                    }
                } else {
                    Toast.makeText(User_Info.this, "出错了", Toast.LENGTH_SHORT).show();
                }

            }
        });


        //稽查项目按钮点击事件
        jichaxiangmuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(User_Info.this)
                        .setTitle("请选择")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setSingleChoiceItems(jichaxiangmu_String, -1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ((Button) findViewById(R.id.jichaxiangmu)).setText(jichaxiangmu_arrayList.get(which));
                                ((Button) findViewById(R.id.jichaleibie)).setText("");
                                kuaisubeizhuText.setText("");
                            }
                        })
                        .setPositiveButton("确定", null)
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ((Button) findViewById(R.id.jichaxiangmu)).setText("");
                                ((Button) findViewById(R.id.jichaleibie)).setText("");
                                kuaisubeizhuText.setText("");
                            }
                        })
                        .show();
            }
        });

        //稽查类别按钮点击事件
        jichaleibieBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("".equals(jichaxiangmuBtn.getText().toString())) {
                    Toast.makeText(User_Info.this, "请选择稽查项目", Toast.LENGTH_SHORT).show();
                } else if ("企业热用户".equals(jichaxiangmuBtn.getText().toString())) {
                    new AlertDialog.Builder(User_Info.this)
                            .setTitle("请选择")
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .setSingleChoiceItems(xinjiade_String, -1, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ((Button) findViewById(R.id.jichaleibie)).setText(xinjiade_arrayList.get(which));
                                }
                            })
                            .setPositiveButton("确定", null)
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ((Button) findViewById(R.id.jichaleibie)).setText("");
                                    kuaisubeizhuText.setText("");
                                }
                            })
                            .show();

                } else {
                    new AlertDialog.Builder(User_Info.this)
                            .setTitle("请选择")
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .setSingleChoiceItems(jichaleibie_String, -1, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ((Button) findViewById(R.id.jichaleibie)).setText(jichaleibie_arrayList.get(which));
                                }
                            })
                            .setPositiveButton("确定", null)
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ((Button) findViewById(R.id.jichaleibie)).setText("");
                                    kuaisubeizhuText.setText("");
                                }
                            })
                            .show();
                }

            }
        });


        //设置三个存放照片的按钮的点击事件
        button_listener();


        //设置保存按钮的点击事件
        baocun_listener();

        //设置UI
        initView();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    //复写关闭Activity方法
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //应用的最后一个Activity关闭时应释放DB
        mgr.closeDB();
    }

    //操作数据库的方法
    //这个方法是点击保存按钮的时候调用的
    public void initSQLite() {
        //首先查询一下这个数据有没有存在数据库中

        //根据RoomID查询
        Cursor c = mgr.search(roomid, "RoomID");
        while (c.moveToNext()) {
            if (!"".equals(c.getString(c.getColumnIndex("RoomID")))) {
                //删除这行
                SQLite_Jicha_Model sqLite_jicha_model = new SQLite_Jicha_Model();
                sqLite_jicha_model.RoomID = roomid;
                mgr.deleteOldPerson(sqLite_jicha_model);
            }
        }


        //将三张图片转换为base64字符串
        imageView1 = (ImageView) findViewById(R.id.image1);
        imageView2 = (ImageView) findViewById(R.id.image2);
        imageView3 = (ImageView) findViewById(R.id.image3);

        if (imageView1.getDrawable() == null) {
            bitmap1_zhuanma = "";
        } else {
            Bitmap bitmap1 = ((BitmapDrawable) imageView1.getDrawable()).getBitmap();
            Bitmap bitmap1_yasuo = compressImage(bitmap1);
            bitmap1_zhuanma = encode(bitmap1_yasuo);
            System.out.println(bitmap1_zhuanma);
        }
        if (imageView2.getDrawable() == null) {
            bitmap2_zhuanma = "";
        } else {
            Bitmap bitmap2 = ((BitmapDrawable) imageView2.getDrawable()).getBitmap();
            Bitmap bitmap2_yasuo = compressImage(bitmap2);
            bitmap2_zhuanma = encode(bitmap2_yasuo);
        }
        if (imageView3.getDrawable() == null) {
            bitmap3_zhuanma = "";
        } else {
            Bitmap bitmap3 = ((BitmapDrawable) imageView3.getDrawable()).getBitmap();
            Bitmap bitmap3_yasuo = compressImage(bitmap3);
            bitmap3_zhuanma = encode(bitmap3_yasuo);
        }


        //将数据写入数据库
        //使用转码后的图片的字符串
        ArrayList<SQLite_Jicha_Model> jicha_models = new ArrayList<>();
        SQLite_Jicha_Model sqLite_jicha_model = new SQLite_Jicha_Model(roomid,
                "",
                jichaleibieBtn.getText().toString(),
                jichaxiangmuBtn.getText().toString(),
                kuaisubeizhuText.getText().toString(),
                bitmap1_zhuanma,
                bitmap2_zhuanma,
                bitmap3_zhuanma,
                Common.operator,
                // yezhumingchengStr,
                "no");
        jicha_models.add(sqLite_jicha_model);
        mgr.add(jicha_models);

        List<SQLite_Jicha_Model> list = mgr.query();
//        Toast.makeText(User_Info.this,list.get(0).Type,Toast.LENGTH_LONG).show();

    }
//    encode(compressImage(findViewById(R.id.image3).getDrawingCache())).toString()


    //封装的快速备注的选择AlertDialog
    public void kuaisubeizhuxuanxiang(String string) {

        String[] str1 = new String[]{"已断管", "未断", "整改（未封堵）", "经核实已缴费"};
        String[] str2 = new String[]{"已接管", "未接"};
        String[] str3 = new String[]{"物业用房", "二道门", "信息不详(已拆迁)", "其他(无供暖设施)", "自供采暖", "垃圾房", "自来水泵房"};
        String[] str4 = new String[]{"已照相取证", "已摄像取证", "经核实已缴费"};
        String[] str5 = new String[]{"已关栓", "整改", "无防盗箱，无铅封"};
        String[] str6 = new String[]{"已开栓", "整改", "无防盗箱，无铅封"};
        String[] str7 = new String[]{"已照相取证", "已摄像取证", "照相、摄像已经同时取证"};
        String[] kuaisubeizhuStr = new String[]{};

        switch (string) {
            case "断管":
                kuaisubeizhuStr = str1;
                break;
            case "接管":
                kuaisubeizhuStr = str2;
                break;
            case "无法取证":
                kuaisubeizhuStr = str3;
                break;
            case "窃热（放水）":
                kuaisubeizhuStr = str4;
                break;
            case "关栓":
                kuaisubeizhuStr = str5;
                break;
            case "开栓":
                kuaisubeizhuStr = str6;
                break;
            case "窃热":
                kuaisubeizhuStr = str7;
                break;
            default:
                Toast.makeText(User_Info.this, "错误", Toast.LENGTH_SHORT).show();
                break;
        }

        final String[] finalKuaisubeizhuStr = kuaisubeizhuStr;
        new AlertDialog.Builder(User_Info.this)
                .setTitle("快速备注信息")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setSingleChoiceItems(kuaisubeizhuStr, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        kuaisubeizhuText.setText(finalKuaisubeizhuStr[which]);
                    }
                })
                .setPositiveButton("确定", null)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((EditText) findViewById(R.id.kuaisubeizhuxinxi)).setText("");
                    }
                })
                .show();
    }

    //设置UI
    public void initView() {
        //下载前一页传过来的数据
        Intent intent = getIntent();
        fangjianxinxiStr = intent.getStringExtra("fangjianxinxi");
        fangjianbianhaoStr = intent.getStringExtra("fangjianbianhao");
        yezhumingchengStr = intent.getStringExtra("yezhumingcheng");
        yonghukahaoStr = intent.getStringExtra("yonghukahao");
        lianxifangshiStr = intent.getStringExtra("lianxifangshi");
        gongnuanmianjiStr = intent.getStringExtra("gongnuanmianji");
        jianzhumianjiStr = intent.getStringExtra("jianzhumianji");
        fangwuchaoxiangStr = intent.getStringExtra("fangwuchaoxiang");
        yewuzhuangtaiStr = intent.getStringExtra("yewuzhuangtai");
        jichajiluStr = intent.getStringExtra("jichajilu");
        roomid = intent.getStringExtra("roomid");
        Operator = intent.getStringExtra("operator");
        //设置UI
        fangjianxinxi.setText(fangjianxinxiStr);
        fangjianbianhao.setText(fangjianbianhaoStr);
        yezhumingcheng.setText(yezhumingchengStr);
        yonghukahao.setText(yonghukahaoStr);
        lianxifangshi.setText(lianxifangshiStr);
        gongnuanmianji.setText(gongnuanmianjiStr);
        jianzhumianji.setText(jianzhumianjiStr);
        fangwuchaoxiang.setText(fangwuchaoxiangStr);
        yewuzhuangtai.setText(yewuzhuangtaiStr);
        jichajilu.setText(jichajiluStr);

    }


    //设置3个存放照片的按钮
    public void button_listener() {
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(User_Info.this, FloatBottomActivity.class);
                startActivityForResult(intent, 11);

            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(User_Info.this, FloatBottomActivity.class);
                startActivityForResult(intent, 22);
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(User_Info.this, FloatBottomActivity.class);
                startActivityForResult(intent, 33);
            }
        });
    }

    //保存按钮的点击事件
    public void baocun_listener() {
        baocun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!"".equals(jichaxiangmuBtn.getText().toString()) && !"".equals(jichaleibieBtn.getText().toString())
                        && !"".equals(kuaisubeizhuText.getText().toString())) {
                    if (bitmap1 != null || bitmap2 != null || bitmap3 != null || bm1 != null || bm2 != null || bm3 != null) {
                        new AlertDialog.Builder(User_Info.this)
                                .setTitle("注意")
                                .setIcon(android.R.drawable.ic_dialog_info)
                                .setMessage("如果您选择确定,则本次修改将覆盖您上次保存的本户的稽查结果(主要是对于照片的修改)")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //操作数据库的方法
                                        initSQLite();
                                        finish();
                                    }
                                })
                                .setNegativeButton("取消", null)
                                .show();
                    } else {
                        Toast.makeText(User_Info.this, "您需要全部填写'稽查项目'、'稽查类别'、'稽查备注'和‘稽查相片’信息才能保存数据", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(User_Info.this, "您需要全部填写'稽查项目'、'稽查类别'、'稽查备注'和‘稽查相片’信息才能保存数据", Toast.LENGTH_LONG).show();
                }

            }
        });
    }


    //复写拍照存放照片和从相册选取照片的回调功能
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //取得回调的图片路径
        if (requestCode == 11 && resultCode == 1001) {
            String imagepath = data.getStringExtra("result");
            bm1 = BitmapFactory.decodeFile(imagepath);
            Bitmap biliyasuo = comp(bm1);
            ((ImageView) findViewById(R.id.image1)).setImageBitmap(biliyasuo);
            System.out.println(bm1.getByteCount());


        }
        if (requestCode == 22 && resultCode == 1001) {
            String imagepath = data.getStringExtra("result");
            bm2 = BitmapFactory.decodeFile(imagepath);
            Bitmap biliyasuo = comp(bm2);
            ((ImageView) findViewById(R.id.image2)).setImageBitmap(biliyasuo);
            //压缩后的图片,以备后续上传使用

        }
        if (requestCode == 33 && resultCode == 1001) {
            String imagepath = data.getStringExtra("result");
            bm3 = BitmapFactory.decodeFile(imagepath);
            Bitmap biliyasuo = comp(bm3);
            ((ImageView) findViewById(R.id.image3)).setImageBitmap(biliyasuo);

        }


        //拍照直接回调bitmap
        if (requestCode == 11 && resultCode == 1002) {
//            Bitmap bitmap = data.getParcelableExtra("bitmap");
//            Bitmap biliyasuo = comp(bitmap);
//            ((ImageView) findViewById(R.id.image1)).setImageBitmap(biliyasuo);
            Uri uri = data.getData();
            try {
                bitmap1 = MediaStore.Images.Media.getBitmap(User_Info.this.getContentResolver(), uri);
                final Bitmap biliyasuo = comp(bitmap1);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((ImageView) findViewById(R.id.image1)).setImageBitmap(biliyasuo);
                        System.out.println(biliyasuo.getByteCount() / 1024 / 8);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }

            //压缩后的图片,以备后续上传使用

        }
        if (requestCode == 22 && resultCode == 1002) {
//            Bitmap bitmap = data.getParcelableExtra("bitmap");
//            Bitmap biliyasuo = comp(bitmap);
//            ((ImageView) findViewById(R.id.image2)).setImageBitmap(biliyasuo);

            Uri uri = data.getData();
            try {
                bitmap2 = MediaStore.Images.Media.getBitmap(User_Info.this.getContentResolver(), uri);
                final Bitmap biliyasuo = comp(bitmap2);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((ImageView) findViewById(R.id.image2)).setImageBitmap(biliyasuo);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }

            //压缩后的图片,以备后续上传使用

        }
        if (requestCode == 33 && resultCode == 1002) {
//            Bitmap bitmap = data.getParcelableExtra("bitmap");
//            Bitmap biliyasuo = comp(bitmap);
//            ((ImageView) findViewById(R.id.image3)).setImageBitmap(biliyasuo);

            Uri uri = data.getData();
            try {
                bitmap3 = MediaStore.Images.Media.getBitmap(User_Info.this.getContentResolver(), uri);
                final Bitmap biliyasuo = comp(bitmap3);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((ImageView) findViewById(R.id.image3)).setImageBitmap(biliyasuo);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
            //压缩后的图片,以备后续上传使用

        }
    }


    //比例压缩图片
    private Bitmap comp(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        if (baos.toByteArray().length / 1024 > 1024) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 50, baos);//这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 1280f;//这里设置高度为800f
        float ww = 720f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        System.out.println(bitmap.getByteCount());
//        return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
        return compressImage(bitmap);//
    }


    //质量压缩图片
    public static Bitmap compressImage(Bitmap image) {
        //L.showlog(压缩图片);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
//		Log.i(test,原始大小 + baos.toByteArray().length);
        while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于(maxkb)50kb,大于继续压缩
//			Log.i(test,压缩一次!);
            baos.reset();// 重置baos即清空baos
            options -= 15;// 每次都减少3
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
        }
//		Log.i(test,压缩后大小 + baos.toByteArray().length);
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    //将图片转为base64的方法
    private String encode(Bitmap bm) {
        //convert to byte array
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] bytes = baos.toByteArray();

        //base64 encode
        byte[] encode = Base64.encode(bytes, Base64.DEFAULT);
        String fanhui_base64_byte = new String(encode);
        return fanhui_base64_byte;
    }


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("User_Info Page") // TODO: Define a title for the content shown.
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
