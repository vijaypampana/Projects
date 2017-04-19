Feature: Bank Account Login Page

  Scenario Outline: User navigates to applicant page
  Given the user navigates to <url2> page
  When the tester type <firstname> in firstname field
    And the tester type <lastname> in lastname field
    And the tester type <addrline1> in addrline1 field
    And the tester type <city> in city field
    And the tester type <state> in state field
    And the tester type <zipcode> in zipcode field
    And the tester click on next button
  Then the tester navigates to income page
  Examples:
  |url2|firstname|lastname|addrline1|city|state|zipcode|
  |http://localhost:8080/March09/web/applicant|vijay|pampana|Downey Drive|Manchester|CT|06040|
