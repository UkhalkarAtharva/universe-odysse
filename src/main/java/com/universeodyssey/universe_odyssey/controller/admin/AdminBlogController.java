package com.universeodyssey.universe_odyssey.controller.admin;

import com.universeodyssey.universe_odyssey.model.Blog;
import com.universeodyssey.universe_odyssey.repository.BlogRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/blogs")
public class AdminBlogController {

    private final BlogRepository blogRepository;

    public AdminBlogController(BlogRepository blogRepository) {
        this.blogRepository = blogRepository;
    }

    @GetMapping
    public String listBlogs(Model model) {
        model.addAttribute("blogs", blogRepository.findAll());
        model.addAttribute("activePage", "blogs");
        return "admin/blog-list";
    }

    @GetMapping("/delete/{id}")
    public String deleteBlog(@PathVariable Long id) {
        blogRepository.deleteById(id);
        return "redirect:/admin/blogs";
    }
}
