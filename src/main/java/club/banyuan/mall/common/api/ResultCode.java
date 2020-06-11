package club.banyuan.mall.common.api;

/**
 * @author HanChao
 * @date 2020-06-10 19:20
 * 描述信息：
 */
/**
 * 枚举了一些常用API操作码
 * @author edz
 */
public enum ResultCode implements IErrorCode {
    SUCCESS(200, "操作成功"),
    FAILED(500, "操作失败"),
    VALIDATE_FAILED(404, "参数检验失败"),
    BUSINESS_FAILED(405, "业务处理失败"),
    UNAUTHORIZED(401, "暂未登录或token已经过期"),
    FORBIDDEN(403, "没有相关权限");

    private long code;

    private String message;

    private ResultCode(long code, String message) {
        this.code = code;
        this.message = message;
    }

    public long getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}