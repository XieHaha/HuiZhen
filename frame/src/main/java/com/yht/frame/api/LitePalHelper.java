package com.yht.frame.api;

import com.yht.frame.data.bean.HospitalBean;
import com.yht.frame.data.bean.PatientBean;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * @author 顿顿
 * @date 19/7/5 14:27
 * @des
 */
public class LitePalHelper<T extends DataSupport> {
    /**
     * 存储医院
     *
     * @param list
     */
    public void updateAll(List<T> list, final Class<T> classOfT) {
        DataSupport.deleteAll(classOfT);
        DataSupport.saveAll(list);
    }

    /**
     * 模糊查询 医院
     *
     * @param key
     * @return
     */
    public static List<HospitalBean> findHospitals(String key) {
        return DataSupport.where("hospitalName like ?", "%" + key + "%").find(HospitalBean.class);
    }

    /**
     * 模糊查询 患者
     *
     * @param key
     * @return
     */
    public static List<PatientBean> findPatients(String key) {
        return DataSupport.where("name like ?", "%" + key + "%").find(PatientBean.class);
    }
}
