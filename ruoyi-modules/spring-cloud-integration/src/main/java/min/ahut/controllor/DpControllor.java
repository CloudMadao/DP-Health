package min.ahut.controllor;

import com.ruoyi.common.core.web.domain.AjaxResult;
import min.ahut.domain.GroupEntity;
import min.ahut.mapper.DpMapper;
import min.ahut.utils.HistogramSort;
import min.ahut.utils.NoiseMaker;
import min.ahut.utils.NoiseModify;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by wzw on 2021/3/23.
 */
@RestController
@RequestMapping("/dp")
public class DpControllor {
    @Autowired
    DpMapper dpMapper;
    @Autowired
    NoiseMaker noiseMaker;
    @Autowired
    HistogramSort histogramSort;
    @Autowired
    NoiseModify noiseModify;

    /**
     *按县区对病人进行分组处理-原始数据
     */
    @GetMapping("/countrycount")
    public AjaxResult getCountFromCountry(@RequestParam(value ="sguardiancountry",required = false) String...countryName){
        List<GroupEntity> countFromCountryByName = dpMapper.getCountFromCountryByName(countryName);
        return AjaxResult.success(countFromCountryByName);
    }

    /**
     *按县区对病人进行分组处理且加噪
     */
    @PostMapping("/countrycount")
    public AjaxResult getCountFromCountryAddNoise(@RequestParam(value ="sguardiancountry",required = false) String...countryName){
        List<GroupEntity> countFromCountryByName = dpMapper.getCountFromCountryByName(countryName);
        List<GroupEntity> sort = histogramSort.sort(countFromCountryByName);

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

        return AjaxResult.success(countFromCountryByName);
    }
}
