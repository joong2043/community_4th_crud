package com.ll.exam.article.service;

import com.ll.exam.annotation.Autowired;
import com.ll.exam.annotation.Service;
import com.ll.exam.article.dto.ArticleDto;
import com.ll.exam.article.repository.ArticleRepository;

import java.util.List;

@Service
public class ArticleService {
    @Autowired
    private ArticleRepository articleRepository;

    public List<ArticleDto> getArticles() {
        return articleRepository.getArticles();
    }

    public long getArticlesCount(){
        return articleRepository.getArticlesCount();
    }

    public ArticleDto getArticleById(long id) {
        return articleRepository.getArticleById(id);
    }

    public long write(String title, String body, boolean isBlind) {
        return articleRepository.write(title,body,isBlind);
    }

    public long modify(long id, String updateTitle, String updateBody, boolean isBlind) {
        return articleRepository.modify(id,updateTitle,updateBody,isBlind);

    }
}
