package com.yht.frame.http.retrofit;

import com.yht.frame.data.BaseResponse;
import com.yht.frame.data.base.CooperateHospitalBean;
import com.yht.frame.data.base.DoctorAuthBean;
import com.yht.frame.data.base.DoctorInfoBean;
import com.yht.frame.data.base.HospitalBean;
import com.yht.frame.data.base.HospitalDepartBean;
import com.yht.frame.data.base.HospitalTitleBean;
import com.yht.frame.data.base.LoginBean;
import com.yht.frame.data.base.VerifyCodeBean;
import com.yht.frame.data.bean.CombineBean;
import com.yht.frame.data.bean.CooperateDocBean;
import com.yht.frame.data.bean.HospitalProductTypeBean;
import com.yht.frame.data.bean.PatientBean;
import com.yht.frame.data.bean.PatientCaseDetailBean;
import com.yht.frame.data.bean.RegistrationBean;
import com.yht.frame.data.bean.TransPatientBean;
import com.yht.frame.data.bean.VersionBean;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * @author dundun
 */
public interface ApiUrlManager {
    /**
     * 微信登录
     *
     * @param info map参数
     * @return 返回
     */
    @POST("/client/wx/login")
    Observable<BaseResponse<LoginBean>> weChatLogin(@Body Map<String, String> info);

    /**
     * 微信绑定手机号
     *
     * @param info map参数
     * @return 返回
     */
    @POST("/client/wx/bind")
    Observable<BaseResponse<LoginBean>> weChatBind(@Body Map<String, String> info);

    /**
     * 获取验证码
     *
     * @param info map参数
     * @return 返回值
     */
    @POST("/client/get-verify-code")
    Observable<BaseResponse<VerifyCodeBean>> getVerifyCode(@Body Map<String, String> info);

    /**
     * 登录
     *
     * @param info map参数
     * @return 返回值
     */
    @POST("/client/confirm-verify-code")
    Observable<BaseResponse<LoginBean>> login(@Body Map<String, String> info);

    /**
     * 上传图片
     *
     * @param token token
     * @param file  图片
     * @return 返回值
     */
    @Multipart
    @POST("/file/upload_file")
    Observable<BaseResponse<String>> uploadImg(@Header("token") String token, @Part MultipartBody.Part file);

    /**
     * 获取医院列表 （医生认证模块）
     *
     * @param token token
     * @return 返回值
     */
    @GET("/client/hospital/selectInput")
    Observable<BaseResponse<List<HospitalBean>>> getHospitalListByAuth(@Header("token") String token);

    /**
     * 获取科室树
     *
     * @param token token
     * @param info  map参数
     * @return 返回值
     */
    @POST("/client/department/tree")
    Observable<BaseResponse<List<HospitalDepartBean>>> getDepartTree(@Header("token") String token,
            @Body Map<String, String> info);

    /**
     * 提交医生认证数据
     *
     * @param token token
     * @param info  map参数
     * @return 返回值
     */
    @POST("/client/doctor/auth")
    Observable<BaseResponse<String>> submitDoctorAuth(@Header("token") String token, @Body Map<String, Object> info);

    /**
     * 获取已提交认证信息
     *
     * @param token  token
     * @param mobile 手机号
     * @return 返回值
     */
    @GET("/client/doctor/authInfo/{mobile}")
    Observable<BaseResponse<DoctorAuthBean>> getDoctorAuth(@Header("token") String token,
            @Path("mobile") String mobile);

    /**
     * 获取医生个人详情
     *
     * @param token  token
     * @param mobile 手机号
     * @return 返回值
     */
    @GET("/client/doctor/model/{mobile}")
    Observable<BaseResponse<DoctorInfoBean>> getDoctorInfo(@Header("token") String token,
            @Path("mobile") String mobile);

    /**
     * 获取指定类型的字典数据
     *
     * @param token token
     * @param info  参数 比如jobTitle、depart等
     * @return 返回值
     */
    @GET("/dm/query-by-type")
    Observable<BaseResponse<List<HospitalTitleBean>>> getDataByType(@Header("token") String token,
            @Query("type") String info);

    /**
     * 医生基本信息加收入信息(总收入 已到账 余额 本月收入 本月已到账)
     *
     * @param token token
     * @return 返回值
     */
    @GET("/client/doctorcenter/doctor_balance_info")
    Observable<BaseResponse<List<HospitalTitleBean>>> getDoctorInfoAndBalanceInfo(@Header("token") String token);

    /**
     * 医生收入信息 (预约检查+预约转诊+远程会珍)
     *
     * @param info  map参数
     * @param token token
     * @return 返回值
     */
    @GET("/client/doctorcenter/doctor_income")
    Observable<BaseResponse<List<HospitalTitleBean>>> getDoctorBalanceInfo(@Header("token") String token,
            @Query("doctorOrderTranId") String info);

