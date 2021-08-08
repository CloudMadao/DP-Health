package cn.ahut.controller;

import cn.ahut.Mapper.JsbDataShow;
import cn.ahut.untils.SigOp.HoldVerPro;
import cn.ahut.utils.*;
import com.ruoyi.common.core.web.domain.AjaxResult;
import it.unisa.dia.gas.jpbc.PairingParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by wzw on 2021/7/17.
 */

@RestController
@RequestMapping("/dpcount")
public class DpCountControllor {
    @Autowired
    JsbDataShow jsbDataShow;
    @Autowired
    private DpMiwen dpMiwen;
    @Autowired
    HistogramSort histogramSort;
    @Autowired
    NoiseMaker noiseMaker;
    @Autowired
    NoiseModify noiseModify;

    @Autowired
    HoldVerPro hpv;

    private PairingParameters param ;

    /**
     *按肇事次数对病人进行分组处理-原始数据
     */
    @GetMapping("/ihitcount")
    public AjaxResult getCountFromCountry() throws Exception {
        List<GroupEntity> ihitCount = dpMiwen.getIhitCount();
        return AjaxResult.success(dpMiwen.getIhitCount());
    }

    /**
     *按县区对病人进行分组处理且加噪
     */
    @PostMapping("/ihitcount")
    public AjaxResult getCountFromCountryAddNoise() throws Exception {
        List<GroupEntity> ihitCount = dpMiwen.getIhitCount();
        List<GroupEntity> sort = histogramSort.sort(ihitCount);

        for (GroupEntity gp:sort){
            System.out.println(gp.getProperty()+"----"+gp.getCount());
        }
        System.out.println("----------------------");

        sort.get(0).setCount(sort.get(0).getCount()+noiseModify.noiseModify(noiseMaker.getLpNoise(0.6,1))); //第一个元素加噪
        for(int i=1;i<sort.size();){
            double lpNoise1 = noiseMaker.getLpNoise(0.6, 1);
            long noise1 = noiseModify.noiseModify(lpNoise1);
            double lpNoise2 = noiseMaker.getLpNoise(0.6, 1);
            long noise2 = noiseModify.noiseModify(lpNoise1);

            if(i==sort.size()-1&&sort.get(i).getCount()+noise1<=sort.get(i-1).getCount()){
                sort.get(i).setCount(sort.get(i).getCount()+noise1);
                break;
            }
            if(sort.get(i).getCount()+noise1<=sort.get(i-1).getCount()
                    &&sort.get(i).getCount()+noise1>=sort.get(i+1).getCount()+noise2){
                sort.get(i).setCount(sort.get(i).getCount()+noise1);
                sort.get(i+1).setCount(sort.get(i+1).getCount()+noise2);
                i+=2;
            }

        }

        for (GroupEntity gp:sort){
            System.out.println(gp.getProperty()+"----"+gp.getCount());
        }

        return AjaxResult.success(sort);
    }

}
