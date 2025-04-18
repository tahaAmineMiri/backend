#!/bin/bash

create_class() {
  local path=$1
  local name=$(basename "$path" .java)
  local package_name=$(dirname "${path#src/main/java/}" | tr '/' '.')

  cat <<EOF > "$path"
package $package_name;

public class $name {

}
EOF
}

create_interface() {
  local path=$1
  local name=$(basename "$path" .java)
  local package_name=$(dirname "${path#src/main/java/}" | tr '/' '.')

  cat <<EOF > "$path"
package $package_name;

public interface $name {

}
EOF
}

create_enum() {
  local path=$1
  local name=$(basename "$path" .java)
  local package_name=$(dirname "${path#src/main/java/}" | tr '/' '.')

  cat <<EOF > "$path"
package $package_name;

public enum $name {
    // TODO: define values
}
EOF
}
