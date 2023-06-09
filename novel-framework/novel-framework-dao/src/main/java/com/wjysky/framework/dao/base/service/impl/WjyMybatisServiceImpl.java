package com.wjysky.framework.dao.base.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wjysky.framework.dao.base.mapper.WjyMybatisMapper;
import com.wjysky.framework.dao.base.service.IWjyMybatisService;

/**
 * Mybatis服务实现类
 *
 * @param <M>
 * @param <T>
 * @author duanzhijun24470@talkweb.com.cn
 * @date 2022/04/28
 * @apiNote
 */
public class WjyMybatisServiceImpl<M extends WjyMybatisMapper<T>, T> extends ServiceImpl<M, T> implements IWjyMybatisService<T> {

}
