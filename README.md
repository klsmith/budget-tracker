
# Project: budget-tracker
**Plan Version 1.0.0**

## Project Goal
To create an application that is able to keep track of a budget. It should be versatile enough to track from day to day, week to week, and month to month.

## Features

### P1 - Needs
* Navigable Web UI
	* Google Chrome Browser Support
* Track Spending:
	* Amount Spent
	* Date Spent
	* Category
* Track Income:
	* Date Earned
* Reporting Totals, base on any combination of...
	* Spending vs Income
	* Category
	* Date
* Track Budget Limits
	* by Category
	* by Date

### P2 - Wants
* Track Savings
* Track Recurring Spending/Income
	* Daily
	* Weekly
	* Monthly
	* Bi-Weekly
	* Etc...
* Automatic Spending/Income Projection
	* Must be able to override projection with corrections as needed.
* Scan Receipts
	* Link images of Receipts to specific expenses.
	
### P3 - Fluff
* Safari Browser Support
* Firefox Browser Support
* Internet Explorer / Edge Support
* Payment Reminders
	* For recurring expenses where the payee doesn't allow auto-pay.
* Multiple Bank Accounts
	* Track cross-account moves
* Automatic Receipt Import
	* Read Receipt images to generate expenses in system.

## Implemenation Considerations
* Server:
	* RESTful
	* MariaDB (database)
	* Java
		* spark-java (web-framework)
		* pebble (templating engine)
		* slf4j + log4j (logging)
		* sql2o (database interaction)
		* junit (unit/integration tests)
		* quicktheories (property based tests)
* Client:
	* The UI should be simple enough to avoid the use of complicated front-end frameworks (at least for version 1).
	* HTML
	* CSS (focus on grid templates)
	* Javascript (vanilla, minimal use)

* Tools:
	* Eclipse (IDE)
	* Maven (Dependency and Build Management)
	* Git (version control)

## Timeline
**1.0.0 Start:** Friday, 3/1/2019  
**1.0.0 Deadline:** Friday, 5/10/2019  

### Week 1 (3/1 - 3/8)
- [x] Create repository on github.
- [x] Setup permissions.
- [x] Create issues for features.
- [x] Create project skeleton.
    - [x] maven configured
    - [x] spark-java configured
    - [x] logging configured
    - [x] database configured

### Week 2 (3/8 - 3/15)
- [ ] Track Spending in Database:
	- [ ] Amount Spent
	- [ ] Date Spent
	- [ ] Category
- [ ] Unit Tests
- [ ] Integration Tests

### Week 3 (3/15 - 3/22)
- [ ] Track Income In Database:
	- [ ] Date Earned
- [ ] Track Budget Limits
	- [ ] by Category
	- [ ] by Date
- [ ] Unit Tests
- [ ] Integration Tests

### Week 4 (3/22 - 3/29)
- [ ] Navigable Web UI
	- [ ] Google Chrome Browser Support
	- [ ] Covers all previously implemented features.

### Week 5 (3/29 - 4/5)
- [ ] Reporting Totals, base on any combination of...
	- [ ] Spending vs Income
	- [ ] Category
	- [ ] Date
- [ ] Unit Tests
- [ ] Integration Tests
- [ ] UI Updates

### Week 6 (4/5 - 4/12)
- [ ] Track Recurring Spending/Income
	- [ ] Daily
	- [ ] Weekly
	- [ ] Monthly
	- [ ] Bi-Weekly
	- [ ] Etc...
- [ ] Unit Tests
- [ ] Integration Tests
- [ ] UI Updates

### Week 7 (4/12 - 4/19)
- [ ] Automatic Spending/Income Projection
	- [ ] Must be able to override projection with corrections as needed.
- [ ] Unit Tests
- [ ] Integration Tests
- [ ] UI Updates

### Week 8 (4/19 - 4/26)
- [ ] Track Savings
- [ ] Scan Receipts
	- [ ] Link images of Receipts to specific expenses
- [ ] Unit Tests
- [ ] Integration Tests
- [ ] UI Updates
	
### Week 9 (4/26 - 5/3)
- [ ] Automated Functional Browser Tests
- [ ] Optional Work
	- [ ] Payment Reminders
		- [ ] For recurring expenses where the payee doesn't allow auto-pay.
	- [ ] Multiple Bank Accounts
		- [ ] Track cross-account moves
	- [ ] Automatic Receipt Import
		- [ ] Read Receipt images to generate expenses in system.
	- [ ] Unit Tests
	- [ ] Integration Tests
	- [ ] UI Updates

### Week 10 (5/3 - 5/10)
- [ ] Write Postmortem
- [ ] Optional Work:
	- [ ] Safari Browser Support
	- [ ] Firefox Browser Support
	- [ ] Internet Explorer / Edge Support
	- [ ] Unit Tests
	- [ ] Integration Tests
	- [ ] UI Updates`
