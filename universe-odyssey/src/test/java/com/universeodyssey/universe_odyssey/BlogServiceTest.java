package com.universeodyssey.universe_odyssey;

import com.universeodyssey.universe_odyssey.model.Blog;
import com.universeodyssey.universe_odyssey.model.BlogComment;
import com.universeodyssey.universe_odyssey.model.BlogLike;
import com.universeodyssey.universe_odyssey.model.BlogView;
import com.universeodyssey.universe_odyssey.model.User;
import com.universeodyssey.universe_odyssey.repository.BlogCommentRepository;
import com.universeodyssey.universe_odyssey.repository.BlogLikeRepository;
import com.universeodyssey.universe_odyssey.repository.BlogRepository;
import com.universeodyssey.universe_odyssey.repository.BlogViewRepository;
import com.universeodyssey.universe_odyssey.service.BlogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BlogServiceTest {

    @Mock
    private BlogRepository blogRepository;

    @Mock
    private BlogLikeRepository likeRepository;

    @Mock
    private BlogCommentRepository commentRepository;

    @Mock
    private BlogViewRepository viewRepository;

    @InjectMocks
    private BlogService blogService;

    private User user;
    private Blog blog;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");

        blog = new Blog();
        blog.setId(1L);
        blog.setTitle("Test Blog");
        blog.setViewCount(0L);
        blog.setAuthor(user);
    }

    @Test
    void testGetBlogById_IncrementsViewCount_WhenNotViewed() {
        when(blogRepository.findById(1L)).thenReturn(Optional.of(blog));
        when(viewRepository.existsByBlogAndUser(blog, user)).thenReturn(false);

        Optional<Blog> result = blogService.getBlogById(1L, user);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getViewCount());
        verify(viewRepository, times(1)).save(any(BlogView.class));
        verify(blogRepository, times(1)).save(blog);
    }

    @Test
    void testGetBlogById_DoesNotIncrementViewCount_WhenAlreadyViewed() {
        when(blogRepository.findById(1L)).thenReturn(Optional.of(blog));
        when(viewRepository.existsByBlogAndUser(blog, user)).thenReturn(true);

        Optional<Blog> result = blogService.getBlogById(1L, user);

        assertTrue(result.isPresent());
        assertEquals(0, result.get().getViewCount());
        verify(viewRepository, never()).save(any(BlogView.class));
        verify(blogRepository, never()).save(blog);
    }

    @Test
    void testToggleLike_Likes_WhenNotLiked() {
        when(blogRepository.findById(1L)).thenReturn(Optional.of(blog));
        when(likeRepository.findByBlogAndUser(blog, user)).thenReturn(Optional.empty());

        boolean liked = blogService.toggleLike(1L, user);

        assertTrue(liked);
        verify(likeRepository, times(1)).save(any(BlogLike.class));
    }

    @Test
    void testToggleLike_Unlikes_WhenLiked() {
        when(blogRepository.findById(1L)).thenReturn(Optional.of(blog));
        BlogLike existingLike = new BlogLike();
        when(likeRepository.findByBlogAndUser(blog, user)).thenReturn(Optional.of(existingLike));

        boolean liked = blogService.toggleLike(1L, user);

        assertFalse(liked);
        verify(likeRepository, times(1)).delete(existingLike);
    }

    @Test
    void testAddComment() {
        when(blogRepository.findById(1L)).thenReturn(Optional.of(blog));
        when(commentRepository.save(any(BlogComment.class))).thenAnswer(i -> i.getArguments()[0]);

        BlogComment comment = blogService.addComment(1L, "Nice post!", user);

        assertNotNull(comment);
        assertEquals("Nice post!", comment.getContent());
        assertEquals(user, comment.getUser());
        assertEquals(blog, comment.getBlog());
    }
}
