package com.huafa.model;

import java.util.List;

/**
 * Created by lee on 2016/11/3.
 */

public class Buliding_info_model {

    public static class Data {
        private String C_RoomNum;

        private int M_Total;

        private String C_EnrolAddress;

        private int I_RoomID;

        private String C_OwnerName;

        private String C_CardNum;

        private double M_UseArea;

        private double M_Area;

        private String C_Mark_Now;

        private String C_Direction;

        private String C_Mark;

        private String C_Touch1;

        private String PeccantCount;

        public String getOperator() {
            return operator;
        }

        public void setOperator(String operator) {
            this.operator = operator;
        }

        private String operator;
        public void setC_RoomNum(String C_RoomNum) {
            this.C_RoomNum = C_RoomNum;
        }

        public String getC_RoomNum() {
            return this.C_RoomNum;
        }

        public void setM_Total(int M_Total) {
            this.M_Total = M_Total;
        }

        public int getM_Total() {
            return this.M_Total;
        }

        public void setC_EnrolAddress(String C_EnrolAddress) {
            this.C_EnrolAddress = C_EnrolAddress;
        }

        public String getC_EnrolAddress() {
            return this.C_EnrolAddress;
        }

        public void setI_RoomID(int I_RoomID) {
            this.I_RoomID = I_RoomID;
        }

        public int getI_RoomID() {
            return this.I_RoomID;
        }

        public void setC_OwnerName(String C_OwnerName) {
            this.C_OwnerName = C_OwnerName;
        }

        public String getC_OwnerName() {
            return this.C_OwnerName;
        }

        public void setC_CardNum(String C_CardNum) {
            this.C_CardNum = C_CardNum;
        }

        public String getC_CardNum() {
            return this.C_CardNum;
        }

        public void setM_UseArea(double M_UseArea) {
            this.M_UseArea = M_UseArea;
        }

        public double getM_UseArea() {
            return this.M_UseArea;
        }

        public void setM_Area(double M_Area) {
            this.M_Area = M_Area;
        }

        public double getM_Area() {
            return this.M_Area;
        }

        public void setC_Mark_Now(String C_Mark_Now) {
            this.C_Mark_Now = C_Mark_Now;
        }

        public String getC_Mark_Now() {
            return this.C_Mark_Now;
        }

        public void setC_Direction(String C_Direction) {
            this.C_Direction = C_Direction;
        }

        public String getC_Direction() {
            return this.C_Direction;
        }

        public void setC_Mark(String C_Mark) {
            this.C_Mark = C_Mark;
        }

        public String getC_Mark() {
            return this.C_Mark;
        }

        public void setC_Touch1(String C_Touch1) {
            this.C_Touch1 = C_Touch1;
        }

        public String getC_Touch1() {
            return this.C_Touch1;
        }

        public void setPeccantCount(String PeccantCount) {
            this.PeccantCount = PeccantCount;
        }

        public String getPeccantCount() {
            return this.PeccantCount;
        }
    }


    private String Result;

    private String Err;

    private List<Data> Data;

    public void setResult(String Result) {
        this.Result = Result;
    }

    public String getResult() {
        return this.Result;
    }

    public void setErr(String Err) {
        this.Err = Err;
    }

    public String getErr() {
        return this.Err;
    }

    public void setData(List<Data> Data) {
        this.Data = Data;
    }

    public List<Data> getData() {
        return this.Data;
    }
}
