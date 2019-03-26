package com.redis.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import com.redis.entity.Article;
import com.redis.repository.ArticleRepository;

@Service
public class ArticleService implements IArticleService {
	private final static Logger LOG = LoggerFactory.getLogger(ArticleService.class);
	@Autowired
	private ArticleRepository articleRepository;

	@Override
	@Cacheable(value = "articleCache", key = "#articleId")
	public Article getArticleById(long articleId) {
		LOG.info("--- Inside getArticleById() ---");
		Optional<Article> article = articleRepository.findById(articleId);
		if(article.isPresent()) {
			return (Article)article.get();
		}
		return null;
		
	}

	@Override
	@Cacheable(value = "allArticlesCache", unless = "#result.size() == 0")
	public List<Article> getAllArticles() {
		LOG.info("--- Inside getAllArticles() ---");
		List<Article> list = new ArrayList<>();
		articleRepository.findAll().forEach(e -> list.add(e));
		return list;
	}

	@Override
	@Caching(put = { @CachePut(value = "articleCache", key = "#article.articleId") }, evict = {
			@CacheEvict(value = "allArticlesCache", allEntries = true) })
//	@CachePut(value= "articleCache", key= "#article.articleId")
	public Article addArticle(Article article) {
		LOG.info("--- Inside addArticle() ---");
		return articleRepository.save(article);
	}

	@Override
	@Caching(put = { @CachePut(value = "articleCache", key = "#article.articleId") }, evict = {
			@CacheEvict(value = "allArticlesCache", allEntries = true) })
	public Article updateArticle(Article article) {
		LOG.info("--- Inside updateArticle() ---");
		return articleRepository.save(article);
	}

	@Override
	@Caching(evict = { @CacheEvict(value = "articleCache", key = "#articleId"),
			@CacheEvict(value = "allArticlesCache", allEntries = true) })
	public void deleteArticle(long articleId) {
		LOG.info("--- Inside deleteArticle() ---");
		articleRepository.delete(articleRepository.findById(articleId).get());
	}
}