package com.gossip.service.impl;

import com.gossip.service.TopKeyService;
import com.gossip.utils.JedisUtils;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.*;

/**
 * @Author: WK
 * @Data: 2019/7/27 11:45
 * @Description: com.gossip.service.impl
 */
@Service
public class TopKeyServiceImpl implements TopKeyService {
        @Override
        public List<Map<String, Object>> findByTopKey(Integer num) {
            //1. 获取jedis对象
            Jedis jedis = JedisUtils.getJedis();

            //2. 执行查询 : 从大到小 的 前 num个数据
            Set<Tuple> tuples = jedis.zrevrangeWithScores("bigData:gossip:topkey", 0, (num - 1));


            //3. 封装数据
            List<Map<String, Object>> mapList = new ArrayList<>();
            for (Tuple tuple : tuples) {

                String topKey = tuple.getElement();
                double score = tuple.getScore();

                Map<String,Object> map = new HashMap<>();

                map.put("topKey",topKey);
                map.put("score",score);

                mapList.add(map);
            }
            //4. 释放资源
            jedis.close();

            return mapList;
    }
}
