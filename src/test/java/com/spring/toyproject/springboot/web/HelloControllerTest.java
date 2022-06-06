package com.spring.toyproject.springboot.web;

import com.spring.toyproject.springboot.web.dto.HelloResponseDto;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = HelloController.class)
public class HelloControllerTest extends TestCase {

    /**
     * 웹 API를 테스트할 때 사용한다.
     * 스프링 MVC 테스트의 시작점이다.
     * 이 클래스를 통해 HTTP GET, POST 등에 대한 API를 테스트할 수 있다.
     */
    @Autowired
    private MockMvc mvc;

    @Test
    public void hello가_리턴된다() throws Exception{
        String hello = "hello!";

        // 아래와 같이 . 으로 연결되는 것을 체이닝이라고 한다.

        // HTTP header의 Statue 를 검증한다.
        // 흔히 알고 있는 200, 404, 500 등의 상태를 검증
        // 여기선 OK 즉, 200인지 아닌지를 검증한다.
        mvc.perform(get("/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string(hello)); // Controller에서 리턴하는 값을 검증한다.
    }

    @Test
    public void helloDto가_리턴된다() throws Exception{
        // 외부에서 넘긴 파라미터를 잘 받아서
        // 원하는 값을 반환하는가?

        //given
        String name = "mina";
        int amount = 10000;

        // when, then
        mvc.perform(get("/hello/dto")
                        .param("name", name)
                        .param("amount", String.valueOf(amount)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(name)))
                .andExpect(jsonPath("$.amount", is(amount)));
    }
}