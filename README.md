# Cycle-Appointment

[![Java CI with Maven](https://github.com/perkashlal/Cycle-Appointment/actions/workflows/ci.yml/badge.svg)](https://github.com/perkashlal/Cycle-Appointment/actions/workflows/ci.yml)
[![Coverage Status](https://coveralls.io/repos/github/perkashlal/Cycle-Appointment/badge.svg?branch=main)](https://coveralls.io/github/perkashlal/Cycle-Appointment?branch=main)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=perkashlal_Cycle-Appointment&metric=alert_status)](https://sonarcloud.io/project/overview?id=perkashlal_Cycle-Appointment)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=perkashlal_Cycle-Appointment&metric=vulnerabilities)](https://sonarcloud.io/component_measures?metric=vulnerabilities&id=perkashlal_Cycle-Appointment)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=perkashlal_Cycle-Appointment&metric=bugs)](https://sonarcloud.io/component_measures?metric=bugs&id=perkashlal_Cycle-Appointment)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=perkashlal_Cycle-Appointment&metric=code_smells)](https://sonarcloud.io/component_measures?metric=code_smells&id=perkashlal_Cycle-Appointment)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=perkashlal_Cycle-Appointment&metric=coverage)](https://sonarcloud.io/component_measures?metric=coverage&id=perkashlal_Cycle-Appointment)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=perkashlal_Cycle-Appointment&metric=duplicated_lines_density)](https://sonarcloud.io/component_measures?metric=duplicated_lines_density&id=perkashlal_Cycle-Appointment)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=perkashlal_Cycle-Appointment&metric=sqale_index)](https://sonarcloud.io/component_measures?metric=sqale_index&id=perkashlal_Cycle-Appointment)

Cycle Appointment is a Java Swing desktop application for managing cycle repair appointments. It is designed for a small repair shop that needs to store and manage appointment details such as the customer name, cycle model, repair issue, and appointment date.

The application lets the user view appointments, add new appointments, delete existing appointments, and show validation errors when required fields are missing. The user interface is implemented with Swing, while the application logic is separated into controller, model, repository, and view layers.

MongoDB is used as the persistence layer for storing appointments. During automated tests, Testcontainers starts a temporary MongoDB container, so integration and end-to-end tests can run without requiring a manually configured database.

The project is built with Maven and follows a Test-Driven Development workflow. It includes unit tests, Swing UI tests, MongoDB repository tests, integration tests, and end-to-end tests. Code quality is checked with JaCoCo coverage, PIT mutation testing, GitHub Actions CI, Coveralls, and SonarCloud analysis.
