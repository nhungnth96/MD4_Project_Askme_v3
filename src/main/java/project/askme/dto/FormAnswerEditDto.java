package project.askme.dto;

import org.springframework.web.multipart.MultipartFile;

public class FormAnswerEditDto {
    private Long answerId;
    private MultipartFile image;
    private String body;

    public FormAnswerEditDto() {
    }

    public FormAnswerEditDto(String body) {
        this.body = body;
    }

    public FormAnswerEditDto(MultipartFile image, String body) {
        this.image = image;
        this.body = body;
    }

    public FormAnswerEditDto(Long answerId, MultipartFile image, String body) {
        this.answerId = answerId;
        this.image = image;
        this.body = body;
    }

    public Long getAnswerId() {
        return answerId;
    }

    public void setAnswerId(Long answerId) {
        this.answerId = answerId;
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
