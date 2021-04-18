package cn.ahut.utils;

import cn.ahut.Mapper.JsbDataShow;
import cn.ahut.entity.DictInfo;
import cn.ahut.entity.Jmpsychosispersoninfop;
import cn.ahut.entity.Psychosispersoninfop;
import cn.ahut.entity.key.PrivateKey;
import cn.ahut.entity.key.PublicKey;
import cn.ahut.untils.BGNKey.get_PrivateKey;
import cn.ahut.untils.BGNKey.get_PubilcKey;
import cn.ahut.untils.BGNOp.DoDecrypt;
import cn.ahut.untils.Get_Param;
import com.ruoyi.common.security.service.TokenService;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import it.unisa.dia.gas.plaf.jpbc.pairing.a1.TypeA1Pairing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by wzw on 2021/4/15.
 * 解密精神病患者信息
 */

@Component
public class Decrypt {
    @Autowired
    private JsbDataShow jsbDataShow;
    @Autowired
    private get_PrivateKey get_sk;
    @Autowired
    private get_PubilcKey get_pk;
    @Autowired
    private Get_Param get_Param;

    private PairingParameters param ;

    public Jmpsychosispersoninfop decryptJsb(Psychosispersoninfop jsbInfo) throws Exception {
        Jmpsychosispersoninfop jmpsychosispersoninfop = new Jmpsychosispersoninfop();
        //获得param具体值
        param=get_Param.getparam();
        //配对曲线 通过曲线去找群G1的元素
        TypeA1Pairing pairing = (TypeA1Pairing) PairingFactory.getPairing(param);
        PublicKey pk = get_pk.createpk(param);
        PrivateKey sk = get_sk.createsk(param);

        Element element = pairing.getG1().newElementFromBytes(jsbInfo.getIhit()); //肇事次数
        int m = DoDecrypt.decrypt(element, pk, sk); //获取加密代码
        jmpsychosispersoninfop.setIhit(m);

        Element element1 = pairing.getG1().newElementFromBytes(jsbInfo.getSpsychosiscode());//重症精神病
        int m1 = DoDecrypt.decrypt(element1, pk, sk);
        DictInfo dictInfo1 = jsbDataShow.selectByZzCode(m1);
        jmpsychosispersoninfop.setSpsychosiscode(dictInfo1.getDmsm());

        Element element2 = pairing.getG1().newElementFromBytes(jsbInfo.getSsymptomscode()); //精神病代码
        int m2 = DoDecrypt.decrypt(element2, pk, sk);
        DictInfo dictInfo2 = jsbDataShow.selectByJsbCode(m2);
        jmpsychosispersoninfop.setSsymptomscode(dictInfo2.getDmsm());

        Element element3 = pairing.getG1().newElementFromBytes(jsbInfo.getTreatmenteffectcode()); //治疗效果
        int m3 = DoDecrypt.decrypt(element3, pk, sk);
        DictInfo dictInfo3 = jsbDataShow.selectByZlCode(m3);
        jmpsychosispersoninfop.setTreatmenteffectcode(dictInfo3.getDmsm());


        /** 明文部分不需要解密*/
        jmpsychosispersoninfop.setIfirstdiseaseage("");
        jmpsychosispersoninfop.setDfilltime(jsbInfo.getDfilltime());
        jmpsychosispersoninfop.setIhospitalizations("");
        jmpsychosispersoninfop.setSno(jsbInfo.getSno());
        jmpsychosispersoninfop.setSguardianname(jsbInfo.getSguardianname());
        jmpsychosispersoninfop.setSguardianprovince(jsbInfo.getSguardianprovince());
        jmpsychosispersoninfop.setSguardiancity(jsbInfo.getSguardiancity());
        jmpsychosispersoninfop.setSguardiancountry(jsbInfo.getSguardiancountry());
        jmpsychosispersoninfop.setSguardiantelephone(jsbInfo.getSguardiantelephone());
        jmpsychosispersoninfop.setSpatienttelephone(jsbInfo.getSpatienttelephone());
        jmpsychosispersoninfop.setSchargephysician(jsbInfo.getSchargephysician());

        return jmpsychosispersoninfop;
    }
}
