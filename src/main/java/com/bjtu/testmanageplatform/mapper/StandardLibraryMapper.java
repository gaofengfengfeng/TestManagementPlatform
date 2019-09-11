package com.bjtu.testmanageplatform.mapper;

import com.bjtu.testmanageplatform.model.StandardLibrary;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: gaofeng
 * @Date: 2019-07-15
 * @Description:
 */
@Component
@Mapper
public interface StandardLibraryMapper {


    @Insert("INSERT INTO standard_library(standard_id, standard_rank, headline, " +
            "secondary_headline, third_headline, name, headline_rank, secondary_headline_rank, third_headline_rank, name_rank, rank," +
            "content, create_time) VALUES (#{standard_id}, #{standard_rank}, #{headline}, " +
            "#{secondary_headline}, #{third_headline}, #{name}, #{headline_rank}, #{secondary_headline_rank}, " +
            "#{third_headline_rank}, #{name_rank}, #{rank}, #{content}, #{create_time})")
    Integer create(StandardLibrary standardLibrary);

    @Select("SELECT * FROM standard_library WHERE headline_rank=#{0} AND " +
            "secondary_headline_rank=#{1} AND third_headline_rank=#{2} AND name_rank=#{3}")
    StandardLibrary selectByRank(Integer headlineRank, Integer secondaryHeadlineRank,
                                 String thirdHeadlineRank, Integer nameRank);


    @Select("SELECT * FROM standard_library WHERE standard_rank=3 AND headline_rank=#{0} AND " +
            "secondary_headline_rank=#{1} ORDER BY headline_rank, secondary_headline_rank, " +
            "name_rank")
    List<StandardLibrary> selectListByStandardRankThird(Integer headlineRank,
                                                        Integer secondaryHeadlineRank);

    @Select("SELECT * FROM standard_library WHERE standard_rank=4 AND headline_rank=#{0} AND " +
            "secondary_headline_rank=#{1} AND third_headline_rank=#{2} ORDER BY headline_rank, secondary_headline_rank, " +
            "name_rank")
    List<StandardLibrary> selectListByStandardRankFour(Integer headlineRank,
                                                       Integer secondaryHeadlineRank,
                                                       String thirdHeadlineRank);

    @Select("SELECT * FROM standard_library WHERE standard_rank=1 ORDER BY headline_rank, " +
            "secondary_headline_rank, name_rank")
    List<StandardLibrary> selectListByStandardRankFirst();

    @Select("SELECT * FROM standard_library WHERE standard_rank=1 ORDER BY headline_rank")
    List<StandardLibrary> selectHeadlinesByRank();


    @Select("SELECT * FROM standard_library WHERE standard_rank=2 AND " +
            "headline_rank=#{headlineRank} ORDER BY secondary_headline_rank")
    List<StandardLibrary> selectSecondaryHeadlinesByRankAndHeadline(Integer headlineRank);

    @Select({
            "<script>",
            "SELECT * FROM standard_library where standard_rank=3 AND project_rank in",
            "<foreach collection='project_rank' item='item' open='(' separator=',' close=')'>",
            "#{item}",
            "</foreach>",
            " AND headline_rank=#{1} AND secondary_headline_rank=#{2} ORDER BY third_headline_rank",
            "</script>"
    })
    List<StandardLibrary> selectThirdHeadlinesByRankAndHeadlineAndSecondaryHeadline(@Param("project_rank") String[] rank,
                                                                                    Integer headlineRank,
                                                                                    Integer secondaryHeadlineRank);


    @Select({
            "<script>",
            "SELECT * FROM standard_library where standard_rank=3 AND project_rank in",
            "<foreach collection='project_rank' item='item' open='(' separator=',' close=')'>",
            "#{item}",
            "</foreach>",
            " AND headline_rank=#{1} AND secondary_headline_rank=#{2} AND third__headline_rank=#{3} ORDER BY name_rank",
            "</script>"
    })
    List<StandardLibrary> selectNamesByRankAndHeadlineAndSecondaryHeadlineAndThirdHeadline(@Param("project_rank") String[] rank,
                                                                                           Integer headlineRank,
                                                                                           Integer secondaryHeadlineRank,
                                                                                           String thirdHeadlineRank);

    @Select("SELECT * FROM standard_library WHERE standard_rank=2 AND headline_rank=#{0} ORDER BY" +
            " headline_rank, secondary_headline_rank, name_rank")
    List<StandardLibrary> selectListByStandardRankSecond(Integer headlineRank);
}
