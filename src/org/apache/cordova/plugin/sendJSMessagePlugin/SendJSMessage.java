package org.apache.cordova.plugin.sendJSMessagePlugin;

import android.content.Intent;

import com.huafa.lixianjicha.Building_Info_Activity;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by apple on 16/9/14.
 */
public class SendJSMessage  extends CordovaPlugin{

    public CallbackContext callbackContext;

    public boolean execute(String actions, JSONArray args,CallbackContext callbackContent) throws JSONException{
        // this.callbackContext = callbackContent;
    try{
            if ("sendJSMessage".equals(actions)){
                // JSONObject arg_object = args.getJSONObject(0);
                // String title = arg_object.getString("title");
                 String s=args.getString(0);
                 String s1=args.getString(1);
                Intent intent = new Intent(cordova.getActivity(), Building_Info_Activity.class);
                intent.putExtra("buildid",  args.getString(0));
                intent.putExtra("operator",args.getString(1));
             //   intent.putExtra("buildid",args.getString(1));
                cordova.startActivityForResult(this,intent, 0);
                callbackContext.success("回调成功啦");
                return true;
            }
        }catch(Exception e){
            System.err.println("Exception: " + e.getMessage());
            // callbackContext.error(e.getMessage());
            return false;
        }
           callbackContent.error("哎哟!出错了!");
            return false; 
    }
        @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        super.onActivityResult(requestCode, resultCode, intent);
        if(intent == null)  return;
//传递返回值 给js方法
        String data = intent.getStringExtra("data");
        callbackContext.success(data);



    }
}
