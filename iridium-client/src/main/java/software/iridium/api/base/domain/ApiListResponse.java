/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.api.base.domain;

import java.util.List;

public class ApiListResponse<T> extends ApiDataResponse<List<T>> {

  private static final long serialVersionUID = -1508644182734811250L;

  public ApiListResponse() {
    super();
  }

  public ApiListResponse(List<T> data) {
    super(data);
  }

  public ApiListResponse(List<T> data, String code, String message) {
    super(data, code, message);
  }
}
