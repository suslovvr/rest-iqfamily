package net.vorlon.iqfamily.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.vorlon.iqfamily.model.web.SendAmountRequest;
import net.vorlon.iqfamily.model.web.UserAccountEntry;
import net.vorlon.iqfamily.model.web.UserEntry;
import net.vorlon.iqfamily.model.web.UserFilter;
import net.vorlon.iqfamily.service.AccountService;
import net.vorlon.iqfamily.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/account")
@Api(value = "/api/account", description = "расчетные Счета")
public class AccountController {

    private final AccountService service;

    @ApiOperation("Получение баланса пользователя")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/user-balance")
    public UserAccountEntry getUserBalance() {
        log.info("get user balance");
        return service.getUserBalance();
    }


    @ApiOperation("перечисление")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/send-amount")
    public Boolean getUserBalance(@RequestBody SendAmountRequest req) {
        log.info("get user balance");
        BigDecimal amount=new BigDecimal(req.getAmount());
        return service.sendAmountToUserByName(req.getUserName(),  amount);
    }
}
