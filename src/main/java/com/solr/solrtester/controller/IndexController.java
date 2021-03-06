package com.solr.solrtester.controller;

import com.solr.solrtester.model.SerchModel;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/")
public class IndexController {


    @Value("${solr.instance.url}")
    private String solrInstanceUrl ;

    @RequestMapping("/")
    public ModelAndView index(){

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        return modelAndView;
    }


    @RequestMapping("/serch")
    public ModelAndView serch(String keyword,String fileds[]) throws IOException, SolrServerException {


        HttpSolrClient client = new HttpSolrClient.Builder(solrInstanceUrl).build();
        // 创建SolrQuery
        SolrQuery query = new SolrQuery();
        // 输入查询条件
        String queryString =  SerchModel.queryString(fileds,keyword);
        query.setQuery(queryString);

        long startTime = System.currentTimeMillis();
        // 执行查询并返回结果
        QueryResponse response = client.query(query);

        // 获取匹配的所有结果
        SolrDocumentList list = response.getResults();
        long endTime = System.currentTimeMillis();

        long useTime = endTime - startTime;
        // 匹配结果总数
        long count = list.getNumFound();
        List<SerchModel> smlist = SerchModel.build(list,count);


        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("result");
        modelAndView.addObject("smlist",smlist);
        modelAndView.addObject("count",count);
        modelAndView.addObject("useTime",useTime);
        return modelAndView;
    }
}
