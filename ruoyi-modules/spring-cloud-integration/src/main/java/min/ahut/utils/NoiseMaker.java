package min.ahut.utils;

import org.springframework.stereotype.Component;

/**
 * 生成噪声
 * Created by wzw on 2021/3/23.
 */
@Component
public class NoiseMaker {
    /**
     * 用于生成服从Laplace分布的随机噪声
     *
     * @param epsilon     隐私预算参数
     * @param sensitivity 全局敏感度
     */
    public double getLpNoise(double epsilon, double sensitivity) {
        double beta = sensitivity / epsilon;
        double p = Math.random();
        //根据Laplace的逆累计分布函数生成噪声
        return -beta * Math.signum(p - 0.5) * Math.log(1 - 2 * Math.abs(p - 0.5));
    }
}
