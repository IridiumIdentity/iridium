/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.api.base.domain;

public class ApiDataResponse<T> extends ApiResponse {

  private static final long serialVersionUID = -4047733646237443154L;
  private T data;

  public ApiDataResponse() {
    super("200", "OK");
    this.data = null;
  }

  public ApiDataResponse(T data) {
    super("200", "OK");
    this.data = data;
  }

  public ApiDataResponse(T data, String code, String message) {
    super(code, message);
    this.data = data;
  }

  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }
}
