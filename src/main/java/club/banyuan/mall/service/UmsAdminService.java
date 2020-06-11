package club.banyuan.mall.service;

import club.banyuan.mall.common.dto.UmsAdminCreateParam;
import club.banyuan.mall.common.model.UmsAdmin;
import club.banyuan.mall.exception.CommonException;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author HanChao
 * @date 2020-06-10 11:05
 * 描述信息：
 */
public interface UmsAdminService {

    /**
     * 根据用户名获取后台管理员
     */
    UmsAdmin getAdminByUsername(String username);

    String login(String username, String password);

    UserDetails loadUserByUsername(String username);

    /**
     *
     * 创建用户
     */
    void createUser(UmsAdminCreateParam param) throws CommonException;
}
