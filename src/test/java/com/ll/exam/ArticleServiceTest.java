package com.ll.exam;

import com.ll.exam.article.dto.ArticleDto;
import com.ll.exam.article.service.ArticleService;
import com.ll.exam.mymap.MyMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

public class ArticleServiceTest {

    // @BeforeEach를 붙인 아래 메서드는
    // @Test가 달려있는 메서드가 실행되기 전에 자동으로 실행이 된다.
    // 주로 테스트 환경을 깔끔하게 정리하는 역할을 한다.
    // 즉 각각의 테스트케이스가 독립적인 환경에서 실행될 수 있도록 하는 역할을 한다.

    @BeforeEach
    public void beforeEach() {
        // 게시물 테이블을 깔끔하게 삭제한다.
        // DELETE FROM article; // 보다 TRUNCATE article; 로 삭제하는게 더 깔끔하고 흔적이 남지 않는다.
        truncateArticleTable();
        // 게시물 3개를 만든다.
        // 테스트에 필요한 샘플데이터를 만든다고 보면 된다.
        makeArticleTestData();
    }

    private void makeArticleTestData() {
        MyMap myMap = Container.getObj(MyMap.class);

        IntStream.rangeClosed(1, 3).forEach(no -> {
            boolean isBlind = false;
            String title = "제목%d".formatted(no);
            String body = "내용%d".formatted(no);

            myMap.run("""
                    INSERT INTO article
                    SET createdDate = NOW(),
                    modifiedDate = NOW(),
                    title = ?,
                    `body` = ?,
                    isBlind = ?
                    """, title, body, isBlind);
        });
    }

    private void truncateArticleTable() {
        MyMap myMap = Container.getObj(MyMap.class);
        // 테이블을 깔끔하게 지워준다.
        myMap.run("TRUNCATE article");
    }

    @Test
    public void 존재한다(){
        ArticleService articleService = Container.getObj(ArticleService.class);
        assertThat(articleService).isNotNull();
    }
    @Test
    public void getArticles(){
        ArticleService articleService = Container.getObj(ArticleService.class);
        List<ArticleDto> articleDtoList = articleService.getArticles();
        assertThat(articleDtoList.size()).isEqualTo(3);
    }
    @Test
    public void getArticleById(){
        ArticleService articleService = Container.getObj(ArticleService.class);

        ArticleDto articleDto = articleService.getArticleById(1);

        assertThat(articleDto.getId()).isEqualTo(1L);
        assertThat(articleDto.getTitle()).isEqualTo("제목1");
        assertThat(articleDto.getBody()).isEqualTo("내용1");
        assertThat(articleDto.getCreatedDate()).isNotNull();
        assertThat(articleDto.getModifiedDate()).isNotNull();
        assertThat(articleDto.isBlind()).isFalse();

    }

    @Test
    public void getArticleCount(){
        ArticleService articleService = Container.getObj(ArticleService.class);

        long articlesCount = articleService.getArticlesCount();

        assertThat(articlesCount).isEqualTo(3);
    }

    @Test
    public void write(){
        ArticleService articleService = Container.getObj(ArticleService.class);

        long newArticleId = articleService.write("newTitle","newBody",false);

        ArticleDto articleDto = articleService.getArticleById(newArticleId);

        assertThat(articleDto.getId()).isEqualTo(newArticleId);
        assertThat(articleDto.getTitle()).isEqualTo("newTitle");
        assertThat(articleDto.getBody()).isEqualTo("newBody");
        assertThat(articleDto.getCreatedDate()).isNotNull();
        assertThat(articleDto.getModifiedDate()).isNotNull();
        assertThat(articleDto.isBlind()).isEqualTo(false);
    }

    @Test
    public void modify(){
        ArticleService articleService = Container.getObj(ArticleService.class);

        articleService.modify(1,"newTitle","newBody",true);

        ArticleDto articleDto = articleService.getArticleById(1);

        assertThat(articleDto.getId()).isEqualTo(1);
        assertThat(articleDto.getTitle()).isEqualTo("newTitle");
        assertThat(articleDto.getBody()).isEqualTo("newBody");
        assertThat(articleDto.getCreatedDate()).isNotNull();
        assertThat(articleDto.getModifiedDate()).isNotNull();
        assertThat(articleDto.isBlind()).isEqualTo(true);
    }
}
