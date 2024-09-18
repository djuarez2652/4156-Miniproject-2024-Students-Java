package dev.coms4156.project.individualproject;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.HashMap;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;

/**
 * This class contains all the unit tests for the RouteController class.
 */
@SpringBootTest
@ContextConfiguration
public class RouteControllerUnitTests {

  @BeforeEach
  public void setup() {
    mock = MockitoAnnotations.openMocks(this);
    IndividualProjectApplication.overrideDatabase(mockDatabase);
    mockRouteController = new RouteController();
  }

  @AfterEach
  public void teardown() {
    if (mock != null) {
      try {
        mock.close();
      } catch (Exception e) {
        System.err.println("Error when closing mock");
      }
    }
  }


  @Test
  public void indexTest() {
    String expectedString = "Welcome, in order to make an API call direct your browser or Postman to an endpoint "
            + "\n\n This can be done using the following format: \n\n http:127.0.0"
            + ".1:8080/endpoint?arg=value";
    assertEquals(expectedString, mockRouteController.index());
  }

  @Test
  public void retrieveDepartmentThatExistsTest() {
    HashMap<String, Department> mockMapping = getMockDepartmentMapping();
    when(mockDatabase.getDepartmentMapping()).thenReturn(mockMapping);

    ResponseEntity<?> actualResponse = mockRouteController.retrieveDepartment("COMS");

    assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
    assertEquals(mockMapping.get("COMS").toString(), actualResponse.getBody());
  }

  @Test
  public void retrieveDepartmentThatDoesNotExistsTest() {
    String expectedBody = "Department Not Found";
    HashMap<String, Department> mockMapping = getMockDepartmentMapping();
    when(mockDatabase.getDepartmentMapping()).thenReturn(mockMapping);

    ResponseEntity<?> actualResponse = mockRouteController.retrieveDepartment("IEOR");

    assertEquals(HttpStatus.NOT_FOUND, actualResponse.getStatusCode());
    assertEquals(expectedBody, actualResponse.getBody());
  }

  @Test
  public void retrieveCourseThatExistsInDeptThatExistsTest() {
    HashMap<String, Department> mockMapping = getMockDepartmentMapping();
    HashMap<String, Course> mockCourseMapping = mockMapping.get("COMS").getCourseSelection();
    when(mockDatabase.getDepartmentMapping()).thenReturn(mockMapping);

    ResponseEntity<?> actualResponse = mockRouteController.retrieveCourse("COMS", 1000);


    assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
    assertEquals(mockCourseMapping.get("1000").toString(), actualResponse.getBody());
  }

  @Test
  public void retrieveCourseInDeptThatDoesNotExistsTest() {
    String expectedBody = "Department Not Found";
    HashMap<String, Department> mockMapping = getMockDepartmentMapping();
    HashMap<String, Course> mockCourseMapping = mockMapping.get("COMS").getCourseSelection();
    when(mockDatabase.getDepartmentMapping()).thenReturn(mockMapping);

    ResponseEntity<?> actualResponse = mockRouteController.retrieveCourse("IEOR", 1000);


    assertEquals(HttpStatus.NOT_FOUND, actualResponse.getStatusCode());
    assertEquals(expectedBody, actualResponse.getBody());
  }

  @Test
  public void retrieveCourseThatDoesNotExistInDeptThatExistsTest() {
    String expectedBody = "Course Not Found";
    HashMap<String, Department> mockMapping = getMockDepartmentMapping();
    when(mockDatabase.getDepartmentMapping()).thenReturn(mockMapping);

    ResponseEntity<?> actualResponse = mockRouteController.retrieveCourse("COMS", 2000);


    assertEquals(HttpStatus.NOT_FOUND, actualResponse.getStatusCode());
    assertEquals(expectedBody, actualResponse.getBody());
  }

