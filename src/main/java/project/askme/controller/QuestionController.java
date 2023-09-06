package project.askme.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import project.askme.dto.FormAnswerDto;
import project.askme.dto.FormQuestDto;
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
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/questions")
public class QuestionController {
    @Autowired
    private UserService userService;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private AnswerService answerService;
    @Autowired
    private CategoryService categoryService;
    public User getCurrentUserFromSession(HttpSession session) {
        return (User) session.getAttribute("userIsLogin");
    }
    // ============================== GET QUESTION ==============================
    @GetMapping()
    public String showAllQuestion(Model model) {
        List<Question> questions = questionService.findAll();
        model.addAttribute("questions", questions);
        return "question/questions";
    }
    @GetMapping("/category/{categoryName}")
    public String showQuestionByCat(@PathVariable("categoryName") String categoryName, Model model) {
        List<Question> questions = new ArrayList<>();
            for (Question question : questionService.findAll()) {
                if(question!=null && question.getCategory().getName().equals(categoryName)) {
                    questions.add(question);
                }
            }
            model.addAttribute("questions", questions);
            return "question/questions";
    }

    @GetMapping("/search")
    public String showQuestionBySearch(@RequestParam("keyword") String keyword, Model model) {
        List<Question> questions = new ArrayList<>();
        for (Question question : questionService.findAll()) {
            if(question!=null
                    && (question.getTitle().toLowerCase().contains(keyword)
                    || question.getBody().toLowerCase().contains(keyword))) {
                questions.add(question);
            }
        }
        model.addAttribute("questions", questions);
        return "question/questions";
    }
    // ============================== GET ASK ==============================
    @GetMapping("/ask")
    public ModelAndView showAskForm(Model model) {
        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        return new ModelAndView("question/askForm", "askForm", new FormQuestDto());
    }
    // ============================== POST QUESTION ==============================
    @Value("${uploadImagePath}")
    private String uploadImagePath;
    @PostMapping("/ask")
    public String actionAsk(@SessionAttribute("userIsLogin") User user,@ModelAttribute("askForm") FormQuestDto formQuestDto) {
        questionService.formToModel(formQuestDto,user);
        return "redirect:/";
    }
    // ============================== GET DETAIL ==============================
    @GetMapping("/detail/{questionId}")
    public String questionDetail(@PathVariable("questionId") Long questionId, Model model,HttpSession session) {
        questionService.incrementViewCount(questionId);
        Question question = questionService.findById(questionId);
        if(question!=null) {
            User user = userService.findById(question.getUserId());
            User userIsLogin = getCurrentUserFromSession(session);
            List<Answer> answers = answerService.findAllByQuestId(questionId);
            model.addAttribute("answers", answers);
            model.addAttribute("question", question);
            model.addAttribute("user", user);
            model.addAttribute("userIsLogin", userIsLogin);
            model.addAttribute("answerForm", new FormAnswerDto());
            session.setAttribute("currentQuestId",questionId);
            return "question/detail";
        }
        return "redirect:/404";
    }
    @GetMapping("/edit/{questionId}")
    public ModelAndView editQuestion(@PathVariable("questionId")  Long questionId, Model model) {
        Question question = questionService.findById(questionId);
        model.addAttribute("question",question);
            List<Category> categories = categoryService.findAll();
            model.addAttribute("categories", categories);
            FormQuestEditDto formQuestEditDto = new FormQuestEditDto();
            formQuestEditDto.setQuestId(question.getId());
            formQuestEditDto.setTitle(question.getTitle());
            formQuestEditDto.setCategoryId(question.getCategory().getId());
            formQuestEditDto.setBody(question.getBody());
            return new ModelAndView("question/editQuestion", "editQuest", formQuestEditDto);

    }

    @PostMapping("/update")
    public String updateQuestion(@ModelAttribute("editQuest") FormQuestEditDto formQuestEditDto, HttpSession session) {
        Question question = questionService.findById(formQuestEditDto.getQuestId());
            MultipartFile imageFile = formQuestEditDto.getImage();
            String image = imageFile.getOriginalFilename();
            try {
                FileCopyUtils.copy(imageFile.getBytes(), new File(uploadImagePath + image));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            question.setTitle(formQuestEditDto.getTitle());
            question.setCategory(categoryService.findById(formQuestEditDto.getCategoryId()));
            question.setImage(image);
            question.setBody(formQuestEditDto.getBody());
            questionService.save(question);

        return "redirect:/questions/detail/" + formQuestEditDto.getQuestId();
    }
    @GetMapping("/delete/{questionId}")
    public String deleteAnswer(@PathVariable Long questionId) {

        questionService.delete(questionId);

        return "redirect:/questions";
    }

    @PostMapping("/addAnswer")
    public String addAnswer(@ModelAttribute("answerForm") FormAnswerDto formAnswerDto, HttpSession session,Model model) {
        Long questionId = (Long) session.getAttribute("currentQuestId");
        System.out.println(questionId);
        User user = getCurrentUserFromSession(session);
        answerService.formToModel(formAnswerDto, questionId,user);
        return "redirect:/questions/detail/" + questionId;
    }


    @GetMapping("/select-best-answer/{answerId}-{questionId}")
    public String selectBestAnswer(@PathVariable Long answerId,@PathVariable Long questionId) {
        answerService.selectBestAnswer(answerId, questionId);
        return "redirect:/questions/detail/" + questionId;
    }

}
