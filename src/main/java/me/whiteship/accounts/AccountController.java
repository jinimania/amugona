package me.whiteship.accounts;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import me.whiteship.commons.ErrorResponse;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

/**
 * @author LeeSooHoon
 */
@RestController
public class AccountController {

    @Autowired
    private AccountService service;

    @Autowired
    private AccountRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    @RequestMapping("/hello")
    public String hello() {
        return "Hello Spring Boot";
    }

    @RequestMapping(value = "/accounts", method = POST)
    public ResponseEntity<Object> createAccount(@RequestBody @Valid AccountDto.Create create, BindingResult result) {
        if (result.hasErrors()) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setMessage("잘못된 요청입니다.");
            errorResponse.setCode("bad.request");
            // TODO BindingResult 안에 들어있는 에러 정보 사용하기.
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        Account newAccount = service.createAccount(create);
        // 서비스의 처리여부
        // 1. 리턴 타입으로 판단.
        // 2. 파라미터 이용.
        // 3. 서비스에서 예외를 던짐(굿)
        return new ResponseEntity<>(modelMapper.map(newAccount, AccountDto.Response.class), HttpStatus.CREATED);
    }

    // TODO stream() vs parallelStream()
    // TODO HATEOAS
    // TODO 뷰
    // NSPA 1. Thymeleaf
    // SPA 2. 앵귤러 3. 리엑트
    @RequestMapping(value = "/accounts", method = GET)
    @ResponseStatus(value = HttpStatus.OK)
    public PageImpl<AccountDto.Response> getAccounts(Pageable pageable) {
        Page<Account> page = repository.findAll(pageable);
        List<AccountDto.Response> content = page.getContent().parallelStream()
                .map(account -> modelMapper.map(account, AccountDto.Response.class))
                .collect(Collectors.toList());

        return new PageImpl<AccountDto.Response>(content, pageable, page.getTotalElements());
    }

    @RequestMapping(value = "/accounts/{id}", method = GET)
    @ResponseStatus(value = HttpStatus.OK)
    public AccountDto.Response getAccount(@PathVariable Long id) {
        final Account account = service.getAccount(id);
        return  modelMapper.map(account, AccountDto.Response.class);
    }

    // 전체 업데이트: PUT
    // - (username:"whiteship", password:"pass", fullName:null)

    // 부분 업데이트: PATCH
    // - (username:"whiteship")
    // - (password:"pass")
    // - (username:"whiteship", password:"pass")
    @RequestMapping(value = "/accounts/{id}", method = PUT)
    public ResponseEntity updateAccount(@PathVariable Long id, @RequestBody @Valid AccountDto.Update updateDto, BindingResult result) {
        if (result.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        final Account updatedAccount = service.updateAccount(id, updateDto);
        return new ResponseEntity<>(modelMapper.map(updatedAccount, AccountDto.Response.class), HttpStatus.OK);
    }

    // TODO 예외 처리 네번재 방법 (콜백 비스무리한거...)
    @ExceptionHandler(value = UserDuplicatedException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorResponse handleUserDuplicatedException(UserDuplicatedException e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage("[" + e.getUserName() + "] 중복된 userName 입니다.");
        errorResponse.setCode("duplicated.userName.exception");
        return errorResponse;
    }

    @ExceptionHandler(value = AccountNotFoundException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorResponse handleAccountNotFoundException(AccountNotFoundException e) {
        final ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage("[" + e.getId() + "]에 해당하는 계정이 없습니다.");
        errorResponse.setCode("account.not.found.exception");
        return errorResponse;
    }

}