  @Test
  public void isCourseFullWithCourseThatExistsAndNotFullTest() {
    boolean expectedResult = false;
    HashMap<String, Department> mockMapping = getMockDepartmentMapping();
    HashMap<String, Course> mockCourseMapping = mockMapping.get("COMS").getCourseSelection();
    mockCourseMapping.get("1000").setEnrolledStudentCount(1);

    when(mockDatabase.getDepartmentMapping()).thenReturn(mockMapping);

    ResponseEntity<?> actualResponse = mockRouteController.isCourseFull("COMS", 1000);

    assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
    assertEquals(expectedResult, actualResponse.getBody());
  }

  @Test
  public void isCourseFullWithCourseThatExistsAndIsFullTest() {
    boolean expectedResult = true;
    HashMap<String, Department> mockMapping = getMockDepartmentMapping();
    Course mockCourse = mockMapping.get("COMS").getCourseSelection().get("1000");
    mockCourse.setEnrolledStudentCount(400);

    when(mockDatabase.getDepartmentMapping()).thenReturn(mockMapping);

    ResponseEntity<?> actualResponse = mockRouteController.isCourseFull("COMS", 1000);

    assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
    assertEquals(expectedResult, actualResponse.getBody());
  }

  @Test
  public void isCourseFullWithCourseThatDoesNotExistTest() {
    String expectedResult = "Course Not Found";
    HashMap<String, Department> mockMapping = getMockDepartmentMapping();

    when(mockDatabase.getDepartmentMapping()).thenReturn(mockMapping);

    ResponseEntity<?> actualResponse = mockRouteController.isCourseFull("COMS", 2000);

    assertEquals(HttpStatus.NOT_FOUND, actualResponse.getStatusCode());
    assertEquals(expectedResult, actualResponse.getBody());
  }

  @Test
  public void getMajorCtFromDeptWithDeptThatExistsTest() {
    HashMap<String, Department> mockMapping = getMockDepartmentMapping();
    String expectedResult = "There are: " + mockMapping.get("COMS")
            .getNumberOfMajors() + " majors in the department";

    when(mockDatabase.getDepartmentMapping()).thenReturn(mockMapping);

    ResponseEntity<?> actualResponse = mockRouteController.getMajorCtFromDept("COMS");

    assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
    assertEquals(expectedResult, actualResponse.getBody());
  }

  @Test
  public void getMajorCtFromDeptWithDeptThatDoeNotExistTest() {
    String expectedResult = "Department Not Found";
    HashMap<String, Department> mockMapping = getMockDepartmentMapping();

    when(mockDatabase.getDepartmentMapping()).thenReturn(mockMapping);

    ResponseEntity<?> actualResponse = mockRouteController.getMajorCtFromDept("IEOR");

    assertEquals(HttpStatus.NOT_FOUND, actualResponse.getStatusCode());
    assertEquals(expectedResult, actualResponse.getBody());
  }

  @Test
  public void identifyDeptChairWithDeptThatExistsTest() {
    HashMap<String, Department> mockMapping = getMockDepartmentMapping();
    String expectedResult = mockMapping.get("COMS").getDepartmentChair() + " is "
            + "the department chair.";

    when(mockDatabase.getDepartmentMapping()).thenReturn(mockMapping);

    ResponseEntity<?> actualResponse = mockRouteController.identifyDeptChair("COMS");

    assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
    assertEquals(expectedResult, actualResponse.getBody());
  }

  @Test
  public void identifyDeptChairWithDeptThatDoesNotExistTest() {
    String expectedResult = "Department Not Found";
    HashMap<String, Department> mockMapping = getMockDepartmentMapping();

    when(mockDatabase.getDepartmentMapping()).thenReturn(mockMapping);

    ResponseEntity<?> actualResponse = mockRouteController.identifyDeptChair("IEOR");

    assertEquals(HttpStatus.NOT_FOUND, actualResponse.getStatusCode());
    assertEquals(expectedResult, actualResponse.getBody());
  }

