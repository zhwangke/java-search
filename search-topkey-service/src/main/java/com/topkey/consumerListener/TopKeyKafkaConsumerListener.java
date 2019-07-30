package com.topkey.consumerListener;

/**
 * @Author: WK
 * @Data: 2019/7/28 19:52
 * @Description: com.topkey.consumerListener
 */
import com.topkey.service.TopKeyForKafkaService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.stereotype.Component;

@Component
public class TopKeyKafkaConsumerListener implements MessageListener<String,String>{
    @Autowired
    private TopKeyForKafkaService topKeyForKafkaService;

    @Override
    public void onMessage(ConsumerRecord<String, String> record) {

        try {
            //1. 从kafka中获取热搜词语
            String topKey = record.value();
            System.out.println("从kakfa中获取到了:"+topKey);
            //2. 调用service, 写入缓存数据:
            topKeyForKafkaService.findByTopKeyToRedis(topKey);

            System.out.println(topKey+"数据缓存成功......");

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
