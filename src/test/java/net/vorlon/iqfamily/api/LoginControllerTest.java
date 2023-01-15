package net.vorlon.iqfamily.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.extern.slf4j.Slf4j;
import net.vorlon.iqfamily.RestApplication;
import net.vorlon.iqfamily.model.web.AuthRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.MOCK, classes={ RestApplication.class })
public class LoginControllerTest {
    private AuthRequest authRequest;
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setupMock() {

    }
    @Before
    public void setUp() {
        authRequest=new AuthRequest();
        authRequest.setLogin("+798765432112");
        authRequest.setPassword("12345678");
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }
    @Test
    public void createJwtTest() throws Exception{
        System.out.println("LoginController simpleTest");
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        try{
            String json = ow.writeValueAsString(authRequest);
            this.mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk());
        }catch(JsonProcessingException e){
            System.out.println(e.getMessage());
        }
    }
}
