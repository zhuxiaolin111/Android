package com.huafa.model;

import java.util.List;

/**
 * Created by Administrator on 2017/2/28.
 */

public class tijiao_result_model {
    private String result;
    private String err;
    private List<Jicha_leibie_model.Data> data;


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

    public void setData(List<Jicha_leibie_model.Data> data) {
        this.data = data;
    }
    public List<Jicha_leibie_model.Data> getData() {
        return data;
    }
}
