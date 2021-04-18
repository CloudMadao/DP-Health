package cn.ahut.controller;


import cn.ahut.Mapper.JsbDataShow;
import cn.ahut.entity.*;
import cn.ahut.entity.key.PrivateKey;
import cn.ahut.entity.key.PublicKey;
import cn.ahut.untils.BGNKey.get_PrivateKey;
import cn.ahut.untils.BGNKey.get_PubilcKey;
import cn.ahut.untils.BGNOp.DoDecrypt;
import cn.ahut.untils.Get_Param;
import cn.ahut.utils.Decrypt;
import cn.ahut.utils.Encryption;
import com.ruoyi.common.core.constant.UserConstants;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.web.page.TableDataInfo;
import com.ruoyi.common.security.service.TokenService;
import com.ruoyi.common.security.utils.SecurityUtils;
import com.ruoyi.system.api.domain.SysUser;
import com.ruoyi.system.api.model.LoginUser;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import it.unisa.dia.gas.plaf.jpbc.pairing.a1.TypeA1Pairing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wzw on 2020/10/30.
 */
@RestController
@RequestMapping("/jsb")
public class JsbControllor extends BaseController {
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

    private PairingParameters param ;

    @GetMapping("/list")
    public TableDataInfo list(Psychosispersoninfop psychosispersoninfop) throws Exception {
        startPage();
        LoginUser loginUser = tokenService.getLoginUser();
        SysUser sysUser = loginUser.getSysUser(); //获取登录用户

        if(sysUser.getAuthorityId()==1){ //有部分明文权限
            List<Psychosispersoninfop> psychosispersoninfops = jsbDataShow.selectJsbByDoctorId(sysUser.getUserId()); //获取当前社区医生负录入的患者
            List<Jmpsychosispersoninfop> list = new ArrayList<Jmpsychosispersoninfop>();
            for(Psychosispersoninfop jsb:psychosispersoninfops){
                list.add(decrypt.decryptJsb(jsb));
            }
            return getDataTable(list);
        }
        List<Psychosispersoninfop> list = jsbDataShow.selectAllJsb(psychosispersoninfop);
        return getDataTable(list);
    }

    @GetMapping("/list/file")
    public TableDataInfo listFile(Psychosispersoninfop psychosispersoninfop)
    {
        startPage();
        List<FileDemo> list = jsbDataShow.selectAllFile();
        return getDataTable(list);
    }

    /**
     * 删除糖尿病患者
     */
    @DeleteMapping("/{sno}")
    public AjaxResult remove(@PathVariable String[] sno)
    {
        return toAjax(jsbDataShow.deleteJsbBySno(sno));
    }


    /**
     * 解密患者信息
     */
    @PostMapping("/{sno}")
    public TableDataInfo dataJm(@PathVariable String sno) throws Exception
    {
        Jmpsychosispersoninfop jmpsychosispersoninfop = new Jmpsychosispersoninfop();
        Psychosispersoninfop psychosispersoninfop = jsbDataShow.selectOneBySno(sno);
       //获得param具体值
        param=get_Param.getparam();
        //配对曲线 通过曲线去找群G1的元素
        TypeA1Pairing pairing = (TypeA1Pairing) PairingFactory.getPairing(param);
        PublicKey pk = get_pk.createpk(param);
        PrivateKey sk = get_sk.createsk(param);

        Element element = pairing.getG1().newElementFromBytes(psychosispersoninfop.getIhit());
        int m = DoDecrypt.decrypt(element, pk, sk); //获取加密代码
        jmpsychosispersoninfop.setIhit(m);

        Element element1 = pairing.getG1().newElementFromBytes(psychosispersoninfop.getSpsychosiscode());//重症精神病
        int m1 = DoDecrypt.decrypt(element1, pk, sk);
        DictInfo dictInfo1 = jsbDataShow.selectByZzCode(m1);
        jmpsychosispersoninfop.setSpsychosiscode(dictInfo1.getDmsm());

        Element element2 = pairing.getG1().newElementFromBytes(psychosispersoninfop.getSsymptomscode()); //精神病代码
        int m2 = DoDecrypt.decrypt(element2, pk, sk);
        DictInfo dictInfo2 = jsbDataShow.selectByJsbCode(m2);
        jmpsychosispersoninfop.setSsymptomscode(dictInfo2.getDmsm());

        Element element3 = pairing.getG1().newElementFromBytes(psychosispersoninfop.getTreatmenteffectcode()); //治疗效果
        int m3 = DoDecrypt.decrypt(element3, pk, sk);
        DictInfo dictInfo3 = jsbDataShow.selectByZlCode(m3);
        jmpsychosispersoninfop.setTreatmenteffectcode(dictInfo3.getDmsm());

        Element element4=pairing.getG1().newElementFromBytes(psychosispersoninfop.getIfirstdiseaseage());//首次发病
        int m4 = DoDecrypt.decrypt(element4, pk, sk);
        jmpsychosispersoninfop.setIfirstdiseaseage("");
        jmpsychosispersoninfop.setDfilltime(psychosispersoninfop.getDfilltime());
        jmpsychosispersoninfop.setIhospitalizations("");
        jmpsychosispersoninfop.setSno(psychosispersoninfop.getSno());
        jmpsychosispersoninfop.setSguardianname(psychosispersoninfop.getSguardianname());
        jmpsychosispersoninfop.setSguardianprovince(psychosispersoninfop.getSguardianprovince());
        jmpsychosispersoninfop.setSguardiancity(psychosispersoninfop.getSguardiancity());
        jmpsychosispersoninfop.setSguardiancountry(psychosispersoninfop.getSguardiancountry());
        jmpsychosispersoninfop.setSguardiantelephone(psychosispersoninfop.getSguardiantelephone());
        jmpsychosispersoninfop.setSpatienttelephone(psychosispersoninfop.getSpatienttelephone());
        jmpsychosispersoninfop.setSchargephysician(psychosispersoninfop.getSchargephysician());

        startPage();
        List<Jmpsychosispersoninfop> list = new ArrayList<Jmpsychosispersoninfop>();
        list.add(jmpsychosispersoninfop);
        return getDataTable(list);
    }

