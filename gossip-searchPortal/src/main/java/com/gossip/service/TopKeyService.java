package com.gossip.service;

import java.util.List;
import java.util.Map;

/**
 * @Author: WK
 * @Data: 2019/7/27 11:44
 * @Description: com.gossip.service
 */
public interface TopKeyService {
    public List<Map<String,Object>> findByTopKey(Integer num);
}
