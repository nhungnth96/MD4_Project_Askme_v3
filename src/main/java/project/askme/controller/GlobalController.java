package project.askme.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import project.askme.model.Answer;
import project.askme.model.Category;
import project.askme.model.Question;
import project.askme.model.User;
import project.askme.service.AnswerService;
import project.askme.service.CategoryService;
import project.askme.service.QuestionService;
import project.askme.service.UserService;

import javax.servlet.http.HttpSession;
import java.util.List;

@ControllerAdvice
public class GlobalController {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private AnswerService answerService;

    @Autowired
    private UserService userService;

//        @ModelAttribute("currentUser")
//    public User loggedInUser(HttpSession session) {
//        User user = (User) session.getAttribute("userIsLogin");
//
//        if (user != null) {
//            User isLoginUser = new User();
//            isLoginUser.setId(user.getId());
//            isLoginUser.setUsername(user.getUsername());
//            isLoginUser.setFullName(user.getFullName());
//            isLoginUser.setAvatar(user.getAvatar());
//            isLoginUser.setAddress(user.getAddress());
//            isLoginUser.setAbout(user.getAbout());
//
//            return isLoginUser;
//        }
//
//        return null;
//    }

    @ModelAttribute("allQuestions")
    public List<Question> questions() {
        return questionService.findAll();
    }
    @ModelAttribute("allCategories")
    public List<Category> categories() {
        return categoryService.findAll();
    }
    @ModelAttribute("allAnswers")
    public List<Answer> answers() {
        return answerService.findAll();
    }
    @ModelAttribute("allUsers")
    public List<User> users() {
        return userService.findAll();
    }
    @ModelAttribute("newUsers")
    public List<User> newUsers() {
        return userService.findNewUsers();
    }

    @ModelAttribute("recentQ")
    public List<Question> recentQuestionList() {
        return questionService.findRecentQuestionList();
    }
    @ModelAttribute("recentA")
    public List<Question> recentAnswerList() {
        return questionService.findRecentAnswerList();
    }
    @ModelAttribute("mostA")
    public List<Question> mostAnsweredQuestionList() {
        return questionService.findMostAnsweredQuestionList();
    }
    @ModelAttribute("noA")
    public List<Question> noAnswersQuestionList() {
        return questionService.findNoAnswersQuestionList();
    }

}
