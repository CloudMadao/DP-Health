package min.ahut.controllor;

import com.ruoyi.common.core.web.domain.AjaxResult;
import min.ahut.domain.GroupEntity;
import min.ahut.mapper.DpMapper;
import min.ahut.utils.HistogramSort;
import min.ahut.utils.NoiseMaker;
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

   /*     for(GroupEntity gp:countFromCountryByName){
            double lpNoise = noiseMaker.getLpNoise(1, 1);
            long noise = Math.round(lpNoise);
            long result = (long)gp.getCount() + noise;

            if(result<0){ //添加非负约束
                gp.setCount(0);
            }else {
                gp.setCount(result);
            }
        }*/

        for (GroupEntity gp:countFromCountryByName){
            System.out.println(gp.getProperty()+"----"+gp.getCount());
        }

        System.out.println("---------------------------");

        List<GroupEntity> sort = histogramSort.sort(countFromCountryByName);

        for (GroupEntity gp:sort){
            System.out.println(gp.getProperty()+"----"+gp.getCount());
        }

        return AjaxResult.success(countFromCountryByName);
    }
}
