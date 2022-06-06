package com.spring.toyproject.springboot.web.dto;

import com.spring.toyproject.springboot.domain.posts.Posts;
import lombok.Getter;

/**
 * 데이터 조회시 사용되는 dto
 */
@Getter
public class PostsResponseDto {

    private Long id;
    private String title;
    private String content;
    private String author;

    public PostsResponseDto(Posts entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.author = entity.getAuthor();
    }
}
