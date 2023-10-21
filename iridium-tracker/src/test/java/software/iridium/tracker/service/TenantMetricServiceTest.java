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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import software.iridium.api.util.AttributeValidator;
import software.iridium.entity.IdentityEntity;
import software.iridium.tracker.mapper.ProviderCountResponseMapper;
import software.iridium.tracker.repository.IdentityEntityRepository;

@ExtendWith(MockitoExtension.class)
class TenantMetricServiceTest {

  @Mock private IdentityEntityRepository mockIdentityRepository;
  @Mock private AttributeValidator mockAttributeValidator;
  @Mock private ProviderCountResponseMapper mockResponseMapper;
  @Mock private HttpServletRequest mockServletRequest;

  @InjectMocks private TenantMetricService subject;

  @AfterEach
  public void ensureNoUnexpectedMockInteractions() {
    Mockito.verifyNoMoreInteractions(
        mockIdentityRepository, mockAttributeValidator, mockResponseMapper, mockServletRequest);
  }

  @Test
  public void groupByAccountTypes_AllGood_BehavesAsExpected() {
    final var tenantId = "the tenant id";
    final var from = new Date();
    List<IdentityEntity> identities = new ArrayList<>();

    ReflectionTestUtils.setField(subject, "groupByRange", "-1");

    when(mockAttributeValidator.isUuid(same(tenantId))).thenReturn(true);
    when(mockAttributeValidator.isNotNull(same(from))).thenReturn(true);
    when(mockAttributeValidator.isBeforeCurrentDate(same(from))).thenReturn(true);
    when(mockIdentityRepository.findAllByParentTenantIdAndCreatedAfterAndActiveTrue(
            same(tenantId), same(from)))
        .thenReturn(identities);

    subject.groupByAccountTypes(tenantId, from, mockServletRequest);

    verify(mockAttributeValidator).isUuid(same(tenantId));
    verify(mockAttributeValidator).isNotNull(same(from));
    verify(mockAttributeValidator).isBeforeCurrentDate(same(from));
    verify(mockIdentityRepository)
        .findAllByParentTenantIdAndCreatedAfterAndActiveTrue(same(tenantId), same(from));
    verify(mockResponseMapper).map(same(identities));
  }

  @Test
  public void groupByAccountTypes_DateOutsideOfRange_ExceptionThrown() {
    final var tenantId = "the tenant id";
    Calendar cal = Calendar.getInstance();
    cal.setTime(new Date());
    cal.add(Calendar.DATE, -2);
    final var from = cal.getTime();
    List<IdentityEntity> identities = new ArrayList<>();

    ReflectionTestUtils.setField(subject, "groupByRange", "-1");

    when(mockAttributeValidator.isUuid(same(tenantId))).thenReturn(true);
    when(mockAttributeValidator.isNotNull(same(from))).thenReturn(true);
    when(mockAttributeValidator.isBeforeCurrentDate(same(from))).thenReturn(true);

    final var exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> subject.groupByAccountTypes(tenantId, from, mockServletRequest));

    assertThat(exception.getMessage(), is(equalTo("from parameter not in acceptable range")));

    verify(mockAttributeValidator).isUuid(same(tenantId));
    verify(mockAttributeValidator).isNotNull(same(from));
    verify(mockAttributeValidator).isBeforeCurrentDate(same(from));
    verify(mockIdentityRepository, never())
        .findAllByParentTenantIdAndCreatedAfterAndActiveTrue(same(tenantId), same(from));
    verify(mockResponseMapper, never()).map(same(identities));
  }
}
