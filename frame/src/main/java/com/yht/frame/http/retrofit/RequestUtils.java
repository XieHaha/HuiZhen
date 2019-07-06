package com.yht.frame.http.retrofit;

import android.content.Context;

import com.google.gson.JsonObject;
import com.yht.frame.data.BaseResponse;
import com.yht.frame.data.Tasks;
import com.yht.frame.data.base.DoctorAuthBean;
import com.yht.frame.data.base.HospitalBean;
import com.yht.frame.data.base.LoginBean;
import com.yht.frame.data.bean.HospitalProductBean;
import com.yht.frame.data.bean.HospitalProductTypeBean;
import com.yht.frame.data.bean.PatientBean;
import com.yht.frame.http.listener.ResponseListener;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * 提交参数方式
 *
 * @author dundun
 */
public class RequestUtils {
    public static void weChatLogin(Context context, String code, String merchant, final ResponseListener listener) {
        Map<String, String> params = new HashMap<>(16);
        params.put("code", code);
        params.put("merchant", merchant);
        RetrofitManager.getApiUrlManager()
                       .weChatLogin(params)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, true, Tasks.WE_CHAT_LOGIN, listener));
    }

    public static void weChatBind(Context context, String openid, String unionid, String phone, String prepareId,
            String code, String merchant, final ResponseListener listener) {
        Map<String, String> params = new HashMap<>(16);
        params.put("openid", openid);
        params.put("unionid", unionid);
        params.put("phone", phone);
        params.put("prepareId", prepareId);
        params.put("verifyCode", code);
        params.put("merchant", merchant);
        RetrofitManager.getApiUrlManager()
                       .weChatBind(params)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, true, Tasks.WE_CHAT_BIND, listener));
    }

    public static void getVerifyCode(Context context, String phone, String merchant,
            final ResponseListener<BaseResponse> listener) {
        Map<String, String> params = new HashMap<>(16);
        params.put("phone", phone);
        params.put("merchant", merchant);
        RetrofitManager.getApiUrlManager()
                       .getVerifyCode(params)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, true, Tasks.GET_VERIFY_CODE, listener));
    }

    public static void login(Context context, String prepareId, String verifyCode,
            final ResponseListener<BaseResponse> listener) {
        Map<String, String> params = new HashMap<>(16);
        params.put("prepareId", prepareId);
        params.put("verifyCode", verifyCode);
        RetrofitManager.getApiUrlManager()
                       .login(params)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, true, Tasks.LOGIN_AND_REGISTER, listener));
    }

    public static void uploadImg(Context context, String token, File file,
            final ResponseListener<BaseResponse> listener) {
        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), reqFile);
        RetrofitManager.getApiUrlManager()
                       .uploadImg(token, body)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, Tasks.UPLOAD_FILE, listener));
    }

    public static void getHospitalListByAuth(Context context, String token,
            final ResponseListener<BaseResponse> listener) {
        RetrofitManager.getApiUrlManager()
                       .getHospitalListByAuth(token)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, true, Tasks.GET_HOSPITAL_LIST_BY_AUTH,
                                                                 listener));
    }

    public static void getDepartTree(Context context, String hospitalCode, String token,
            final ResponseListener<BaseResponse> listener) {
        Map<String, String> params = new HashMap<>(16);
        params.put("hospitalCode", hospitalCode);
        RetrofitManager.getApiUrlManager()
                       .getDepartTree(token, params)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, true, Tasks.GET_DEPART_LIST, listener));
    }

    public static void submitDoctorAuth(Context context, DoctorAuthBean bean, String token, String phone,
            final ResponseListener<BaseResponse> listener) {
        Map<String, Object> params = new HashMap<>(16);
        params.put("certFront", bean.getCertFront());
        params.put("certBack", bean.getCertBack());
        params.put("doctorName", bean.getDoctorName());
        params.put("doctorPhone", phone);
        params.put("doctorPhoto", bean.getDoctorPhoto());
        params.put("hospitalName", bean.getLastApplyHospitalName());
        params.put("jobTitle", bean.getJobTitle());
        params.put("departmentId", bean.getLastApplyDepartmentId());
        params.put("doctorSex", bean.getDoctorSex());
        RetrofitManager.getApiUrlManager()
                       .submitDoctorAuth(token, params)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, true, Tasks.SUBMIT_DOCTOR_AUTH, listener));
    }

    public static void getDoctorInfo(Context context, String token, String phone,
            final ResponseListener<BaseResponse> listener) {
        RetrofitManager.getApiUrlManager()
                       .getDoctorInfo(token, phone)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, true, Tasks.GET_DOCTOR_INFO, listener));
    }

    public static void getDoctorAuth(Context context, String token, String phone,
            final ResponseListener<BaseResponse> listener) {
        RetrofitManager.getApiUrlManager()
                       .getDoctorAuth(token, phone)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, true, Tasks.GET_DOCTOR_AUTH, listener));
    }

    public static void getDataByType(Context context, String token, String type,
            final ResponseListener<BaseResponse> listener) {
        Map<String, Object> params = new HashMap<>(16);
        params.put("type", type);
        RetrofitManager.getApiUrlManager()
                       .getDataByType(token, params)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, true, Tasks.DATA_JOB_TITLE, listener));
    }

    public static void getDoctorInfoAndBalanceInfo(Context context, String token,
            final ResponseListener<BaseResponse> listener) {
        RetrofitManager.getApiUrlManager()
                       .getDoctorInfoAndBalanceInfo(token)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, true, Tasks.GET_DOCTOR_INFO_AND_BALANCE_INFO,
                                                                 listener));
    }

    public static void getDoctorBalanceInfo(Context context, String doctorOrderTranId, String token,
            final ResponseListener<BaseResponse> listener) {
        Map<String, String> params = new HashMap<>(16);
        params.put("doctorOrderTranId", doctorOrderTranId);
        RetrofitManager.getApiUrlManager()
                       .getDoctorBalanceInfo(token, params)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(
                               new AbstractLoadViewObserver<>(context, true, Tasks.GET_DOCTOR_BALANCE_INFO, listener));
    }

    public static void getDoctorWithdraw(Context context, String doctorOrderTranId, String token,
            final ResponseListener<BaseResponse> listener) {
        Map<String, String> params = new HashMap<>(16);
        params.put("doctorOrderTranId", doctorOrderTranId);
        RetrofitManager.getApiUrlManager()
                       .getDoctorWithdraw(token, params)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, true, Tasks.GET_DOCTOR_WITHDRAW, listener));
    }

    public static void getDoctorWithdrawByMonth(Context context, String queryMonth, String token,
            final ResponseListener<BaseResponse> listener) {
        Map<String, String> params = new HashMap<>(16);
        params.put("query_month", queryMonth);
        RetrofitManager.getApiUrlManager()
                       .getDoctorWithdrawByMonth(token, params)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, true, Tasks.GET_DOCTOR_WITHDRAW_BY_MONTH,
                                                                 listener));
    }

    public static void getDoctorIncomeList(Context context, String pageSize, String startPage, String token,
            final ResponseListener<BaseResponse> listener) {
        Map<String, String> params = new HashMap<>(16);
        params.put("start_page", startPage);
        params.put("page_size", pageSize);
        RetrofitManager.getApiUrlManager()
                       .getDoctorIncomeList(token, params)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(
                               new AbstractLoadViewObserver<>(context, true, Tasks.GET_DOCTOR_INCOME_LIST, listener));
    }

    public static void getDoctorIncomeWithOutList(Context context, String pageSize, String startPage, String token,
            final ResponseListener<BaseResponse> listener) {
        Map<String, String> params = new HashMap<>(16);
        params.put("start_page", startPage);
        params.put("page_size", pageSize);
        RetrofitManager.getApiUrlManager()
                       .getDoctorIncomeWithOutList(token, params)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, true, Tasks.GET_DOCTOR_INCOME_WITHOUT_LIST,
                                                                 listener));
    }

    public static void getDoctorIncomeByMonthList(Context context, String queryMonth, String pageSize, String startPage,
            String token, final ResponseListener<BaseResponse> listener) {
        Map<String, String> params = new HashMap<>(16);
        params.put("query_month", queryMonth);
        params.put("start_page", startPage);
        params.put("page_size", pageSize);
        RetrofitManager.getApiUrlManager()
                       .getDoctorIncomeByMonthList(token, params)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, true, Tasks.GET_DOCTOR_INCOME_BY_MONTH_LIST,
                                                                 listener));
    }

    public static void getPatientListByDoctorCode(Context context, String code, String token,
            final ResponseListener<BaseResponse> listener) {
        Map<String, String> params = new HashMap<>(16);
        params.put("code ", code);
        RetrofitManager.getApiUrlManager()
                       .getPatientListByDoctorCode(token, params)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, Tasks.GET_PATIENT_LIST_BY_DOCTOR_CODE,
                                                                 listener));
    }

    public static void getPatientDetailByPatientCode(Context context, String code, String token,
            final ResponseListener<BaseResponse> listener) {
        Map<String, String> params = new HashMap<>(16);
        params.put("code ", code);
        RetrofitManager.getApiUrlManager()
                       .getPatientDetailByPatientCode(token, params)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(
                               new AbstractLoadViewObserver<>(context, true, Tasks.GET_PATIENT_DETAIL_BY_PATIENT_CODE,
                                                              listener));
    }

    /******************************以上为新接口 2019年7月5日14:03:44*************************************/
    public static void getSplash(Context context, String client, String deviceSystem, String versionCode,
            final ResponseListener listener) {
        Map<String, String> params = new HashMap<>(16);
        params.put("client", client);
        params.put("deviceSystem", deviceSystem);
        params.put("edition", versionCode);
        RetrofitManager.getApiUrlManager()
                       .getSplash(params)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, Tasks.GET_SPLASH, listener));
    }

    public static void getNewVersion(Context context, final ResponseListener listener) {
        Map<String, String> params = new HashMap<>(16);
        params.put("device_system", "android");
        params.put("client", "doctor");
        RetrofitManager.getApiUrlManager()
                       .getNewVersion(params)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, Tasks.UPDATE_VERSION, listener));
    }

    public static void getApplyPatientList(Context context, String doctorId, int pageNo, int pageSize,
            final ResponseListener<BaseResponse> listener) {
        Map<String, Object> merchant = new HashMap<>(16);
        merchant.put("doctorId", doctorId);
        merchant.put("pageNo", pageNo);
        merchant.put("pageSize", pageSize);
        RetrofitManager.getApiUrlManager()
                       .getApplyPatientList(merchant)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, Tasks.GET_APPLY_PATIENT_LIST, listener));
    }

    public static void getPatientList(Context context, String doctorId, int pageNo, int pageSize,
            final ResponseListener<BaseResponse> listener) {
        Map<String, Object> merchant = new HashMap<>(16);
        merchant.put("doctorId", doctorId);
        merchant.put("pageNo", pageNo);
        merchant.put("pageSize", pageSize);
        RetrofitManager.getApiUrlManager()
                       .getPatientList(merchant)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, Tasks.GET_PATIENTS_LIST, listener));
    }

    public static void getTransferList(Context context, String doctorId, int pageNo, int pageSize,
            final ResponseListener<BaseResponse> listener) {
        Map<String, Object> merchant = new HashMap<>(16);
        merchant.put("doctorId", doctorId);
        merchant.put("pageNo", pageNo);
        merchant.put("pageSize", pageSize);
        RetrofitManager.getApiUrlManager()
                       .getTransferList(merchant)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, Tasks.GET_TRANSFER_LIST, listener));
    }

    public static void getOrderList(Context context, String doctorId, int pageNo, int pageSize,
            final ResponseListener<BaseResponse> listener) {
        Map<String, Object> merchant = new HashMap<>(16);
        merchant.put("doctorId", doctorId);
        merchant.put("pageNo", pageNo);
        merchant.put("pageSize", pageSize);
        RetrofitManager.getApiUrlManager()
                       .getOrderList(merchant)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, Tasks.GET_ORDER_LIST, listener));
    }

    public static void getCooperateList(Context context, String doctorId, int pageNo, int pageSize,
            final ResponseListener<BaseResponse> listener) {
        Map<String, Object> merchant = new HashMap<>(16);
        merchant.put("doctorId", doctorId);
        merchant.put("pageNo", pageNo);
        merchant.put("pageSize", pageSize);
        RetrofitManager.getApiUrlManager()
                       .getCooperateList(merchant)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, Tasks.GET_COOPERATE_DOC_LIST, listener));
    }

    public static void applyCooperateDoc(Context context, String colleborateDoctorId, String doctorId,
            int requestSource, final ResponseListener<BaseResponse> listener) {
        Map<String, Object> merchant = new HashMap<>(16);
        merchant.put("doctorId", doctorId);
        merchant.put("colleborateDoctorId", colleborateDoctorId);
        merchant.put("requestSource", requestSource);
        RetrofitManager.getApiUrlManager()
                       .applyCooperateDoc(merchant)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, Tasks.APPLY_COOPERATE_DOC, listener));
    }

    public static void dealDocApply(Context context, String appliedId, String applyId, int proCode, int requestSource,
            final ResponseListener<BaseResponse> listener) {
        Map<String, Object> merchant = new HashMap<>(16);
        merchant.put("appliedId", appliedId);
        merchant.put("applyId", applyId);
        merchant.put("proCode", proCode);
        merchant.put("requestSource", requestSource);
        RetrofitManager.getApiUrlManager()
                       .dealDocApply(merchant)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, Tasks.DEAL_DOC_APPLY, listener));
    }

    public static void cancelCooperateDoc(Context context, String doctorId, String doctorId2,
            final ResponseListener<BaseResponse> listener) {
        Map<String, Object> merchant = new HashMap<>(16);
        merchant.put("id1", doctorId);
        merchant.put("id2", doctorId2);
        RetrofitManager.getApiUrlManager()
                       .cancelCooperateDoc(merchant)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, Tasks.CANCEL_COOPERATE_DOC, listener));
    }

    public static void getApplyCooperateList(Context context, String doctorId, int pageNo, int pageSize,
            final ResponseListener<BaseResponse> listener) {
        Map<String, Object> merchant = new HashMap<>(16);
        merchant.put("doctorId", doctorId);
        merchant.put("pageNo", pageNo);
        merchant.put("pageSize", pageSize);
        RetrofitManager.getApiUrlManager()
                       .getApplyCooperateList(merchant)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(
                               new AbstractLoadViewObserver<>(context, Tasks.GET_APPLY_COOPERATE_DOC_LIST, listener));
    }

    public static void qualifiyDoc(Context context, String doctorId, String name, String identityNumber, String title,
            String department, String hospital, File idFront, File idEnd, File qualifiedFront, File qualifiedEnd,
            final ResponseListener<BaseResponse> listener) {
        Map<String, Object> merchant = new HashMap<>(16);
        merchant.put("doctorId", doctorId);
        merchant.put("name", name);
        merchant.put("identityNumber", identityNumber);
        merchant.put("hospital", hospital);
        merchant.put("title", title);
        merchant.put("department", department);
        RequestBody idFrontBody = RequestBody.create(MediaType.parse("image/*"), idFront);
        RequestBody idEndBody = RequestBody.create(MediaType.parse("image/*"), idEnd);
        RequestBody qualifiedFrontBody = RequestBody.create(MediaType.parse("image/*"), qualifiedFront);
        RequestBody qualifiedEndBody = RequestBody.create(MediaType.parse("image/*"), qualifiedEnd);
        MultipartBody.Part[] file = new MultipartBody.Part[4];
        file[0] = MultipartBody.Part.createFormData("idFront", idFront.getName(), idFrontBody);
        file[1] = MultipartBody.Part.createFormData("idEnd", idEnd.getName(), idEndBody);
        file[2] = MultipartBody.Part.createFormData("qualifiedFront", qualifiedFront.getName(), qualifiedFrontBody);
        file[3] = MultipartBody.Part.createFormData("qualifiedEnd", qualifiedEnd.getName(), qualifiedEndBody);
        RetrofitManager.getApiUrlManager()
                       .qualifiyDoc(file, merchant)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, Tasks.QUALIFIY_DOC, listener));
    }

    public static void updateUserInfo(Context context, String doctorId, int fieldId, JsonObject json,
            final ResponseListener<BaseResponse> listener) {
        Map<String, Object> merchant = new HashMap<>(16);
        merchant.put("doctorId", doctorId);
        merchant.put("fieldId", fieldId);
        merchant.put("json", json);
        RetrofitManager.getApiUrlManager()
                       .updateUserInfo(merchant)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, Tasks.UPDATE_USER_INFO, listener));
    }

    public static void getTransferPatientToList(Context context, String doctorId, int pageNo, int pageSize,
            final ResponseListener<BaseResponse> listener) {
        Map<String, Object> merchant = new HashMap<>(16);
        merchant.put("doctorId", doctorId);
        merchant.put("pageNo", pageNo);
        merchant.put("pageSize", pageSize);
        RetrofitManager.getApiUrlManager()
                       .getTransferPatientToList(merchant)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, Tasks.GET_PATIENTS_TO_LIST, listener));
    }

    public static void getTransferPatientFromList(Context context, String doctorId, int pageNo, int pageSize,
            final ResponseListener<BaseResponse> listener) {
        Map<String, Object> merchant = new HashMap<>(16);
        merchant.put("doctorId", doctorId);
        merchant.put("pageNo", pageNo);
        merchant.put("pageSize", pageSize);
        RetrofitManager.getApiUrlManager()
                       .getTransferPatientFromList(merchant)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, Tasks.GET_PATIENTS_FROM_LIST, listener));
    }

    public static void getTransferByPatient(Context context, String doctorId, String patientId, int pageNo,
            int pageSize, int days, final ResponseListener<BaseResponse> listener) {
        Map<String, Object> merchant = new HashMap<>(16);
        merchant.put("doctorId", doctorId);
        merchant.put("patientId", patientId);
        merchant.put("pageNo", pageNo);
        merchant.put("pageSize", pageSize);
        //传0 代表不限制时间
        merchant.put("days", days);
        RetrofitManager.getApiUrlManager()
                       .getTransferByPatient(merchant)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, Tasks.GET_TRANSFER_BY_PATIENT, listener));
    }

    public static void getPatientOrders(Context context, String doctorId, String patientId, int page, int pageSize,
            final ResponseListener<BaseResponse> listener) {
        Map<String, Object> params = new HashMap<>(16);
        params.put("days", 0);
        params.put("pageNo", page);
        params.put("pageSize", pageSize);
        params.put("patientId", patientId);
        params.put("doctorId", doctorId);
        RetrofitManager.getApiUrlManager()
                       .getPatientOrders(params)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, Tasks.GET_PATIENT_ORDER_LIST, listener));
    }

    public static void getPatientLimitCaseList(Context context, String doctorId, String patientId,
            final ResponseListener<BaseResponse> listener) {
        Map<String, Object> merchant = new HashMap<>(16);
        merchant.put("doctorId", doctorId);
        merchant.put("patientId", patientId);
        RetrofitManager.getApiUrlManager()
                       .getPatientLimitCaseList(merchant)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, Tasks.GET_PATIENT_LIMIT_CASE_LIST, listener));
    }

    public static void deletePatientCase(Context context, String patientId, int fieldId, String caseCreatorId,
            String caseOperatorId, final ResponseListener<BaseResponse> listener) {
        Map<String, Object> merchant = new HashMap<>(16);
        merchant.put("patientId", patientId);
        merchant.put("fieldId", fieldId);
        merchant.put("caseCreatorId", caseCreatorId);
        merchant.put("caseOperatorId", caseOperatorId);
        RetrofitManager.getApiUrlManager()
                       .deletePatientCase(merchant)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, Tasks.DELETE_PATIENT_CASE, listener));
    }

    public static void getDocInfo(Context context, String doctorId, final ResponseListener<BaseResponse> listener) {
        RetrofitManager.getApiUrlManager()
                       .getDocInfo(doctorId)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, Tasks.GET_DOC_INFO, listener));
    }

    public static void getPatientInfo(Context context, String patientId,
            final ResponseListener<BaseResponse> listener) {
        RetrofitManager.getApiUrlManager()
                       .getPatientInfo(patientId)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, Tasks.GET_PATIENT_INFO, listener));
    }

    public static void getTransferDetailById(Context context, int transferId,
            final ResponseListener<BaseResponse> listener) {
        Map<String, Integer> merchant = new HashMap<>(16);
        merchant.put("transferId", transferId);
        RetrofitManager.getApiUrlManager()
                       .getTransferDetailById(merchant)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, Tasks.GET_TRANSFER_DETAIL_BY_ID, listener));
    }

    public static void cancelTransferPatient(Context context, int transferId,
            final ResponseListener<BaseResponse> listener) {
        Map<String, Integer> merchant = new HashMap<>(16);
        merchant.put("transferId", transferId);
        RetrofitManager.getApiUrlManager()
                       .cancelTransferPatient(merchant)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, Tasks.CANCEL_TRANSFER_PATIENT, listener));
    }

    public static void refuseTransferPatient(Context context, int transferId,
            final ResponseListener<BaseResponse> listener) {
        Map<String, Integer> merchant = new HashMap<>(16);
        merchant.put("transferId", transferId);
        RetrofitManager.getApiUrlManager()
                       .refuseTransferPatient(merchant)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, Tasks.REFUSE_TRANSFER_PATIENT, listener));
    }

    public static void recvTransferPatient(Context context, int transferId, String hospitalId,
            final ResponseListener<BaseResponse> listener) {
        Map<String, Object> merchant = new HashMap<>(16);
        merchant.put("transferId", transferId);
        merchant.put("hospitalId", hospitalId);
        RetrofitManager.getApiUrlManager()
                       .recvTransferPatient(merchant)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, Tasks.RECV_TRANSFER_PATIENT, listener));
    }

    public static void getHospitalListByDoctorId(Context context, String doctorId,
            final ResponseListener<BaseResponse> listener) {
        Map<String, Object> params = new HashMap<>(16);
        params.put("doctorId", doctorId);
        RetrofitManager.getApiUrlManager()
                       .getHospitalListByDoctorId(params)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(
                               new AbstractLoadViewObserver<>(context, Tasks.GET_HOSPITAL_LIST_BY_DOCTORID, listener));
    }

    public static void getCooperateHospitalDoctorList(Context context, String hospitalId, int pageNo, int pageSize,
            final ResponseListener<BaseResponse> listener) {
        Map<String, Object> merchant = new HashMap<>(16);
        merchant.put("hospitalId", hospitalId);
        merchant.put("pageNo", pageNo);
        merchant.put("pageSize", pageSize);
        RetrofitManager.getApiUrlManager()
                       .getCooperateHospitalDoctorList(merchant)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, Tasks.GET_COOPERATE_HOSPITAL_DOCTOR_LIST,
                                                                 listener));
    }

    public static void getHospitalProductListByHospitalId(Context context, String hospitalId,
            final ResponseListener<BaseResponse> listener) {
        Map<String, Object> params = new HashMap<>(16);
        params.put("hospitalId", hospitalId);
        RetrofitManager.getApiUrlManager()
                       .getHospitalProductListByHospitalId(params)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, Tasks.GET_HOSPITAL_PRODUCT_LIST_BY_HOSPITALID,
                                                                 listener));
    }

    public static void addProductOrderNew(Context context, String diagnosisInfo, LoginBean loginSuccessBean,
            PatientBean patientBean, HospitalBean curHospital, HospitalProductBean curProduct,
            HospitalProductTypeBean curProductType, final ResponseListener<BaseResponse> listener) {
        Map<String, Object> params = new HashMap<>(20);
        params.put("diagnosisInfo", diagnosisInfo);
        params.put("productTypeId", curProductType.getProductTypeId());
        params.put("productTypeName", curProductType.getProductTypeName());
        params.put("doctorId", loginSuccessBean.getDoctorCode());
        params.put("doctorName", loginSuccessBean.getDoctorName());
        params.put("hospitalName", curHospital.getHospitalName());
        params.put("patientSex", patientBean.getSex());
        params.put("patientId", patientBean.getPatientId());
        params.put("patientName", patientBean.getName());
        params.put("patientBirthDate", patientBean.getBirthDate());
        params.put("productDescription", curProduct.getProductDescription());
        params.put("productId", curProduct.getProductId());
        params.put("productName", curProduct.getProductName());
        params.put("productPrice", curProduct.getProductPrice());
        params.put("productPriceUnit", curProduct.getProductPriceUnit());
        RetrofitManager.getApiUrlManager()
                       .addProductOrderNew(params)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, Tasks.ADD_PRODUCT_ORDER_NEW, listener));
    }

    public static void addPatientByScanOrChangePatient(Context context, String doctorId, String fromDoctorId,
            String patientId, int requestSource, final ResponseListener<BaseResponse> listener) {
        Map<String, Object> merchant = new HashMap<>(16);
        merchant.put("toDoctorId", doctorId);
        merchant.put("fromDoctorId", fromDoctorId);
        merchant.put("patientId", patientId);
        merchant.put("requestSource", requestSource);
        RetrofitManager.getApiUrlManager()
                       .addPatientByScanOrChangePatient(merchant)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, Tasks.ADD_PATIENT_BY_SCAN_OR_CHANGE_PATIENT,
                                                                 listener));
    }

    public static void deletePatient(Context context, String doctorId, String patientId,
            final ResponseListener<BaseResponse> listener) {
        RetrofitManager.getApiUrlManager()
                       .deletePatient(doctorId, patientId)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, Tasks.DELETE_PATIENT, listener));
    }

    public static void getCooperateHospitalList(Context context, String doctorId,
            final ResponseListener<BaseResponse> listener) {
        Map<String, String> merchant = new HashMap<>(16);
        merchant.put("doctorId", doctorId);
        RetrofitManager.getApiUrlManager()
                       .getCooperateHospitalList(merchant)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, Tasks.GET_COOPERATE_HOSPITAL_LIST, listener));
    }

    public static void getDetailById(Context context, String fieldId, final ResponseListener<BaseResponse> listener) {
        Map<String, String> merchant = new HashMap<>(16);
        merchant.put("fieldId", fieldId);
        RetrofitManager.getApiUrlManager()
                       .getDetailById(merchant)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, Tasks.GET_CASE_DETAIL_BY_ID, listener));
    }

    public static void getPatientCombine(Context context, String patientId,
            final ResponseListener<BaseResponse> listener) {
        RetrofitManager.getApiUrlManager()
                       .getPatientCombine(patientId)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, Tasks.GET_PATIENT_COMBINE, listener));
    }

    public static void addPatientCase(Context context, Map<String, Object> params,
            final ResponseListener<BaseResponse> listener) {
        RetrofitManager.getApiUrlManager()
                       .addPatientCase(params)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, Tasks.ADD_PATIENT_CASE, listener));
    }

    public static void updatePatientCase(Context context, Map<String, Object> params,
            final ResponseListener<BaseResponse> listener) {
        RetrofitManager.getApiUrlManager()
                       .updatePatientCase(params)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, Tasks.ADD_PATIENT_CASE, listener));
    }

    public static void modifyNickName(Context context, String doctorId, String colleborateDoctorId, String nickname,
            final ResponseListener<BaseResponse> listener) {
        Map<String, Object> merchant = new HashMap<>(16);
        merchant.put("doctorId", doctorId);
        merchant.put("colleborateDoctorId", colleborateDoctorId);
        merchant.put("nickname", nickname);
        RetrofitManager.getApiUrlManager()
                       .modifyNickName(merchant)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, Tasks.MODIFY_NICK_NAME, listener));
    }

    public static void modifyNickNameByPatient(Context context, String doctorId, String patientId, String nickname,
            String from, final ResponseListener<BaseResponse> listener) {
        Map<String, Object> merchant = new HashMap<>(16);
        merchant.put("doctorId", doctorId);
        merchant.put("patientId", patientId);
        merchant.put("nickname", nickname);
        merchant.put("from", from);
        RetrofitManager.getApiUrlManager()
                       .modifyNickNameByPatient(merchant)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, Tasks.MODIFY_NICK_NAME_BY_PATIENT, listener));
    }

    public static void addPatientByScan(Context context, String doctorId, String patientId, int requestSource,
            final ResponseListener<BaseResponse> listener) {
        Map<String, Object> merchant = new HashMap<>(16);
        merchant.put("doctorId", doctorId);
        merchant.put("patientId", patientId);
        merchant.put("requestSource", requestSource);
        RetrofitManager.getApiUrlManager()
                       .addPatientByScan(merchant)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, Tasks.ADD_PATIENT_BY_SCAN_OR_CHANGE_PATIENT,
                                                                 listener));
    }

    public static void refusePatientApply(Context context, String doctorId, String patientId, int requestSource,
            final ResponseListener<BaseResponse> listener) {
        Map<String, Object> merchant = new HashMap<>(16);
        merchant.put("doctorId", doctorId);
        merchant.put("patientId", patientId);
        merchant.put("requestSource", requestSource);
        RetrofitManager.getApiUrlManager()
                       .refusePatientApply(merchant)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, Tasks.REFUSE_PATIENT_APPLY, listener));
    }

    public static void agreePatientApply(Context context, String doctorId, String patientId, int requestSource,
            final ResponseListener<BaseResponse> listener) {
        Map<String, Object> merchant = new HashMap<>(16);
        merchant.put("doctorId", doctorId);
        merchant.put("patientId", patientId);
        //操作者id
        merchant.put("fromId", doctorId);
        merchant.put("requestSource", requestSource);
        RetrofitManager.getApiUrlManager()
                       .agreePatientApply(merchant)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, Tasks.AGREE_PATIENT_APPLY, listener));
    }

    private static Map<String, String> addHeader(String token) {
        Map<String, String> header = new HashMap<>(16);
        header.put("token", token);
        return header;
    }
}

