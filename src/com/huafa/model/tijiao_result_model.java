package com.huafa.model;

import java.util.List;

/**
 * Created by Administrator on 2017/2/28.
 */

public class tijiao_result_model {

    private String result;
    private String err;
    private List<Data> data;


    public void setResult(String result) {
        this.result = result;
    }
    public String getResult() {
        return result;
    }

    public void setErr(String err) {
        this.err = err;
    }
    public String getErr() {
        return err;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }
    public List<Data> getData() {
        return data;
    }


    public static class Data {

        public String getResult_code() {
            return result_code;
        }

        public void setResult_code(String result_code) {
            this.result_code = result_code;
        }

        private String result_code;


    }
}