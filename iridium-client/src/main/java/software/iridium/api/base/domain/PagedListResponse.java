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

    public PagedListResponse(final List<T> data, final Integer count, final Integer page, final Integer pageSize) {
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
