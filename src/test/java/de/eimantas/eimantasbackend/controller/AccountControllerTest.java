package de.eimantas.eimantasbackend.controller;

import de.eimantas.eimantasbackend.TestUtils;
import de.eimantas.eimantasbackend.entities.Account;
import de.eimantas.eimantasbackend.entities.AccountHistory;
import de.eimantas.eimantasbackend.entities.converter.EntitiesConverter;
import de.eimantas.eimantasbackend.entities.dto.AccountDTO;
import de.eimantas.eimantasbackend.helpers.EntityHelper;
import de.eimantas.eimantasbackend.repo.AccountHistoryRepository;
import de.eimantas.eimantasbackend.repo.AccountRepository;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.adapters.RefreshableKeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import javax.inject.Inject;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class AccountControllerTest {

  private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
      MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

  private MockMvc mockMvc;

  @SuppressWarnings("rawtypes")
  private HttpMessageConverter mappingJackson2HttpMessageConverter;

  @Autowired
  private EntitiesConverter entitiesConverter;

  private Account acc;

  @Autowired
  private AccountRepository accountRepository;

  @Inject
  private AccountHistoryRepository accountHistoryRepository;


  private KeycloakAuthenticationToken mockPrincipal;


  @Autowired
  private WebApplicationContext webApplicationContext;

  @Autowired
  void setConverters(HttpMessageConverter<?>[] converters) {

    this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
        .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter).findAny().orElse(null);

    assertNotNull("the JSON message converter must not be null", this.mappingJackson2HttpMessageConverter);
  }

  @Before
  public void setup() throws Exception {

    accountRepository.deleteAll();

    // auth stuff
    mockPrincipal = Mockito.mock(KeycloakAuthenticationToken.class);
    Mockito.when(mockPrincipal.getName()).thenReturn(TestUtils.USER_ID);

    KeycloakPrincipal keyPrincipal = Mockito.mock(KeycloakPrincipal.class);
    RefreshableKeycloakSecurityContext ctx = Mockito.mock(RefreshableKeycloakSecurityContext.class);

    AccessToken token = Mockito.mock(AccessToken.class);
    Mockito.when(token.getSubject()).thenReturn(TestUtils.USER_ID);
    Mockito.when(ctx.getToken()).thenReturn(token);
    Mockito.when(keyPrincipal.getKeycloakSecurityContext()).thenReturn(ctx);
    Mockito.when(mockPrincipal.getPrincipal()).thenReturn(keyPrincipal);

    this.mockMvc = webAppContextSetup(webApplicationContext).build();
    acc = TestUtils.getAccount();
    acc.setUserId(TestUtils.USER_ID);

    accountRepository.save(acc);

    AccountHistory accountHistory = EntityHelper.createHistory(acc.getId());
    accountHistory.setUserId(TestUtils.USER_ID);

    AccountHistory accountHistory1 = EntityHelper.createHistory(123);
    accountHistory1.setUserId(TestUtils.USER_ID);


    accountHistoryRepository.save(accountHistory);
    accountHistoryRepository.save(accountHistory1);


  }


  @Test
  public void readNonExistingOverview() throws Exception {

    // given(controller.principal).willReturn(allEmployees);
    mockMvc.perform(get("/account/overview/" + 98).principal(mockPrincipal)).andExpect(status().isNotFound());
  }


  @SuppressWarnings("unchecked")
  protected String json(Object o) throws IOException {
    MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
    this.mappingJackson2HttpMessageConverter.write(o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
    return mockHttpOutputMessage.getBodyAsString();
  }


  @Test
  public void testGetAccountList() throws Exception {


    // given(controller.principal).willReturn(allEmployees);
    mockMvc.perform(get("/account/list").principal(mockPrincipal)).andExpect(status().isOk())
        .andDo(MockMvcResultHandlers.print()).andExpect(content().contentType(contentType))
        .andExpect(jsonPath("$.[0].id", is(acc.getId().intValue())))
        .andExpect(jsonPath("$.[0].active", is(acc.isActive())))
        .andExpect(jsonPath("$.[0].bank", is(acc.getBank())))
        .andExpect(jsonPath("$.[0].name", is(acc.getName())));


  }


  @Test
  public void testGetAccountHistoryList() throws Exception {


    // given(controller.principal).willReturn(allEmployees);
    mockMvc.perform(get("/account/history/get/" + acc.getId()).principal(mockPrincipal)).andExpect(status().isOk())
        .andDo(MockMvcResultHandlers.print()).andExpect(content().contentType(contentType))
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$.[0].refAccountId", is(acc.getId().intValue())));
  }


  @Test
  public void testGetAccounHistoriestList() throws Exception {
    // given(controller.principal).willReturn(allEmployees);
    mockMvc.perform(get("/account/history/list").principal(mockPrincipal)).andExpect(status().isOk())
        .andDo(MockMvcResultHandlers.print()).andExpect(content().contentType(contentType))
        .andExpect(jsonPath("$", hasSize(2)));
  }

  @Test
  public void testCreateAccount() throws Exception {

    AccountDTO dto = TestUtils.getAccountDTO();
    String bookmarkJson = json(dto);

    this.mockMvc.perform(post("/account/save").principal(mockPrincipal).contentType(contentType).content(bookmarkJson))
        .andExpect(status().isOk())
        .andDo(MockMvcResultHandlers.print())
        .andExpect(jsonPath("$.id", is(greaterThan(0))))
        .andExpect(jsonPath("$.active", is(true)))
        .andExpect(jsonPath("$.bank", is("test")))
        .andExpect(jsonPath("$.name", is("testname")))
        .andExpect(jsonPath("$.userId", is(TestUtils.USER_ID)));

  }

  @Test
  public void testUpdateAccount() throws Exception {

    String name = "updated";
    AccountDTO dto = entitiesConverter.getAccountDTO(acc);
    dto.setName(name);
    String bookmarkJson = json(dto);

    this.mockMvc.perform(put("/account/save").principal(mockPrincipal).contentType(contentType).content(bookmarkJson))
        .andExpect(status().isOk())
        .andDo(MockMvcResultHandlers.print())
        .andExpect(jsonPath("$.id", is(acc.getId().intValue())))
        .andExpect(jsonPath("$.active", is(true)))
        .andExpect(jsonPath("$.bank", is("test")))
        .andExpect(jsonPath("$.name", is(name)))
        .andExpect(jsonPath("$.userId", is(TestUtils.USER_ID)));

  }

  @Test
  public void testUpdateAccountWrongId() throws Exception {

    String name = "updated";
    AccountDTO dto = entitiesConverter.getAccountDTO(acc);
    dto.setName(name);
    dto.setId(123123L);
    String bookmarkJson = json(dto);

    this.mockMvc.perform(put("/account/save").principal(mockPrincipal).contentType(contentType).content(bookmarkJson))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(status().isBadRequest());

  }

  @Test
  public void testUpdateAccountNoId() throws Exception {

    String name = "updated";
    AccountDTO dto = entitiesConverter.getAccountDTO(acc);
    dto.setName(name);
    dto.setId(null);
    String bookmarkJson = json(dto);

    this.mockMvc.perform(put("/account/save").principal(mockPrincipal).contentType(contentType).content(bookmarkJson))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(status().isOk());

  }


  @Test
  public void testCreateAccountDtoWithID() throws Exception {

    AccountDTO dto = TestUtils.getAccountDTO();
    dto.setId(12123L);
    String bookmarkJson = json(dto);

    this.mockMvc.perform(post("/account/save").principal(mockPrincipal).contentType(contentType).content(bookmarkJson))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(status().isBadRequest());
  }

  @Test
  public void testCreateAccountDtoNOMock() throws Exception {

    AccountDTO dto = TestUtils.getAccountDTO();
    dto.setId(12L);
    String bookmarkJson = json(dto);

    this.mockMvc.perform(post("/account/save").principal(mockPrincipal).contentType(contentType))
        .andExpect(status().isBadRequest())
        .andDo(MockMvcResultHandlers.print());

  }


  @Test
  @Ignore
  public void testGetAccountListNoAuth() throws Exception {

    mockMvc.perform(get("/account/list")).andExpect(status().isBadRequest())
        .andDo(MockMvcResultHandlers.print());
  }


}
