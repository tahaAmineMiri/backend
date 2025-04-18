create_test_class() {
  local path=$1
  local name=$(basename "$path" .java)
  local package_name=$(dirname "${path#src/test/java/}" | tr '/' '.')

  cat <<EOF > "$path"
package $package_name;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class $name {

    @Test
    void sampleTest() {
        assertTrue(true);
    }
}
EOF
}
