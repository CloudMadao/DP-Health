package min.ahut.mapper;

import min.ahut.domain.GroupEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by wzw on 2021/3/23.
 */
@Mapper
public interface DpMapper {
    /**
     * @param countryName 传入单个县区名称，可置空
     * @return 返回按县区分组查询结果
     */
    List<GroupEntity> getCountFromCountryByName(@Param(value = "countryName") String...countryName);
}
