package club.banyuan.mall.service;

import club.banyuan.mall.common.dto.UmsAdminCreateParam;
import club.banyuan.mall.common.dto.UmsAdminListResponse;
import club.banyuan.mall.common.dto.UmsAdminUpdatePasswordParam;
import club.banyuan.mall.common.mapper.UmsAdminMapper;
import club.banyuan.mall.common.model.UmsAdmin;
import club.banyuan.mall.common.model.UmsAdminExample;
import club.banyuan.mall.common.model.UmsResource;
import club.banyuan.mall.common.util.JwtTokenUtil;
import club.banyuan.mall.config.security.AdminUserDetails;
import club.banyuan.mall.dao.UmsAdminDao;
import club.banyuan.mall.dao.param.AdminQueryParam;
import club.banyuan.mall.exception.CommonException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author HanChao
 * @date 2020-06-10 11:06
 * 描述信息：
 */
@Service
public class UmsAdminServiceImpl implements UmsAdminService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UmsAdminServiceImpl.class);

    @Autowired
    private UmsAdminMapper adminMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public UmsAdmin getAdminByUsername(String username) {
        UmsAdminExample example = new UmsAdminExample();
        example.createCriteria().andUsernameEqualTo(username);
        List<UmsAdmin> adminList = adminMapper.selectByExample(example);
        if (adminList != null && adminList.size() > 0) {
            return adminList.get(0);
        }
        return null;
    }

    @Override
    public String login(String username, String password) {
        String token = null;

        // 密码需要客户端加密后传递
        try {
            UserDetails userDetails = loadUserByUsername(username);
            if (!passwordEncoder.matches(password, userDetails.getPassword())) {
                throw new BadCredentialsException("密码不正确");
            }

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            token = jwtTokenUtil.generateToken(userDetails);

            // updateLoginTimeByUsername(username);
            // insertLoginLog(username);
        } catch (AuthenticationException e) {
            LOGGER.warn("登录异常:{}", e.getMessage());
        }
        return token;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        // 获取用户信息
        UmsAdmin admin = getAdminByUsername(username);
        if (admin != null) {
//            List<UmsResource> resourceList = getResourceList(admin.getId());
            List<UmsResource> resourceList = new ArrayList<>();
            UmsResource resource = new UmsResource();
            resource.setUrl("/hello2");
            resourceList.add(resource);
            return new AdminUserDetails(admin, resourceList);
        }
        throw new UsernameNotFoundException("用户名或密码错误");
    }

    @Override
    public void createUser(UmsAdminCreateParam param) throws CommonException {
        UmsAdmin umsAdmin = new UmsAdmin();
        BeanUtils.copyProperties(param, umsAdmin);
        umsAdmin.setCreateTime(new Date());
        umsAdmin.setStatus(true);

        // 查询是否有相同用户名的用户
        UmsAdminExample example = new UmsAdminExample();
        example.createCriteria().andUsernameEqualTo(umsAdmin.getUsername());
        List<UmsAdmin> umsAdminList = adminMapper.selectByExample(example);
        if (umsAdminList.size() > 0) {
            throw new CommonException("用户名已存在!");
        }

        // 将密码进行加密操作
        String encodePassword = passwordEncoder.encode(umsAdmin.getPassword());
        umsAdmin.setPassword(encodePassword);

        adminMapper.insert(umsAdmin);

        // push message queue
    }

    @Autowired
    private UmsAdminDao adminDao;

    @Override
    public UmsAdminListResponse list(AdminQueryParam param) {
        int limit = param.getPageSize();
        int offset = (param.getPageNum() - 1) * limit;
        param.setOffset(offset);

        UmsAdminListResponse response = new UmsAdminListResponse();
        response.setPageNum(param.getPageNum());
        response.setPageSize(param.getPageSize());

        int count = adminDao.count(param);
        int totalPages = (count % limit > 0) ? (count / limit + 1) : count / limit; // 三元表达式
        response.setTotal(count); // 总共多少条
        response.setTotalPage(totalPages); // 总共多少页

        List<UmsAdmin> admins = adminDao.findManyByParam(param);
        response.setList(admins);

        return response;
    }

    @Override
    public void updatePassword(UmsAdminUpdatePasswordParam param) throws CommonException {
        String username = param.getUsername();
        UmsAdmin admin = adminDao.fineOneByUsername(username);
        if (admin == null) {
            throw new CommonException("用户不存在");
        }
        if (passwordEncoder.matches(param.getNewPassword(), admin.getPassword())) {
            throw new CommonException("新密码不能与旧密码一样");
        }
        String newPasswordEncoded = passwordEncoder.encode(param.getNewPassword());
        adminDao.updatePassword(admin.getId(), newPasswordEncoded);
    }
}
