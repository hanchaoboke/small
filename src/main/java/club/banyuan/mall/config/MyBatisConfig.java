package club.banyuan.mall.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author HanChao
 * @date 2020-06-12 09:14
 * 描述信息：
 */
@Configuration
@MapperScan({"club.banyuan.mall.common.mapper","club.banyuan.mall.dao"})
public class MyBatisConfig {
}
