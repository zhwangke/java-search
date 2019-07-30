package com.topkey.service.impl;

/**
 * @Author: WK
 * @Data: 2019/7/28 19:34
 * @Description: com.topkey.service.impl
 */
import com.alibaba.dubbo.config.annotation.Reference;
import com.google.gson.Gson;
import com.gossip.pojo.PageBean;
import com.gossip.pojo.ResultBean;
import com.search.service.IndexSearcherService;
import com.topkey.service.TopKeyForKafkaService;
import com.topkey.utils.JedisUtils;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

@Service  // 这个并不是需要远程调用的服务, 是一个普通服务项
public class TopKeyForKafkaServiceImpl  implements TopKeyForKafkaService {
    @Reference
    private IndexSearcherService indexSearcherService;

    @Override
    public void findByTopKeyToRedis(String topkey) throws Exception {
        //1. 调用查询solr的服务工程, 查询数据
        ResultBean resultBean = new ResultBean();

        resultBean.setKeywords(topkey);
        resultBean.setPageBean(new PageBean());

        resultBean = indexSearcherService.findByPage(resultBean);

        //2. 判断总页数是否是大于5

        Integer pageNumber = resultBean.getPageBean().getPageNumber();

        if(pageNumber > 5 ){
            pageNumber = 5 ;
        }

        //3. 循环获取
        Gson gson = new Gson();
        Jedis jedis = JedisUtils.getJedis();
        for(int i = 1 ; i<= pageNumber ; i++){
            resultBean.getPageBean().setPage(i); // 设置页码

            resultBean = indexSearcherService.findByPage(resultBean);
            //4. 将数据转化成json格式的数据
            String resultBeanJson = gson.toJson(resultBean);

            //5.  将这个json数据, 放置到redis中
            jedis.setex("bigdata:gossip:"+topkey+":"+i,60*30,resultBeanJson);
        }
        jedis.close();

    }

}
