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
package software.iridium.api.filter;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import software.iridium.api.base.error.NotAuthorizedException;

@ExtendWith(MockitoExtension.class)
class ExceptionHandlerFilterTest {

  @Mock private FilterChain mockFilterChain;
  @Mock private HttpServletRequest mockServletRequest;
  @Mock private HttpServletResponse mockServletResponse;
  @Mock private ObjectMapper mockObjectMapper;
  @InjectMocks private ExceptionHandlerFilter subject;

  @Test
  public void verifyDoFilterInternal_whenAcceptHeaderContainsJson()
      throws ServletException, IOException {
    final var mockPrintWriter = mock(PrintWriter.class);
    when(mockServletRequest.getHeader(HttpHeaders.ACCEPT)).thenReturn("json");
    doThrow(new NotAuthorizedException())
        .when(mockFilterChain)
        .doFilter(mockServletRequest, mockServletResponse);
    when(mockServletResponse.getWriter()).thenReturn(mockPrintWriter);
    when(mockObjectMapper.writeValueAsString(any())).thenReturn("api response");

    subject.doFilterInternal(mockServletRequest, mockServletResponse, mockFilterChain);

    verify(mockPrintWriter).write("api response");
  }

  @Test
  public void verifyDoFilterInternal_whenAcceptHeaderNotContainsJson()
      throws ServletException, IOException {
    final var mockPrintWriter = mock(PrintWriter.class);
    when(mockServletRequest.getHeader(HttpHeaders.ACCEPT)).thenReturn("some/header");
    doThrow(new NotAuthorizedException())
        .when(mockFilterChain)
        .doFilter(mockServletRequest, mockServletResponse);
    when(mockServletResponse.getWriter()).thenReturn(mockPrintWriter);
    when(mockObjectMapper.writeValueAsString(any())).thenReturn("expectedResponse");

    subject.doFilterInternal(mockServletRequest, mockServletResponse, mockFilterChain);

    verify(mockPrintWriter).write("expectedResponse");
  }

  @Test
  public void verifyDoFilterInternal_whenNoException() throws ServletException, IOException {

    subject.doFilterInternal(mockServletRequest, mockServletResponse, mockFilterChain);

    verifyNoInteractions(mockServletRequest, mockServletResponse);
  }
}
