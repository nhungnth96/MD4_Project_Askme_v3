package project.askme.dto;

import org.springframework.web.multipart.MultipartFile;

public class FormAnswerDto {
    private MultipartFile image;
    private String body;

    public FormAnswerDto() {
    }

    public FormAnswerDto(MultipartFile image, String body) {
        this.image = image;
        this.body = body;
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
