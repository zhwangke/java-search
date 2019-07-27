package com.gossip.service;

import com.gossip.pojo.ResultBean;

/**
 * @Author: WK
 * @Data: 2019/7/25 20:09
 * @Description: com.gossip.service
 */
public interface IndexSearcherProtalService {
    public ResultBean findByPage(ResultBean resultBean) throws Exception;
}
