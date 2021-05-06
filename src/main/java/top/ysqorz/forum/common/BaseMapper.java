package top.ysqorz.forum.common;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * 特别注意：该接口不能被扫描到，否则会出错
 *
 * Mapper接口：基本的增、删、改、查方法
 * MySqlMapper：针对MySQL的额外补充接口，支持批量插入
 * @author passerbyYSQ
 * @create 2020-10-31 21:27
 */
public interface BaseMapper<T> extends Mapper<T>, MySqlMapper<T> {

}
