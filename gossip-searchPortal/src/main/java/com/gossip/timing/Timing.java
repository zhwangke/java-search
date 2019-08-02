package com.gossip.timing;

import com.gossip.service.IndexWriterProtalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @Author: WK
 * @Data: 2019/7/23 13:36
 * @Description: com.gossip.timing
 */
// 定时执行的类
//@Component
public class Timing {

    @Autowired
    private IndexWriterProtalService indexWriterProtalService;
    // 只要能够让timing这个方法定时的执行, 那么service的方法也就会定时的执行  SpringTask

    //@Scheduled(cron = "0/10 * * * * ?")
    public void timing(){

        try {
            System.out.println(111);
            indexWriterProtalService.saveBeans();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

