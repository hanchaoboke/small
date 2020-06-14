package club.banyuan.mall.dao;

import club.banyuan.mall.common.model.UmsAdmin;
import club.banyuan.mall.dao.param.AdminQueryParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author HanChao
 * @date 2020-06-12 09:23
 * 描述信息：
 */
public interface UmsAdminDao {

    /**
     * 返回公共多少条数据
     */
    int count(AdminQueryParam param);

    /**
     * 查询指定范围的用户
     */
    List<UmsAdmin> findManyByParam(AdminQueryParam param);

    /**
     * 根据用户名查询用户
     */
    UmsAdmin fineOneByUsername(String username);

    /**
     * 更新用户密码
     */
    void updatePassword(@Param("id") Long id, @Param("password") String newPassword);
}
