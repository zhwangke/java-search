package com.topkey.service;

/**
 * @Author: WK
 * @Data: 2019/7/28 19:16
 * @Description: com.topkey.service
 */
public interface TopKeyForKafkaService {
    public  void findByTopKeyToRedis(String topkey) throws Exception;
}
