package project.askme.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project.askme.model.Category;
import project.askme.model.User;
import project.askme.service.AnswerService;
import project.askme.service.CategoryService;
import project.askme.service.QuestionService;
import project.askme.service.UserService;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserService userService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private AnswerService answerService;

    @ModelAttribute("questionCountToday")
    public int questionCountToday() {
        return questionService.getQuestionCountForToday();
    }
    @ModelAttribute("questionCountThisWeek")
    public int questionCountThisWeek() {
        return questionService.getQuestionCountForThisWeek();
    }
    @ModelAttribute("answerCountToday")
    public int answerCountToday() {
        return questionService.getAnswerCountForToday();
    }
    @ModelAttribute("answerCountThisWeek")
    public int answerCountThisWeek() {
        return questionService.getAnswerCountForThisWeek();
    }
    @ModelAttribute("userCountToday")
    public int userCountToday() {
        return questionService.getUserCountForToday();
    }
    @ModelAttribute("userCountThisWeek")
    public int userCountThisWeek() {
        return questionService.getUserCountForThisWeek();
    }
    @GetMapping()
    public String goDashboard() {

        return "admin/index";
    }
    // ============================== GET USER LIST ==============================
    @GetMapping("/users")
    public String showAllUser(Model model) {
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        return "admin/user";
    }
    // ============================== GET CAT LIST ==============================
    @GetMapping("/categories")
    public String showAllCat(Model model) {
        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        return "admin/category";
    }
    @PostMapping("/addCat")
    public String addCategory(@RequestParam("name") String name) {
        Category category = new Category();
        category.setName(name);
        categoryService.save(category);

        return "redirect:/categories";
    }
    @PostMapping("/editCat")
    public String editCategory(@RequestParam ("id")  Long id, @RequestParam ("name") String name) {
        Category category = categoryService.findById(id);
        category.setName(name);
        categoryService.save(category);

        return "redirect:/categories";
    }
    @GetMapping("/delete/{categoryId}")
    public String deleteAnswer(@PathVariable Long categoryId) {

        categoryService.delete(categoryId);

        return "redirect:/questions";
    }

}
