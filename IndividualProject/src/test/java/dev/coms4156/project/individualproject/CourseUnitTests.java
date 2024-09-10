package dev.coms4156.project.individualproject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

/**
 * Unit tests for the {@code Course} class.
 *
 * <p>This class contains the tests for validating the functionality
 * of the {@code Course} class, including the methods and constructors.
 */
@SpringBootTest
@ContextConfiguration
public class CourseUnitTests {

  @BeforeAll
  public static void setupCourseForTesting() {
    testCourse = new Course("Griffin Newbold", "417 IAB", "11:40-12:55", 250);
  }


  @Test
  public void toStringTest() {
    String expectedResult = "\nInstructor: Griffin Newbold; Location: 417 IAB; Time: 11:40-12:55";
    assertEquals(expectedResult, testCourse.toString());
  }

  @Test
  public void getMethodsTest() {
    String location = testCourse.getCourseLocation();
    String instructorName = testCourse.getInstructorName();
    String timeSlot = testCourse.getCourseTimeSlot();

    assertEquals("417 IAB", location);
    assertEquals("Griffin Newbold", instructorName);
    assertEquals("11:40-12:55", timeSlot);
  }

  @Test
  public void reassignTest() {
    String newInstructor = "Jane Doe";
    String newLocation = "633 Mudd";
    String newTimeSlot = "1:10-2:25";

    testCourse.reassignInstructor(newInstructor);
    testCourse.reassignLocation(newLocation);
    testCourse.reassignTime(newTimeSlot);

    assertEquals("633 Mudd", testCourse.getCourseLocation());
    assertEquals("Jane Doe", testCourse.getInstructorName());
    assertEquals("1:10-2:25", testCourse.getCourseTimeSlot());
  }

  @Test
  public void enrollmentTest() {
    testCourse.setEnrolledStudentCount(120);
    assertTrue(testCourse.dropStudent());
    assertTrue(testCourse.enrollStudent());

    testCourse.setEnrolledStudentCount(0);
    assertFalse(testCourse.dropStudent());
    assertFalse(testCourse.isCourseFull());

    testCourse.setEnrolledStudentCount(250);
    assertFalse(testCourse.enrollStudent());
    assertTrue(testCourse.isCourseFull());
  }


  /** The test course instance used for testing. */
  public static Course testCourse;
}

