package com.journaldev.elasticsearch.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.Scroll;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.journaldev.elasticsearch.bean.AnnonceTravail;
import com.journaldev.elasticsearch.bean.Conference;
@Repository
public class AnnonceTravailDao {
	
	private final String INDEX = "emploi";
    private final String TYPE = "offre";

    private RestHighLevelClient restHighLevelClient;

    private ObjectMapper objectMapper;

    public AnnonceTravailDao( ObjectMapper objectMapper, RestHighLevelClient restHighLevelClient) {
        this.objectMapper = objectMapper;
        this.restHighLevelClient = restHighLevelClient;
    }

    public AnnonceTravail insertAnnonceTravail(AnnonceTravail annonceTravail){
    	annonceTravail.setId(UUID.randomUUID().toString());
        Map<String, Object> dataMap = objectMapper.convertValue(annonceTravail, Map.class);
        IndexRequest indexRequest = new IndexRequest(INDEX, TYPE, annonceTravail.getId())
                .source(dataMap);
        try {
            IndexResponse response = restHighLevelClient.index(indexRequest);
        } catch(ElasticsearchException e) {
            e.getDetailedMessage();
        } catch (java.io.IOException ex){
            ex.getLocalizedMessage();
        }
        return annonceTravail;
    }

    public Map<String, Object> getAnnonceTravailById(String id){
        GetRequest getRequest = new GetRequest(INDEX, TYPE, id);
        GetResponse getResponse = null;
        try {
            getResponse = restHighLevelClient.get(getRequest);
        } catch (java.io.IOException e){
            e.getLocalizedMessage();
        }
        Map<String, Object> sourceAsMap = getResponse.getSourceAsMap();
        return sourceAsMap;
    }

    public Map<String, Object> updateAnnonceTravailById(String id, AnnonceTravail annonceTravail){
        UpdateRequest updateRequest = new UpdateRequest(INDEX, TYPE, id)
                .fetchSource(true);    // Fetch Object after its update
        Map<String, Object> error = new HashMap<>();
        error.put("Error", "Unable to update annonceTravail");
        try {
            String bookJson = objectMapper.writeValueAsString(annonceTravail);
            updateRequest.doc(bookJson, XContentType.JSON);
            UpdateResponse updateResponse = restHighLevelClient.update(updateRequest);
            Map<String, Object> sourceAsMap = updateResponse.getGetResult().sourceAsMap();
            return sourceAsMap;
        }catch (JsonProcessingException e){
            e.getMessage();
        } catch (java.io.IOException e){
            e.getLocalizedMessage();
        }
        return error;
    }

    public void deleteAnnonceTravailById(String id) {
        DeleteRequest deleteRequest = new DeleteRequest(INDEX, TYPE, id);
        try {
            DeleteResponse deleteResponse = restHighLevelClient.delete(deleteRequest);
        } catch (java.io.IOException e){
            e.getLocalizedMessage();
        }
    }
    
    public List<Map<String, Object>> getAllAnnoneTravails(){
        List<Map<String,Object>> esData = new ArrayList<Map<String,Object>>();
        SearchRequest request = new SearchRequest(INDEX).scroll(new TimeValue(60000));
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchSourceBuilder.sort(FieldSortBuilder.DOC_FIELD_NAME, SortOrder.ASC);
        request.source(searchSourceBuilder);
        try {
			SearchResponse scrollResp = restHighLevelClient.search(request);
			for(SearchHit hit : scrollResp.getHits()){
                esData.add(hit.getSourceAsMap());
               }
			final Scroll scroll = new Scroll(TimeValue.timeValueMinutes(1L));
        	String scrollId = scrollResp.getScrollId();
        	SearchHit[] searchHits = scrollResp.getHits().getHits();
        	while (searchHits != null && searchHits.length > 1) { 
        	    SearchScrollRequest scrollRequest = new SearchScrollRequest(scrollId); 
        	    scrollRequest.scroll(scroll);
        	    scrollResp = restHighLevelClient.searchScroll(new SearchScrollRequest(scrollId).scroll(new TimeValue(60000)));
        	    scrollId = scrollResp.getScrollId();
        	    searchHits = scrollResp.getHits().getHits();
        	    for(SearchHit hit : searchHits){
                    esData.add(hit.getSourceAsMap());
                    
                }
        	    
        	}
        	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return esData;
        
}
}
