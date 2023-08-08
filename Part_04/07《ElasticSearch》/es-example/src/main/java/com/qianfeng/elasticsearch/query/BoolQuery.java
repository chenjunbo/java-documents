package com.qianfeng.elasticsearch.query;

import java.io.IOException;

public interface BoolQuery {
    public void boolQuery(String indexName) throws IOException ;
    public void boostingQuery(String indexName) throws IOException;
}
