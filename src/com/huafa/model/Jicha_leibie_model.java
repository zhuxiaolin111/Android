package com.huafa.model;

import java.util.List;

/**
 * Created by chensiqi on 2016/11/4.
 */

public class Jicha_leibie_model {


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

        private String cPeccancytype;


        public void setCPeccancytype(String cPeccancytype) {
            this.cPeccancytype = cPeccancytype;
        }
        public String getCPeccancytype() {
            return cPeccancytype;
        }

    }


}
