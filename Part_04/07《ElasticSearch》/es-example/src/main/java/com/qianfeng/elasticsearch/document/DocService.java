package com.qianfeng.elasticsearch.document;

import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.update.UpdateResponse;

import java.io.IOException;
import java.util.Map;

public interface DocService {
    public void add(String index,String json) throws IOException;
    public void deleteDocWithId(String indexName, String docId) throws IOException;
    public void bulkDeleteDoc(String indexName, String[] docIdArr) throws IOException;
    public void refresh(String indexName) throws IOException;
    public long countDoc(String indexName) throws IOException ;
    public void add(String index,String json,String id) throws IOException ;
    public UpdateResponse update(String index, Map<String, Object> map, String id) throws IOException ;
    public BulkResponse bulkUpdateOrInsertDoc(String indexName, BulkRequest builder) throws IOException ;
    public void deleteByQuery(String indexName,String fieldName,String  fieldValue) throws IOException ;
}
