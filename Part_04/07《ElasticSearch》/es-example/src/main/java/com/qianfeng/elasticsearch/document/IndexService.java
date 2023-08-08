package com.qianfeng.elasticsearch.document;


import org.elasticsearch.client.indices.CreateIndexRequest;

import java.io.IOException;

public interface IndexService {
    public void createIndex(String index, CreateIndexRequest request) throws IOException;

    public void deleteIndex(String index) throws IOException;

    public boolean existsIndex(String index) throws IOException;
}

