package min.ahut.utils;

import org.springframework.stereotype.Component;

/**
 * Created by wzw on 2021/4/13.
 * 噪声修正
 */

@Component
public class NoiseModify {
    public long noiseModify(double noise){
        if(noise<0){
            return  (long)Math.floor(noise);
        }else {
            return  (long)Math.ceil(noise);
        }
    }
}
