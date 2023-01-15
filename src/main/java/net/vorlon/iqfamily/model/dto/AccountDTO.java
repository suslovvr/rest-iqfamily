package net.vorlon.iqfamily.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import net.vorlon.iqfamily.model.web.UserEntry;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class AccountDTO {

    @NotBlank
    private Long id;

    @NotBlank
    private Long userId;

    @NotBlank
    private BigDecimal balance;

}
