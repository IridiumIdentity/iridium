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

import java.net.URISyntaxException;
import software.iridium.cli.command.InitCommand;

public class PathUtils {

  public static String getJarPath() {
    String jarPath;
    try {
      jarPath =
          InitCommand.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
      return jarPath.substring(0, jarPath.lastIndexOf("/")) + "/../conf/";
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }
}
