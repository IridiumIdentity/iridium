package software.iridium.email.api.repository;

import software.iridium.email.api.entity.EmailTemplateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailTemplateRepository extends JpaRepository<EmailTemplateEntity, String> {
    // intentionally left blank
}
