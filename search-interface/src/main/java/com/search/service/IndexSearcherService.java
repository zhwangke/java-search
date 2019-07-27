package com.search.service;

import com.gossip.pojo.ResultBean;

/**
 * @Author: WK
 * @Data: 2019/7/25 19:02
 * @Description: com.search.service
 */
public interface IndexSearcherService {
    public ResultBean findByPage(ResultBean resultBean) throws Exception;
}
