package com.search.service;

import com.gossip.pojo.News;

import java.util.List;

/**
 * @Author: WK
 * @Data: 2019/7/22 20:31
 * @Description: com.search.service
 */
public interface IndexWriterService {
    /**
     * 保存
     * @param newsList
     */
    public void saveBeans(List<News> newsList) throws Exception;
}
