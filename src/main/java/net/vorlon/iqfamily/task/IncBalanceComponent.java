package net.vorlon.iqfamily.task;

import net.vorlon.iqfamily.model.domain.Account;
import net.vorlon.iqfamily.model.dto.AccountDTO;
import net.vorlon.iqfamily.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Scope("prototype")
public class IncBalanceComponent implements Runnable{
    private Long userId;
    private BigDecimal TEN_PCT =new BigDecimal(10.0);
    @Autowired
    private AccountService service;

    public void setUserId(Long userId){
        this.userId=userId;
    }


    @Override
    public void run() {
        System.out.println("incrementing balance for user " + userId + " is starting");

        AccountDTO accountDTO=service.getUserAccount(userId);
        BigDecimal limit =accountDTO.getBalance().multiply(new BigDecimal(2.07));
        for(int i=0;i<10000;i++){
            if(!service.incBalanceOnPctWithLimit(userId,TEN_PCT,limit)){
                break;
            }
        } {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("incrementing balance for user " + userId + " is complited");

    }
}
