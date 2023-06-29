/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package software.iridium.cli.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.File;
import java.io.IOException;

public class YamlParser {

  private static final ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());

  private static final String configurationPath = PathUtils.getJarPath();

  public static <T> T readValue(String fileName, Class<T> valueType) {
    try {
      return objectMapper.readValue(new File(configurationPath + fileName), valueType);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static <T> T readValue(String fileName, TypeReference<T> valueTypeRef) {
    try {
      return objectMapper.readValue(new File(configurationPath + fileName), valueTypeRef);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
