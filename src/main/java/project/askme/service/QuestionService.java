package project.askme.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import project.askme.dto.FormQuestDto;
import project.askme.model.Question;
import project.askme.model.User;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService implements IGenericService<Question,Long> {
    @Autowired
    DataSource dataSource;
    @Autowired
    CategoryService categoryService;
    @Autowired
    UserService userService;
    @Override
    public List<Question> findAll() {
        List<Question> questions = new ArrayList<>();
        Connection con = null;
        try {
            con = dataSource.getConnection();
            CallableStatement callSt = con.prepareCall("{call proc_getQuestList()}");

            ResultSet rs = callSt.executeQuery();
            while (rs.next()) {
                Question question = new Question();
                question.setId(rs.getLong("id"));
                question.setCategory(categoryService.findById(rs.getLong("category_id")));
                question.setUserId(rs.getLong("user_id"));
                question.setUserFullName(rs.getString("user_full_name"));
                question.setUserAvatar(rs.getString("user_avatar"));
                question.setCreatedDate(rs.getDate("created_date"));
                question.setEditedDate(rs.getDate("edited_date"));
                question.setClosedDate(rs.getDate("closed_date"));
                question.setTitle(rs.getString("title"));
                question.setBody(rs.getString("body"));
                question.setImage(rs.getString("image"));
                question.setScore(rs.getInt("score"));
                question.setViewCount(rs.getInt("view_count"));
                question.setAnswerCount(rs.getInt("answer_count"));
                question.setVoteCount(rs.getInt("vote_count"));
                question.setStatus(rs.getBoolean("status"));
                questions.add(question);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

        }
        return questions;
    }

    @Override
    public Question findById(Long id) {

        Connection con = null;
        Question question = null;
        try {
            con = dataSource.getConnection();
            CallableStatement callSt = con.prepareCall("{call proc_findQuestById(?)}");
            callSt.setLong(1,id);
            ResultSet rs = callSt.executeQuery();
            while (rs.next()) {
                question = new Question();
                question.setId(rs.getLong("id"));
                question.setCategory(categoryService.findById(rs.getLong("category_id")));
                question.setUserId(rs.getLong("user_id"));
                question.setUserFullName(rs.getString("user_full_name"));
                question.setUserAvatar(rs.getString("user_avatar"));
                question.setCreatedDate(rs.getDate("created_date"));
                question.setEditedDate(rs.getDate("edited_date"));
                question.setClosedDate(rs.getDate("closed_date"));
                question.setTitle(rs.getString("title"));
                question.setBody(rs.getString("body"));
                question.setImage(rs.getString("image"));
                question.setScore(rs.getInt("score"));
                question.setViewCount(rs.getInt("view_count"));
                question.setAnswerCount(rs.getInt("answer_count"));
                question.setVoteCount(rs.getInt("vote_count"));
                question.setStatus(rs.getBoolean("status"));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (con!=null){
                try {
                    con.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return question;
    }

    @Value("${uploadImagePath}")
    private String uploadImagePath;
    public void formToModel(FormQuestDto formQuestDto, User user) {
        MultipartFile imageFile = formQuestDto.getImage();
        String image = imageFile.getOriginalFilename();
        try {
            FileCopyUtils.copy(imageFile.getBytes(), new File(uploadImagePath + image));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        Question question = new Question();
        question.setUserId(user.getId());
        question.setUserFullName(user.getFullName());
        question.setUserAvatar(user.getAvatar());
        question.setTitle(formQuestDto.getTitle());
        question.setCategory(categoryService.findById(formQuestDto.getCategoryId()));
        question.setImage(image);
        question.setBody(formQuestDto.getBody());
        save(question);

    }
    @Override
    public void save(Question question) {
        Connection con = null;
        try {
            con = dataSource.getConnection();
            if (question.getId() == null) {
                CallableStatement callSt = con.prepareCall("{call proc_insertQuest(?,?,?,?,?,?,?,?)}");
                callSt.setLong(1,question.getCategory().getId());
                callSt.setLong(2,question.getUserId());
                callSt.setString(3,question.getUserFullName());
                callSt.setString(4,question.getUserAvatar());
                callSt.setString(5,question.getTitle());
                callSt.setString(6,question.getBody());
                callSt.setString(7,question.getImage());
                callSt.registerOutParameter(8, Types.INTEGER);
                callSt.execute();
                Long newQuestId = callSt.getLong(8);
//                CallableStatement callSt1 = con.prepareCall("{call proc_insertImage(?,?)}");
//                callSt1.setString(1, question.getImage());
//                callSt1.setLong(2, question.getUserId());
//                callSt1.executeUpdate();
            } else {
                CallableStatement callSt = con.prepareCall("{call proc_updateQuest(?,?,?,?,?)}");
                callSt.setLong(1,question.getCategory().getId());
                callSt.setString(2,question.getTitle());
                callSt.setString(3,question.getBody());
                callSt.setString(4,question.getImage());
                callSt.setLong(5,question.getId());
                callSt.executeUpdate();
//                CallableStatement callSt1 = con.prepareCall("{call proc_changeImage(?,?)}");
//                callSt1.setString(1, question.getImage());
//                callSt1.setLong(2, question.getUserId());
//                callSt1.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Override
    public void delete(Long questionId) {
        Connection con = null;
        try {
            con = dataSource.getConnection();
            CallableStatement callSt = con.prepareCall("{call proc_deleteQuestion(?)}");
            callSt.setLong(1,questionId);
            callSt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (con!=null){
                try {
                    con.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public List<Question> findRecentQuestionList(){
        List<Question> questions = new ArrayList<>();
        Connection con = null;
        try {
            con = dataSource.getConnection();
            CallableStatement callSt = con.prepareCall("{call proc_getRecentQuestion()}");

            ResultSet rs = callSt.executeQuery();
            while (rs.next()) {
                Question question = new Question();
                question.setId(rs.getLong("id"));
                question.setCategory(categoryService.findById(rs.getLong("category_id")));
                question.setUserId(rs.getLong("user_id"));
                question.setUserFullName(rs.getString("user_full_name"));
                question.setUserAvatar(rs.getString("user_avatar"));
                question.setCreatedDate(rs.getDate("created_date"));
                question.setEditedDate(rs.getDate("edited_date"));
                question.setClosedDate(rs.getDate("closed_date"));
                question.setTitle(rs.getString("title"));
                question.setBody(rs.getString("body"));
                question.setImage(rs.getString("image"));
                question.setScore(rs.getInt("score"));
                question.setViewCount(rs.getInt("view_count"));
                question.setAnswerCount(rs.getInt("answer_count"));
                question.setVoteCount(rs.getInt("vote_count"));
                question.setStatus(rs.getBoolean("status"));
                questions.add(question);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

        }
        return questions;
    }
    public List<Question> findRecentAnswerList(){
        List<Question> questions = new ArrayList<>();
        Connection con = null;
        try {
            con = dataSource.getConnection();
            CallableStatement callSt = con.prepareCall("{call proc_getRecentAnswer()}");

            ResultSet rs = callSt.executeQuery();
            while (rs.next()) {
                Question question = new Question();
                question.setId(rs.getLong("id"));
                question.setCategory(categoryService.findById(rs.getLong("category_id")));
                question.setUserId(rs.getLong("user_id"));
                question.setUserFullName(rs.getString("user_full_name"));
                question.setUserAvatar(rs.getString("user_avatar"));
                question.setCreatedDate(rs.getDate("created_date"));
                question.setEditedDate(rs.getDate("edited_date"));
                question.setClosedDate(rs.getDate("closed_date"));
                question.setTitle(rs.getString("title"));
                question.setBody(rs.getString("body"));
                question.setImage(rs.getString("image"));
                question.setScore(rs.getInt("score"));
                question.setViewCount(rs.getInt("view_count"));
                question.setAnswerCount(rs.getInt("answer_count"));
                question.setVoteCount(rs.getInt("vote_count"));
                question.setStatus(rs.getBoolean("status"));
                questions.add(question);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

        }
        return questions;
    }
    public List<Question> findMostAnsweredQuestionList(){
        List<Question> questions = new ArrayList<>();
        Connection con = null;
        try {
            con = dataSource.getConnection();
            CallableStatement callSt = con.prepareCall("{call proc_getMostAnsweredQuestion()}");

            ResultSet rs = callSt.executeQuery();
            while (rs.next()) {
                Question question = new Question();
                question.setId(rs.getLong("id"));
                question.setCategory(categoryService.findById(rs.getLong("category_id")));
                question.setUserId(rs.getLong("user_id"));
                question.setUserFullName(rs.getString("user_full_name"));
                question.setUserAvatar(rs.getString("user_avatar"));
                question.setCreatedDate(rs.getDate("created_date"));
                question.setEditedDate(rs.getDate("edited_date"));
                question.setClosedDate(rs.getDate("closed_date"));
                question.setTitle(rs.getString("title"));
                question.setBody(rs.getString("body"));
                question.setImage(rs.getString("image"));
                question.setScore(rs.getInt("score"));
                question.setViewCount(rs.getInt("view_count"));
                question.setAnswerCount(rs.getInt("answer_count"));
                question.setVoteCount(rs.getInt("vote_count"));
                question.setStatus(rs.getBoolean("status"));
                questions.add(question);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

        }
        return questions;
    }
    public List<Question> findNoAnswersQuestionList(){
        List<Question> questions = new ArrayList<>();
        Connection con = null;
        try {
            con = dataSource.getConnection();
            CallableStatement callSt = con.prepareCall("{call proc_getNoAnswersQuestion()}");

            ResultSet rs = callSt.executeQuery();
            while (rs.next()) {
                Question question = new Question();
                question.setId(rs.getLong("id"));
                question.setCategory(categoryService.findById(rs.getLong("category_id")));
                question.setUserId(rs.getLong("user_id"));
                question.setUserFullName(rs.getString("user_full_name"));
                question.setUserAvatar(rs.getString("user_avatar"));
                question.setCreatedDate(rs.getDate("created_date"));
                question.setEditedDate(rs.getDate("edited_date"));
                question.setClosedDate(rs.getDate("closed_date"));
                question.setTitle(rs.getString("title"));
                question.setBody(rs.getString("body"));
                question.setImage(rs.getString("image"));
                question.setScore(rs.getInt("score"));
                question.setViewCount(rs.getInt("view_count"));
                question.setAnswerCount(rs.getInt("answer_count"));
                question.setVoteCount(rs.getInt("vote_count"));
                question.setStatus(rs.getBoolean("status"));
                questions.add(question);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

        }
        return questions;
    }

    public int getQuestionCountForToday(){
        Connection con = null;
        int count = 0;
        try {
            con = dataSource.getConnection();
            CallableStatement callSt = con.prepareCall("{call GetQuestionCountForToday()}");
            ResultSet rs = callSt.executeQuery();
            if (rs.next()) {
                count = rs.getInt("question_count");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (con!=null){
                try {
                    con.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return count;
    };
    public int getQuestionCountForThisWeek(){
        Connection con = null;
        int count = 0;
        try {
            con = dataSource.getConnection();
            CallableStatement callSt = con.prepareCall("{call GetQuestionCountForThisWeek()}");
            ResultSet rs = callSt.executeQuery();
            if (rs.next()) {
                count = rs.getInt("question_count");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (con!=null){
                try {
                    con.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return count;
    };
    public int getAnswerCountForToday(){
        Connection con = null;
        int count = 0;
        try {
            con = dataSource.getConnection();
            CallableStatement callSt = con.prepareCall("{call GetAnswerCountForToday()}");
            ResultSet rs = callSt.executeQuery();
            if (rs.next()) {
                count = rs.getInt("answer_count");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (con!=null){
                try {
                    con.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return count;
    };
    public int getAnswerCountForThisWeek(){
        Connection con = null;
        int count = 0;
        try {
            con = dataSource.getConnection();
            CallableStatement callSt = con.prepareCall("{call GetAnswerCountForThisWeek()}");
            ResultSet rs = callSt.executeQuery();
            if (rs.next()) {
                count = rs.getInt("answer_count");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (con!=null){
                try {
                    con.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return count;
    };
    public int getUserCountForToday(){
        Connection con = null;
        int count = 0;
        try {
            con = dataSource.getConnection();
            CallableStatement callSt = con.prepareCall("{call GetUserCountForToday()}");
            ResultSet rs = callSt.executeQuery();
            if (rs.next()) {
                count = rs.getInt("user_count");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (con!=null){
                try {
                    con.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return count;
    };
    public int getUserCountForThisWeek(){
        Connection con = null;
        int count = 0;
        try {
            con = dataSource.getConnection();
            CallableStatement callSt = con.prepareCall("{call GetUserCountForThisWeek()}");
            ResultSet rs = callSt.executeQuery();
            if (rs.next()) {
                count = rs.getInt("user_count");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (con!=null){
                try {
                    con.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return count;
    };

    public void incrementViewCount(Long questionId){
        Connection con = null;
        try {
            con = dataSource.getConnection();
            CallableStatement callSt = con.prepareCall("{call IncrementViewCount(?)}");
            callSt.setLong(1, questionId);
            callSt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (con!=null){
                try {
                    con.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }
    public void incrementQVoteCount(Long questionId){
        Connection con = null;
        try {
            con = dataSource.getConnection();
            CallableStatement callSt = con.prepareCall("{call IncrementQVoteCount(?)}");
            callSt.setLong(1, questionId);
            callSt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (con!=null){
                try {
                    con.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }
    public void decreaseQVoteCount(Long questionId){
        Connection con = null;
        try {
            con = dataSource.getConnection();
            CallableStatement callSt = con.prepareCall("{call DecreaseQVoteCount(?)}");
            callSt.setLong(1, questionId);
            callSt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (con!=null){
                try {
                    con.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }
    public void incrementAVoteCount(Long answerId){
        Connection con = null;
        try {
            con = dataSource.getConnection();
            CallableStatement callSt = con.prepareCall("{call IncrementAVoteCount(?)}");
            callSt.setLong(1, answerId);
            callSt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (con!=null){
                try {
                    con.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }
    public void decreaseAVoteCount(Long answerId){
        Connection con = null;
        try {
            con = dataSource.getConnection();
            CallableStatement callSt = con.prepareCall("{call DecreaseAVoteCount(?)}");
            callSt.setLong(1, answerId);
            callSt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (con!=null){
                try {
                    con.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }
    }

