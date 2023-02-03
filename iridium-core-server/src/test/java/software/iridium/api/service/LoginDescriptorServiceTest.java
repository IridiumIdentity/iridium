/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
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
