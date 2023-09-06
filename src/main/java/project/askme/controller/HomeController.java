package project.askme.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import project.askme.dto.FormLoginDto;
import project.askme.dto.FormRegisterDto;
import project.askme.model.User;
import project.askme.service.CategoryService;
import project.askme.service.QuestionService;
import project.askme.service.UserService;

import javax.servlet.http.HttpSession;

@Controller
public class HomeController {
    @Autowired
    private UserService userService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private QuestionService questionService;


    @RequestMapping("/")
    public String goToHome(Model model) {
//        List<Question> recentQuestionList = questionService.findRecentQuestionList();
//        List<Question> recentAnswerList = questionService.findRecentAnswerList();
//        List<Question> mostAnsweredQuestionList = questionService.findMostAnsweredQuestionList();
//        List<Question> noAnswersQuestionList = questionService.findNoAnswersQuestionList();
//        model.addAttribute("recentQ",recentQuestionList);
//        model.addAttribute("recentA",recentAnswerList);
//        model.addAttribute("mostA",mostAnsweredQuestionList);
//        model.addAttribute("noA",noAnswersQuestionList);

        return "index";
    }

    // ============================== GET REGISTER ==============================
    @GetMapping("/register")
    public ModelAndView register() {

        return new ModelAndView("register", "registerForm", new FormRegisterDto());
    }

    // ============================== POST REGISTER ==============================
    @PostMapping("/register")
    public String register(@ModelAttribute("registerForm") FormRegisterDto formRegisterDto, Model model) {
        if(formRegisterDto.getFullName().trim().equals("")){
            model.addAttribute("error", "The full name field is empty");
        }
        else if(formRegisterDto.getEmail().trim().equals("")){
            model.addAttribute("error", "The email field is empty");
        } else if (!userService.validateEmail(formRegisterDto.getEmail())) {
            model.addAttribute("error", "The email is invalid format");
        } else if(userService.checkExistEmail(formRegisterDto.getEmail())){
            model.addAttribute("error", "The email is existed");
        }
        else if(formRegisterDto.getUsername().trim().equals("")){
            model.addAttribute("error", "The username field is empty");
        }else if(!userService.validateUsername(formRegisterDto.getUsername())){
            model.addAttribute("error", "The username must be at least 6 characters.");
        }else if(userService.checkExistUsername(formRegisterDto.getUsername())){
            model.addAttribute("error", "The username is existed");
        }
        else if(formRegisterDto.getPassword().trim().equals("")){
            model.addAttribute("error", "The password field is empty.");
        }else if(!userService.validatePassword(formRegisterDto.getPassword())){
            model.addAttribute("error", "The password must be at least 6 characters, at least 1 uppercase letter, 1 lowercase letter and 1 symbol character.");
        } else {
            userService.register(formRegisterDto);
            model.addAttribute("success", "Registration successful! Please login.");
            return "redirect:login";
        }
        return "register";

    }

    // ============================== GET LOGIN ==============================
    @GetMapping("/login")
    public ModelAndView login() {

        return new ModelAndView("login", "loginForm", new FormLoginDto());
    }

    // ============================== POST LOGIN ==============================
    @PostMapping("/login")
    public String login(HttpSession session, @ModelAttribute("loginForm") FormLoginDto formLoginDto, Model model) {
        if(formLoginDto.getUsername().trim().equals("")){
            model.addAttribute("error", "The username field is empty");
        }else if(!userService.validateUsername(formLoginDto.getUsername())){
            model.addAttribute("error", "The username must be at least 6 characters.");
        }else if(formLoginDto.getPassword().trim().equals("")){
            model.addAttribute("error", "The password field is empty.");
        }else if(!userService.validatePassword(formLoginDto.getPassword())){
            model.addAttribute("error", "The password must be at least 6 characters, at least 1 uppercase letter, 1 lowercase letter and 1 symbol character.");
        } else {
            User user = userService.login(formLoginDto);
            if (user == null) {
                model.addAttribute("error", "Username or password is incorrect!!!");
            } else if (!user.isStatus()) {
                model.addAttribute("error", "Your account has been locked!!!");
            } else {
                model.addAttribute("success", "Login successful!");
                model.addAttribute("user",user);
                session.setAttribute("userIsLogin", user);
                System.out.println(user);
                if (user.getRoleId() == 1) {
                    // admin
                    return "admin/index";
                } else {
                    // user
                    return "index";
                }
            }
        }
        return "login";

    }

    // ============================== GET LOGOUT ==============================
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("userIsLogin");
        return "redirect:/";
    }


}