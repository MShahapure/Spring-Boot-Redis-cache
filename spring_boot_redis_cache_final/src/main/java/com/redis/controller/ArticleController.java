package com.redis.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.UriComponentsBuilder;

import com.redis.entity.Article;
import com.redis.service.IArticleService;

@Controller
@RequestMapping("user")
public class ArticleController {
	@Autowired
	private IArticleService articleService;
	
	@GetMapping("article/{id}")
	public ResponseEntity<Article> getArticleById(@PathVariable("id") Long id) {
		Article article = articleService.getArticleById(id);
		return new ResponseEntity<>(article, HttpStatus.OK);
	}
	@GetMapping("articles")
	public ResponseEntity<List<Article>> getAllArticles() {
		List<Article> list = articleService.getAllArticles();
		return new ResponseEntity<>(list, HttpStatus.OK);
	}
	@PostMapping("article")
	public ResponseEntity<Article> addArticle(@RequestBody Article article, UriComponentsBuilder builder) {
		Article savedArticle = articleService.addArticle(article);  
        return new ResponseEntity<>(savedArticle, HttpStatus.CREATED);
	}
	@PutMapping("article")
	public ResponseEntity<Article> updateArticle(@RequestBody Article article) {
		articleService.updateArticle(article);
		return new ResponseEntity<>(article, HttpStatus.OK);
	}
	@DeleteMapping("article/{id}")
	public ResponseEntity<Void> deleteArticle(@PathVariable("id") Long id) {
		articleService.deleteArticle(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}	
} 