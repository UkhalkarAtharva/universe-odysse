package com.universeodyssey.universe_odyssey.controller;

import com.universeodyssey.universe_odyssey.model.Blog;
import com.universeodyssey.universe_odyssey.model.BlogComment;
import com.universeodyssey.universe_odyssey.model.User;
import com.universeodyssey.universe_odyssey.repository.UserRepository;
import com.universeodyssey.universe_odyssey.service.BlogService;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/blogs")
public class BlogController {

    private final BlogService blogService;
    private final UserRepository userRepository;

    public BlogController(BlogService blogService, UserRepository userRepository) {
        this.blogService = blogService;
        this.userRepository = userRepository;
    }

    private User getCurrentUser(HttpSession session) {
        if (session == null)
            return null;
        Object userIdObj = session.getAttribute("userId");
        if (userIdObj == null)
            return null;
        Long userId = (userIdObj instanceof Long) ? (Long) userIdObj : Long.parseLong(userIdObj.toString());
        return userRepository.findById(userId).orElse(null);
    }

    @GetMapping
    public ResponseEntity<?> getAllBlogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Blog> blogs = blogService.getAllPublishedBlogs(pageable);
        return ResponseEntity.ok(blogs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBlogById(@PathVariable Long id, HttpSession session) {
        User currentUser = getCurrentUser(session);
        Optional<Blog> blogOpt = blogService.getBlogById(id, currentUser);
        if (blogOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Blog blog = blogOpt.get();

        Map<String, Object> response = new HashMap<>();
        response.put("blog", blog);
        response.put("likes", blogService.getLikeCount(id));

        // User currentUser = getCurrentUser(session); // Already defined above
        if (currentUser != null) {
            response.put("hasLiked", blogService.hasUserLiked(id, currentUser));
            response.put("isAuthor", blog.getAuthor().getId().equals(currentUser.getId()));
        } else {
            response.put("hasLiked", false);
            response.put("isAuthor", false);
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<?> createBlog(@RequestBody Blog blog, HttpSession session) {
        User user = getCurrentUser(session);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Please log in to create a blog");
        }

        try {
            Blog created = blogService.createBlog(blog, user);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to create blog: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBlog(@PathVariable Long id, @RequestBody Blog blog, HttpSession session) {
        User user = getCurrentUser(session);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Please log in");
        }

        try {
            Blog updated = blogService.updateBlog(id, blog, user);
            return ResponseEntity.ok(updated);
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to update blog: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBlog(@PathVariable Long id, HttpSession session) {
        User user = getCurrentUser(session);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Please log in");
        }

        try {
            blogService.deleteBlog(id, user);
            return ResponseEntity.ok("Blog deleted successfully");
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to delete blog: " + e.getMessage());
        }
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<?> toggleLike(@PathVariable Long id, HttpSession session) {
        User user = getCurrentUser(session);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Please log in to like");
        }

        try {
            boolean liked = blogService.toggleLike(id, user);
            Map<String, Object> response = new HashMap<>();
            response.put("liked", liked);
            response.put("count", blogService.getLikeCount(id));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<?> getComments(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(blogService.getComments(id, pageable));
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<?> addComment(@PathVariable Long id, @RequestBody Map<String, String> payload,
            HttpSession session) {
        User user = getCurrentUser(session);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Please log in to comment");
        }

        String content = payload.get("content");
        if (content == null || content.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Comment content cannot be empty");
        }

        try {
            BlogComment comment = blogService.addComment(id, content, user);
            return ResponseEntity.status(HttpStatus.CREATED).body(comment);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to add comment: " + e.getMessage());
        }
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId, HttpSession session) {
        User user = getCurrentUser(session);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Please log in");
        }

        try {
            blogService.deleteComment(commentId, user);
            return ResponseEntity.ok("Comment deleted");
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to delete comment: " + e.getMessage());
        }
    }
}
