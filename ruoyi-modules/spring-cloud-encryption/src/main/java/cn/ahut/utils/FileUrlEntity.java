package cn.ahut.utils;

/**
 * Created by wzw on 2021/7/17.
 */
public class FileUrlEntity {

    private String miwenPath;
    private String miwenName;
    private String sgnPath;
    private String sgnName;


    public FileUrlEntity() {
    }

    public FileUrlEntity(String miwenPath, String miwenName, String sgnPath, String sgnName) {
        this.miwenPath = miwenPath;
        this.miwenName = miwenName;
        this.sgnPath = sgnPath;
        this.sgnName = sgnName;
    }

    public String getSgnPath() {
        return sgnPath;
    }

    public void setSgnPath(String sgnPath) {
        this.sgnPath = sgnPath;
    }

    public String getSgnName() {
        return sgnName;
    }

    public void setSgnName(String sgnName) {
        this.sgnName = sgnName;
    }

    public String getMiwenPath() {
        return miwenPath;
    }

    public void setMiwenPath(String miwenPath) {
        this.miwenPath = miwenPath;
    }

    public String getMiwenName() {
        return miwenName;
    }

    public void setMiwenName(String miwenName) {
        this.miwenName = miwenName;
    }

    @Override
    public String toString() {
        return "FileUrlEntity{" +
                "miwenPath='" + miwenPath + '\'' +
                ", miwenName='" + miwenName + '\'' +
                ", sgnPath='" + sgnPath + '\'' +
                ", sgnName='" + sgnName + '\'' +
                '}';
    }
}
