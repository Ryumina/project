package com.spring.toyproject.springboot.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.toyproject.springboot.domain.posts.Posts;
import com.spring.toyproject.springboot.domain.posts.PostsRepository;
import com.spring.toyproject.springboot.web.dto.PostsSaveRequestDto;
import com.spring.toyproject.springboot.web.dto.PostsUpdateRequestDto;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostsApiControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Before
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @After
    public void tearDown() throws Exception {
        postsRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = "USER")
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
        mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                        .andExpect(status().isOk());

        // then
        List<Posts> allData = postsRepository.findAll();

        assertThat(allData.get(0).getTitle()).isEqualTo(title);
        assertThat(allData.get(0).getContent()).isEqualTo(content);
    }

    @Test
    @WithMockUser(roles = "USER")
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

        // when
        mvc.perform(put(url)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(new ObjectMapper().writeValueAsString(updateRequestDto)))
                        .andExpect(status().isOk());

        // then
        List<Posts> all = postsRepository.findAll();

        assertThat(all.get(0).getTitle()).isEqualTo(title);
        assertThat(all.get(0).getContent()).isEqualTo(content);
    }

//    @Test
//    public void 게시글_삭제() {
//
//        // given
//        PostsSaveRequestDto requestDto1 = PostsSaveRequestDto.builder()
//                .title("title1")
//                .content("content1")
//                .author("author1")
//                .build();
//
//        PostsSaveRequestDto requestDto2 = PostsSaveRequestDto.builder()
//                .title("title2")
//                .content("content2")
//                .author("author2")
//                .build();
//
//        String url = "http://localhost:" + port + "/api/v1/posts";
//
//        // 등록
//        ResponseEntity<Long> responseEntity1 = restTemplate.postForEntity(url, requestDto1, Long.class);
//        ResponseEntity<Long> responseEntity2 = restTemplate.postForEntity(url, requestDto2, Long.class);
//
//        List<Posts> posts = postsRepository.findAll();
//
//        Long deleteId = posts.get(0).getId();
//
//        // when
//        // 등록된 2개의 데이터에서 한개를 삭제한다.
//        String deleteUrl = "http://localhost:" + port + "/api/v1/posts/" + deleteId;
//        restTemplate.delete(deleteUrl);
//
//        // then
//        // 남은 데이터를 확인한다.
//        List<Posts> result = postsRepository.findAll();
//
//        Assertions.assertThat(result.size()).isEqualTo(1);
////        틀린 테스트
////        Assertions.assertThat(result.get(0).getTitle()).isEqualTo("title1");
//        Assertions.assertThat(result.get(0).getTitle()).isEqualTo("title2");
//    }
}