    /**
     * 新增患者信息
     * @param
     */
    @PostMapping("/sufferer")
    public void add(@Validated @RequestBody PsychosispersoninfoTable psychosispersoninfoTable) throws Exception {
        //获取录入医生信息
        LoginUser loginUser = tokenService.getLoginUser();
        SysUser sysUser = loginUser.getSysUser();

        Psychosispersoninfop psy = new Psychosispersoninfop();
        psy.setSno(psychosispersoninfoTable.getSno());
        psy.setSguardianname(psychosispersoninfoTable.getSguardianname());
        psy.setSguardianprovince(psychosispersoninfoTable.getSguardianprovince());
        psy.setSguardiancity(psychosispersoninfoTable.getSguardiancity());
        psy.setSguardiancountry(psychosispersoninfoTable.getSguardiancountry());
        psy.setSguardiantelephone(psychosispersoninfoTable.getSguardiantelephone());
        psy.setSpatienttelephone(psychosispersoninfoTable.getSpatienttelephone());
        psy.setSdoctoradvice(psychosispersoninfoTable.getSdoctoradvice()); //医生建议
        psy.setDoctorid(sysUser.getUserId());

        psy.setSchargephysician(jsbDataShow.selectJsbByDoctorId(sysUser.getUserId()).get(0).getSchargephysician());

        Integer ihit = Integer.valueOf(psychosispersoninfoTable.getIhit()); //肇事次数
        psy.setIhit(encryption.doEncryption(ihit));

        Integer ssymptomscode = Integer.valueOf(psychosispersoninfoTable.getSsymptomscode()); //精神病症状代码
        psy.setSsymptomscode(encryption.doEncryption(ssymptomscode));

        Integer spsychosiscode = Integer.valueOf(psychosispersoninfoTable.getSpsychosiscode()); //重症精神病症状代码
        psy.setSpsychosiscode(encryption.doEncryption(spsychosiscode));

        Integer treatmenteffectcode = Integer.valueOf(psychosispersoninfoTable.getTreatmenteffectcode());
        psy.setTreatmenteffectcode(encryption.doEncryption(treatmenteffectcode));

        jsbDataShow.insertJsb(psy);
    }


    @PostMapping("/dataimport")
    public void importData(@Validated @RequestBody DataImport dataImport) throws Exception {
        System.out.println(dataImport.getOriginTableName());
        System.out.println(dataImport.getStartTime());
        System.out.println(dataImport.getEndTime());

        List<PsychosispersoninfoTable> allOriginInfo = jsbDataShow.getAllOriginInfo(dataImport);
        PsychosispersoninfoTable psychosispersoninfoTable = allOriginInfo.get(0);
        System.out.println(psychosispersoninfoTable.getIhit());
    }
}
