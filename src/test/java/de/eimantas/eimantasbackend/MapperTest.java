package de.eimantas.eimantasbackend;

import de.eimantas.eimantasbackend.TestUtils;
import de.eimantas.eimantasbackend.entities.Account;
import de.eimantas.eimantasbackend.entities.Project;
import de.eimantas.eimantasbackend.entities.converter.EntitiesConverter;
import de.eimantas.eimantasbackend.entities.dto.ProjectDTO;
import de.eimantas.eimantasbackend.messaging.AccountsSender;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class MapperTest {


  private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

  private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
      MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

  private MockMvc mockMvc;

  @Autowired
  EntitiesConverter entitiesConverter;

  @Autowired
  private WebApplicationContext webApplicationContext;

  List<Project> projectList;



  @Before
  public void setup() throws Exception {

    projectList = new ArrayList<>();

    for (int i = 0;i<10;i++) {
      projectList.add(TestUtils.getProject());
    }

  }


  @Test
  public void testQueue() throws Exception {

   int size = projectList.size();

   List<ProjectDTO> converted = entitiesConverter.getProjectDTO(projectList);
   assertThat(converted).isNotNull();
  // converted.forEach(p -> logger.info(p.toString()));
   assertThat(converted.size()).isEqualTo(size);

  }


}
