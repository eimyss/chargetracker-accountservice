package de.eimantas.eimantasbackend.service;

import de.eimantas.eimantasbackend.TestUtils;
import de.eimantas.eimantasbackend.entities.Account;
import de.eimantas.eimantasbackend.repo.AccountRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.adapters.RefreshableKeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.mockito.Mockito;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import javax.inject.Inject;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class AccountServiceTest {

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    private MockMvc mockMvc;

    @Inject
    private AccountService accountService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private AccountRepository accountRepository;

    private int monthsGoBack = 6;
    private int expensesForMonth = 3;

    private Account acc = null;


    private Account acc2;


    private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());
    private KeycloakAuthenticationToken mockPrincipal;

    @Before
    public void setup() throws Exception {

        accountRepository.deleteAll();

        // auth stuff
        mockPrincipal = Mockito.mock(KeycloakAuthenticationToken.class);
        Mockito.when(mockPrincipal.getName()).thenReturn("test");

        KeycloakPrincipal keyPrincipal = Mockito.mock(KeycloakPrincipal.class);
        RefreshableKeycloakSecurityContext ctx = Mockito.mock(RefreshableKeycloakSecurityContext.class);

        AccessToken token = Mockito.mock(AccessToken.class);
        Mockito.when(token.getSubject()).thenReturn("Subject-111");
        Mockito.when(ctx.getToken()).thenReturn(token);
        Mockito.when(keyPrincipal.getKeycloakSecurityContext()).thenReturn(ctx);
        Mockito.when(mockPrincipal.getPrincipal()).thenReturn(keyPrincipal);



        this.mockMvc = webAppContextSetup(webApplicationContext).build();

        acc = TestUtils.getAccount();


        accountRepository.save(acc);
        acc.setUserId("1L");

        acc2 = TestUtils.getAccount();


        accountRepository.save(acc2);
        acc2.setUserId("1L");


    }


    @Test
    public void getTotalAccounts() throws Exception {

        Stream<Account> accounts = accountService.getAccountsByUserId("1L");
        assertThat(accounts).isNotNull();
        assertThat(((Stream) accounts).count()).isEqualTo(2);

    }

    @Test
    @Transactional
    public void getExpensesByAccount() throws Exception {

        Stream<Account> accounts = accountService.getAccountsByUserId("1L");
        assertThat(accounts).isNotNull();
        List<Account> accountList = accounts.collect(Collectors.toList());
        assertThat(accountList.size()).isEqualTo(2);

        for (Account acc : accountList) {
            assertThat(acc.getCreateDate()).isNotNull();
            assertThat(acc.getBank()).isNotNull();
        }

    }


    @Test
    public void testEditAccount() throws Exception {

        Optional<Account> accountOpt = accountService.getAccountById(acc.getId(), mockPrincipal);
        assertThat(accountOpt.isPresent()).isTrue();

        Account retrieved = accountOpt.get();
        String name = retrieved.getName();
        String neuName = "edited By Test";

        retrieved.setName(neuName);
        accountService.saveAccount(retrieved, mockPrincipal);

        Optional<Account> accupdate = accountRepository.findById(acc.getId());

        assertThat(accupdate.isPresent()).isTrue();
        assertThat(accupdate.get().getName()).isEqualTo(neuName);

    }


}
