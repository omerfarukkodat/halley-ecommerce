package com.kodat.of.halleyecommerce.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.kodat.of.halleyecommerce.product")
public class ElasticSearchConfig {
}
