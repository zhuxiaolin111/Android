package com.huafa.model;

/**
 * Created by chensiqi on 2016/11/7.
 */

public class SQLite_Jicha_Model {
    public SQLite_Jicha_Model(){

    }

    public int _id;
    public String RoomID;
    public String Num;
    public String Type;
    public String Item;
    public String Remark;
    public String media1;
    public String media2;
    public String media3;
    public String Operator;
    public String shifoujicha;


    public SQLite_Jicha_Model(String RoomID,String Num,String Type,String Item,
                              String Remark,String media1,String media2,String media3,
                              String Operator, String shifoujicha){
        this.RoomID = RoomID;
        this.Num = Num;
        this.Type = Type;
        this.Item = Item;
        this.Remark = Remark;
        this.media1 = media1;
        this.media2 = media2;
        this.media3 = media3;
        this.Operator = Operator;
        this.shifoujicha = shifoujicha;
    }

}
