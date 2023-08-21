@personServiceUnit
Feature: Person Service Unit Tests
  Scenario: call to get all the people and their quotes
    When user makes a call to get all people
    Then service will return a list of three people
    And service will return quotes along with the specified people

  Scenario: call to get all people when there aren't any
    When user makes a call to get all people when the person table is empty
    Then service will return an empty list

  Scenario: code will prevent creating a person with duplicate data
    When a person is not found by id, but the name and date find a match
    Then an exception will be thrown even without a call to the database

  Scenario: a person found by id but having different name or dates will fail
    When a person is found by id but either the name fields or dates field does not match the existing record
    Then an illegal state exception will be thrown

  Scenario: name date checker will not throw exception if person data integrity is maintained
    When a Person was found or created and there is no data conflict
    Then no exception will be thrown

  Scenario: person data checker will not throw exception if only person id and quotes are passed in
    When no person specific data other that id is passed in
    Then exception will never be thrown by the person data checker

  Scenario: createOrAddPerson method will add a person if the passed in person is not found
    When find or create person is called with mocked data that does not find a person
    Then the create person method will be called
