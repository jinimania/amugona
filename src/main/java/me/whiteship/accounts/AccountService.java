package me.whiteship.accounts;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
    private ModelMapper modelMapper;

    public Account createAccount(AccountDto.Create dto) {
        Account account = modelMapper.map(dto, Account.class);
        // TODO 유효한 userName인지 판단
        String userName = dto.getUserName();
        if (repository.findByUserName(userName) != null) {
//            logger.error("user duplicated exception, {}", userName);
            log.error("user duplicated exception, {}", userName);
            throw new UserDuplicatedException(userName);
        }


        // TODO password 해싱
        Date now = new Date();
        account.setJoined(now);
        account.setUpdated(now);
        return repository.save(account);
    }
}