    /**
     * 医生提现信息
     *
     * @param info  map参数
     * @param token token
     * @return 返回值
     */
    @GET("/client/doctorcenter/doctor_cash")
    Observable<BaseResponse<List<HospitalTitleBean>>> getDoctorWithdraw(@Header("token") String token,
            @Query("doctorOrderTranId") String info);

    /**
     * 医生某月收入汇总 (纯收入(不包含提现) 预约检查+预约转诊+远程会珍)
     *
     * @param info  map参数
     * @param token token
     * @return 返回值
     */
    @GET("/client/doctorcenter/doctor_cash_by_month")
    Observable<BaseResponse<List<HospitalTitleBean>>> getDoctorWithdrawByMonth(@Header("token") String token,
            @Query("query_month") String info);

    /**
     * 医生收入明细信息 (医生提现+预约检查+预约转诊+远程会珍)
     *
     * @param info  map参数
     * @param token token
     * @return 返回值
     */
    @GET("/client/doctorcenter/doctor_tran_list")
    Observable<BaseResponse<List<HospitalTitleBean>>> getDoctorIncomeList(@Header("token") String token,
            @QueryMap Map<String, String> info);

    /**
     * 医生收入明细信息 (不包含提现)
     *
     * @param info  map参数
     * @param token token
     * @return 返回值
     */
    @GET("/client/doctorcenter/doctor_tran_list_only_income")
    Observable<BaseResponse<List<HospitalTitleBean>>> getDoctorIncomeWithOutList(@Header("token") String token,
            @QueryMap Map<String, String> info);

    /**
     * 医生某月收入明细信息 (纯收入(不包含提现) 预约检查+预约转诊+远程会珍)
     *
     * @param info  map参数
     * @param token token
     * @return 返回值
     */
    @GET("/client/doctorcenter/doctor_tran_list_by_month")
    Observable<BaseResponse<List<HospitalTitleBean>>> getDoctorIncomeByMonthList(@Header("token") String token,
            @QueryMap Map<String, String> info);

    /**
     * 根据医生编码获取患者列表信息
     *
     * @param info  map参数
     * @param token token
     * @return 返回值
     */
    @GET("/client/patient/getbydoccode")
    Observable<BaseResponse<List<PatientBean>>> getPatientListByDoctorCode(@Header("token") String token,
            @Query("code") String info);

    /**
     * 根据患者编码获取患者信息
     *
     * @param info  map参数
     * @param token token
     * @return 返回值
     */
    @GET("/client/patient/getbypatcode")
    Observable<BaseResponse<List<PatientBean>>> getPatientDetailByPatientCode(@Header("token") String token,
            @Query("code") String info);
    /********************************分隔线*********************************/
    /**
     * 首页广告
     *
     * @param info map参数
     * @return 返回广告页链接
     */
    @POST("DPInternal/resource/splash")
    Observable<BaseResponse<String>> getSplash(@Body Map<String, String> info);

    /**
     * 版本更新
     *
     * @param info map参数
     * @return 返回值
     */
    @POST("app/version")
    Observable<BaseResponse<VersionBean>> getNewVersion(@Body Map<String, String> info);

    /**
     * 获取患者申请列表
     *
     * @param info 参数
     * @return 返回值
     */
    @POST("dp/patient/request")
    Observable<BaseResponse<List<PatientBean>>> getApplyPatientList(@Body Map<String, Object> info);

    /**
     * 获取患者列表
     *
     * @param info 参数
     * @return 返回值
     */
    @POST("dp/patient")
    Observable<BaseResponse<List<PatientBean>>> getPatientList(@Body Map<String, Object> info);

    /**
     * 转诊记录   包括转入转出
     *
     * @param info 参数
     * @return 返回值
     */
    @POST("trans/all/doctor/notes")
    Observable<BaseResponse<List<TransPatientBean>>> getTransferList(@Body Map<String, Object> info);

    /**
     * 开单记录
     *
     * @param info 参数
     * @return 返回值
     */
    @POST("order/doctor/orders/list")
    Observable<BaseResponse<List<RegistrationBean>>> getOrderList(@Body Map<String, Object> info);

    /**
     * 获取合作医生列表
     *
     * @param info 参数
     * @return 返回值
     */
    @POST("colleborate/doctorList")
    Observable<BaseResponse<List<CooperateDocBean>>> getCooperateList(@Body Map<String, Object> info);

    /**
     * 合作医生申请
     *
     * @param info 参数
     * @return 返回值
     */
    @POST("colleborate/applyRequest")
    Observable<BaseResponse<PatientBean>> applyCooperateDoc(@Body Map<String, Object> info);

