package dev.coms4156.project.individualproject;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

/**
 * This class contains all the unit tests for the Department class.
 */
@SpringBootTest
@ContextConfiguration
public class DepartmentUnitTests {

  /**
   * Runs before all tests which sets up the dummy data/objects used during testing.
   */
  @BeforeAll
  public static void setupDepartmentForTesting() {
    HashMap<String, Course> courses = new HashMap<>();
    Course coms1004 = new Course("Adam Cannon", "417 IAB", "11:40-12:55", 400);
    coms1004.setEnrolledStudentCount(249);
    Course coms3134 = new Course("Brian Borowski", "417 IAB", "11:40-12:55", 250);
    coms3134.setEnrolledStudentCount(242);
    courses.put("1004", coms1004);
    courses.put("3134", coms3134);
    String deptCode = "COMS";
    String departmentChair = "John Doe";
    int numberOfMajors = 2700;
    testDepartment = new Department(deptCode, courses, departmentChair, numberOfMajors);
  }


  @Test
  public void getNumberOfMajorsTest() {
    int expectedResult = 2700;
    assertEquals(expectedResult, testDepartment.getNumberOfMajors());
  }

  @Test
  public void getDepartmentChairTest() {
    String expectedResult = "John Doe";
    assertEquals(expectedResult, testDepartment.getDepartmentChair());
  }

  @Test
  public void addPersonToMajorTest() {
    int expectedResult = testDepartment.getNumberOfMajors() + 1;
    testDepartment.addPersonToMajor();
    assertEquals(expectedResult, testDepartment.getNumberOfMajors());
  }

  @Test
  public void dropPersonToMajorTest() {
    int expectedResult = testDepartment.getNumberOfMajors() - 1;
    testDepartment.dropPersonFromMajor();
    assertEquals(expectedResult, testDepartment.getNumberOfMajors());
  }

  @Test
  public void toStringTest() {
    String expectedResult = "COMS 1004: \n" +
            "Instructor: Adam Cannon; Location: 417 IAB; Time: 11:40-12:55\n" +
            "COMS 3134: \n" +
            "Instructor: Brian Borowski; Location: 417 IAB; Time: 11:40-12:55\n";
    assertEquals(expectedResult, testDepartment.toString());
  }



  /** The test department instance used for testing. */
  public static Department testDepartment;
}
