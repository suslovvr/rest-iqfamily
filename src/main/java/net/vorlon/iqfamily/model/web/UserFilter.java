package net.vorlon.iqfamily.model.web;

import lombok.Data;

@Data
public class UserFilter {
    private String dateOfBirth;
    private String email;
    private String phone;
    private String name;
}

