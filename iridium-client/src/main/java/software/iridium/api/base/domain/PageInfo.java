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

public class PageInfo implements Serializable {

  private static final long serialVersionUID = 2807598297706283004L;

  private Integer page;

  private Integer pageSize;

  private Integer count;

  public PageInfo() {
    super();
    page = 1;
    pageSize = 20;
    count = 0;
  }

  public PageInfo(final Integer page, final Integer pageSize, final Integer count) {
    this.page = page;
    this.pageSize = pageSize;
    this.count = count;
  }

  public Integer getPage() {
    return page;
  }

  public void setPage(final Integer page) {
    this.page = page;
  }

  public Integer getPageSize() {
    return pageSize;
  }

  public void setPageSize(final Integer pageSize) {
    this.pageSize = pageSize;
  }

  public Integer getCount() {
    return count;
  }

  public void setCount(final Integer count) {
    this.count = count;
  }
}
