package project.askme.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import project.askme.dto.FormAnswerEditDto;
import project.askme.dto.FormQuestEditDto;
import project.askme.model.Answer;
import project.askme.model.Category;
import project.askme.model.Question;
import project.askme.model.User;
import project.askme.service.AnswerService;
import project.askme.service.CategoryService;
import project.askme.service.QuestionService;
import project.askme.service.UserService;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/answers")
public class AnswerController {
    @Autowired
    private UserService userService;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private AnswerService answerService;

    public User getCurrentUserFromSession(HttpSession session) {
        return (User) session.getAttribute("userIsLogin");
    }
    @Value("${uploadImagePath}")
    private String uploadImagePath;
    @GetMapping("/edit/{answerId}")
    public ModelAndView editAnswer(@PathVariable Long answerId, Model model, HttpSession session) {
        Answer answer = answerService.findById(answerId);
        model.addAttribute("answer",answer);
        FormAnswerEditDto formAnswerEditDto = new FormAnswerEditDto();
        formAnswerEditDto.setAnswerId(answer.getId());
        System.out.println(formAnswerEditDto.getAnswerId());
        formAnswerEditDto.setBody(answer.getBody());
        return new ModelAndView("question/editAnswer", "editAnswer", formAnswerEditDto);
    }

    @PostMapping("/update")
    public String updateAnswer(@ModelAttribute("editAnswer") FormAnswerEditDto formAnswerEditDto, HttpSession session) {
        Answer editAnswer = answerService.findById(formAnswerEditDto.getAnswerId());
        MultipartFile imageFile = formAnswerEditDto.getImage();
        String image = imageFile.getOriginalFilename();
        try {
            FileCopyUtils.copy(imageFile.getBytes(), new File(uploadImagePath + image));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
                editAnswer.setImage(image);
                editAnswer.setBody(formAnswerEditDto.getBody());
                answerService.save(editAnswer);
                return "redirect:/questions/detail/" + editAnswer.getQuestionId();

        }
    @GetMapping("/delete/{answerId}")
    public String deleteAnswer(@PathVariable Long answerId) {
        Answer answer = answerService.findById(answerId);
        answerService.delete(answerId);

                return "redirect:/questions/detail/" + answer.getQuestionId();
            }
        }



