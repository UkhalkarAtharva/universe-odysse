package com.universeodyssey.universe_odyssey.service;

import com.universeodyssey.universe_odyssey.model.Blog;
import com.universeodyssey.universe_odyssey.model.BlogComment;
import com.universeodyssey.universe_odyssey.model.BlogLike;
import com.universeodyssey.universe_odyssey.model.User;
import com.universeodyssey.universe_odyssey.repository.BlogCommentRepository;
import com.universeodyssey.universe_odyssey.repository.BlogLikeRepository;
import com.universeodyssey.universe_odyssey.repository.BlogRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class BlogService {

    private final BlogRepository blogRepository;
    private final BlogLikeRepository likeRepository;
    private final BlogCommentRepository commentRepository;
    private final com.universeodyssey.universe_odyssey.repository.BlogViewRepository viewRepository;

    public BlogService(BlogRepository blogRepository, BlogLikeRepository likeRepository,
            BlogCommentRepository commentRepository,
            com.universeodyssey.universe_odyssey.repository.BlogViewRepository viewRepository) {
        this.blogRepository = blogRepository;
        this.likeRepository = likeRepository;
        this.commentRepository = commentRepository;
        this.viewRepository = viewRepository;
    }

    public Page<Blog> getAllPublishedBlogs(Pageable pageable) {
        return blogRepository.findByStatusOrderByCreatedAtDesc(Blog.BlogStatus.PUBLISHED, pageable);
    }

    public Page<Blog> getUserBlogs(User user, Pageable pageable) {
        return blogRepository.findByAuthorOrderByCreatedAtDesc(user, pageable);
    }

    public Optional<Blog> getBlogById(Long id, User user) {
        Optional<Blog> blogOpt = blogRepository.findById(id);

        blogOpt.ifPresent(blog -> {
            if (user != null) {
                boolean hasViewed = viewRepository.existsByBlogAndUser(blog, user);
                if (!hasViewed) {
                    com.universeodyssey.universe_odyssey.model.BlogView view = new com.universeodyssey.universe_odyssey.model.BlogView();
                    view.setBlog(blog);
                    view.setUser(user);
                    viewRepository.save(view);

                    blog.setViewCount(blog.getViewCount() + 1);
                    blogRepository.save(blog);
                }
            }
        });

        return blogOpt;
    }

    // Overload for backward compatibility if needed, or just internal use
    public Optional<Blog> getBlogById(Long id) {
        return getBlogById(id, null);
    }

    @Transactional
    public Blog createBlog(Blog blog, User author) {
        blog.setAuthor(author);
        return blogRepository.save(blog);
    }

    @Transactional
    public Blog updateBlog(Long id, Blog updatedBlog, User user) {
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Blog not found"));

        if (!blog.getAuthor().getId().equals(user.getId())) {
            throw new SecurityException("You are not authorized to edit this blog");
        }

        blog.setTitle(updatedBlog.getTitle());
        blog.setContent(updatedBlog.getContent());
        blog.setSummary(updatedBlog.getSummary());
        blog.setTags(updatedBlog.getTags());
        blog.setStatus(updatedBlog.getStatus());

        return blogRepository.save(blog);
    }

    @Transactional
    public void deleteBlog(Long id, User user) {
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Blog not found"));

        if (!blog.getAuthor().getId().equals(user.getId())) {
            throw new SecurityException("You are not authorized to delete this blog");
        }

        // Delete likes and comments first (cascade logic if not handled by DB)
        // Ideally DB cascade handles this, but manual cleanup ensures safety
        // For now relying on JPA/DB constraints or we can add manual delete here

        blogRepository.delete(blog);
    }

    @Transactional
    public boolean toggleLike(Long blogId, User user) {
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new IllegalArgumentException("Blog not found"));

        Optional<BlogLike> existingLike = likeRepository.findByBlogAndUser(blog, user);
        if (existingLike.isPresent()) {
            likeRepository.delete(existingLike.get());
            return false; // unliked
        } else {
            BlogLike like = new BlogLike();
            like.setBlog(blog);
            like.setUser(user);
            likeRepository.save(like);
            return true; // liked
        }
    }

    public long getLikeCount(Long blogId) {
        Blog blog = blogRepository.findById(blogId).orElseThrow();
        return likeRepository.countByBlog(blog);
    }

    public boolean hasUserLiked(Long blogId, User user) {
        if (user == null)
            return false;
        Blog blog = blogRepository.findById(blogId).orElseThrow();
        return likeRepository.existsByBlogAndUser(blog, user);
    }

    @Transactional
    public BlogComment addComment(Long blogId, String content, User user) {
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new IllegalArgumentException("Blog not found"));

        BlogComment comment = new BlogComment();
        comment.setBlog(blog);
        comment.setUser(user);
        comment.setContent(content);

        return commentRepository.save(comment);
    }

    @Transactional
    public void deleteComment(Long commentId, User user) {
        BlogComment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));

        if (!comment.getUser().getId().equals(user.getId())) {
            throw new SecurityException("You are not authorized to delete this comment");
        }

        commentRepository.delete(comment);
    }

    public Page<BlogComment> getComments(Long blogId, Pageable pageable) {
        Blog blog = blogRepository.findById(blogId).orElseThrow();
        return commentRepository.findByBlogOrderByCreatedAtDesc(blog, pageable);
    }
}
