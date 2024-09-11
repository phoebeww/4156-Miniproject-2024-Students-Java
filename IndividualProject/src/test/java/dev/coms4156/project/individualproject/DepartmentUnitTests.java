package dev.coms4156.project.individualproject;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

/**
 * Unit tests for the {@code Department} class.
 *
 * <p>This class contains the tests for validating the functionality
 * of the {@code Department} class, including the methods and constructors.
 */
@SpringBootTest
@ContextConfiguration
public class DepartmentUnitTests {

  /**
   * This function sets up a new Department object with the given parameters for test.
   */
  @BeforeAll
  public static void setupDepartmentForTesting() {
    classes = new HashMap<>();
    Course course1 = new Course("Jane Doe", "K112", "12:10-13:25", 30);
    Course course2 = new Course("Alicia Mark", "H136", "11:10-12:25", 25);
    Course course3 = new Course("Emily Clark", "J101", "14:10-15:25", 40);
    classes.put("CS101", course1);
    classes.put("CS102", course2);
    classes.put("CS103", course3);
    testDept = new Department("COMS", classes, "Jane Doe", 250);
  }

  @Test
  public void getTest() {
    assertEquals(250, testDept.getNumberOfMajors());
    assertEquals("Jane Doe", testDept.getDepartmentChair());
    assertEquals(classes, testDept.getCourseSelection());
  }

  @Test
  public void majorTest() {
    testDept.increaseMajorNumber();
    assertEquals(251, testDept.getNumberOfMajors());

    testDept.decreaseMajorNumber();
    assertEquals(250, testDept.getNumberOfMajors());

    testDept1 = new Department("ECON", classes, "Jane Doe", 0);
    testDept1.decreaseMajorNumber();
    assertEquals(0, testDept1.getNumberOfMajors());
  }

  @Test
  public void courseTest() {
    Course testCourse = new Course("Mark Lee", "D410", "16:10-17:25", 50);
    testDept.createCourse("CS104", "Mark Lee",
        "D410", "16:10-17:25", 50);
    classes.put("CS104", testCourse);

    assertEquals(classes, testDept.getCourseSelection());
  }

  @Test
  public void toStringTest() {
    String expectedOutput = """
        COMS CS103:
        Instructor: Emily Clark; Location: J101; Time: 14:10-15:25
        COMS CS104:
        Instructor: Mark Lee; Location: D410; Time: 16:10-17:25
        COMS CS101:
        Instructor: Jane Doe; Location: K112; Time: 12:10-13:25
        COMS CS102:
        Instructor: Alicia Mark; Location: H136; Time: 11:10-12:25
        """;
    assertEquals(expectedOutput, testDept.toString());
  }

  /** The test Department instance used for testing. */
  public static Department testDept;
  public static Department testDept1;
  public static Map<String, Course> classes;
}
