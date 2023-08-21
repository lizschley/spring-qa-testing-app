@integration
Feature: Test Person and Quotes Integration
  Scenario: client makes call to GET existing quotes
    When the client calls GET existing quotes
    Then the quote client receives status code of 200
    And the client gets a sorted array of four people and four quotes

  Scenario: system does a uniqueness check before a new user is saved
    When json string input having a new id with existing name and dates
    Then the database will throw data integrity violation exception
    And the transaction will roll back and the new record will not be created

  Scenario: the add new person with quotes endpoint allows the system to create a person and their quotes
    When the api input is turned into a java class
    Then the api call will show a successful return code
    And api return body will show the correct number of quotes and the correct data

  Scenario: the quotes endpoint allows the system to find an existing person and add new quotes
    When the input is turned into a java class
    Then api will be successful and return body will show the correct number of quotes

  Scenario: find a person by id and person is found
    When a person is found by id and the return code is 200
    Then a person and their quotes are returned

  Scenario: find a person by id and person is not found
    Then a person is not found by id and the return code is 404

  Scenario: find a person by name returns the an array with the correct person
    When a person is found by name
    Then  the person is returned in an array with a successful return code

  Scenario: find a person by name with wrong name returns an empty array
    When a person is found by name when the name is not in the database
    Then the result is an empty array along with a successful return code


