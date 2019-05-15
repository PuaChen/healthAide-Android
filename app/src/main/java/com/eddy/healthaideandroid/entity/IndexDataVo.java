package com.eddy.healthaideandroid.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author Eddy·Chen
 * @Email 835033913@qq.com
 * @Create 2019-05-11 11:24
 */
public class IndexDataVo implements Serializable {

    /**
     * 体温
     */
    private Double animalHeat;

    /**
     * 血氧
     */
    private Double blood;

    /**
     * 血糖
     */
    private Double glucose;

    /**
     * 胆固醇
     */
    private Double cholesterol;

    /**
     * 血压 高压
     */
    private Double pressureH;

    /**
     * 血压 低压
     */
    private Double pressureL;

    /**
     * 血尿酸
     */
    private Double uric;

    /**
     * 甘油三酯
     */
    private Double triglyceride;

    /**
     * 尿液酸碱度
     */
    private Double urinePh;

    /**
     * 疾病类型
     */
    private String illnessName;

    /**
     * 疾病建议
     */
    private String illnessInfo;


    private Date createTime;


    public Double getAnimalHeat() {
        return animalHeat;
    }

    public void setAnimalHeat(Double animalHeat) {
        this.animalHeat = animalHeat;
    }

    public Double getBlood() {
        return blood;
    }

    public void setBlood(Double blood) {
        this.blood = blood;
    }

    public Double getGlucose() {
        return glucose;
    }

    public void setGlucose(Double glucose) {
        this.glucose = glucose;
    }

    public Double getCholesterol() {
        return cholesterol;
    }

    public void setCholesterol(Double cholesterol) {
        this.cholesterol = cholesterol;
    }

    public Double getPressureH() {
        return pressureH;
    }

    public void setPressureH(Double pressureH) {
        this.pressureH = pressureH;
    }

    public Double getPressureL() {
        return pressureL;
    }

    public void setPressureL(Double pressureL) {
        this.pressureL = pressureL;
    }

    public Double getUric() {
        return uric;
    }

    public void setUric(Double uric) {
        this.uric = uric;
    }

    public Double getTriglyceride() {
        return triglyceride;
    }

    public void setTriglyceride(Double triglyceride) {
        this.triglyceride = triglyceride;
    }

    public Double getUrinePh() {
        return urinePh;
    }

    public void setUrinePh(Double urinePh) {
        this.urinePh = urinePh;
    }

    public String getIllnessName() {
        return illnessName;
    }

    public void setIllnessName(String illnessName) {
        this.illnessName = illnessName;
    }

    public String getIllnessInfo() {
        return illnessInfo;
    }

    public void setIllnessInfo(String illnessInfo) {
        this.illnessInfo = illnessInfo;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "IndexDataVo{" +
                "animalHeat=" + animalHeat +
                ", blood=" + blood +
                ", glucose=" + glucose +
                ", cholesterol=" + cholesterol +
                ", pressureH=" + pressureH +
                ", pressureL=" + pressureL +
                ", uric=" + uric +
                ", triglyceride=" + triglyceride +
                ", urinePh=" + urinePh +
                ", illnessName='" + illnessName + '\'' +
                ", illnessInfo='" + illnessInfo + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
