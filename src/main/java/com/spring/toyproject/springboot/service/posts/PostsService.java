package com.spring.toyproject.springboot.service.posts;

import com.spring.toyproject.springboot.domain.posts.Posts;
import com.spring.toyproject.springboot.domain.posts.PostsRepository;
import com.spring.toyproject.springboot.web.dto.PostsResponseDto;
import com.spring.toyproject.springboot.web.dto.PostsSaveRequestDto;
import com.spring.toyproject.springboot.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PostsService {

    // Posts 클래스로 데이터베이스에 접근하게 해줄 jpa Repository
    // 기본 CRUD 메서드가 자동 생성된다.
    private final PostsRepository postsRepository;

    @Transactional
    public Long save(PostsSaveRequestDto requestDto) {
        return postsRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto) {
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물이 없습니다. id=" + id));

        posts.update(requestDto.getTitle(), requestDto.getContent());

        return id;
    }

    public PostsResponseDto findById(Long id) {
        Posts entity = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물이 없습니다. id=" + id));

        return new PostsResponseDto(entity);
    }


}
