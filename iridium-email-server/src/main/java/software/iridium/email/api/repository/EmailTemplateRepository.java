/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.email.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import software.iridium.email.api.entity.EmailTemplateEntity;

public interface EmailTemplateRepository extends JpaRepository<EmailTemplateEntity, String> {
  // intentionally left blank
}
