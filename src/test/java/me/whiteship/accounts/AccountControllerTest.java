package me.whiteship.accounts;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import me.whiteship.Application;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author LeeSooHoon
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
//@ContextConfiguration(loader = SpringApplicationContextLoader.class, classes = Application.class)
@WebAppConfiguration
@Transactional
//@TransactionConfiguration(defaultRollback = true)
public class AccountControllerTest {

    @Autowired
    WebApplicationContext wac;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    AccountService service;

    MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    // TODO 서비스 호출에서 예외 상황을 비동기 콜백으로 처리하는 것도 해주세요. 예외 던지지 말고
    @Test
    public void createAccount() throws Exception {
        AccountDto.Create createDto = accountCreateDto();

        ResultActions result = mockMvc.perform(post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)));

        result.andDo(print());
        // {"id":1,"userName":"whiteship","fullName":null,"joined":1441702790919,"updated":1441702790919}
        result.andExpect(status().isCreated());
        result.andExpect(jsonPath("$.userName", is("whiteship")));

        result = mockMvc.perform(post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)));

        result.andDo(print());
        result.andExpect(status().isBadRequest());
        result.andExpect(jsonPath("$.code", is("duplicated.userName.exception")));
    }

    @Test
    public void createAccount_BadRequest() throws Exception {
        AccountDto.Create createDto = new AccountDto.Create();
        createDto.setUserName("  ");
        createDto.setPassword("1234 ");

        ResultActions result = mockMvc.perform(post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)));

        result.andDo(print());
        result.andExpect(status().isBadRequest());
        result.andExpect(jsonPath("$.code", is("bad.request")));
    }

    // TODO getAccounts()

    @Test
    public void getAccounts() throws Exception {
        AccountDto.Create createDto = accountCreateDto();
        service.createAccount(createDto);

        ResultActions result = mockMvc.perform(get("/accounts"));

        // {"content":
        // [{"id":1,"userName":"whiteship","fullName":null,"joined":1441773700552,"updated":1441773700552}],
        // "totalElements":1,
        // "totalPages":1,
        // "last":true,
        // "size":20,
        // "number":0,
        // "sort":null,
        // "first":true,
        // "numberOfElements":1}
        result.andDo(print());
        result.andExpect(status().isOk());
    }

    private AccountDto.Create accountCreateDto() {
        AccountDto.Create createDto = new AccountDto.Create();
        createDto.setUserName("whiteship");
        createDto.setPassword("password");
        return createDto;
    }

    @Test
    public void getAccount() throws Exception {
        final AccountDto.Create createDto = accountCreateDto();
        final Account account = service.createAccount(createDto);
        final ResultActions result = mockMvc.perform(get("/accounts/" + account.getId()));

        result.andDo(print());
        result.andExpect(status().isOk());
    }

    @Test
    public void updateAccount() throws Exception {
        final AccountDto.Create createDto = accountCreateDto();
        final Account account = service.createAccount(createDto);

        final AccountDto.Update updateDto = new AccountDto.Update();
        updateDto.setFullName("keesun baik");
        updateDto.setPassword("pass");

        final ResultActions result = mockMvc.perform(put("/accounts/" + account.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)));

        result.andDo(print());
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.fullName", is("keesun baik")));
    }

    @Test
    public void deleteAccount() throws Exception {
        ResultActions result = mockMvc.perform(delete("/accounts/1"));
        result.andDo(print());
        result.andExpect(status().isBadRequest());

        final AccountDto.Create createDto = accountCreateDto();
        final Account account = service.createAccount(createDto);

        result = mockMvc.perform(delete("/accounts/" + account.getId()));
        result.andDo(print());
        result.andExpect(status().isNoContent());
    }

}
