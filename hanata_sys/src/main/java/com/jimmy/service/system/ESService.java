package com.jimmy.service.system;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.jimmy.bean.ESModel;
import com.jimmy.bean.es.EsRecord;
import com.jimmy.exception.CustomException;
import com.jimmy.utils.CommonUtils;
import org.elasticsearch.ElasticsearchStatusException;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.ScoreSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Jimmy He
 * @date 2020-06-16
 */
@Service
public class ESService {

    @Autowired
    RestHighLevelClient restHighLevelClient;

    @Value("${elasticsearch.indexName}")
    private String indexName;

    @Value("${elasticsearch.recordIndex}")
    private String recordIndex;


    public void newRecord(EsRecord esRecord) throws Exception {
        IndexRequest indexRequest = new IndexRequest(recordIndex);
        indexRequest.id(esRecord.getUid());

        indexRequest.source(JSON.toJSONString(esRecord), XContentType.JSON);
        IndexResponse response = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);

    }

    public void newDocument(ESModel esModel) throws IOException {


        IndexRequest indexRequest = new IndexRequest(indexName);
        indexRequest.id(esModel.getUid());

        indexRequest.source(JSON.toJSONString(esModel), XContentType.JSON);

        IndexResponse response = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);

        System.out.println("response:" + response);
    }

    public void updateDocument(String uid, ESModel esModel) throws IOException {

        UpdateRequest request = new UpdateRequest(indexName, uid);
        UpdateRequest updateRequest = request.doc(JSON.toJSONString(esModel), XContentType.JSON);
        try {
            UpdateResponse response = restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
            System.out.println("========" + response);
        } catch (ElasticsearchStatusException e) {
            this.newDocument(esModel);
        }

    }


    public Map<String,Object> search(String keyword, int from, int size) throws IOException, CustomException {
        if(keyword.equals("")){
            throw new CustomException("请输入查询词");
        }

        SearchRequest request = new SearchRequest("hanata");

        SearchSourceBuilder builder = new SearchSourceBuilder();

        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();

        boolQueryBuilder.should(QueryBuilders.matchQuery("content", keyword));
        boolQueryBuilder.should(QueryBuilders.matchQuery("title", keyword));
        boolQueryBuilder.must(QueryBuilders.termQuery("deleted", "0"));
        boolQueryBuilder.must(QueryBuilders.termQuery("open","1"));
        boolQueryBuilder.must(QueryBuilders.termQuery("state","1"));

        builder.query(boolQueryBuilder);
        builder.from(from);
        builder.size(size);

        HighlightBuilder highlightBuilder = new HighlightBuilder();
        HighlightBuilder.Field content = new HighlightBuilder.Field("content");
        HighlightBuilder.Field title = new HighlightBuilder.Field("title");

        title.preTags("<span class='highlight'>");
        title.postTags("</span>");

        content.preTags("<span class='highlight'>");
        content.postTags("</span>");

        highlightBuilder.field(title);
        highlightBuilder.field(content);
        builder.highlighter(highlightBuilder);

        request.source(builder);

        SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        long total = response.getHits().getTotalHits().value;
        return new HashMap<String,Object>(){
            {
                put("currentPage", from%size + 1);
                put("total",total);
                put("data",CommonUtils.formatESSearch(response));
            }
        };


    }

    public Map<String,Object> visitor(int from, int size) throws IOException {
        SearchRequest request = new SearchRequest("record");

        SearchSourceBuilder builder = new SearchSourceBuilder();

        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();


        builder.query(boolQueryBuilder);
        builder.from(from);
        builder.size(size);

        builder.sort(new FieldSortBuilder("createTime").order(SortOrder.DESC));

        request.source(builder);

        SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        long total = response.getHits().getTotalHits().value;
        return new HashMap<String,Object>(){
            {
                put("currentPage", from%size + 1);
                put("total",total);
                put("data",CommonUtils.formatESVistorSearch(response));
            }
        };


    }
}
