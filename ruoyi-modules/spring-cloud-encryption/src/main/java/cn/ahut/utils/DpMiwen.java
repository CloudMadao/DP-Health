package cn.ahut.utils;

import cn.ahut.Mapper.JsbDataShow;
import cn.ahut.Mapper.personMapper;
import cn.ahut.entity.code_id.Ihit;
import cn.ahut.entity.key.PrivateKey;
import cn.ahut.entity.key.PublicKey;
import cn.ahut.untils.BGNKey.get_PrivateKey;
import cn.ahut.untils.BGNKey.get_PubilcKey;
import cn.ahut.untils.BGNOp.DoDecrypt;
import cn.ahut.untils.Byt_Code;
import cn.ahut.untils.Get_Param;
import com.ruoyi.common.security.service.TokenService;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import it.unisa.dia.gas.plaf.jpbc.pairing.a1.TypeA1Pairing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wzw on 2021/7/17.
 */

@Component
public class DpMiwen {

    @Autowired
    JsbDataShow jsbDataShow;
    @Autowired
    private get_PrivateKey get_sk;
    @Autowired
    private get_PubilcKey get_pk;
    @Autowired
    private Get_Param get_Param;
    @Autowired
    Decrypt decrypt;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private Encryption encryption;

    @Autowired
    private personMapper personmapper;

    private PairingParameters param ;

    public List<GroupEntity> getIhitCount() throws Exception {
        //获得param具体值
        param=get_Param.getparam();
        //配对曲线 通过曲线去找群G1的元素
        TypeA1Pairing pairing = (TypeA1Pairing) PairingFactory.getPairing(param);
        PublicKey pk = get_pk.createpk(param);
        PrivateKey sk = get_sk.createsk(param);

        List<GroupEntity> miwenCounts = new ArrayList<>();

        List<Ihit> selectihit = personmapper.selectihit();

        for(Ihit a:selectihit){
            GroupEntity groupEntity = new GroupEntity();
            byte[] pid = a.getPid();
            byte[] hit = a.getHit();
            Element element = pairing.getG1().newElementFromBytes(hit);
            int m = DoDecrypt.decrypt(element, pk, sk); //解密Ihit肇事代码
            if(pid!=null){
                List<String> strings = Byt_Code.c_byte(pid); //pid对应id反序列化
                groupEntity.setProperty(String.valueOf(m));
                groupEntity.setCount(strings.size());
                miwenCounts.add(groupEntity);
            }
        }
        return miwenCounts;
    }
}
