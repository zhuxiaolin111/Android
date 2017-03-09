package com.huafa.model;

import java.util.List;

/**
 * Created by lee on 2016/11/1.
 */

public class Jicha_xiangmu_model {
    /**
     * Copyright 2016 bejson.com
     */

    /**
     * Auto-generated: 2016-11-01 23:28:27
     *
     * @author bejson.com (i@bejson.com)
     * @website http://www.bejson.com/java2pojo/
     */

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

            private String cPeccancyname;


            public void setCPeccancyname(String cPeccancyname) {
                this.cPeccancyname = cPeccancyname;
            }
            public String getCPeccancyname() {
                return cPeccancyname;
            }

        }


}
