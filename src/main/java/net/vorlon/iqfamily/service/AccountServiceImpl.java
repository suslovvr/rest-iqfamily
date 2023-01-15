package net.vorlon.iqfamily.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.vorlon.iqfamily.dao.AccountRepository;
import net.vorlon.iqfamily.dao.EmailRepository;
import net.vorlon.iqfamily.dao.UserRepository;
import net.vorlon.iqfamily.exception.IdNotFoundException;
import net.vorlon.iqfamily.exception.InsufficientFundsException;
import net.vorlon.iqfamily.exception.InvalidSumValueException;
import net.vorlon.iqfamily.exception.UsernameNotFoundException;
import net.vorlon.iqfamily.model.domain.Account;
import net.vorlon.iqfamily.model.domain.Email;
import net.vorlon.iqfamily.model.domain.User;
import net.vorlon.iqfamily.model.dto.AccountDTO;
import net.vorlon.iqfamily.model.dto.UserDTO;
import net.vorlon.iqfamily.model.spec.UserSpecifications;
import net.vorlon.iqfamily.model.web.UserAccountEntry;
import net.vorlon.iqfamily.model.web.UserEntry;
import net.vorlon.iqfamily.model.web.UserFilter;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final ModelMapper modelMapper;

    private final AccountRepository repository;
    private final UserRepository userRepository;

    @Override
    public UserAccountEntry getBalanceByUserId(Long userId){
        Account entity=findByUserId(userId);
        return afterFetch(entity);
    }

    @Override
    public UserAccountEntry getUserBalance(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> entityUser=userRepository.findByName(authentication.getPrincipal().toString());
        Account entity=findByUserId(entityUser.get().getId());
        UserAccountEntry retVal = modelMapper.map(entity, UserAccountEntry.class);
        retVal.setUserName(entityUser.get().getName());
        return retVal;
    }

    @Override
    public void crateAccountByUserId(Long userId){
        if(repository.countByUserId(userId)==0l) {
            Account account=new Account();
            account.setUserId(userId);
            account.setBalance(new BigDecimal(0.0));
            repository.save(account);
        }
    }
    @Override
    public void letBalanceForUserId(Long userId, BigDecimal balance){
        if(balance.compareTo(new BigDecimal(0.0))<0){return;}
        if(repository.countByUserId(userId)==0l) {
            Account account=new Account();
            account.setUserId(userId);
            account.setBalance(balance);
            repository.save(account);
        }else{
            Account account=findByUserId(userId);
            account.setBalance(balance);
            repository.save(account);
        }
    }
    @Override
    public boolean incBalanceOnPctWithLimit(Long userId, BigDecimal pct, BigDecimal limit){
        Account account=findByUserId(userId);
        BigDecimal targetValue=account.getBalance().multiply(new BigDecimal(1.0).add(pct.divide(new BigDecimal(100.0))));
        if(targetValue.compareTo(limit)<0){
            letBalanceForUserId(userId,targetValue);
            return true;
        }
        return false;
    }
    @Override
    public boolean sendAmountToUserByName(String userName, BigDecimal amount) throws InvalidSumValueException,
            InsufficientFundsException,UsernameNotFoundException{
        amount=amount.setScale(2, BigDecimal.ROUND_HALF_DOWN);
        if(ZERO.compareTo(amount)>0){
             throw new InvalidSumValueException("неверное зачение суммы %s",amount);
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> entityUser=userRepository.findByName(authentication.getPrincipal().toString());
        Account entity=findByUserId(entityUser.get().getId());
        if(entity.getBalance().compareTo(amount)<0){
            throw new InsufficientFundsException("недостаточно средств.");
        }
        Optional<User> recipient=userRepository.findByName(userName);
        if(recipient.isEmpty()){
            throw new UsernameNotFoundException("получатель не найден");
        }
        if(recipient.get().getId()!=entityUser.get().getId()){
            Account recipientAccount = findByUserId(recipient.get().getId());
            letBalanceForUserId(entityUser.get().getId(), entity.getBalance().subtract(amount));
            letBalanceForUserId(recipientAccount.getUserId(), recipientAccount.getBalance().add(amount));
            return true;
        }
        return false;
    }

    @Override
    public AccountDTO getUserAccount(Long userId){
        Optional<Account> result= repository.findByUserId(userId);
        if(result.isPresent()){
            return modelMapper.map(result.get(),AccountDTO.class);
        }
        throw new IdNotFoundException(userId);
    }

    private UserAccountEntry afterFetch(Account entity) {
        log.info("afterFetch entity :{}", entity);
        UserAccountEntry retVal = modelMapper.map(entity, UserAccountEntry.class);
        Optional<String> userName=userRepository.findNameById(entity.getUserId());
        if(userName.isPresent()) {
            retVal.setUserName(userName.get());
        }
        return retVal;
    }


    private void validate(UserDTO dto, LocalDateTime dt) {
        log.info("validate entity :{}", dto);
    }
    private Account findByUserId(Long userId) {
        return repository.findByUserId(userId).orElseThrow(() -> new IdNotFoundException(userId));
    }
    private Account findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new IdNotFoundException(id));
    }
}
