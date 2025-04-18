#!/bin/bash

# Define the base directory
BASE_DIR="src/main/java/com/incon/backend"

# Define the mapper directory path
MAPPER_DIR="$BASE_DIR/mapper"

# Create the mapper directory
mkdir -p "$MAPPER_DIR"

# List of mapper class names
classes=(
  "CartMapper"
  "CompanyMapper"
  "OrderMapper"
  "PaymentMapper"
  "ProductMapper"
  "RFQMapper"
  "ReviewMapper"
  "SubscriptionMapper"
  "UserMapper"
)

# Create each file with class boilerplate
for class in "${classes[@]}"; do
  cat > "$MAPPER_DIR/${class}.java" <<EOF
package com.incon.backend.mapper;

import org.springframework.stereotype.Component;

@Component
public class ${class} {

    // TODO: Add mapping methods here

}
EOF
done

echo "Mapper classes created at $MAPPER_DIR."
