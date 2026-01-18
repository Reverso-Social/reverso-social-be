package com.reverso.controller;

import com.reverso.dto.response.BlogPostResponse;
import com.reverso.service.interfaces.BlogPostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BlogPostController.class)
@AutoConfigureMockMvc(addFilters = false)
class BlogPostControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockitoBean
        private BlogPostService blogPostService;

        @Test
        void updateBlogPost_shouldReturn200() throws Exception {
                UUID id = UUID.randomUUID();

                BlogPostResponse response = BlogPostResponse.builder()
                                .id(id)
                                .title("Updated title")
                                .build();

                when(blogPostService.update(eq(id), any(), any()))
                                .thenReturn(response);

                MockMultipartFile data = new MockMultipartFile(
                                "data",
                                "",
                                MediaType.APPLICATION_JSON_VALUE,
                                """
                                                {
                                                  "title": "Updated title"
                                                }
                                                """.getBytes());

                mockMvc.perform(
                                multipart("/api/blogposts/{id}", id)
                                                .file(data)
                                                .with(req -> {
                                                        req.setMethod("PUT");
                                                        return req;
                                                }))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.title")
                                                .value("Updated title"));
        }

        @Test
        void findById_shouldReturnPost() throws Exception {
                UUID id = UUID.randomUUID();

                BlogPostResponse response = BlogPostResponse.builder()
                                .id(id)
                                .title("Post")
                                .build();

                when(blogPostService.findById(id))
                                .thenReturn(response);

                mockMvc.perform(get("/api/blogposts/{id}", id))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.title")
                                                .value("Post"));
        }

        @Test
        void findBySlug_shouldReturnPost() throws Exception {
                BlogPostResponse response = BlogPostResponse.builder()
                                .slug("my-post")
                                .title("Post")
                                .build();

                when(blogPostService.findBySlug("my-post"))
                                .thenReturn(response);

                mockMvc.perform(get("/api/blogposts/slug/{slug}", "my-post"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.slug")
                                                .value("my-post"));
        }

        @Test
        void findAll_shouldReturnList() throws Exception {
                BlogPostResponse response = BlogPostResponse.builder()
                                .title("Post")
                                .build();

                when(blogPostService.findAll(null, null))
                                .thenReturn(List.of(response));

                mockMvc.perform(get("/api/blogposts"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$[0].title")
                                                .value("Post"));
        }

        @Test
        void findLatest_shouldReturnList() throws Exception {
                BlogPostResponse response = BlogPostResponse.builder()
                                .title("Latest post")
                                .build();

                when(blogPostService.findLatestPublished(5))
                                .thenReturn(List.of(response));

                mockMvc.perform(get("/api/blogposts/latest")
                                .param("limit", "5"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$[0].title")
                                                .value("Latest post"));
        }

        @Test
        void delete_shouldReturn204() throws Exception {
                UUID id = UUID.randomUUID();

                doNothing().when(blogPostService).delete(id);

                mockMvc.perform(delete("/api/blogposts/{id}", id))
                                .andExpect(status().isNoContent());
        }
}