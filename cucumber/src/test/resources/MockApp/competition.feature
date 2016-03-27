#@competition @all
Feature: Test Competition

Background:
  When I logout
  Given I navigate to the home page
  When I press login
  When I try to login with 'valid' credentials
  When I press competition main button

Scenario: Open the competition page
	Then I should see the compotitions page

Scenario: Open the competition page again
	Then I should see the compotitions page
