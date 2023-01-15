package net.vorlon.iqfamily.model.web;

import com.sun.istack.NotNull;
import lombok.Data;

//import javax.validation.constraints.NotNull;
@Data
public class AuthRequest {
    @NotNull
    private String login;
    @NotNull
    private String password;




}