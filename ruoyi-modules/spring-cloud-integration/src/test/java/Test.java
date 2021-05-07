import min.ahut.IntergrationApplication;
import min.ahut.utils.NoiseMaker;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by wzw on 2021/4/19.
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes={IntergrationApplication.class})
public class Test {
    @Autowired
    NoiseMaker noiseMaker;

    @org.junit.Test
    public double make(){
        while(noiseMaker.getLpNoise(1,1)==1){
            return 1.0;
        }
        return 1.0;
    }
}
