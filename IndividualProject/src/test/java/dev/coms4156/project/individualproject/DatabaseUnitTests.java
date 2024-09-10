package dev.coms4156.project.individualproject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

/**
 * Unit tests for the {@code MyFileDatabase} class.
 *
 * <p>This class contains the tests for validating the functionality
 * of the {@code MyFileDatabase} class, including the methods and constructors.
 */
@SpringBootTest
@ContextConfiguration
public class DatabaseUnitTests {

  /**
   * This function sets up a new mapping for test data file, and construct a new MyFileDatabase
   * object to test.
   */
  @BeforeAll
  public static void setupDatabaseForTesting() {
    //set up test files
    String[] times = {"11:40-12:55", "4:10-5:25", "10:10-11:25", "2:40-3:55"};
    String[] locations = {"417 IAB", "309 HAV", "301 URIS"};

    //data for comp dept
    Course coms1004 = new Course("Adam Cannon", locations[0], times[0], 400);
    coms1004.setEnrolledStudentCount(249);
    Course coms3134 = new Course("Brian Borowski", locations[2], times[1], 250);
    coms3134.setEnrolledStudentCount(242);

    HashMap<String, Course> courses = new HashMap<>();
    courses.put("1004", coms1004);
    courses.put("3134", coms3134);
    Department compSci = new Department("COMS", courses, "Luca Carloni", 2700);
    HashMap<String, Department> mapping = new HashMap<>();
    mapping.put("COMS", compSci);

    //data for econ dept
    Course econ1105 = new Course("Waseem Noor", locations[1], times[3], 210);
    econ1105.setEnrolledStudentCount(187);
    Course econ2257 = new Course("Tamrat Gashaw", "428 PUP", times[2], 125);
    econ2257.setEnrolledStudentCount(63);

    courses = new HashMap<>();
    courses.put("1105", econ1105);
    courses.put("2257", econ2257);
    Department econ = new Department("ECON", courses, "Michael Woodford", 2345);
    mapping.put("ECON", econ);

    try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("./dbtest.txt"))) {
      out.writeObject(mapping);
      System.out.println("Data write to ./dbtest.txt successfully.");
    } catch (IOException e) {
      e.printStackTrace();
    }
    testDatabase = new MyFileDatabase(0, "./dbtest.txt");
  }

  @Test
  public void deSerializeTest() {
    HashMap<String, Department> deserializedMap = testDatabase.deSerializeObjectFromFile();

    assertNotNull(deserializedMap);
    assertTrue(deserializedMap.containsKey("COMS"));
    assertEquals("Luca Carloni", deserializedMap.get("COMS").getDepartmentChair());
    assertEquals(2700, deserializedMap.get("COMS").getNumberOfMajors());

    assertTrue(deserializedMap.containsKey("ECON"));
    assertEquals("Michael Woodford", deserializedMap.get("ECON").getDepartmentChair());
    assertEquals(2345, deserializedMap.get("ECON").getNumberOfMajors());
  }

  @Test
  public void setMappingTest() {
    // create a new mapping for test
    Course ieor2500 = new Course("Uday Menon", "627 MUDD", "11:40-12:55", 50);
    ieor2500.setEnrolledStudentCount(52);
    Course ieor3404 = new Course("Christopher J Dolan", "303 MUDD", "10:10-11:25", 73);
    ieor3404.setEnrolledStudentCount(80);

    HashMap<String, Course> courses = new HashMap<>();
    courses.put("2500", ieor2500);
    courses.put("3404", ieor3404);

    Department ieor = new Department("IEOR", courses, "Jay Sethuraman", 67);
    HashMap<String, Department> newMap = new HashMap<>();
    newMap.put("IEOR", ieor);

    testDatabase.setMapping(newMap);
    assertEquals(newMap, testDatabase.getDepartmentMapping());
  }


  public static MyFileDatabase testDatabase;
}
