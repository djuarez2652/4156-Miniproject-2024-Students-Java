package dev.coms4156.project.individualproject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.lang.reflect.Field;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

/**
 * This class contains all the unit tests for the IndividualProjectApplication class.
 */
@SpringBootTest
@ContextConfiguration
public class IndividualProjectApplicationUnitTests {

  /**
   * Runs before each test which sets up the mock needed for tests.
   */
  @BeforeEach
  public void setup() {
    mock = MockitoAnnotations.openMocks(this);
    IndividualProjectApplication.overrideDatabase(mockDatabase);
    app = new IndividualProjectApplication();
  }

  /**
   * Runs after each test is done which tears down mocks safely.
   */
  @AfterEach
  public void teardown() {
    if (mock != null) {
      try {
        mock.close();
      } catch (Exception e) {
        System.err.println("Error closing mock");
      }
    }
  }


  @Test
  public void runWithSetupTest() {
    String[] args = {"setup"};
    app.run(args);
    assertNotNull(IndividualProjectApplication.myFileDatabase);
  }

  @Test
  public void runEmptyTest() {
    String[] args = {""};
    app.run(args);
    assertNotNull(IndividualProjectApplication.myFileDatabase);
  }

  @Test
  public void overrideDatabaseTest() {
    Boolean saveData;
    saveData = getSaveData();
    if (saveData == null) {
      fail("Error in getting saveData value");
    }

    boolean expectedSaveData = false;
    IndividualProjectApplication.overrideDatabase(mockDatabase);

    assertEquals(mockDatabase, IndividualProjectApplication.myFileDatabase);
    assertEquals(expectedSaveData, saveData);
  }

  @Test
  public void onTerminationSaveDataFalseTest() {
    Boolean prevSaveData = getSaveData();
    if (prevSaveData == null) {
      fail("Error in getting saveData value");
    }

    setPrivateStaticField("saveData", false);
    app.onTermination();

    verify(mockDatabase, times(0)).saveContentsToFile();
    setPrivateStaticField("saveData", prevSaveData);
  }

  @Test
  public void onTerminationSaveDataTrueTest() {
    Boolean prevSaveData = getSaveData();
    if (prevSaveData == null) {
      fail("Error in getting saveData value");
    }

    setPrivateStaticField("saveData", true);
    app.onTermination();

    verify(mockDatabase, times(1)).saveContentsToFile();
    setPrivateStaticField("saveData", prevSaveData);
  }

  @Test
  public void resetDataFileTest() {
    app.resetDataFile();
    verify(IndividualProjectApplication.myFileDatabase, times(1)).setMapping(any());
  }

  /**
   * Attempts to retrieve the private static field saveData.
   *
   * @return      A {@code Boolean} of the private static field saveData.
   */
  private Boolean getSaveData() {
    Field saveDataField = null;
    try {
      saveDataField = IndividualProjectApplication.class.getDeclaredField("saveData");
      saveDataField.setAccessible(true);
      Boolean value = (Boolean) saveDataField.get(null);
      saveDataField.setAccessible(false);
      return value;
    } catch (Exception e) {
      if (saveDataField != null) {
        saveDataField.setAccessible(false);
      }
      return null;
    }
  }

  /**
   * Attempts to set a private static field of the IndividualProjectApplication
   * class by using reflection.
   *
   * @param name      A {@code String} representing the name of the private
   *                  static field.
   *
   * @param val       A {@code Object} representing the new value to be set.
   */
  private void setPrivateStaticField(String name, Object val) {
    try {
      Field field = IndividualProjectApplication.class.getDeclaredField(name);
      field.setAccessible(true);
      field.set(null, val);
    } catch (Exception e) {
      fail("Error setting field " + name);
    }
  }

  /** The test application instance with injected mocks used for testing. */
  @InjectMocks
  private IndividualProjectApplication app;

  /** The mock database instance used for testing. */
  @Mock
  private MyFileDatabase mockDatabase;

  /** The AutoClosable instance used for managing mock lifecycle. */
  private AutoCloseable mock;
}