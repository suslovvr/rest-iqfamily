package net.vorlon.iqfamily.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import net.vorlon.iqfamily.model.web.UserEntry;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@ToString(callSuper=true)
public class UserDTO{

    @NotBlank
    private String name;

    private LocalDate birthDate;

    public void merge(UserEntry entry) {
        this.name = entry.getName();
        this.birthDate = entry.getBirthDate();
    }
}
