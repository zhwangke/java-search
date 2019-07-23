package com.gossip.mapper;

import com.gossip.pojo.News;

import java.util.List;

/**
 * @Author: WK
 * @Data: 2019/7/22 20:52
 * @Description: com.gossip.mapper
 */
public interface NewsMapper {
    //1.根据上一次的最大id 获取分页数据

    /**
     * select * from news where id >lastMaxId limit 0,10
     * @param id
     * @return
     */
    public List<News> findByLastMaxId(String id);

    //2.根据上一次的最大id 获取本次最大id

    /**
     * select max(id) from (select * from news where id >lastMaxId limit 0,10) temp;
     * @param lastMaxId
     * @return
     */
    public String findToNextMaxId(String lastMaxId);
}
