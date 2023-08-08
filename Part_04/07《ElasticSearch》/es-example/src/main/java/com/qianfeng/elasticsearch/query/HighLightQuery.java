package com.qianfeng.elasticsearch.query;

import java.io.IOException;

public interface HighLightQuery {
    public void hightLightQuery(String indexName,String field,String keyword) throws IOException;
    public void hightLightQueryByFragment(String indexName, int fragmentSize) throws IOException;
    public void hightLightQueryByNumOfFragments(String indexName,  int fragmentSize,int numOfFragments) throws IOException;
    public void hightLightNoMatchSize(String indexName, int fragmentSize,int numOfFragments,int noMatchSize) throws IOException;
}
