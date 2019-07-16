package com.bjtu.testmanageplatform.mapper;

import com.bjtu.testmanageplatform.model.StandardLibrary;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

/**
 * @Author: gaofeng
 * @Date: 2019-07-15
 * @Description:
 */
@Component
@Mapper
public interface StandardLibraryMapper {


    @Insert("INSERT INTO standard_library(standard_id, standard_rank, headline, " +
            "secondary_headline, name, headline_rank, secondary_headline_rank, name_rank, rank, " +
            "content, create_time) VALUES (#{standard_id}, #{standard_rank}, #{headline}, " +
            "#{secondary_headline}, #{name}, #{headline_rank}, #{secondary_headline_rank}, " +
            "#{name_rank}, #{rank}, #{content}, #{create_time})")
    Integer create(StandardLibrary standardLibrary);

    @Select("SELECT * FROM standard_library WHERE headline_rank=#{0} AND " +
            "secondary_headline_rank=#{1} AND name_rank=#{2}")
    StandardLibrary selectByRank(Integer headlineRank, Integer secondaryHeadlineRank,
                                 Integer nameRank);
}
