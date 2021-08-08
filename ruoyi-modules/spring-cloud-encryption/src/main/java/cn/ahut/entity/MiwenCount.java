package cn.ahut.entity;

/**
 * Created by wzw on 2021/7/17.
 */
public class MiwenCount {
    private int dm;  //小表对应密文解密结果
    private int pid; //密文字段对应人数pid集合

    public MiwenCount() {
    }

    public MiwenCount(int dm, int pid) {
        this.dm = dm;
        this.pid = pid;
    }

    @Override
    public String toString() {
        return "MiwenCount{" +
                "dm=" + dm +
                ", pid=" + pid +
                '}';
    }

    public int getDm() {
        return dm;
    }

    public void setDm(int dm) {
        this.dm = dm;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }
}
