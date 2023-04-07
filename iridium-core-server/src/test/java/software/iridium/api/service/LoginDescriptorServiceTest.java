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

package software.iridium.api.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import software.iridium.api.base.error.ResourceNotFoundException;
import software.iridium.api.entity.LoginDescriptorEntity;
import software.iridium.api.mapper.LoginDescriptorResponseMapper;
import software.iridium.api.repository.LoginDescriptorEntityRepository;
import software.iridium.api.util.AttributeValidator;

@ExtendWith(MockitoExtension.class)
class LoginDescriptorServiceTest {

  @Mock private LoginDescriptorEntityRepository mockLoginDescriptorRepository;
  @Mock private LoginDescriptorResponseMapper mockResponseMapper;
  @Mock private AttributeValidator mockAttributeValidator;
  @InjectMocks private LoginDescriptorService subject;

  @AfterEach
  public void ensureNoUnexpectedMockInteractions() {
    Mockito.verifyNoMoreInteractions(
        mockLoginDescriptorRepository, mockResponseMapper, mockAttributeValidator);
  }

  @Test
  public void getBySubdomain_AllGood_BehavesAsExpected() {
    final var subdomain = "subdomain";
    final var loginDescriptor = new LoginDescriptorEntity();

    when(mockAttributeValidator.isValidSubdomain(same(subdomain))).thenReturn(true);
    when(mockLoginDescriptorRepository.findByTenant_Subdomain(same(subdomain)))
        .thenReturn(Optional.of(loginDescriptor));

    subject.getBySubdomain(subdomain);

    verify(mockAttributeValidator).isValidSubdomain(same(subdomain));
    verify(mockLoginDescriptorRepository).findByTenant_Subdomain(same(subdomain));
    verify(mockResponseMapper).map(same(loginDescriptor));
  }

  @Test
  public void getBySubdomain_LoginDescriptorNotFound_ExceptionThrown() {
    final var subdomain = "subdomain";

    when(mockAttributeValidator.isValidSubdomain(same(subdomain))).thenReturn(true);
    when(mockLoginDescriptorRepository.findByTenant_Subdomain(same(subdomain)))
        .thenReturn(Optional.empty());

    final var exception =
        assertThrows(ResourceNotFoundException.class, () -> subject.getBySubdomain(subdomain));

    verify(mockAttributeValidator).isValidSubdomain(same(subdomain));
    verify(mockLoginDescriptorRepository).findByTenant_Subdomain(same(subdomain));
    assertThat(
        exception.getMessage(),
        is(equalTo("login descriptor not found for subdomain: " + subdomain)));
  }
}
