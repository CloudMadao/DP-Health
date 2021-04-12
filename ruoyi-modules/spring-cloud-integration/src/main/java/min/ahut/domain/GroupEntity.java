package min.ahut.domain;

import lombok.Data;

/**
 * 用于保存分组查询结果
 * Created by wzw on 2021/3/23.
 */

@Data
public class GroupEntity {
    String property;
    double count;
}
