package com.spring.toyproject.springboot.web;

import com.spring.toyproject.springboot.config.auth.LoginUser;
import com.spring.toyproject.springboot.config.auth.dto.SessionUser;
import com.spring.toyproject.springboot.service.posts.PostsService;
import com.spring.toyproject.springboot.web.dto.PostsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final PostsService postsService;
    private final HttpSession httpSession;

    /**
     * 메인 화면 이동
     * @param model
     * @return
     */
    @GetMapping("/")
        public String index(Model model, @LoginUser SessionUser user) {
        return "index";
    }

    /**
     * 게시판 조회 화면 진입
     */
    @GetMapping("/findListBoard")
    public String findListBoard(Model model, @LoginUser SessionUser user) {
        model.addAttribute("posts", postsService.findAllDesc());

        // 아래 반복되던 코드 개선
        // SessionUser user = (SessionUser) httpSession.getAttribute("user");
        if(user != null) {
            Map<String, Object> resultMap = new HashMap<>();

            resultMap.put("userName", user.getName());
            resultMap.put("userEmail", user.getEmail());

            model.addAttribute("userData", resultMap);
        }

        return "board/findListBoard";
    }

    /**
     * 게시글 등록 화면 이동
     * @return
     */
    @GetMapping("/posts/save")
    public String postsSave() {
        return "posts-save";
    }

    /**
     * 게시글 수정 화면 이동
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/posts/update/{id}")
    public String postsUpdate(@PathVariable Long id, Model model) {

        PostsResponseDto dto = postsService.findById(id);
        model.addAttribute("post", dto);

        return "posts-update";
    }
}
