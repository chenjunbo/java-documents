package com.qianfeng.elasticsearch.query;


import org.elasticsearch.index.query.Operator;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface BaseQuery {
    public List<Map<String, Object>> termQuery(String indexName, String fieldName, String fieldValue) throws IOException;
    public List<Map<String,Object>> termsQuery(String indexName, String fieldName, String... fieldValues) throws IOException;
    public void queryAll(String indexName) throws IOException ;
    public void queryMatch(String indexName, String field,String keyWord) throws IOException ;
    public void queryMatchWithOperate(String indexName, String field, String keyWord, Operator op) throws IOException;
    public void queryMulitMatch(String indexName,String keyWord,String ...fieldNames) throws IOException;
    public void queryMatchPhrase(String indexName,String fieldName,String keyWord) throws IOException ;
//    public void queryMatchPrefixQuery(String indexName,String fieldName,String keyWord) throws IOException;
    public void idsQuery(String indexName,String ... ids) throws IOException ;
    public void prefixQuery(String indexName, String field, String prefix) throws IOException ;
    public void fuzzyQuery(String indexName,String field,String value) throws IOException ;
    public void wildCardQuery(String indexName, String fieldName, String wildcard) throws IOException;
    public void rangeQuery(String indexName, String fieldName, int from,int to) throws IOException ;
    public void regexpQuery(String indexName, String fieldName, String regexp) throws IOException;
//    public void moreLikeThisQuery(String indexName, String[] fieldNames, String[] likeTexts) throws IOException;
    public void scrollQuery(String indexName) throws IOException ;
}