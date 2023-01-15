package net.vorlon.iqfamily.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.vorlon.iqfamily.dao.EmailRepository;
import net.vorlon.iqfamily.dao.UserRepository;
import net.vorlon.iqfamily.exception.IdNotFoundException;
import net.vorlon.iqfamily.model.dto.UserDTO;
import net.vorlon.iqfamily.model.spec.UserSpecifications;
import net.vorlon.iqfamily.model.web.UserEntry;
import net.vorlon.iqfamily.model.domain.*;
import net.vorlon.iqfamily.model.web.UserFilter;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static java.time.LocalDateTime.now;

import net.vorlon.iqfamily.model.domain.User;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final ModelMapper modelMapper;

    private final UserRepository repository;
    private final EmailRepository emailRepository;

    @Override
    public List<UserEntry> getAll(Integer pageNo, Integer pageSize) {
        Pageable paging = PageRequest.of(pageNo, pageSize);
        return repository.findAll(paging)
                .stream().map(entity -> afterFetch(entity)).collect(Collectors.toList());
    }


    @Override
    public UserEntry findByName(String name) {
        User entity = repository.findByName(name).orElse(null);
        if(entity!=null){
            return afterFetch(entity);
        }else{
            return null;
        }
    }

    private UserEntry afterFetch(User entity) {
        log.info("afterFetch entity :{}", entity);
        UserEntry retVal = modelMapper.map(entity, UserEntry.class);
        List<String> emailList=entity.getEmails().stream().map(e -> e.getEmail()).collect(Collectors.toList());
        retVal.setEmails(
                entity.getEmails().stream().map(e -> e.getEmail()).collect(
                () -> new LinkedList<>(),
                (list, element) -> list.add(element),
                (listA,listB ) -> listA.addAll(listB)));
        retVal.setPhones(
                entity.getPhones().stream().map(e -> e.getPhone()).collect(
                        () -> new LinkedList<>(),
                        (list, element) -> list.add(element),
                        (listA,listB ) -> listA.addAll(listB)));
        return retVal;
    }


    private void validate(UserDTO dto, LocalDateTime dt) {
        log.info("validate entity :{}", dto);
    }

    @Override
    public UserEntry getById(Long id) {
        return afterFetch(findById(id));
    }

    public UserEntry createEmail(String email){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> entity=repository.findByName(authentication.getPrincipal().toString());
        if(entity.isPresent()){
            Email theEmail=new Email(entity.get(),email);
            entity.get().getEmails().add(theEmail);//new Email(entity.get(),email));
            log.info(theEmail.toString());
            repository.save(entity.get());
        }
        return afterFetch(entity.get());
    }

    public UserEntry createPhone(String phone){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> entity=repository.findByName(authentication.getPrincipal().toString());
        if(entity.isPresent()){
            Phone thePhone=new Phone(entity.get(),phone);
            entity.get().getPhones().add(thePhone);//new Email(entity.get(),email));
            log.info("added phone {}",thePhone.toString());
            repository.save(entity.get());
        }
        return afterFetch(entity.get());
    }
    @Transactional
    @Override
    public UserEntry updateEmail(String old_email,String email){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> entity=repository.findByName(authentication.getPrincipal().toString());
        if(entity.isPresent()){
            Email updEmail=null;
            for(Email theEmail:entity.get().getEmails()) {
                if (theEmail.getEmail().equalsIgnoreCase(old_email)) {
                    updEmail = theEmail;
                    break;
                }
            }
            if(updEmail!=null) {
                updEmail.setEmail(email);
                log.info("user email {} was updated", updEmail.getEmail());
                repository.save(entity.get());
            }
        }
        return afterFetch(entity.get());
    }
    @Override
    public UserEntry deleteEmail(String email){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> entity=repository.findByName(authentication.getPrincipal().toString());
        if(entity.isPresent()){
            Email delEmail=null;
            for(Email theEmail:entity.get().getEmails()) {
                if (theEmail.getEmail().equalsIgnoreCase(email)) {
                    delEmail = theEmail;
                    break;
                }
            }
            if(delEmail!=null) {
                emailRepository.deleteByEmail(email);
                log.info("user email {} was removed", delEmail.getEmail());
                entity.get().getEmails().remove(delEmail);
            }
        }
        return afterFetch(entity.get());
    }
    @Override
    public UserDetails loadUserByUsernameAndPassword(String username, String password){
        Optional<User> userEntity = repository.findByName(username);
        User incomingUser=null;
        if (userEntity.isPresent()) {
            incomingUser=userEntity.get();
        }else {
            List<User> userList=repository.findByEmail(username);
            if(userList.size()>0) {
                incomingUser=userList.get(0);
            }else {
                userList=repository.findByPhone(username);
                if(userList.size()>0) {
                    incomingUser=userList.get(0);
                }
            }
        }
            if(incomingUser == null) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unknown user");
            }
            if (!password.equals(incomingUser.getPassword())) {
                log.info("Неверный пароль");
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Bad password");

            }
            List<GrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));

            log.info("роль " + authorities.toString());
            UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                    .username(incomingUser.getName())
                    .password(password)
                    .authorities(authorities)
                    .build();
            log.info(userDetails.toString());
        return userDetails;
    }

    @Override
    public List<UserEntry> find(UserFilter filter, Integer pageNo, Integer pageSize){
        List<User> result=repository.findAll(buildSpecification(filter));
        return result.stream().map(user -> afterFetch(user)).collect(Collectors.toList());
    }
    private Specification<User> buildSpecification(UserFilter filter) {
        Specification<User> retval=Specification.where(anyUsers());
        if(!StringUtils.isEmpty(filter.getName())){
            retval = retval.and(UserSpecifications.userHasName(filter.getName()));
        }
        if(!StringUtils.isEmpty(filter.getDateOfBirth())){
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("y-M-d");

                //convert String to LocalDate
                LocalDate brthDate = LocalDate.parse(filter.getDateOfBirth(), formatter);

                retval = retval.and(UserSpecifications.userHasBirthday(brthDate));
                //log.error("user buildSpecification for brthDate {} with eroor {}",filter.getDateOfBirth(),e.getMessage());
        }
        if(!StringUtils.isEmpty(filter.getEmail())){
            retval = retval.and(UserSpecifications.hasUserEmail(filter.getEmail()));
        }
        return retval;
    }
    private static Specification<User> anyUsers() {
        return new Specification<User>() {
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.equal(cb.literal(1), 1);
            }
        };
    }
    private User findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new IdNotFoundException(id));
    }
}
