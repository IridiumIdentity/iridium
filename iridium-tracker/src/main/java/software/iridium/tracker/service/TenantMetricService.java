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
package software.iridium.tracker.service;

import static com.google.common.base.Preconditions.checkArgument;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import software.iridium.api.tracker.AggregateProviderSummary;
import software.iridium.api.util.AttributeValidator;
import software.iridium.api.util.DateUtils;
import software.iridium.tracker.mapper.ProviderCountResponseMapper;
import software.iridium.tracker.repository.IdentityEntityRepository;

@Component
public class TenantMetricService {

  @Autowired private IdentityEntityRepository identityRepository;
  @Autowired private AttributeValidator validator;
  @Autowired private ProviderCountResponseMapper providerCountResponseMapper;

  @Value("${group-by.account.type.range}")
  private String groupByRange;

  @Transactional(propagation = Propagation.SUPPORTS)
  public List<AggregateProviderSummary> groupByAccountTypes(
      final String tenantId, final Date from, final HttpServletRequest request) {

    checkArgument(validator.isUuid(tenantId), "tenantId must be a valid uuid");
    checkArgument(validator.isNotNull(from), "from query parameter required.");
    checkArgument(validator.isBeforeCurrentDate(from), "from must be before current date");

    final var daysPrior = DateUtils.addDaysToCurrentTime(Integer.valueOf(groupByRange));

    if (from.before(daysPrior)) {
      throw new IllegalArgumentException("from parameter not in acceptable range");
    }

    return providerCountResponseMapper.map(
        identityRepository.findAllByParentTenantIdAndCreatedAfterAndActiveTrue(tenantId, from));
  }
}
