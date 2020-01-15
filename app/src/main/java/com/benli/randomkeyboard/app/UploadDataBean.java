package com.benli.randomkeyboard.app;


import androidx.annotation.Keep;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

@Keep
public class UploadDataBean<T> implements Cloneable {

    /**
     * 数据ID，随机生成全局唯一ID
     */
    @SerializedName("dataId")
    public String dataID;

    /**
     * 数据类型：1、行为数据，2、设备数据，3、通讯录数据，4、应用数据，5、通话记录数据，6、短信数据
     */
    public int type;

    /**
     * 收集开始时间
     */
    public long startTime;

    /**
     * 收集结束时间
     */
    public long endTime;

    /**
     * 数据收集节点
     */
    public String node;

    /**
     * 数据分片片数
     */
    @SerializedName("pageSize")
    public int orderLength;

    /**
     * 当前数据段序号
     */
    @SerializedName("pageIndex")
    public int order;

    /**
     * 数据总条数
     */
    @SerializedName("totalCount")
    public int total;

    /**
     * 当前数据片条数
     */
    public int pageCount;

    /**
     * 具体数据
     */
    public T data;


    public String toJsonString() {
        String jsonString = new Gson().toJson(this);
        return jsonString;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
