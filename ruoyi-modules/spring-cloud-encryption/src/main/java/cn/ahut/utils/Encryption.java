package cn.ahut.utils;

import cn.ahut.Mapper.personMapper;
import cn.ahut.entity.key.PrivateKey;
import cn.ahut.entity.key.PublicKey;
import cn.ahut.untils.BGNKey.get_PrivateKey;
import cn.ahut.untils.BGNKey.get_PubilcKey;
import cn.ahut.untils.BGNOp.DoEncrypt;
import cn.ahut.untils.BGNOp.Doselect;
import cn.ahut.untils.Get_Param;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.PairingParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by wzw on 2021/4/16.
 * 对信息加密加密
 */
@Component
public class Encryption {
    @Autowired
    private get_PrivateKey get_sk;
    @Autowired
    private get_PubilcKey get_pk;
    @Autowired
    private Get_Param get_Param;

    private PairingParameters param ;

    public byte[] doEncryption(int info) throws Exception {
        param= get_Param.getparam();
        PrivateKey sk=get_sk.createsk(param);
        PublicKey pk=get_pk.createpk(param);
        Element infoElement = DoEncrypt.encrypt(info, pk);
        return infoElement.toBytes();
    }
}
