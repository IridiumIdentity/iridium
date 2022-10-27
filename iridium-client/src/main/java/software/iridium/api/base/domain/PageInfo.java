package software.iridium.api.base.domain;

import java.io.Serializable;

public class PageInfo implements Serializable {


    private static final long serialVersionUID = 2807598297706283004L;

    private Integer page;

    private Integer pageSize;

    private Integer count;

    public PageInfo () {
        super();
        page = 1;
        pageSize = 20;
        count = 0;
    }

    public PageInfo (final Integer page, final Integer pageSize, final Integer count) {
        this.page = page;
        this.pageSize = pageSize;
        this.count = count;
    }

    public Integer getPage () {
        return page;
    }

    public void setPage (final Integer page) {
        this.page = page;
    }

    public Integer getPageSize () {
        return pageSize;
    }

    public void setPageSize (final Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getCount () {
        return count;
    }

    public void setCount (final Integer count) {
        this.count = count;
    }
}
