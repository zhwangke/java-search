package com.search.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.gossip.pojo.News;
import com.gossip.pojo.PageBean;
import com.gossip.pojo.ResultBean;
import com.search.service.IndexSearcherService;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: WK
 * @Data: 2019/7/25 19:05
 * @Description: com.search.service.impl
 */

@Service
public class IndexSearcherServiceImpl implements IndexSearcherService{

    @Autowired
    private SolrServer solrServer;

    @Override
    public ResultBean findByPage(ResultBean resultBean) throws Exception {

        //1. 封装查询的条件:  SolrQuery
        //1.1 主入口的条件: 根据关键词查询
        SolrQuery solrQuery = new SolrQuery(resultBean.getKeywords());
        //1.2 分页的条件
        Integer page = resultBean.getPageBean().getPage();
        Integer pageSize = resultBean.getPageBean().getPageSize();
        //从第几条开始查
        solrQuery.setStart( (page -1)*pageSize );
        //每页显示多少
        solrQuery.setRows(pageSize);

        //1.3 添加高亮的条件
        solrQuery.setHighlight(true); //开启高亮
        solrQuery.addHighlightField("title");
        solrQuery.addHighlightField("content");
        solrQuery.setHighlightSimplePre("<font color = 'red'>");
        solrQuery.setHighlightSimplePost("</font>");

        //1.4 添加搜索工具的条件 : 先判断, 后添加
        //1.4.1: 时间范围查询
        String dateStart = resultBean.getDateStart();
        String dateEnd = resultBean.getDateEnd();
        if(dateStart != null && !"".equals(dateStart) && dateEnd !=null && !"".equals(dateEnd)){
            // 说明 dateStart  和 dateEnd 都有内容, 需要进行日期范围查询:  UTC
            // 07/23/2019 15:13:29
            SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            Date oldDateStart = format1.parse(dateStart);
            Date oldDateEnd = format1.parse(dateEnd);
            String newDateStart = format2.format(oldDateStart);
            String newDateEnd = format2.format(oldDateEnd);

            solrQuery.addFilterQuery("time:[ "+newDateStart+" TO "+newDateEnd+" ]");
        }
        //1.4.2: 根据编辑查询
        String editor = resultBean.getEditor();
        if(editor != null && !"".equals(editor)){
            solrQuery.addFilterQuery("editor:"+editor);
        }
        //1.4.3: 根据来源查询
        String source = resultBean.getSource();
        if(source != null && !"".equals(source)){
            solrQuery.addFilterQuery("source:"+source);
        }



        //2. 执行查询操作
        QueryResponse response = solrServer.query(solrQuery);

        //3. 封装数据 : pageBean
        PageBean pageBean = resultBean.getPageBean();
        //3.1 封装每页的数据
        //List<News> newsList = response.getBeans(News.class); // 封装不成功的, 因为有一个time字段是date, 但是pojo是string
        SolrDocumentList documentList = response.getResults(); // 搜索结果数据
        Map<String, Map<String, List<String>>> highlighting = response.getHighlighting(); //高亮的结果内容

        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss" );
        List<News>  newsList = new ArrayList<>();
        for (SolrDocument document : documentList) {
            String id = (String) document.get("id");
            String title = (String)document.get("title");
            Date date = (Date) document.get("time");
            source =(String) document.get("source");
            String content =(String) document.get("content");
            editor = (String) document.get("editor");
            String docurl = (String) document.get("docurl");

            Map<String, List<String>> listMap = highlighting.get(id);
            List<String> list = listMap.get("title");
            if(list != null && list.size() > 0 ){
                // 有高亮的内容
                title = list.get(0);
            }
            list = listMap.get("content");
            if(list != null && list.size() > 0 ){
                // 有高亮的内容
                content = list.get(0);
            }else {
                // 如果content没有高亮的内容, 应该对原有数据, 进行截取操作
                int size = content.length();
                if(size >100){
                    content = content.substring(0,99)+"......";
                }
            }

            News news = new News();

            news.setId(id);
            news.setTitle(title);

            String newTime = format1.format(date);
            news.setTime(newTime);

            news.setSource(source);
            news.setContent(content);
            news.setEditor(editor);
            news.setDocurl(docurl);


            newsList.add(news);
        }

        pageBean.setNewsList(newsList);

        //3.2 总条数封装
        Long pageCount = documentList.getNumFound();

        pageBean.setPageCount(pageCount.intValue());

        //3.3 封装 总页数:
        Double pageNumber = Math.ceil((double) pageCount / pageSize);

        pageBean.setPageNumber(pageNumber.intValue());


        //4. 将封装好pageBean对象, 设置到resultBean中
        resultBean.setPageBean(pageBean);

        return resultBean;
    }
}
