package com.gossip.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.gossip.pojo.News;
import com.gossip.pojo.ResultBean;
import com.gossip.service.IndexSearcherProtalService;
import com.search.service.IndexSearcherService;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @Author: WK
 * @Data: 2019/7/25 20:10
 * @Description: com.gossip.service.impl
 */
@Service
public class IndexSearcherProtalServiceImpl implements IndexSearcherProtalService {
    @Reference
    private IndexSearcherService indexSearcherService;
    
    @Override
    public ResultBean findByPage(ResultBean resultBean) throws Exception {
        //1调用搜索服务 查询数据
        resultBean = indexSearcherService.findByPage(resultBean);
        //2执行相关操作
        List<News> newsList = resultBean.getPageBean().getNewsList();
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (News news:newsList) {
            String oldtime = news.getTime();
            Date oldDate = format1.parse(oldtime);
            long time = oldDate.getTime();
            oldDate.setTime(time-(1000*60*60*8));

            String newTime = format1.format(oldDate);
            news.setTime(newTime);

        }
        //3返回给controller
        return resultBean;
    }
}
