Feature: features of gherkin test
  Scenario Outline: client makes call to GET hello
    When the client named '<name>' calls hello
    Then the client receives status code of 200
    And the client with name '<name>' receives server hello message
    Examples:
      | name |
      | Ninja |
      | Ronin |
      | Nemo |