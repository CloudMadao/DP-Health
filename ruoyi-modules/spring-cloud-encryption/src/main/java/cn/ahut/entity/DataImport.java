package cn.ahut.entity;

/**
 * Created by wzw on 2021/4/17.
 */


public class DataImport {
    private String originTableName;
    private String targetTableName;

    private String startTime;
    private String endTime;

    public String getOriginTableName() {
        return originTableName;
    }

    public void setOriginTableName(String originTableName) {
        this.originTableName = originTableName;
    }

    public String getTargetTableName() {
        return targetTableName;
    }

    public void setTargetTableName(String targetTableName) {
        this.targetTableName = targetTableName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public DataImport() {
    }

    public DataImport(String originTableName, String targetTableName, String startTime, String endTime) {
        this.originTableName = originTableName;
        this.targetTableName = targetTableName;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "DataImport{" +
                "originTableName='" + originTableName + '\'' +
                ", targetTableName='" + targetTableName + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                '}';
    }
}
