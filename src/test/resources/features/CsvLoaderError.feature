Feature: Test Non-existing file error
  Scenario: csv file for automatic loading has wrong name
    Then the app or tests will throw an error if the file does not exist
