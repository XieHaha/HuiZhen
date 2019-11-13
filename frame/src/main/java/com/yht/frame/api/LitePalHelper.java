package com.yht.frame.api;

import com.yht.frame.data.bean.DoctorBean;
import com.yht.frame.data.bean.HospitalBean;
import com.yht.frame.data.bean.PatientBean;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.util.List;

/**
 * @author 顿顿
 * @date 19/7/5 14:27
 */
public class LitePalHelper<T extends LitePalSupport> {
    /**
     * 数据库
     */
    public static final String DATA_BASE_NAME = "yhtd";
    /**
     * 存储医院
     */
    public void updateAll(List<T> list, final Class<T> classOfT) {
        LitePal.deleteAll(classOfT);
        LitePal.saveAll(list);
    }

    /**
     * 模糊查询 医院
     */
    public static List<HospitalBean> findHospitals(String key) {
        return LitePal.where("hospitalName like ?", "%" + key + "%").find(HospitalBean.class);
    }

    /**
     * 模糊查询 居民
     */
    public static List<PatientBean> findPatients(String key) {
        return LitePal.where("name like ?", "%" + key + "%").find(PatientBean.class);
    }

    /**
     * 模糊查询 医生
     */
    public static List<DoctorBean> findDoctors(String key) {
        return LitePal.where("doctorName like ?", "%" + key + "%").find(DoctorBean.class);
    }
}
