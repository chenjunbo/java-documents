package com.qianfeng.elasticsearch.query;

import java.io.IOException;

public interface FilterQuery {
    public void filterInBoolQuery(String indexName) throws IOException;
    public void rangeQuery(String indexName, String fieldName, int from,int to) throws IOException;
    public void existQuery(String indexName, String fieldName) throws IOException;
    //type已经舍弃，所以此方法不再支持
    //public void typeQuery(String typeName) throws IOException ;
}
