Feature: Bank Account Login Page

  Scenario Outline: User Valid Login
  Given the user navigates to <url1> page
  When the tester type <username> in username field
    And the tester type <password> in password field
    And the tester click on login button
  Then the tester navigates to application page
  Examples:
  |url1|username|password|
  |http://localhost:8080/March09/web/login|vijay|pampana|

  Scenario Outline: User trying to Login with no password
    Given the user navigates to <url1> page
    When the tester type <username> in username field
    And the tester click on login button
    Then the password error message is thrown on the login page
    Examples:
      |url1|username|
      |http://localhost:8080/March09/web/login|vijay|