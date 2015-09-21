package me.whiteship.accounts;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import lombok.extern.slf4j.Slf4j;

/**
 * @author LeeSooHoon
 */
@Service
@Transactional
@Slf4j
public class AccountService {

//    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AccountRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    public Account createAccount(AccountDto.Create dto) {
        Account account = modelMapper.map(dto, Account.class);

        String userName = dto.getUserName();
        if (repository.findByUserName(userName) != null) {
//            logger.error("user duplicated exception, {}", userName);
            log.error("user duplicated exception, {}", userName);
            throw new UserDuplicatedException(userName);
        }

        account.setPassword(passwordEncoder.encode(account.getPassword()));

        Date now = new Date();
        account.setJoined(now);
        account.setUpdated(now);
        return repository.save(account);
    }

    public Account updateAccount(Long id, AccountDto.Update updateDto) {
        final Account account = getAccount(id);
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        account.setFullName(updateDto.getFullName());
        return repository.save(account);
    }

    public Account getAccount(Long id) {
        final Account account = repository.findOne(id);
        if (account == null) {
            throw new AccountNotFoundException(id);
        }
        return account;
    }

    public void deleteAccount(Long id) {
        repository.delete(getAccount(id));
    }
}
