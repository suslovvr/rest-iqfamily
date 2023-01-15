package net.vorlon.iqfamily.service;

import net.vorlon.iqfamily.RestApplication;
import net.vorlon.iqfamily.model.web.UserAccountEntry;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.context.SecurityContext;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
//import static org.junit.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RestApplication.class)
public class AccountServiceTest {
    private final String sessionUserName="Маша";
    private final String recipientUserName="Иван";
    private final static BigDecimal ONE_HUNDRED=new BigDecimal(100.0);
    private final static BigDecimal FIFTY=new BigDecimal(50.0);
    @Autowired
    private AccountService accountService;

    @Before
    public void setupMock() {
        MockitoAnnotations.initMocks(this);

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn( sessionUserName);
        SecurityContextHolder.setContext(securityContext);
    }
    @Test
    public void getAmountUser() {
        UserAccountEntry entry=accountService.getUserBalance();
        assertThat(entry.getUserName(),is(sessionUserName));
    }
    @Test
    public void sendAmountUserToUser() {
        UserAccountEntry sessionUserAccount=accountService.getUserBalance();
        BigDecimal startSessionUserBalance= new BigDecimal(sessionUserAccount.getBalance());
        if(FIFTY.compareTo(startSessionUserBalance)>0) {
            accountService.sendAmountToUserByName(recipientUserName, FIFTY);
            UserAccountEntry resultSessUserAccount = accountService.getUserBalance();
            assertThat(startSessionUserBalance.subtract(FIFTY), is(resultSessUserAccount.getBalance()));
        }
    }
}
