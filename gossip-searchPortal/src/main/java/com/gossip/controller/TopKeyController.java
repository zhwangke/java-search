package com.gossip.controller;

import com.gossip.service.TopKeyService;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;
/**
 * @Author: WK
 * @Data: 2019/7/27 16:38
 * @Description: com.gossip.controller
 */
@Controller
public class TopKeyController {
    @Autowired
    private TopKeyService topKeyService;

    @RequestMapping("/top")
    @ResponseBody
    public List<Map<String,Object>>  findByTopKey(Integer num){
        //1. 验证 参数是否正确
        if(num == null){
            num = 5;
        }
        //2. 调用service层, 查询数据
        List<Map<String, Object>> mapList = topKeyService.findByTopKey(num);
        return mapList;
    }
}
