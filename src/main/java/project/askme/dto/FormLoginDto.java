package project.askme.dto;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import project.askme.service.UserService;

import java.util.regex.Pattern;

public class FormLoginDto {
    private String username;
    private String password;

    public FormLoginDto() {
    }

    public FormLoginDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }




}
