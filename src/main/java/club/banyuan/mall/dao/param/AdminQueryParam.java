package club.banyuan.mall.dao.param;

/**
 * @author HanChao
 * @date 2020-06-12 09:23
 * 描述信息：
 */
public class AdminQueryParam {

    private String keyword;
    private Integer pageNum;
    private Integer pageSize;
    private Integer offset;

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
