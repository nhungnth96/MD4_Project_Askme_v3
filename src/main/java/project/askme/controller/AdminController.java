package project.askme.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @GetMapping()
    public String goDashboard(Model model) {
        model.addAttribute("questionCountToday", questionService.getQuestionCountForToday());
        model.addAttribute("questionCountThisWeek", questionService.getQuestionCountForThisWeek());
        model.addAttribute("answerCountToday", questionService.getAnswerCountForToday());
        model.addAttribute("answerCountThisWeek", questionService.getAnswerCountForThisWeek());
        model.addAttribute("userCountToday", questionService.getUserCountForToday());
        model.addAttribute("userCountThisWeek", questionService.getUserCountForThisWeek());
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
    // ============================== ADD CAT ==============================
    @PostMapping("/addCat")
    public String addCat(@RequestParam("name") String name) {
        Category category = new Category();
        category.setName(name);
        categoryService.save(category);

        return "redirect:/categories";
    }
    @GetMapping("/getCategory/{categoryId}")
    @ResponseBody
    public ResponseEntity<Category> getCategory(@PathVariable Long categoryId) {
        Category category = categoryService.findById(categoryId);
        if (category == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(category);
    }
//    @GetMapping("/editCat/{catId}")
//    public String getEditCategoryPage(@PathVariable Long catId, Model model) {
//        Category category = categoryService.findById(catId);
//        model.addAttribute("catEdit", category);
//        return "admin/category"; // Chuyển đến view để hiển thị modal edit
//    }
//    @PostMapping("/editCat")
//    public String editCat(@ModelAttribute("catEdit") Category catEdit) {
//        Category existingCategory = categoryService.findById(catEdit.getId());
//        existingCategory.setName(catEdit.getName());
//        categoryService.save(existingCategory);
//        return "redirect:/admin/category"; // Chuyển hướng sau khi cập nhật thành công
//    }

    @GetMapping("/deleteCat/{categoryId}")
    public String deleteCat(@PathVariable Long categoryId) {

        categoryService.delete(categoryId);

        return "redirect:/categories";
    }
    // ============================== LOCK USER ==============================
    @GetMapping("/lockUser/{userId}")
    public String lockUser(@PathVariable("userId") Long userId) {
        userService.changeUserStatus(userId);
        return "redirect:/admin/users";
    }
}
