package com.reverso.controller;

import com.reverso.dto.response.BlogPostResponse;
import com.reverso.service.interfaces.BlogPostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BlogPostController.class)
@AutoConfigureMockMvc(addFilters = false)
public class BlogPostControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockitoBean
        private BlogPostService blogPostService;

        @Test
        void findLatest_ShouldReturnLatestPosts() throws Exception {

                BlogPostResponse post1 = BlogPostResponse.builder()
                                .id(UUID.randomUUID())
                                .title("Latest Post 1")
                                .slug("latest-post-1")
                                .publishedAt(LocalDateTime.now())
                                .build();

                BlogPostResponse post2 = BlogPostResponse.builder()
                                .id(UUID.randomUUID())
                                .title("Latest Post 2")
                                .slug("latest-post-2")
                                .publishedAt(LocalDateTime.now().minusDays(1))
                                .build();

                given(blogPostService.findLatestPublished(5)).willReturn(List.of(post1, post2));

                mockMvc.perform(get("/api/blogposts/latest?limit=5")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$[0].title").value("Latest Post 1"))
                                .andExpect(jsonPath("$[1].title").value("Latest Post 2"));
        }
}
