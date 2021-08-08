package cn.ahut.utils;



/**
 * 用于保存分组查询结果
 * Created by wzw on 2021/3/23.
 */


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

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public double getCount() {
        return count;
    }

    public void setCount(double count) {
        this.count = count;
    }
}
