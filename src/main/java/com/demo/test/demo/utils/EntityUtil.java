package com.demo.test.demo.utils;

import com.demo.test.demo.entity.VideoEntity;

import java.util.List;

/**
 * 将对象中的必要值转换为中文
 */
public class EntityUtil {

    public List<VideoEntity> turnInChinese(List<VideoEntity> videoEntities){
        for (VideoEntity video: videoEntities
             ) {
            if("0".equals(video.getStatus())){
                video.setStatus("下线");
            }else {
                video.setStatus("上线");
            }
            if("tv".equals(video.getType())){
                video.setType("电视剧");
            }else{
                video.setType("综艺");
            }
            if(video.getVolumnCount() == null){
                video.setVolumnCount(0);
            }
        }
        return videoEntities;
    }
}
