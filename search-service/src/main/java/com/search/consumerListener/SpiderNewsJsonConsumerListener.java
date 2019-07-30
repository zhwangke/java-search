package com.search.consumerListener;

/**
 * @Author: WK
 * @Data: 2019/7/28 18:00
 * @Description: com.search.consumerListener
 */
import com.google.gson.Gson;
import com.gossip.pojo.News;
import com.search.service.IndexWriterService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

@Component
public class SpiderNewsJsonConsumerListener implements MessageListener<Integer,String> {
    @Autowired
    private IndexWriterService indexWriterService;
    @Override
    public void onMessage(ConsumerRecord<Integer, String> record) {
        try {
            //1) 从gossip-newsJson的topic中获取kafka中消息
            String newsJson = record.value();

            //2. 使用Gson 将newsJson转为News对象

            Gson gson = new Gson();

            News news = gson.fromJson(newsJson, News.class);

            //3. 对时间进行处理
            // 2019-05-24 19:06:46
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

            String oldTime = news.getTime();

            Date oldDate = format1.parse(oldTime);

            String newTime = format2.format(oldDate);
            news.setTime(newTime);

            //4. 写入索引
            indexWriterService.saveBeans(Arrays.asList(news));


            System.out.println(newsJson);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}

