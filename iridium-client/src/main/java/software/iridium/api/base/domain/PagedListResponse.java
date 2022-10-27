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
package software.iridium.api.base.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PagedListResponse<T> implements Serializable {

  private static final long serialVersionUID = -6968586227517094581L;

  private PageInfo pageInfo;
  private List<T> data;

  public PagedListResponse() {
    super();
    this.pageInfo = new PageInfo();
    this.data = new ArrayList<>();
  }

  public PagedListResponse(final Integer count, final Integer page, final Integer pageSize) {
    this();
    this.pageInfo = new PageInfo(page, pageSize, count);
  }

  public PagedListResponse(
      final List<T> data, final Integer count, final Integer page, final Integer pageSize) {
    this();
    this.data.addAll(data);
    this.pageInfo = new PageInfo(page, pageSize, count);
  }

  public List<T> getData() {
    return data;
  }

  public void setData(final List<T> data) {
    this.data = data;
  }

  public PageInfo getPageInfo() {
    return pageInfo;
  }

  public void setPageInfo(final PageInfo pageInfo) {
    this.pageInfo = pageInfo;
  }
}
