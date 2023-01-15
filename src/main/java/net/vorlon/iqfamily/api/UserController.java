package net.vorlon.iqfamily.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.vorlon.iqfamily.model.web.UserEntry;
import net.vorlon.iqfamily.model.web.UserFilter;
import net.vorlon.iqfamily.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.constraints.NotBlank;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@Api(value = "/api/user", description = "Пользователи")
public class UserController {

    private final UserService service;

    @ApiOperation("Получение записи по id")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public UserEntry getById(@PathVariable Long id) {
        log.info("getById user {}", id);
        return service.getById(id);
    }

    @ApiOperation("Список всех записей")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<UserEntry> getAll(@RequestParam(defaultValue = "0") Integer pageNo,
                                      @RequestParam(defaultValue = "10") Integer pageSize) {
        log.info("getAll users");
        return service.getAll( pageNo, pageSize);
    }

    @ApiOperation("Добавление email")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/add-email")
    public UserEntry addEmail(@RequestParam @NotBlank String email) {
        log.info("add user email {}", email);
        return service.createEmail(email);
    }

    @ApiOperation("обновление email")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/update-email/{old_email}/to/{email}")
    public UserEntry updEmail(@RequestParam @NotBlank String old_email,
                              @RequestParam @NotBlank String email) {
        log.info("update user email {} to {}",old_email, email);
        return service.updateEmail(old_email,email);
    }

    @ApiOperation("Удаление email")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/delete-email")
    public UserEntry deleteEmail(@RequestParam @NotBlank String email) {
        log.info("delete user email {}", email);
        return service.deleteEmail(email);
    }
    @ApiOperation("Добавление тел.номера")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/add-phone")
    public UserEntry addPhone(@RequestParam @NotBlank String phone) {
        log.info("add user phone {}", phone);
        return service.createPhone(phone);
    }

    @ApiOperation("Поиск по фильтру")
    @ResponseStatus(HttpStatus.OK)
//    @PostMapping("/find")
    @RequestMapping(value = "/find", method = RequestMethod.POST)
    public List<UserEntry> find(@RequestBody UserFilter filter,
                                @RequestParam(defaultValue = "0") Integer pageNo,
                                @RequestParam(defaultValue = "10") Integer pageSize) {
        log.info("getAll users");
        return service.find(filter, pageNo, pageSize);
    }
/*
    @ApiOperation("Получение полного имени из сессии")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/fullName")
    public String fetchFullNameByLogin() {
        log.info("fetchFullNameByLogin ..");
        return service.fetchFullNameByLogin();
    }

 */
}
