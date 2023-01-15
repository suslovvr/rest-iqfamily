package net.vorlon.iqfamily.service;


import net.vorlon.iqfamily.model.web.UserEntry;
import net.vorlon.iqfamily.model.web.UserFilter;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface UserService {
//    UserEntry create(UserEntry entry);

    UserEntry getById(Long id);

    List<UserEntry> getAll(Integer pageNo, Integer pageSize);

    UserEntry findByName(String name);

    UserEntry createEmail(String email);

    UserEntry updateEmail(String old_email, String email);

    UserEntry deleteEmail(String email);

    UserEntry createPhone(String phone);

    List<UserEntry> find(UserFilter filter,Integer pageNo, Integer pageSize);

    UserDetails loadUserByUsernameAndPassword(String username, String password);
}
