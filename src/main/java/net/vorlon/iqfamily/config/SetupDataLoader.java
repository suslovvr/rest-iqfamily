package net.vorlon.iqfamily.config;

import net.vorlon.iqfamily.dao.AccountRepository;
import net.vorlon.iqfamily.dao.UserRepository;
import net.vorlon.iqfamily.model.domain.User;
import net.vorlon.iqfamily.model.dto.AccountDTO;
import net.vorlon.iqfamily.service.AccountService;
import net.vorlon.iqfamily.task.IncBalanceComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

	private boolean alreadySetup = false;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AccountRepository accountRepository;


	@Autowired
	private AccountService accountService;

	@Autowired
	private ApplicationContext context;

	@Autowired
	private ThreadPoolTaskExecutor taskExecutor;
//	IncBalanceComponent

	@Override
	@Transactional
	public void onApplicationEvent(final ContextRefreshedEvent event) {
		if (alreadySetup) {
			return;
		}

		// Create user accounts
		createAccountIfNotFound("Иван",new BigDecimal(50));
		//incAccountBalances();

		alreadySetup = true;
	}

	@Transactional
	private final void createAccountIfNotFound(final String userName, BigDecimal balance) {
		Optional<User> user = userRepository.findByName(userName);
		if(user.isPresent()){
			accountService.letBalanceForUserId(user.get().getId(),balance);
		}

	}

	@Transactional
	private final void incAccountBalances() {
		List<Long> userIds=userRepository.findAllIds();
		userIds.stream().forEach( userId -> {
			IncBalanceComponent runner = (IncBalanceComponent) context.getBean("incBalanceComponent");
			runner.setUserId(userId);
			taskExecutor.execute(runner);
		});
		int count =0;
		for (int i=0;i<1000;i++) {
			count = taskExecutor.getActiveCount();
			System.out.println("Active Threads : " + count);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (count == 1) {
				break;
			}
		}
		if (count == 0) {
			taskExecutor.shutdown();
		}

	}

}