    /**
     * 处理医生申请合作（proCode为字符1（表示同意）或字符3（表示拒绝））  appliedId被申请人  applyId申请人
     *
     * @param info 参数
     * @return 返回值
     */
    @POST("colleborate/applyProcess")
    Observable<BaseResponse<PatientBean>> dealDocApply(@Body Map<String, Object> info);

    /**
     * 取消合作医生关系
     * * doctorId 为操作人id   doctorId2为被操作人id
     *
     * @param info 参数
     * @return 返回值
     */
    @GET("colleborate/delete")
    Observable<BaseResponse<String>> cancelCooperateDoc(@QueryMap Map<String, Object> info);

    /**
     * 获取申请合作医生列表
     *
     * @param info 参数
     * @return 返回值
     */
    @POST("colleborate/applyList")
    Observable<BaseResponse<List<CooperateDocBean>>> getApplyCooperateList(@Body Map<String, Object> info);

    /**
     * 医生资质认证
     *
     * @param files 图片
     * @param info  参数
     * @return 返回值
     */
    @Multipart
    @POST("doctor/info/qualifiy")
    Observable<BaseResponse<String>> qualifiyDoc(@Part MultipartBody.Part[] files, @QueryMap Map<String, Object> info);

    /**
     * 更改个人信息
     *
     * @param info 参数
     * @return 返回值
     */
    @POST("doctor/info/update")
    Observable<BaseResponse<String>> updateUserInfo(@Body Map<String, Object> info);

    /**
     * 获取转诊出去患者列表
     *
     * @param info 参数
     * @return 返回值
     */
    @POST("trans/out/doctor/notes")
    Observable<BaseResponse<List<TransPatientBean>>> getTransferPatientToList(@Body Map<String, Object> info);

    /**
     * 获取收到转诊患者列表
     *
     * @param info 参数
     * @return 返回值
     */
    @POST("trans/in/doctor/notes")
    Observable<BaseResponse<List<TransPatientBean>>> getTransferPatientFromList(@Body Map<String, Object> info);

    /**
     * 获取某个患者的转诊单
     *
     * @param info 参数
     * @return 返回值
     */
    @POST("trans/doctor/patient/notes")
    Observable<BaseResponse<List<TransPatientBean>>> getTransferByPatient(@Body Map<String, Object> info);

    /**
     * 获取当前医生给患者患者所有订单
     *
     * @param info 参数
     * @return 返回值
     */
    @POST("order/doctor/patient/notes")
    Observable<BaseResponse<List<RegistrationBean>>> getPatientOrders(@Body Map<String, Object> info);

    /**
     * 获取患者病例列表
     *
     * @param info 参数
     * @return 返回值
     */
    @POST("case/doctor/query")
    Observable<BaseResponse<List<PatientCaseDetailBean>>> getPatientLimitCaseList(@Body Map<String, Object> info);

    /**
     * 删除患者病例
     *
     * @param info 参数
     * @return 返回值
     */
    @POST("case/delete")
    Observable<BaseResponse<String>> deletePatientCase(@Body Map<String, Object> info);

    /**
     * 获取医生个人信息
     *
     * @param doctorId
     * @return 返回值
     */
    @GET("doctor/info/{doctorId}")
    Observable<BaseResponse<CooperateDocBean>> getDocInfo(@Path("doctorId") String doctorId);

    /**
     * 获取患者个人信息
     *
     * @param patientId
     * @return 返回值
     */
    @GET("patient/info/{patientId}")
    Observable<BaseResponse<PatientBean>> getPatientInfo(@Path("patientId") String patientId);

    /**
     * 获取转诊详情
     *
     * @param info 参数
     * @return 返回值
     */
    @POST("trans/single/detail")
    Observable<BaseResponse<TransPatientBean>> getTransferDetailById(@Body Map<String, Integer> info);

    /**
     * 医生转诊患者
     *
     * @param info 参数
     * @return 返回值
     */
    @POST("trans/doctor/add/notes")
    Observable<BaseResponse<TransPatientBean>> addTransferPatient(@Body Map<String, Object> info);

    /**
     * 取消转诊
     *
     * @param info 参数
     * @return 返回值
     */
    @POST("trans/doctor/cancel/notes")
    Observable<BaseResponse<String>> cancelTransferPatient(@Body Map<String, Integer> info);

    /**
     * 拒绝转诊
     *
     * @param info 参数
     * @return 返回值
     */
    @POST("trans/doctor/refuse")
    Observable<BaseResponse<String>> refuseTransferPatient(@Body Map<String, Integer> info);

