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

  @BeforeAll
  public static void setUpBeforeClass() {
    HashMap<String, Course> courses = new HashMap<>();
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



  /* The test department instance used for testing. */
  public static Department testDepartment;
}
