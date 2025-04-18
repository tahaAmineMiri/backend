#!/bin/bash

BASE_TEST_DIR="src/test/java/com/incon/backend"

# Import test boilerplate function
source ./boilerplate_test.sh

# === Create Test Files ===

# controller/
create_test_class "$BASE_TEST_DIR/controller/AuthControllerTest.java"
create_test_class "$BASE_TEST_DIR/controller/CartControllerTest.java"
create_test_class "$BASE_TEST_DIR/controller/ProductControllerTest.java"

# service/
create_test_class "$BASE_TEST_DIR/service/AuthServiceTest.java"
create_test_class "$BASE_TEST_DIR/service/CartServiceTest.java"
create_test_class "$BASE_TEST_DIR/service/OrderServiceTest.java"

# repository/
create_test_class "$BASE_TEST_DIR/repository/UserRepositoryTest.java"

# util/
create_test_class "$BASE_TEST_DIR/util/FileUploadUtilTest.java"
create_test_class "$BASE_TEST_DIR/util/ValidationUtilTest.java"

echo "✅ Test files and directories created."