  @Test
  public void findCourseLocationWithCourseThatExistsTest() {
    HashMap<String, Department> mockMapping = getMockDepartmentMapping();
    Course mockCourse = mockMapping.get("COMS").getCourseSelection().get("1000");
    String expectedResult = mockCourse.getCourseLocation() + " is where the course "
            + "is located.";

    when(mockDatabase.getDepartmentMapping()).thenReturn(mockMapping);

    ResponseEntity<?> actualResponse = mockRouteController.findCourseLocation("COMS", 1000);

    assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
    assertEquals(expectedResult, actualResponse.getBody());
  }

  @Test
  public void findCourseLocationWithCourseThatDoeNotExistTest() {
    String expectedResult = "Course Not Found";
    HashMap<String, Department> mockMapping = getMockDepartmentMapping();

    when(mockDatabase.getDepartmentMapping()).thenReturn(mockMapping);

    ResponseEntity<?> actualResponse = mockRouteController.findCourseLocation("COMS", 2000);

    assertEquals(HttpStatus.NOT_FOUND, actualResponse.getStatusCode());
    assertEquals(expectedResult, actualResponse.getBody());
  }

  @Test
  public void findCourseInstructorWithCourseThatExistsTest() {
    HashMap<String, Department> mockMapping = getMockDepartmentMapping();
    Course mockCourse = mockMapping.get("COMS").getCourseSelection().get("1000");
    String expectedResult = mockCourse.getInstructorName() + " is the instructor for"
            + " the course.";

    when(mockDatabase.getDepartmentMapping()).thenReturn(mockMapping);

    ResponseEntity<?> actualResponse = mockRouteController.findCourseInstructor("COMS", 1000);

    assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
    assertEquals(expectedResult, actualResponse.getBody());
  }

  @Test
  public void findCourseInstructorWithCourseThatDoesNotExistTest() {
    HashMap<String, Department> mockMapping = getMockDepartmentMapping();
    String expectedResult = "Course Not Found";

    when(mockDatabase.getDepartmentMapping()).thenReturn(mockMapping);

    ResponseEntity<?> actualResponse = mockRouteController.findCourseInstructor("COMS", 2000);

    assertEquals(HttpStatus.NOT_FOUND, actualResponse.getStatusCode());
    assertEquals(expectedResult, actualResponse.getBody());
  }

  @Test
  public void findCourseTimeWithCourseThatExistsTest() {
    HashMap<String, Department> mockMapping = getMockDepartmentMapping();
    Course mockCourse = mockMapping.get("COMS").getCourseSelection().get("1000");
    String expectedResult = "The course meets at: " + mockCourse.getCourseTimeSlot();

    when(mockDatabase.getDepartmentMapping()).thenReturn(mockMapping);

    ResponseEntity<?> actualResponse = mockRouteController.findCourseTime("COMS", 1000);

    assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
    assertEquals(expectedResult, actualResponse.getBody());
  }

  @Test
  public void findCourseTimeWithCourseThatDoeNotExistTest() {
    HashMap<String, Department> mockMapping = getMockDepartmentMapping();
    String expectedResult = "Course Not Found";

    when(mockDatabase.getDepartmentMapping()).thenReturn(mockMapping);

    ResponseEntity<?> actualResponse = mockRouteController.findCourseTime("COMS", 2000);

    assertEquals(HttpStatus.NOT_FOUND, actualResponse.getStatusCode());
    assertEquals(expectedResult, actualResponse.getBody());
  }

  private HashMap<String, Department> getMockDepartmentMapping() {
    Course mockCourse = new Course("John Doe", "417 IAB", "11:40-12:55", 400);
    mockCourse.setEnrolledStudentCount(100);
    HashMap<String, Course> mockCourses = new HashMap<>();
    mockCourses.put("1000", mockCourse);
    Department mockDept = new Department("COMS", mockCourses, "James Bond", 2700);
    HashMap<String, Department> mockMapping = new HashMap<>();
    mockMapping.put("COMS", mockDept);
    return mockMapping;
  }

  @InjectMocks
  private RouteController mockRouteController;

  @Mock
  private MyFileDatabase mockDatabase;

  private AutoCloseable mock;
}
