package com.serach.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.gossip.pojo.News;
import com.search.service.IndexWriterService;
import org.apache.solr.client.solrj.SolrServer;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @Author: WK
 * @Data: 2019/7/22 20:33
 * @Description: com.serach.service.impl
 */

@Service//dubbox跨服务器
public class IndexWriterServiceImpl implements IndexWriterService {
    @Autowired// 从容器中获取
    // 如果在注入的时候, 不知道该加那个注解了, 问自己一个问题:
    // 这个对象的实例是一个服务呢, 还是spring容器中对象
    private SolrServer solrServer;

    @Override
    public void saveBeans(List<News> newsList) throws Exception {
        solrServer.addBeans(newsList);
        solrServer.commit();
    }
}
