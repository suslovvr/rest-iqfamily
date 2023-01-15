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
public class UserAccountEntry {

    @NotBlank
    private String userName;
    private String balance;

}
