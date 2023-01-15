package net.vorlon.iqfamily.service;


import net.vorlon.iqfamily.model.dto.AccountDTO;
import net.vorlon.iqfamily.model.web.UserAccountEntry;
import net.vorlon.iqfamily.model.web.UserEntry;
import net.vorlon.iqfamily.model.web.UserFilter;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {
    public static final  BigDecimal ZERO=new BigDecimal(0.0);
    void crateAccountByUserId(Long userId);
    void letBalanceForUserId(Long userId, BigDecimal balance);
    boolean incBalanceOnPctWithLimit(Long userId, BigDecimal pct, BigDecimal limit);
    boolean sendAmountToUserByName(String userId, BigDecimal amount);
    UserAccountEntry getBalanceByUserId(Long userId);
    UserAccountEntry getUserBalance();
    AccountDTO getUserAccount(Long userId);
}
