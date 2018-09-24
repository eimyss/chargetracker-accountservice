package de.eimantas.eimantasbackend.controller;

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

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class MultipleAccountControllerTest {

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    private MockMvc mockMvc;

    @SuppressWarnings("rawtypes")
    private HttpMessageConverter mappingJackson2HttpMessageConverter;


    private Account acc;

    private Account acc2;
    @Autowired
    private AccountRepository accountRepository;

    private int monthsGoBack = 6;
    private int expensesForMonth = 3;


    private KeycloakAuthenticationToken mockPrincipal;


    // @Autowired
    // private ExpenseController controller;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
                .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter).findAny().orElse(null);

        assertNotNull("the JSON message converter must not be null", this.mappingJackson2HttpMessageConverter);
    }

    @Before
    @Transactional
    public void setup() throws Exception {


        accountRepository.deleteAll();

        // auth stuff
        mockPrincipal = Mockito.mock(KeycloakAuthenticationToken.class);
        Mockito.when(mockPrincipal.getName()).thenReturn("test");

        KeycloakPrincipal keyPrincipal = Mockito.mock(KeycloakPrincipal.class);
        RefreshableKeycloakSecurityContext ctx = Mockito.mock(RefreshableKeycloakSecurityContext.class);

        AccessToken token = Mockito.mock(AccessToken.class);
        Mockito.when(token.getSubject()).thenReturn("1L");
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
    @Transactional
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
    @Transactional
    public void testGetAccountList() throws Exception {


        // given(controller.principal).willReturn(allEmployees);
        mockMvc.perform(get("/account/list").principal(mockPrincipal)).andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print()).andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.[0].id", is(acc.getId().intValue())))
                .andExpect(jsonPath("$.[0].active", is(acc.isActive())))
                .andExpect(jsonPath("$.[0].bank", is(acc.getBank())))
                .andExpect(jsonPath("$.[0].name", is(acc.getName())))
                .andExpect(jsonPath("$.[0].userId", is("1L")))
                .andExpect(jsonPath("$.[1].id", is(acc2.getId().intValue())))
                .andExpect(jsonPath("$.[1].active", is(acc2.isActive())))
                .andExpect(jsonPath("$.[1].bank", is(acc2.getBank())))
                .andExpect(jsonPath("$.[1].name", is(acc2.getName())))
                .andExpect(jsonPath("$.[1].userId", is("1L")));


    }

    @Test
    @Transactional
    public void testGetAccountListIds() throws Exception {


        // given(controller.principal).willReturn(allEmployees);
        mockMvc.perform(get("/account/list/id").principal(mockPrincipal)).andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print()).andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(greaterThan(0))));


    }



}
