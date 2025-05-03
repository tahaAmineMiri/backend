#!/bin/bash

# Run all integration tests
./mvnw clean verify -P integration-test

# Run a specific test class
# mvn clean verify -P integration-test -Dfailsafe.tests=com.incon.backend.integration.controller.BuyerControllerIntegrationTest

# Open test coverage report
# open target/site/jacoco/index.html