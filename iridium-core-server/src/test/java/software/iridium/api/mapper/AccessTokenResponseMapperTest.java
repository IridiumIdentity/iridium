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
package software.iridium.api.mapper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import software.iridium.entity.AccessTokenEntity;
import software.iridium.entity.RefreshTokenEntity;

class AccessTokenResponseMapperTest {

  private AccessTokenResponseMapper subject;

  @BeforeEach
  public void setUpForEachTestCase() {
    subject = new AccessTokenResponseMapper();
  }

  @Test
  public void map_AllGood_MapsAsExpected() {
    final var accessToken = "the access token";
    final var tokenType = "Bearer";
    final var refreshToken = "refresh_token";
    final var entity = new AccessTokenEntity();
    final var refreshTokenEntity = new RefreshTokenEntity();
    refreshTokenEntity.setRefreshToken(refreshToken);
    entity.setAccessToken(accessToken);
    entity.setTokenType(tokenType);
    entity.setRefreshToken(refreshTokenEntity);

    final var response = subject.map(entity);

    assertThat(response.getAccessToken(), is(equalTo(accessToken)));
    assertNull(response.getRedirectUrl());
    assertThat(response.getTokenType(), is(equalTo(tokenType)));
    assertThat(response.getRefreshToken(), is(equalTo(refreshToken)));
  }
}
