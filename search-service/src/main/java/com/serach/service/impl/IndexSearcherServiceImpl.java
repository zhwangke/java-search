package com.serach.service.impl;

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
 * @Description: com.serach.service.impl
 */

@Service
public class IndexSearcherServiceImpl implements IndexSearcherService{

    @Autowired
    private SolrServer solrServer;

    @Override
    public ResultBean findByPage(ResultBean resultBean) throws Exception {
        //1封装查询的条件：solrQuery
            //查询入口的条件 ---  根据关键词
        SolrQuery solrQuery = new SolrQuery(resultBean.getKeywords());
            //分页的条件
        Integer page = resultBean.getPageBean().getPage();
        Integer pageSize = resultBean.getPageBean().getPageSize();
            //从第几条数据开始查 eg:第1页从0条开始
        solrQuery.setStart( (page -1)*pageSize );
            //每页显示的数据
        solrQuery.setRows(pageSize);

        //添加高亮的条件
        solrQuery.setHighlight(true);
        solrQuery.addHighlightField("title");
        solrQuery.addHighlightField("content");
        solrQuery.setHighlightSimplePre("<font color='red'>");
        solrQuery.setHighlightSimplePost("</font>");



        //2执行操作
        QueryResponse response = solrServer.query(solrQuery);

        //3封装数据
        PageBean pageBean = resultBean.getPageBean();
        Map<String, Map<String, List<String>>> highlighting = response.getHighlighting(); //高亮的结果内容
            //封装每页的数据
        // 封装不成功的, 因为有一个time字段是date, 但是pojo是string
        //List<News> newsList = response.getBeans(News.class);
        SolrDocumentList documentList = response.getResults();
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss" );
        List<News> newsList = new ArrayList<News>();
        for (SolrDocument document : documentList) {
            String id = (String) document.get("id");
            String title = (String)document.get("title");
            Date date = (Date) document.get("time");
            String source =(String) document.get("source");
            String content =(String) document.get("content");
            String editor = (String) document.get("editor");
            String docurl = (String) document.get("docurl");
            //############高亮##################
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
            //############高亮##################


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
