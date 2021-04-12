package min.ahut.utils;

import min.ahut.domain.GroupEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by wzw on 2021/4/12.
 * 对原始直方图进行排序
 */
@Component
public class HistogramSort {


    /**
     * @param histogram 输入排序加噪后的直方图
     * 返回有序的直方图
     */
    public List sort(List<GroupEntity> histogram){

        GroupEntity temp = new GroupEntity();

        //对原始直方图进行冒泡排序，获得从大至小的原始直方图
        for(int i=0;i<histogram.size()-1;i++){
            boolean flag = false;
            for(int j=0;j<histogram.size()-1-i;j++){
                if(histogram.get(j+1).getCount()>histogram.get(j).getCount()){
                    temp=histogram.get(j+1);
                    histogram.set(j,histogram.get(j+1));
                    histogram.set(j+1,temp);
                    flag=true;
                }
            }
            if(flag==true){
                break;
            }
        }
        return histogram;
    }
}
