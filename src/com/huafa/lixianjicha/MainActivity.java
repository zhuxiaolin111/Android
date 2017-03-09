/*
       Licensed to the Apache Software Foundation (ASF) under one
       or more contributor license agreements.  See the NOTICE file
       distributed with this work for additional information
       regarding copyright ownership.  The ASF licenses this file
       to you under the Apache License, Version 2.0 (the
       "License"); you may not use this file except in compliance
       with the License.  You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

       Unless required by applicable law or agreed to in writing,
       software distributed under the License is distributed on an
       "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
       KIND, either express or implied.  See the License for the
       specific language governing permissions and limitations
       under the License.
 */

package com.huafa.lixianjicha;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;

import org.apache.cordova.CordovaActivity;


public class MainActivity extends CordovaActivity {
    TelephonyManager tm;
    String imei;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tm = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
          /*
      * 唯一的设备ID：
      * GSM手机的 IMEI 和 CDMA手机的 MEID.
      * Return null if device ID is not available.
      */
        imei = tm.getDeviceId();//String



        //  Toast.makeText(MainActivity.this, imei, Toast.LENGTH_LONG).show();
        // enable Cordova apps to be started in the background
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.getBoolean("cdvStartInBackground", false)) {
            moveTaskToBack(true);
        }
        // Set by <content src="index.html" /> in config.xml

        loadUrl(launchUrl);



        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(4000);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //不能在子线程中执行webview的方法
                            loadUrl("javascript:showImei('" + imei + "')");
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        // 启动服务
        Intent intent = new Intent(this, LocationService.class);
        startService(intent);
    }



}
