package com.lucky.myblog.dao;

import com.lucky.myblog.model.vo.UserVo;
import com.lucky.myblog.model.vo.UserVoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
public interface UserVoMapper {
    int countByExample(UserVoExample example);

    int deleteByExample(UserVoExample example);

    int deleteByPrimaryKey(Integer uid);

    int insert(UserVo record);

    int insertSelective(UserVo record);

    List<UserVo> selectByExample(UserVoExample example);

    UserVo selectByPrimaryKey(Integer uid);

    int updateByExampleSelective(@Param("record") UserVo record, @Param("example") UserVoExample example);

    int updateByExample(@Param("record") UserVo record, @Param("example") UserVoExample example);

    int updateByPrimaryKeySelective(UserVo record);

    int updateByPrimaryKey(UserVo record);
}