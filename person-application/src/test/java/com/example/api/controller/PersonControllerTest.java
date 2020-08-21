package com.example.api.controller;

import com.example.api.entities.Person;
import com.example.api.repository.PersonRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@AutoConfigureRestDocs
public class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    PersonRepository personRepository;

    private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext,
                      RestDocumentationContextProvider restDocumentation) {

        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @Test
    public void shouldReturnAllPersons() throws Exception {
        Person person1 = new Person(1, "ram", "kumar", "ram@gmail.com");
        Person person2 = new Person(2, "sham", "kumar", "sham@gmail.com");
        List<Person> persons = new ArrayList<>();
        persons.add(person1);
        persons.add(person2);
        when(personRepository.findAll()).thenReturn((persons));

//        this.setDummyDataForEmployees();
        mockMvc.perform(RestDocumentationRequestBuilders.get("/person")
                .accept(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$[0].firstName", is("ram")))
                .andExpect(jsonPath("$[0].email", is("ram@gmail.com")))
                .andExpect(jsonPath("$[1].firstName", is("sham")))
                .andExpect(jsonPath("$[1].email", is("sham@gmail.com")))
                .andDo(document("{methodName}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .andReturn();

    }

    @Test
    public void shouldReturnPersonDetails_WhenIdForThatPersonIsPassed() throws Exception {
        Person person = new Person(1, "ram", "kumar", "ram@gmail.com");
        when(personRepository.findById(1)).thenReturn(Optional.of((person)));

        mockMvc.perform(RestDocumentationRequestBuilders.get("/person/1")
                .accept(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is("ram")))
                .andExpect(jsonPath("$.email", is("ram@gmail.com")))
                .andDo(document("{methodName}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("id").description("id of particular person").optional()

                        )))
                .andReturn();


    }

    @Test
    public void shouldReturnStatusNotFound_WhenIdForThePersonIsPassedNotFound() throws Exception {

        mockMvc.perform(RestDocumentationRequestBuilders.get("/person?personid=1")
                .accept(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("{methodName}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .andReturn();


    }

    @Test
    public void shouldStorePersonDetails_WhenPersonObjectWithDetailsIsPassed() throws Exception {

        Person person = new Person(2, "raman", "tyagi", "raman.tyagi@gmail.com");
        when(personRepository.save(any(Person.class))).thenReturn(person);


        mockMvc.perform(RestDocumentationRequestBuilders.post("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(person)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is("raman")))
                .andExpect(jsonPath("$.email", is("raman.tyagi@gmail.com")))
                .andDo(document("{methodName}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .andReturn();
        verify(personRepository, times(1)).save(any(Person.class));


    }

    @Test
    public void shouldUpdatePersonDetails_WhenPersonObjectWithDetailsAndIdIsPassed() throws Exception {

        Person person = new Person(2, "raman", "tyagi", "raman.tyagi@gmail.com");
        when(personRepository.saveAndFlush(any(Person.class))).thenReturn(person);
        when(personRepository.findById(2)).thenReturn(Optional.of((person)));
        mockMvc.perform(RestDocumentationRequestBuilders.put("/person/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(person)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is("raman")))
                .andExpect(jsonPath("$.email", is("raman.tyagi@gmail.com")))
                .andDo(document("{methodName}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .andReturn();


    }

    @Test
    public void shouldReturnBadRequest_WhenPersonIdInParamAndBodyOfObjectNotSame() throws Exception {

        Person person = new Person(2, "raman", "tyagi", "raman.tyagi@gmail.com");
        mockMvc.perform(RestDocumentationRequestBuilders.put("/person/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(person)))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andDo(document("{methodName}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .andReturn();


    }

    @Test
    public void shouldReturnNotFound_WhenPersonIdObjectNotFound() throws Exception {

        Person person = new Person(2, "raman", "tyagi", "raman.tyagi@gmail.com");

        mockMvc.perform(RestDocumentationRequestBuilders.put("/person/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(person)))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andDo(document("{methodName}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .andReturn();


    }


}
