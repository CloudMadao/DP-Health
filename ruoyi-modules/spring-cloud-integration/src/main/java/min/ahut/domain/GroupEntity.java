package min.ahut.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * 用于保存分组查询结果
 * Created by wzw on 2021/3/23.
 */

@Getter
@Setter
public class GroupEntity {
    String property;
    double count;

    public GroupEntity() {
    }

    public GroupEntity(String property, double count) {
        this.property = property;
        this.count = count;
    }

    @Override
    public String toString() {
        return "GroupEntity{" +
                "property='" + property + '\'' +
                ", count=" + count +
                '}';
    }
}
