package com.run.start;

import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.aggregations.Aggregations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ElasticsearchStartRunApplication {
	public static void main(String[] args) {
		SpringApplication.run(ElasticsearchStartRunApplication.class, args);
	}
	
}
