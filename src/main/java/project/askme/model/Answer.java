package project.askme.model;
import java.sql.Date;

public class Answer {
    private Long id;
    private Long questionId;
    private Long userId;
    private String userFullName;
    private String userAvatar;
    private Date createdDate;
    private Date editedDate;
    private String body;
    private String image;
    private int score;
    private int voteCount;
    private boolean status;

    public Answer() {
    }

    public Answer(String body, String image) {
        this.body = body;
        this.image = image;
    }

    public Answer(Long id, Long questionId, Long userId, String userFullName, String userAvatar, Date createdDate, Date editedDate, String body, String image, int score, int voteCount, boolean status) {
        this.id = id;
        this.questionId = questionId;
        this.userId = userId;
        this.userFullName = userFullName;
        this.userAvatar = userAvatar;
        this.createdDate = createdDate;
        this.editedDate = editedDate;
        this.body = body;
        this.image = image;
        this.score = score;
        this.voteCount = voteCount;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getEditedDate() {
        return editedDate;
    }

    public void setEditedDate(Date editedDate) {
        this.editedDate = editedDate;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }


    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
