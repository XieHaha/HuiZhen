package com.yht.frame.http.retrofit;

import com.yht.frame.data.BaseResponse;
import com.yht.frame.data.bean.BannerBean;
import com.yht.frame.data.bean.BaseListBean;
import com.yht.frame.data.bean.ChatTimeBean;
import com.yht.frame.data.bean.CheckBean;
import com.yht.frame.data.bean.CheckDetailBean;
import com.yht.frame.data.bean.CooperateHospitalBean;
import com.yht.frame.data.bean.DoctorAuthBean;
import com.yht.frame.data.bean.DoctorBean;
import com.yht.frame.data.bean.DoctorCurrencyBean;
import com.yht.frame.data.bean.DoctorCurrencyDetailBean;
import com.yht.frame.data.bean.HospitalBean;
import com.yht.frame.data.bean.HospitalDepartBean;
import com.yht.frame.data.bean.HospitalDepartChildBean;
import com.yht.frame.data.bean.HospitalProductBean;
import com.yht.frame.data.bean.HospitalTitleBean;
import com.yht.frame.data.bean.IncomeDetailBean;
import com.yht.frame.data.bean.LoginBean;
import com.yht.frame.data.bean.MessageTotalBean;
import com.yht.frame.data.bean.NotifyMessageBean;
import com.yht.frame.data.bean.OrderNumStatisticsBean;
import com.yht.frame.data.bean.PatientBean;
import com.yht.frame.data.bean.PatientOrderBean;
import com.yht.frame.data.bean.ProtocolBean;
import com.yht.frame.data.bean.ReceiverDoctorBean;
import com.yht.frame.data.bean.ReservationValidateBean;
import com.yht.frame.data.bean.SelectCheckTypeBean;
import com.yht.frame.data.bean.TransferBean;
import com.yht.frame.data.bean.VerifyCodeBean;
import com.yht.frame.data.bean.VersionBean;
import com.yht.frame.data.bean.WithDrawDetailBean;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

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
    @POST("client/wx/doctor-login")
    Observable<BaseResponse<LoginBean>> weChatLogin(@Body Map<String, String> info);

    /**
     * 微信绑定手机号
     *
     * @param info map参数
     * @return 返回
     */
    @POST("client/wx/doctor-bind")
    Observable<BaseResponse<LoginBean>> weChatBind(@Body Map<String, String> info);

    /**
     * 获取验证码
     *
     * @param info map参数
     * @return 返回值
     */
    @POST("client/get-verify-code")
    Observable<BaseResponse<VerifyCodeBean>> getVerifyCode(@Body Map<String, String> info);

    /**
     * 登录
     *
     * @param info map参数
     * @return 返回值
     */
    @POST("client/confirm-verify-code")
    Observable<BaseResponse<LoginBean>> login(@Body Map<String, String> info);

    /**
     * 登录
     *
     * @return 返回值
     */
    @GET("client/sys/doctor_protocol_last_date")
    Observable<BaseResponse<ProtocolBean>> getProtocolUpdateDate();

    /**
     * 上传图片
     *
     * @param token token
     * @param file  图片
     * @return 返回值
     */
    @Multipart
    @POST("client/file/upload_file")
    Observable<BaseResponse<String>> uploadImg(@Header("token") String token, @Part MultipartBody.Part file);

    /**
     * 上传图片  水印
     *
     * @param token token
     * @param file  图片
     * @return 返回值
     */
    @Multipart
    @POST("client/file/upload_image_file")
    Observable<BaseResponse<String>> uploadImgWaterMark(@Header("token") String token, @Part MultipartBody.Part file);

    /**
     * 获取消息列表
     *
     * @param info  map参数
     * @param token token
     * @return 返回值
     */
    @POST("client/msg/list/app")
    Observable<BaseResponse<List<NotifyMessageBean>>> getAppMessageList(@Header("token") String token,
            @Body Map<String, Integer> info);

    /**
     * 获取未读消息总数
     *
     * @param token token
     * @return 返回值
     */
    @GET("client/msg/unread/total")
    Observable<BaseResponse<MessageTotalBean>> getAppUnReadMessageTotal(@Header("token") String token);

    /**
     * 消息全部已读
     *
     * @param token token
     * @return 返回值
     */
    @PUT("client/msg/allRead")
    Observable<BaseResponse<String>> updateAppUnReadMessageAll(@Header("token") String token);

    /**
     * 单条消息已读
     *
     * @param token token
     * @param id    id
     * @return 返回值
     */
    @PUT("client/msg/read/{id}")
    Observable<BaseResponse<String>> updateAppUnReadMessageById(@Header("token") String token, @Path("id") int id);

    /**
     * 单条消息已读 通知点击
     *
     * @param token   token
     * @param unionId id
     * @return 返回值
     */
    @PUT("client/msg/readByUid/{unionId}")
    Observable<BaseResponse<String>> updateAppUnReadMessageByNotify(@Header("token") String token,
            @Path("unionId") String unionId);

    /**
     * 获取医院列表 （医生认证模块）
     *
     * @param token token
     * @return 返回值
     */
    @GET("client/hospital/selectInput")
    Observable<BaseResponse<List<HospitalBean>>> getHospitalListByAuth(@Header("token") String token);

    /**
     * 获取当前医生有预约转诊权限的合作医院。
     *
     * @param token token
     * @return 返回值
     */
    @GET("client/hospital/selectInput/forSelect")
    Observable<BaseResponse<List<HospitalBean>>> getHospitalListByReverse(@Header("token") String token);

    /**
     * 获取当前医生可进行转诊的医院列表。
     *
     * @param token token
     * @param info  info
     * @return 返回值
     */
    @POST("client/hospital/selectInput/forChange")
    Observable<BaseResponse<List<HospitalBean>>> getHospitalListByDoctor(@Header("token") String token,
            @Body Map<String, String> info);

    /**
     * 校验医生是否有预约检查和预约转诊的合作医院。
     *
     * @param token token
     * @return 返回值
     */
    @GET("client/hospital/validate/doctor/service")
    Observable<BaseResponse<ReservationValidateBean>> getValidateHospitalList(@Header("token") String token);

    /**
     * 获取当前医生有预约转诊权限的合作医院下面的一级科室
     *
     * @param token        token
     * @param hospitalCode code
     * @return 返回值
     */
    @GET("client/department/first/{hospitalCode}")
    Observable<BaseResponse<List<HospitalDepartBean>>> getDepartOneListByReverse(@Header("token") String token,
            @Path("hospitalCode") String hospitalCode);

    /**
     * 获取当前医生有预约转诊权限的合作医院下面的二级级科室
     *
     * @param token        token
     * @param hospitalCode code
     * @param pid          pid
     * @return 返回值
     */
    @GET("client/department/second/{hospitalCode}/{pid}")
    Observable<BaseResponse<List<HospitalDepartChildBean>>> getDepartTwoListByReverse(@Header("token") String token,
            @Path("hospitalCode") String hospitalCode, @Path("pid") int pid);

    /**
     * 获取当前医生有预约转诊权限的合作医院下面的医生
     *
     * @param token token
     * @param info  code
     * @return 返回值
     */
    @POST("client/doctor/zzDoctors")
    Observable<BaseResponse<List<ReceiverDoctorBean>>> getDoctorListByReverse(@Header("token") String token,
            @Body Map<String, Object> info);

    /**
     * 接诊医生
     *
     * @param token token
     * @param info  code
     * @return 返回值
     */
    @POST("client/doctor/jzDoctors")
    Observable<BaseResponse<List<ReceiverDoctorBean>>> getReceivingDoctorList(@Header("token") String token,
            @Body Map<String, Object> info);

    /**
     * 获取科室树
     *
     * @param token token
     * @param info  map参数
     * @return 返回值
     */
    @POST("client/department/tree")
    Observable<BaseResponse<List<HospitalDepartBean>>> getDepartTree(@Header("token") String token,
            @Body Map<String, String> info);

    /**
     * 提交医生认证数据
     *
     * @param token token
     * @param info  map参数
     * @return 返回值
     */
    @POST("client/doctor/auth")
    Observable<BaseResponse<LoginBean>> submitDoctorAuth(@Header("token") String token, @Body Map<String, Object> info);

    /**
     * 获取已提交认证信息
     *
     * @param token  token
     * @param mobile 手机号
     * @return 返回值
     */
    @GET("client/doctor/authInfo/{mobile}")
    Observable<BaseResponse<DoctorAuthBean>> getDoctorAuth(@Header("token") String token,
            @Path("mobile") String mobile);

    /**
     * 获取医生个人详情
     *
     * @param token  token
     * @param mobile 手机号
     * @return 返回值
     */
    @GET("client/doctor/model/{mobile}")
    Observable<BaseResponse<ReceiverDoctorBean>> getDoctorInfo(@Header("token") String token,
            @Path("mobile") String mobile);

    /**
     * 获取指定类型的字典数据
     *
     * @param token token
     * @param info  参数 比如jobTitle、depart等
     * @return 返回值
     */
    @GET("dm/query-by-type")
    Observable<BaseResponse<List<HospitalTitleBean>>> getDataByType(@Header("token") String token,
            @Query("type") String info);

    /**
     * 医生基本信息加收入信息(总收入 已到账 余额 本月收入 本月已到账)
     *
     * @param token token
     * @return 返回值
     */
    @GET("client/doctorcenter/doctor_balance_info")
    Observable<BaseResponse<DoctorCurrencyBean>> getDoctorInfoAndBalanceInfo(@Header("token") String token);

    /**
     * 医生收入信息 (预约检查+预约转诊+远程会珍)
     *
     * @param info  map参数
     * @param token token
     * @return 返回值
     */
    @GET("client/doctorcenter/doctor_income")
    Observable<BaseResponse<IncomeDetailBean>> getDoctorIncomeDetail(@Header("token") String token,
            @Query("doctorOrderTranId") int info);

    /**
     * 医生提现信息
     *
     * @param info  map参数
     * @param token token
     * @return 返回值
     */
    @GET("client/doctorcenter/doctor_cash")
    Observable<BaseResponse<WithDrawDetailBean>> getDoctorWithdraw(@Header("token") String token,
            @Query("doctorOrderTranId") int info);

    /**
     * 医生某月收入汇总 (纯收入(不包含提现) 预约检查+预约转诊+远程会珍)
     *
     * @param info  map参数
     * @param token token
     * @return 返回值
     */
    @GET("client/doctorcenter/doctor_cash_by_month")
    Observable<BaseResponse<List<HospitalTitleBean>>> getDoctorWithdrawByMonth(@Header("token") String token,
            @Query("query_month") String info);

    /**
     * 医生收入明细信息 (医生提现+预约检查+预约转诊+远程会珍)
     *
     * @param info  map参数
     * @param token token
     * @return 返回值
     */
    @GET("client/doctorcenter/doctor_tran_list")
    Observable<BaseResponse<List<DoctorCurrencyDetailBean>>> getDoctorIncomeList(@Header("token") String token,
            @QueryMap Map<String, Integer> info);

    /**
     * 医生收入明细信息 (不包含提现)
     *
     * @param info  map参数
     * @param token token
     * @return 返回值
     */
    @GET("client/doctorcenter/doctor_tran_list_only_income")
    Observable<BaseResponse<List<DoctorCurrencyDetailBean>>> getDoctorIncomeWithOutList(@Header("token") String token,
            @QueryMap Map<String, Object> info);

    /**
     * 医生某月收入明细信息 (纯收入(不包含提现) 预约检查+预约转诊+远程会珍)
     *
     * @param info  map参数
     * @param token token
     * @return 返回值
     */
    @GET("client/doctorcenter/doctor_tran_list_by_month")
    Observable<BaseResponse<List<DoctorCurrencyDetailBean>>> getDoctorIncomeByMonthList(@Header("token") String token,
            @QueryMap Map<String, Integer> info);

    /**
     * 根据医生编码获取患者列表信息
     *
     * @param info  map参数
     * @param token token
     * @return 返回值
     */
    @GET("client/patient/getbydoccode")
    Observable<BaseResponse<List<PatientBean>>> getPatientListByDoctorCode(@Header("token") String token,
            @Query("code") String info);

    /**
     * 获取医生好友列表信息
     *
     * @param token token
     * @return 返回值
     */
    @GET("client/doctor/friends")
    Observable<BaseResponse<List<DoctorBean>>> getDoctorListByDoctorCode(@Header("token") String token);

    /**
     * 根据患者编码获取患者信息
     *
     * @param info  map参数
     * @param token token
     * @return 返回值
     */
    @GET("client/patient/getbypatcode")
    Observable<BaseResponse<PatientBean>> getPatientDetailByPatientCode(@Header("token") String token,
            @Query("code") String info);

    /**
     * 根据患者编码获取患者订单记录
     *
     * @param info  map参数
     * @param token token
     * @return 返回值
     */
    @GET("order/query-patient-order")
    Observable<BaseResponse<List<PatientOrderBean>>> getPatientOrderListByPatientCode(@Header("token") String token,
            @QueryMap Map<String, Object> info);

    /**
     * 患者验证（根据身份证号、姓名验证是否匹配）
     *
     * @param info  map参数
     * @param token token
     * @return 返回值
     */
    @GET("client/patient/getpatbyidcardname")
    Observable<BaseResponse<PatientBean>> verifyPatient(@Header("token") String token,
            @QueryMap Map<String, String> info);

    /**
     * 查询患者是否存在未完成的转诊单
     *
     * @param info  map参数
     * @param token token
     * @return 返回值
     */
    @GET("order-transfer/exist")
    Observable<BaseResponse<Boolean>> getPatientExistTransfer(@Header("token") String token,
            @Query("patientCode") String info);

    /**
     * 新增预约检查订单
     *
     * @param info  map参数
     * @param token token
     * @return 返回值
     */
    @POST("order-check/create")
    Observable<BaseResponse<String>> addReserveCheckOrder(@Header("token") String token,
            @Body Map<String, Object> info);

    /**
     * 查询检查订单列表
     *
     * @param info  参数
     * @param token token
     * @return 返回值
     */
    @GET("order-check/query-doctor-order")
    Observable<BaseResponse<List<CheckBean>>> getReserveCheckOrderList(@Header("token") String token,
            @QueryMap Map<String, Integer> info);

    /**
     * 查询检查订单详情
     *
     * @param info  参数
     * @param token token
     * @return 返回值
     */
    @GET("order-check/query-detail")
    Observable<BaseResponse<CheckDetailBean>> getReserveCheckOrderDetail(@Header("token") String token,
            @Query("orderNo") String info);

    /**
     * 添加检查项目查询(医生端预约检查、选择检查项目)
     *
     * @param info  map参数
     * @param token token
     * @return 返回值
     */
    @GET("client/product/checkdoccode")
    Observable<BaseResponse<List<SelectCheckTypeBean>>> getCheckTypeList(@Header("token") String token,
            @QueryMap Map<String, Object> info);

    /**
     * 添加检查项目查询 (获取医院下面的)
     *
     * @param info  map参数
     * @param token token
     * @return 返回值
     */
    @GET("client/product/checkhoscode")
    Observable<BaseResponse<List<SelectCheckTypeBean>>> getCheckTypeByHospitalList(@Header("token") String token,
            @QueryMap Map<String, Object> info);

    /**
     * 新增预约转诊订单
     *
     * @param info  map参数
     * @param token token
     * @return 返回值
     */
    @POST("order-transfer/add")
    Observable<BaseResponse<String>> addReserveTransferOrder(@Header("token") String token,
            @Body Map<String, Object> info);

    /**
     * 取消预约转诊订单
     *
     * @param info  map参数
     * @param token token
     * @return 返回值
     */
    @POST("order-transfer/cancel")
    Observable<BaseResponse<String>> cancelReserveTransferOrder(@Header("token") String token,
            @Body Map<String, String> info);

    /**
     * 接受预约转诊订单
     *
     * @param info  map参数
     * @param token token
     * @return 返回值
     */
    @POST("order-transfer/receive")
    Observable<BaseResponse<String>> receiveReserveTransferOrder(@Header("token") String token,
            @Body Map<String, String> info);

    /**
     * 变更接诊信息
     *
     * @param info  map参数
     * @param token token
     * @return 返回值
     */
    @POST("order-transfer/updateReceiveInfo")
    Observable<BaseResponse<String>> updateReserveTransferOrder(@Header("token") String token,
            @Body Map<String, String> info);

    /**
     * 拒绝预约转诊订单
     *
     * @param info  map参数
     * @param token token
     * @return 返回值
     */
    @POST("order-transfer/reject")
    Observable<BaseResponse<String>> rejectReserveTransferOrder(@Header("token") String token,
            @Body Map<String, String> info);

    /**
     * 再次转诊给其他医生
     *
     * @param info  map参数
     * @param token token
     * @return 返回值
     */
    @POST("order-transfer/transferAgain")
    Observable<BaseResponse<String>> transferAgainOtherDoctor(@Header("token") String token,
            @Body Map<String, String> info);

    /**
     * 查询发起的转诊记录
     *
     * @param info  map参数
     * @param token token
     * @return 返回值
     */
    @GET("order-transfer/initiate/list")
    Observable<BaseResponse<List<TransferBean>>> getInitiateTransferOrderList(@Header("token") String token,
            @QueryMap Map<String, Integer> info);

    /**
     * 查询转诊记录  根据状态
     *
     * @param info  map参数
     * @param token token
     * @return 返回值
     */
    @GET("order-transfer/status/list")
    Observable<BaseResponse<List<TransferBean>>> getTransferStatusOrderList(@Header("token") String token,
            @QueryMap Map<String, Integer> info);

    /**
     * 查询转诊详情
     *
     * @param info  map参数
     * @param token token
     * @return 返回值
     */
    @GET("order-transfer/detail/get")
    Observable<BaseResponse<TransferBean>> getTransferOrderDetail(@Header("token") String token,
            @Query("orderNo") String info);

    /**
     * 工作室 所有订单数量
     *
     * @param token token
     * @return 返回值
     */
    @GET("order/studioOrderStatistics")
    Observable<BaseResponse<OrderNumStatisticsBean>> getStudioOrderStatistics(@Header("token") String token);

    /**
     * 工作室 banner 列表
     *
     * @param token token
     * @return 返回值
     */
    @GET("client/banner/banner_list")
    Observable<BaseResponse<List<BannerBean>>> getBanner(@Header("token") String token);

    /**
     * 版本更新
     *
     * @param info  map参数
     * @param token token
     * @return 返回值
     */
    @GET("version/current-version")
    Observable<BaseResponse<VersionBean>> getVersion(@Header("token") String token, @Query("device") String info);

    /**
     * 添加聊天（开始聊天）
     *
     * @param info  map参数
     * @param token token
     * @return 返回值
     */
    @POST("client/chatcountdown/add")
    Observable<BaseResponse<ChatTimeBean>> startChat(@Header("token") String token, @Body Map<String, String> info);

    /**
     * 获取剩余时间
     *
     * @param info  map参数
     * @param token token
     * @return 返回值
     */
    @POST("client/chatcountdown/getstoptime")
    Observable<BaseResponse<ChatTimeBean>> getChatLastTime(@Header("token") String token,
            @Body Map<String, String> info);

    /**
     * 结束聊天
     *
     * @param info  map参数
     * @param token token
     * @return 返回值
     */
    @POST("client/chatcountdown/updatestoptime")
    Observable<BaseResponse<String>> endChat(@Header("token") String token, @Body Map<String, String> info);
    //二期 3.0.1

    /**
     * 获取当前医生的合作医院列表
     *
     * @param info  map参数
     * @param token token
     * @return 返回值
     */
    @POST("client/hospital/cooperate/list")
    Observable<BaseResponse<BaseListBean<CooperateHospitalBean>>> getCooperateHospitalList(
            @Header("token") String token, @Body Map<String, Integer> info);

    /**
     * 获取当前医生的合作医院下服务项目列表
     *
     * @param info  map参数
     * @param token token
     * @return 返回值
     */
    @POST("client/product/list")
    Observable<BaseResponse<BaseListBean<HospitalProductBean>>> getCooperateHospitalProjectList(
            @Header("token") String token, @Body Map<String, Object> info);

    /**
     * 获取医生二维码（医生端扫描）
     *
     * @param token token
     * @return 返回值
     */
    @GET("client/doctor/qr/user")
    Observable<BaseResponse<VersionBean>> getDoctorQrCode(@Header("token") String token);

    /**
     * 获取医生二维码（微信端扫描）
     *
     * @param token token
     * @return 返回值
     */
    @GET("client/doctor/qr/wx")
    Observable<BaseResponse<VersionBean>> getDoctorQrCodeByWeChat(@Header("token") String token);
}
