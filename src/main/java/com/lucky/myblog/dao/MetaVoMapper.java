package com.lucky.myblog.dao;

import com.lucky.myblog.dto.MetaDto;
import com.lucky.myblog.model.vo.MetaVo;
import com.lucky.myblog.model.vo.MetaVoExample;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
public interface MetaVoMapper {
    int countByExample(MetaVoExample example);

    int deleteByExample(MetaVoExample example);

    int deleteByPrimaryKey(Integer mid);

    int insert(MetaVo record);

    int insertSelective(MetaVo record);

    List<MetaVo> selectByExample(MetaVoExample example);

    MetaVo selectByPrimaryKey(Integer mid);

    int updateByExampleSelective(@Param("record") MetaVo record, @Param("example") MetaVoExample example);

    int updateByExample(@Param("record") MetaVo record, @Param("example") MetaVoExample example);

    int updateByPrimaryKeySelective(MetaVo record);

    int updateByPrimaryKey(MetaVo record);

    List<MetaDto> selectFromSql(Map<String,Object> paraMap);

}