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
package software.iridium.cli.generator;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.lang3.StringUtils;
import software.iridium.cli.util.EncoderUtils;
import software.iridium.entity.ApplicationEntity;
import software.iridium.entity.ApplicationTypeEntity;
import software.iridium.entity.TenantEntity;

public class ApplicationGenerator extends AbstractGenerator {

  public static final Integer CLIENT_ID_SEED_LENGTH = 16;

  public static ApplicationEntity generateIridiumApplication(
      final EntityManager entityManager,
      final TenantEntity iridiumTenant,
      final ApplicationTypeEntity applicationType,
      final ObjectMapper objectMapper,
      final String confPath)
      throws NoSuchAlgorithmException, IOException {
    final var encoderUtils = new EncoderUtils();

    beginTransaction(entityManager);

    final var iridiumManagementApp =
        objectMapper.readValue(
            new File(confPath + "management-application.yaml"), ApplicationEntity.class);
    iridiumManagementApp.setTenantId(iridiumTenant.getId());
    if (StringUtils.isBlank(iridiumManagementApp.getClientId())) {
      iridiumManagementApp.setClientId(encoderUtils.cryptoSecureToHex(CLIENT_ID_SEED_LENGTH));
    }
    iridiumManagementApp.setApplicationType(applicationType);
    entityManager.persist(iridiumManagementApp);
    flushAndCommitTransaction(entityManager);
    return iridiumManagementApp;
  }
}