    /**
     * 接受转诊
     *
     * @param info 参数
     * @return 返回值
     */
    @POST("trans/doctor/receive")
    Observable<BaseResponse<String>> recvTransferPatient(@Body Map<String, Object> info);

    /**
     * 根据医生id获取医院列表
     *
     * @param info 参数
     * @return 返回值
     */
    @POST("hospital/doctor/relation/list")
    Observable<BaseResponse<List<HospitalBean>>> getHospitalListByDoctorId(@Body Map<String, Object> info);

    /**
     * 获取合作医院下面的医生
     *
     * @param info 参数
     * @return 返回值
     */
    @POST("hospital/doctor/relation/internal/doctor/list")
    Observable<BaseResponse<List<CooperateHospitalBean>>> getCooperateHospitalDoctorList(
            @Body Map<String, Object> info);

    /**
     * 根据医院id获取商品类型和类型下的商品详情
     *
     * @param info 参数
     * @return 返回值
     */
    @POST("product/info/doctor/hospital/type/product")
    Observable<BaseResponse<List<HospitalProductTypeBean>>> getHospitalProductListByHospitalId(
            @Body Map<String, Object> info);

    /**
     * 新增订单
     *
     * @param info 参数
     * @return 返回值
     */
    @POST("product/info/doctor/operator/add")
    Observable<BaseResponse<String>> addProductOrderNew(@Body Map<String, Object> info);

    /**
     * 医生转诊患者
     *
     * @param info 参数
     * @return 返回值
     */
    @POST("dp/trans/focus/doctor")
    Observable<BaseResponse<String>> addPatientByScanOrChangePatient(@Body Map<String, Object> info);

    /**
     * 删除患者 （取消关注）
     *
     * @param doctorId
     * @param patientId
     * @return 返回值
     */
    @GET("dp/cancel/focus/{doctorId}/{patientId}")
    Observable<BaseResponse<String>> deletePatient(@Path("doctorId") String doctorId,
            @Path("patientId") String patientId);

    /**
     * 删除患者 （取消关注）
     *
     * @param info 参数
     * @return 返回值
     */
    @POST("hospital/doctor/relation/list")
    Observable<BaseResponse<List<HospitalBean>>> getCooperateHospitalList(@Body Map<String, String> info);

    /**
     * 获取病例详情
     *
     * @param info 参数
     * @return 返回值
     */
    @POST("order/single")
    Observable<BaseResponse<RegistrationBean>> getDetailById(@Body Map<String, String> info);

    /**
     * 获取患者综合病史接口
     *
     * @param patientId
     * @return 返回值
     */
    @GET("patient/combine/{patientId}")
    Observable<BaseResponse<CombineBean>> getPatientCombine(@Path("patientId") String patientId);

    /**
     * 新增患者病例
     *
     * @param info 参数
     * @return 返回值
     */
    @POST("case/save")
    Observable<BaseResponse<PatientCaseDetailBean>> addPatientCase(@Body Map<String, Object> info);

    /**
     * 更新患者病例
     *
     * @param info 参数
     * @return 返回值
     */
    @POST("case/update")
    Observable<BaseResponse<String>> updatePatientCase(@Body Map<String, Object> info);

    /**
     * 合作医生添加备注
     *
     * @param info 参数
     * @return 返回值
     */
    @POST("colleborate/nickname")
    Observable<BaseResponse<String>> modifyNickName(@Body Map<String, Object> info);

    /**
     * 患者备注设置
     * 医生发起的修改，from为’d’，修改的是显示病人的昵称；病人发起的修改，from为’p’，修改的是显示医生的昵称
     *
     * @param info 参数
     * @return 返回值
     */
    @POST("dp/nickname")
    Observable<BaseResponse<String>> modifyNickNameByPatient(@Body Map<String, Object> info);

    /**
     * 医生扫码添加患者
     *
     * @param info 参数
     * @return 返回值
     */
    @POST("dp/scan/focus/patient")
    Observable<BaseResponse<PatientBean>> addPatientByScan(@Body Map<String, Object> info);

    /**
     * 拒绝患者申请
     *
     * @param info 参数
     * @return 返回值
     */
    @POST("dp/scan/against/patient")
    Observable<BaseResponse<String>> refusePatientApply(@Body Map<String, Object> info);

    /**
     * 同意患者申请
     *
     * @param info 参数
     * @return 返回值
     */
    @POST("dp/scan/agree/V2.0")
    Observable<BaseResponse<String>> agreePatientApply(@Body Map<String, Object> info);

    /**
     * 文件下载
     *
     * @param url
     * @return 返回值
     */
    @Streaming
    @GET
    Observable<ResponseBody> downloadFile(@Url String url);
}
