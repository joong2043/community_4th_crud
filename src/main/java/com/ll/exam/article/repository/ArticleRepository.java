package com.ll.exam.article.repository;

import com.ll.exam.annotation.Autowired;
import com.ll.exam.annotation.Repository;
import com.ll.exam.article.dto.ArticleDto;
import com.ll.exam.mymap.MyMap;
import com.ll.exam.mymap.SecSql;

import java.util.List;

@Repository
public class ArticleRepository {
    @Autowired
    private MyMap myMap;

    public List<ArticleDto> getArticles() {
        SecSql sql = myMap.genSecSql();
        sql
                .append("SELECT *")
                .append("FROM article")
                .append("ORDER BY id DESC");
        return sql.selectRows(ArticleDto.class);
    }

    public long getArticlesCount(){
        SecSql sql = myMap.genSecSql();
        sql
                .append("SELECT COUNT(*)")
                .append("FROM article");
        return sql.selectLong();
    }

    public ArticleDto getArticleById(long id) {
        SecSql sql = myMap.genSecSql();
        sql
                .append("SELECT *")
                .append("FROM article")
                .append("WHERE id =?",id);
        return sql.selectRow(ArticleDto.class);
    }

    public long write(String title, String body, boolean isBlind) {
        SecSql sql = myMap.genSecSql();
        sql
                .append("INSERT INTO article")
                .append("SET createdDate = NOW()")
                .append(", modifiedDate = NOW()")
                .append(", title = ?",title)
                .append(", body = ?",body)
                .append(", isBlind = ?",isBlind);
        return sql.insert();
    }

    public long modify(long id, String updateTitle, String updateBody, boolean isBlind) {
        SecSql sql = myMap.genSecSql();
        sql
                .append("update article")
                .append("set title = ?",updateTitle)
                .append(", body=?",updateBody)
                .append(", isBlind=?",isBlind)
                .append(", modifiedDate = NOW()")
                .append("where id=?",id);
        return sql.update();

    }
}
