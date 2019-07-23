package com.gossip.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.gossip.mapper.NewsMapper;
import com.gossip.pojo.News;
import com.gossip.service.IndexWriterProtalService;
import com.gossip.utils.JedisUtils;
import com.search.service.IndexWriterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @Author: WK
 * @Data: 2019/7/22 21:04
 * @Description: com.gossip.service.impl
 */
@Service
public class IndexWriterProtalServiceImpl implements IndexWriterProtalService {

    @Autowired
    private NewsMapper newsMapper;
    @Reference
    private IndexWriterService indexWriterService;
    //索引写入的方法
    @Override
    public void saveBeans() throws Exception {
        //1)从redis中获取上一次的最大id 如果没有默认为 0
        Jedis jedis = JedisUtils.getJedis();
        String lastMaxId = jedis.get("bigData:search:laseMaxId");
        jedis.close();
        if (lastMaxId==null || "".equals(lastMaxId)){
            lastMaxId="0";
        }
        while (true) {
            //2)调用mapper查询数据 如果返回值为0 那么就是没有数据，则当前id为最大值
            List<News> newsList = newsMapper.findByLastMaxId(lastMaxId);
            if (newsList == null || newsList.size() == 0) {
                //当获取不到数据的时候跳出循环
                break;
            }
            //3)其他业务操作
            // 2019-05-13 09:43:25
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            // 2019-05-13'T'09:43:25'Z'
            SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            for (News news : newsList) {
                String oldTime = news.getTime();

                Date oldDate = format1.parse(oldTime);
                String newTime = format2.format(oldDate);

                news.setTime(newTime);
            }
            //4) 调用#服务#写入索引
            indexWriterService.saveBeans(newsList);
            //5)获取本次数据中的最大id
            lastMaxId = newsMapper.findToNextMaxId(lastMaxId);
        }
        //将最大的id存到jedis中
        jedis = JedisUtils.getJedis();
        jedis.set("bigData:search:laseMaxId",lastMaxId);
        jedis.close();
    }
}
