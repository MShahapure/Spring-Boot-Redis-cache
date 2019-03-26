package com.redis.repository;
import org.springframework.data.repository.CrudRepository;

import com.redis.entity.Article;

public interface ArticleRepository extends CrudRepository<Article, Long>  {
}
