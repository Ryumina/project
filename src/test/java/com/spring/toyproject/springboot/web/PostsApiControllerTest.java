package com.spring.toyproject.springboot.web;

import com.spring.toyproject.springboot.domain.posts.Posts;
import com.spring.toyproject.springboot.domain.posts.PostsRepository;
import com.spring.toyproject.springboot.web.dto.PostsSaveRequestDto;
import com.spring.toyproject.springboot.web.dto.PostsUpdateRequestDto;
import junit.framework.TestCase;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.PrivateKey;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostsApiControllerTest extends TestCase {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PostsRepository postsRepository;

    @After
    public void tearDown() throws Exception {
        postsRepository.deleteAll();
    }

    @Test
    public void Posts_등록된다() throws Exception {
        // given
        String title = "title";
        String content = "content";
        PostsSaveRequestDto requestDto = PostsSaveRequestDto.builder()
                .title(title)
                .content(content)
                .author("author")
                .build();

        String url = "http://localhost:" + port + "/api/v1/posts";

        // when
        ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, requestDto, Long.class);

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<Posts> allData = postsRepository.findAll();

        assertThat(allData.get(0).getTitle()).isEqualTo(title);
        assertThat(allData.get(0).getContent()).isEqualTo(content);
    }

    @Test
    public void Posts_수정된다() throws Exception {
        // given
        Posts savedPosts = postsRepository.save(Posts.builder()
                .title("title")
                .content("content")
                .author("author")
                .build());

        Long updateId = savedPosts.getId();
        String title = "title2";
        String content = "content2";

        PostsUpdateRequestDto updateRequestDto = PostsUpdateRequestDto.builder()
                .title(title)
                .content(content)
                .build();

        String url = "http://localhost:" + port + "/api/v1/posts/" + updateId;

        HttpEntity<PostsUpdateRequestDto> requestEntity = new HttpEntity<>(updateRequestDto);

        // when

        ResponseEntity<Long> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Long.class);

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(title);
        assertThat(all.get(0).getContent()).isEqualTo(content);
    }

    @Test
    public void 게시글_삭제() {

        // given
        PostsSaveRequestDto requestDto1 = PostsSaveRequestDto.builder()
                .title("title1")
                .content("content1")
                .author("author1")
                .build();

        PostsSaveRequestDto requestDto2 = PostsSaveRequestDto.builder()
                .title("title2")
                .content("content2")
                .author("author2")
                .build();

        String url = "http://localhost:" + port + "/api/v1/posts";

        // 등록
        ResponseEntity<Long> responseEntity1 = restTemplate.postForEntity(url, requestDto1, Long.class);
        ResponseEntity<Long> responseEntity2 = restTemplate.postForEntity(url, requestDto2, Long.class);

        List<Posts> posts = postsRepository.findAll();

        Long deleteId = posts.get(0).getId();

        // when
        // 등록된 2개의 데이터에서 한개를 삭제한다.
        String deleteUrl = "http://localhost:" + port + "/api/v1/posts/" + deleteId;
        restTemplate.delete(deleteUrl);

        // then
        // 남은 데이터를 확인한다.
        List<Posts> result = postsRepository.findAll();

        Assertions.assertThat(result.size()).isEqualTo(1);
//        틀린 테스트
//        Assertions.assertThat(result.get(0).getTitle()).isEqualTo("title1");
        Assertions.assertThat(result.get(0).getTitle()).isEqualTo("title2");
    }
}