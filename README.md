README for Nimble Calendar
==========================

Nimble Calendar is a full calendar visualization for project activities.
Activities are collected from other 3rd party applications such as GitHub and Pivotal Tracker.

Each day will display avatars for the various people who are contributing to activities.

This project was bootstrapped with JHipster.

Running continuous End to End tests
-----------------------------------

1. Start the application
2. In a separate terminal run `grunt endToEndContinuousTest`

Running JUnit tests in IntelliJ
-------------------------------

In the VM options field enter '-ea -Dspring.profiles.active=dev', 
otherwise you will get errors "Your database connection pool configuration is incorrect"
