package dev.coms4156.project.individualproject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

/**
 * Unit tests for the {@code RouteController} class.
 *
 * <p>This class contains the tests for validating the functionality
 * of the {@code RouteController} class, including the methods and constructors.
 */
@WebMvcTest
public class RouteUnitTests {

  @Autowired
  MockMvc mockMvc;

  @MockBean
  MyFileDatabase myFileDatabase;

  @BeforeEach
  public void setUpData() {
    try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("./data.txt"))) {
      Object obj = in.readObject();
      if (obj instanceof HashMap) {
        HashMap<String, Department> mapping = (HashMap<String, Department>) obj;
        when(myFileDatabase.getDepartmentMapping()).thenReturn(mapping);
      } else {
        throw new IllegalArgumentException("Invalid object type in file.");
      }
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
    }

    IndividualProjectApplication.myFileDatabase = myFileDatabase;
  }

  @Test
  public void indexTest() throws Exception {
    MvcResult mvcResult = mockMvc.perform(get("/index")
        .accept(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk()).andReturn();
    String content = mvcResult.getResponse().getContentAsString();
    String expected = """
      Welcome, in order to make an API call direct your browser or Postman to an endpoint \
      
      
       This can be done using the following format:\s
      
       http:127.0.0\
      .1:8080/endpoint?arg=value""";
    assertEquals(expected, content);
  }

  @Test
  public void retrieveDepartmentTest() throws Exception {
    MvcResult mvcResult1 = mockMvc.perform(get("/retrieveDept")
        .param("deptCode", "COMS")
        .accept(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk()).andReturn();

    String content1 = mvcResult1.getResponse().getContentAsString();
    assertEquals(myFileDatabase.getDepartmentMapping().get("COMS").toString(), content1);

    MvcResult mvcResult2 = mockMvc.perform(get("/retrieveDept")
        .param("deptCode", "CS")
        .accept(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isNotFound()).andReturn();

    String content2 = mvcResult2.getResponse().getContentAsString();
    assertEquals("Department Not Found", content2);

    when(myFileDatabase.getDepartmentMapping()).thenThrow(new RuntimeException("Test Exception"));
    MvcResult mvcResult3 = mockMvc.perform(get("/retrieveDept")
        .param("deptCode", "COMS")
        .accept(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isInternalServerError()).andReturn();

    String content3 = mvcResult3.getResponse().getContentAsString();
    assertEquals("An Error has occurred", content3);
  }

  @Test
  public void retrieveCourseTest() throws Exception {
    MvcResult mvcResult1 = mockMvc.perform(get("/retrieveCourse")
        .param("deptCode", "COMS")
        .param("courseCode", String.valueOf(3157))
        .accept(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk()).andReturn();

    String content1 = mvcResult1.getResponse().getContentAsString();
    assertEquals(myFileDatabase.getDepartmentMapping().get("COMS").getCourseSelection()
        .get(Integer.toString(3157)).toString(), content1);

    MvcResult mvcResult2 = mockMvc.perform(get("/retrieveCourse")
        .param("deptCode", "COMS")
        .param("courseCode", String.valueOf(1234))
        .accept(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isNotFound()).andReturn();

    String content2 = mvcResult2.getResponse().getContentAsString();
    assertEquals("Course Not Found", content2);

    MvcResult mvcResult3 = mockMvc.perform(get("/retrieveCourse")
        .param("deptCode", "CS")
        .param("courseCode", String.valueOf(3157))
        .accept(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isNotFound()).andReturn();

    String content3 = mvcResult3.getResponse().getContentAsString();
    assertEquals("Department Not Found", content3);

//    when(myFileDatabase.getDepartmentMapping()).thenThrow(new RuntimeException("Test Exception"));
//    MvcResult mvcResult4 = mockMvc.perform(get("/retrieveCourse")
//        .param("deptCode", "COMS")
//        .param("courseCode", String.valueOf(3157))
//        .accept(MediaType.APPLICATION_JSON_VALUE))
//        .andExpect(status().isInternalServerError()).andReturn();
//
//    String content4 = mvcResult4.getResponse().getContentAsString();
//    assertEquals("An Error has occurred", content4);
  }

  @Test
  public void courseFullTest() throws Exception {
    MvcResult mvcResult1 = mockMvc.perform(get("/isCourseFull")
        .param("deptCode", "COMS")
        .param("courseCode", String.valueOf(3157))
        .accept(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk()).andReturn();

    String content1 = mvcResult1.getResponse().getContentAsString();
    assertEquals(myFileDatabase.getDepartmentMapping().get("COMS").getCourseSelection()
        .get(Integer.toString(3157)).isCourseFull(), Boolean.parseBoolean(content1));

    MvcResult mvcResult2 = mockMvc.perform(get("/isCourseFull")
        .param("deptCode", "COMS")
        .param("courseCode", String.valueOf(1234))
        .accept(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isNotFound()).andReturn();

    String content2 = mvcResult2.getResponse().getContentAsString();
    assertEquals("Course Not Found", content2);
  }

  @Test
  public void majorCountTest() throws Exception {
    MvcResult mvcResult1 = mockMvc.perform(get("/getMajorCountFromDept")
        .param("deptCode", "COMS")
        .accept(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk()).andReturn();

    String content1 = mvcResult1.getResponse().getContentAsString();
    String expected = "There are: " + myFileDatabase.getDepartmentMapping().get("COMS")
        .getNumberOfMajors() + " majors in the department";
    assertEquals(expected, content1);

    MvcResult mvcResult2 = mockMvc.perform(get("/getMajorCountFromDept")
        .param("deptCode", "CS")
        .accept(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isNotFound()).andReturn();

    String content2 = mvcResult2.getResponse().getContentAsString();
    assertEquals("Department Not Found", content2);

  }

  @Test
  public void deptChairTest() throws Exception {
    MvcResult mvcResult1 = mockMvc.perform(get("/isDeptChair")
        .param("deptCode", "COMS")
        .accept(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk()).andReturn();

    String content1 = mvcResult1.getResponse().getContentAsString();
    String expected = myFileDatabase.getDepartmentMapping().get("COMS").getDepartmentChair()
        + " is " + "the department chair.";
    assertEquals(expected, content1);

    MvcResult mvcResult2 = mockMvc.perform(get("/isDeptChair")
        .param("deptCode", "CS")
        .accept(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isNotFound()).andReturn();

    String content2 = mvcResult2.getResponse().getContentAsString();
    assertEquals("Department Not Found", content2);

  }

}
