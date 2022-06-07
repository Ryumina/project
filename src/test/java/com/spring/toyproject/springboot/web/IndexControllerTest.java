package com.spring.toyproject.springboot.web;

import com.spring.toyproject.springboot.domain.posts.Posts;
import com.spring.toyproject.springboot.domain.posts.PostsRepository;
import com.spring.toyproject.springboot.web.dto.PostsSaveRequestDto;
import junit.framework.TestCase;
import org.apache.coyote.Response;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IndexControllerTest extends TestCase {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private PostsRepository postsRepository;

    @After
    public void clear() throws Exception{
        postsRepository.deleteAll();
    }

    @Test
    public void 메인페이지_접근() {
        // when
        String body = testRestTemplate.getForObject("/", String.class);

        // then
        Assertions.assertThat(body).contains("스프링 부트로 시작하는 웹 서비스");
    }

    @Test
    public void 게시글_등록페이지_접근() {

        // when
        String body = testRestTemplate.getForObject("/posts/save", String.class);

        // then
        Assertions.assertThat(body).contains("게시글 등록");
    }

    @Test
    public void 게시글_수정페이지_접근() {

        // given
        String title = "testTitle";
        String content = "testContent";
        PostsSaveRequestDto requestDto = PostsSaveRequestDto.builder()
                .title(title)
                .content(content)
                .author("author")
                .build();

        String url = "http://localhost:" + port + "/api/v1/posts";

        ResponseEntity<Long> responseEntity = testRestTemplate.postForEntity(url, requestDto, Long.class);

        List<Posts> posts = postsRepository.findAll();

        Long id = posts.get(0).getId();

        String updateUrl = "http://localhost:" + port + "/posts/update/" + id;

        // when /posts/update/{id}
        String body = testRestTemplate.getForObject(updateUrl, String.class);

        // then
        Assertions.assertThat(body).contains("게시글 수정");
        Assertions.assertThat(body).contains("testContent");
    }

    @Test
    public void 게시글_전체조회() {

        // given
        PostsSaveRequestDto requestDto1 = PostsSaveRequestDto.builder()
                .title("title1")
                .content("content1")
                .author("author1")
                .build();

        String url = "http://localhost:" + port + "/api/v1/posts";
        ResponseEntity<Long> responseEntity1 = testRestTemplate.postForEntity(url, requestDto1, Long.class);

        PostsSaveRequestDto requestDto2 = PostsSaveRequestDto.builder()
                .title("title2")
                .content("content2")
                .author("author2")
                .build();
        ResponseEntity<Long> responseEntity2 = testRestTemplate.postForEntity(url, requestDto2, Long.class);

        PostsSaveRequestDto requestDto3 = PostsSaveRequestDto.builder()
                .title("title3")
                .content("content3")
                .author("author3")
                .build();
        ResponseEntity<Long> responseEntity3 = testRestTemplate.postForEntity(url, requestDto3, Long.class);

        // when
        List<Posts> posts = postsRepository.findAllDesc();

        // then
        Assertions.assertThat(posts.size()).isEqualTo(3);
        Assertions.assertThat(posts.get(0).getTitle()).isEqualTo("title3");
    }

    @Test
    public void 게시글_목록페이지_진입() {

        // given
        PostsSaveRequestDto requestDto1 = PostsSaveRequestDto.builder()
                .title("title1")
                .content("content1")
                .author("author1")
                .build();

        String url = "http://localhost:" + port + "/api/v1/posts";
        ResponseEntity<Long> responseEntity1 = testRestTemplate.postForEntity(url, requestDto1, Long.class);

        PostsSaveRequestDto requestDto2 = PostsSaveRequestDto.builder()
                .title("title2")
                .content("content2")
                .author("author2")
                .build();
        ResponseEntity<Long> responseEntity2 = testRestTemplate.postForEntity(url, requestDto2, Long.class);

        PostsSaveRequestDto requestDto3 = PostsSaveRequestDto.builder()
                .title("title3")
                .content("content3")
                .author("author3")
                .build();
        ResponseEntity<Long> responseEntity3 = testRestTemplate.postForEntity(url, requestDto3, Long.class);

        String listUrl = "http://localhost:" + port + "/";

        // when
        String body = testRestTemplate.getForObject(listUrl, String.class);

        // then
        Assertions.assertThat(body).contains("스프링 부트로 시작하는 웹 서비스");
        Assertions.assertThat(body).contains("title2");
        Assertions.assertThat(body).contains("author3");
    }

}