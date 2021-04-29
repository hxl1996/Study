package com.demo.test.demo.entity;

import lombok.Data;

@Data
public class VideoEntity {
    //名字
    private String name;
    //code
    private String code;
    //演员
    private String actorDisplay;
    //简介
    private String description;
    //首播日期
    private String orgAirDate;
    //上下线
    private String status;
    //标签
    private String tags;
    //类型
    private String type;
    //总集数
    private Integer volumnCount;
}
