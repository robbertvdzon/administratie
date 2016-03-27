@login @all
Feature: Test Login

Scenario Outline: Login Success and Failure
	Given I navigate to the home page
    When I press login
	When I try to login with '<type>' credentials
	Then I should see that I logged in '<status>'
    Then I logout

Examples:
	| type		| status			|
	| valid		| successfully		|
	| invalid	| unsuccessfully	|


