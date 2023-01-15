package net.vorlon.iqfamily.model.web;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.LinkedList;

@Data
@NoArgsConstructor
@ToString(callSuper = true)
public class UserEntry {

    @NotBlank
    private String name;
    private LocalDate birthDate;
    private LinkedList<String> emails=null;
    private LinkedList<String> phones=null;


}
