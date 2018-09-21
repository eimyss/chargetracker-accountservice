package de.eimantas.eimantasbackend.service;

import de.eimantas.eimantasbackend.repo.AccountRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.mockito.Mockito;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class SecurityServiceTest {

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    private MockMvc mockMvc;


    @Inject
    private SecurityService securityService;

    @Inject
    private AccountRepository accountRepository;


    @Autowired
    private WebApplicationContext webApplicationContext;


    private KeycloakAuthenticationToken mockPrincipal;


    @Before
    public void setup() throws Exception {

        mockPrincipal = Mockito.mock(KeycloakAuthenticationToken.class);
        Mockito.when(mockPrincipal.getName()).thenReturn("test");

        this.mockMvc = webAppContextSetup(webApplicationContext).build();


    }


    @Test
    public void testGetIdByMock() throws Exception {

        Long accId = securityService.getUserIdFromPrincipal(mockPrincipal);
        assertThat(accId).isNotNull();

    }

    @Test(expected = SecurityException.class)
    public void testGetIdByMockNoPrincipal() throws Exception {

        Long accId = securityService.getUserIdFromPrincipal(null);
        assertThat(accId).isNotNull();

    }

    @Test
    public void testAllowedToRead() throws Exception {

        boolean accId = securityService.isAllowedToReadAcc(mockPrincipal, 1);
        assertThat(accId).isTrue();
    }

    @Test
    public void testAllowedToReadNoUser() throws Exception {
        boolean accId = securityService.isAllowedToReadAcc(mockPrincipal, 0);
        assertThat(accId).isTrue();

    }

    @Test(expected = SecurityException.class)
    public void testAllowedToReadNoPrincipal() throws Exception {

        boolean accId = securityService.isAllowedToReadAcc(null, 1);
        assertThat(accId).isTrue();

    }


}
