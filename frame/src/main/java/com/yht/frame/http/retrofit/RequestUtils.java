package com.yht.frame.http.retrofit;

import android.content.Context;
import android.text.TextUtils;

import com.yht.frame.data.BaseData;
import com.yht.frame.data.BaseResponse;
import com.yht.frame.data.Tasks;
import com.yht.frame.data.bean.DoctorAuthBean;
import com.yht.frame.data.bean.ReserveCheckBean;
import com.yht.frame.data.bean.ReserveRemoteBean;
import com.yht.frame.data.bean.ReserveTransferBean;
import com.yht.frame.http.listener.ResponseListener;

import java.io.File;
import java.util.HashMap;
import java.util.List;
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

    public static void login(Context context, String prepareId, String phone, String verifyCode, String merchant,
            final ResponseListener<BaseResponse> listener) {
        Map<String, String> params = new HashMap<>(16);
        params.put("merchant", merchant);
        params.put("phone", phone);
        params.put("prepareId", prepareId);
        params.put("verifyCode", verifyCode);
        RetrofitManager.getApiUrlManager()
                       .login(params)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, true, false, Tasks.LOGIN_AND_REGISTER,
                                                                 listener));
    }

    public static void updateIntroduce(Context context, String token, String introduce,
            final ResponseListener<BaseResponse> listener) {
        Map<String, String> params = new HashMap<>(16);
        params.put("introduce", introduce);
        RetrofitManager.getApiUrlManager()
                       .updateIntroduce(token, params)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(
                               new AbstractLoadViewObserver<>(context, true, false, Tasks.UPDATE_INTRODUCE, listener));
    }

    public static void getProtocolUpdateDate(Context context, final ResponseListener<BaseResponse> listener) {
        RetrofitManager.getApiUrlManager()
                       .getProtocolUpdateDate()
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(
                               new AbstractLoadViewObserver<>(context, true, Tasks.GET_PROTOCOL_UPDATE_DATE, listener));
    }

    public static void uploadImg(Context context, String token, File file,
            final ResponseListener<BaseResponse> listener) {
        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), reqFile);
        RetrofitManager.getApiUrlManager()
                       .uploadImg(token, body)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, true, Tasks.UPLOAD_FILE, listener));
    }

    public static void uploadImgWaterMark(Context context, String token, File file, boolean showDialog,
            final ResponseListener<BaseResponse> listener) {
        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), reqFile);
        RetrofitManager.getApiUrlManager()
                       .uploadImgWaterMark(token, body)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, showDialog, Tasks.UPLOAD_FILE, listener));
    }

    public static void getAppMessageList(Context context, String token, int pageSize, int startPage,
            final ResponseListener<BaseResponse> listener) {
        Map<String, Integer> params = new HashMap<>(16);
        params.put("pageSize", pageSize);
        params.put("startPage", startPage);
        RetrofitManager.getApiUrlManager()
                       .getAppMessageList(token, params)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, Tasks.GET_APP_MESSAGE_LIST, listener));
    }

    public static void getAppUnReadMessageTotal(Context context, String token,
            final ResponseListener<BaseResponse> listener) {
        RetrofitManager.getApiUrlManager()
                       .getAppUnReadMessageTotal(token)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(
                               new AbstractLoadViewObserver<>(context, Tasks.GET_APP_UNREAD_MESSAGE_TOTAL, listener));
    }

    public static void updateAppUnReadMessageAll(Context context, String token,
            final ResponseListener<BaseResponse> listener) {
        RetrofitManager.getApiUrlManager()
                       .updateAppUnReadMessageAll(token)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(
                               new AbstractLoadViewObserver<>(context, Tasks.UPDATE_APP_UNREAD_MESSAGE_ALL, listener));
    }

    public static void updateAppUnReadMessageById(Context context, String token, int id,
            final ResponseListener<BaseResponse> listener) {
        RetrofitManager.getApiUrlManager()
                       .updateAppUnReadMessageById(token, id)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, Tasks.UPDATE_APP_UNREAD_MESSAGE_BY_ID,
                                                                 listener));
    }

    public static void updateAppUnReadMessageByNotify(Context context, String token, String id,
            final ResponseListener<BaseResponse> listener) {
        RetrofitManager.getApiUrlManager()
                       .updateAppUnReadMessageByNotify(token, id)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, Tasks.UPDATE_APP_UNREAD_MESSAGE_BY_NOTIFY,
                                                                 listener));
    }

    public static void getHospitalListByAuth(Context context, String token,
            final ResponseListener<BaseResponse> listener) {
        RetrofitManager.getApiUrlManager()
                       .getHospitalListByAuth(token)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, true, Tasks.GET_HOSPITAL_LIST_BY_AUTH,
                                                                 listener));
    }

    public static void getHospitalListByReverse(Context context, String token,
            final ResponseListener<BaseResponse> listener) {
        RetrofitManager.getApiUrlManager()
                       .getHospitalListByReverse(token)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, true, Tasks.GET_HOSPITAL_LIST_BY_RESERVE,
                                                                 listener));
    }

    public static void getHospitalListByTransferOther(Context context, String token, String orderNo,
            final ResponseListener<BaseResponse> listener) {
        RetrofitManager.getApiUrlManager()
                       .getHospitalListByTransferOther(token, orderNo)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(
                               new AbstractLoadViewObserver<>(context, true, Tasks.GET_HOSPITAL_LIST_BY_TRANSFER_OTHER,
                                                              listener));
    }

    public static void getHospitalListByReceive(Context context, String token,
            final ResponseListener<BaseResponse> listener) {
        RetrofitManager.getApiUrlManager()
                       .getHospitalListByReceive(token)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, true, Tasks.GET_HOSPITAL_LIST_BY_RECEIVE,
                                                                 listener));
    }

    public static void getValidateHospitalList(Context context, String token,
            final ResponseListener<BaseResponse> listener) {
        RetrofitManager.getApiUrlManager()
                       .getValidateHospitalList(token)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, Tasks.GET_VALIDATE_HOSPITAL_LIST, listener));
    }

    public static void getDepartOneListByReverse(Context context, String token, String hospitalCode,
            final ResponseListener<BaseResponse> listener) {
        RetrofitManager.getApiUrlManager()
                       .getDepartOneListByReverse(token, hospitalCode)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(
                               new AbstractLoadViewObserver<>(context, Tasks.GET_DEPART_ONE_LIST_BY_REVERSE, listener));
    }

    public static void getDepartTwoListByReverse(Context context, String token, String hospitalCode, int pid,
            final ResponseListener<BaseResponse> listener) {
        RetrofitManager.getApiUrlManager()
                       .getDepartTwoListByReverse(token, hospitalCode, pid)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(
                               new AbstractLoadViewObserver<>(context, Tasks.GET_DEPART_TWO_LIST_BY_REVERSE, listener));
    }

    public static void getDoctorListByReverse(Context context, String token, Map<String, Object> params,
            final ResponseListener<BaseResponse> listener) {
        RetrofitManager.getApiUrlManager()
                       .getDoctorListByReverse(token, params)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, Tasks.GET_DOCTOR_LIST_BY_REVERSE, listener));
    }

    public static void getReceivingDoctorList(Context context, String token, Map<String, Object> params,
            final ResponseListener<BaseResponse> listener) {
        RetrofitManager.getApiUrlManager()
                       .getReceivingDoctorList(token, params)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, Tasks.GET_RECEIVING_DOCTOR_LIST, listener));
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
            String merchant, final ResponseListener<BaseResponse> listener) {
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
        params.put("merchant", merchant);
        RetrofitManager.getApiUrlManager()
                       .submitDoctorAuth(token, params)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, true, false, Tasks.SUBMIT_DOCTOR_AUTH,
                                                                 listener));
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
        RetrofitManager.getApiUrlManager()
                       .getDataByType(token, type)
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

    public static void getDoctorIncomeDetail(Context context, int doctorOrderTranId, String token,
            final ResponseListener<BaseResponse> listener) {
        RetrofitManager.getApiUrlManager()
                       .getDoctorIncomeDetail(token, doctorOrderTranId)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(
                               new AbstractLoadViewObserver<>(context, true, Tasks.GET_DOCTOR_INCOME_DETAIL, listener));
    }

    public static void getDoctorWithdraw(Context context, int doctorOrderTranId, String token,
            final ResponseListener<BaseResponse> listener) {
        RetrofitManager.getApiUrlManager()
                       .getDoctorWithdraw(token, doctorOrderTranId)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, true, Tasks.GET_DOCTOR_WITHDRAW, listener));
    }

    public static void getDoctorWithdrawByMonth(Context context, String queryMonth, String token,
            final ResponseListener<BaseResponse> listener) {
        RetrofitManager.getApiUrlManager()
                       .getDoctorWithdrawByMonth(token, queryMonth)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, true, Tasks.GET_DOCTOR_WITHDRAW_BY_MONTH,
                                                                 listener));
    }

    public static void getDoctorIncomeList(Context context, String token, int pageSize, int startPage,
            final ResponseListener<BaseResponse> listener) {
        Map<String, Integer> params = new HashMap<>(16);
        params.put("start_page", startPage);
        params.put("page_size", pageSize);
        RetrofitManager.getApiUrlManager()
                       .getDoctorIncomeList(token, params)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(
                               new AbstractLoadViewObserver<>(context, true, Tasks.GET_DOCTOR_INCOME_LIST, listener));
    }

    public static void getDoctorIncomeWithOutList(Context context, String token, int pageSize, int startPage,
            final ResponseListener<BaseResponse> listener) {
        Map<String, Object> params = new HashMap<>(16);
        params.put("start_page", startPage);
        params.put("page_size", pageSize);
        RetrofitManager.getApiUrlManager()
                       .getDoctorIncomeWithOutList(token, params)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, true, Tasks.GET_DOCTOR_INCOME_WITHOUT_LIST,
                                                                 listener));
    }

    public static void getDoctorIncomeByMonthList(Context context, String token, int queryMonth, int pageSize,
            int startPage, final ResponseListener<BaseResponse> listener) {
        Map<String, Integer> params = new HashMap<>(16);
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
        RetrofitManager.getApiUrlManager()
                       .getPatientListByDoctorCode(token, code)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, Tasks.GET_PATIENT_LIST_BY_DOCTOR_CODE,
                                                                 listener));
    }

    public static void getDoctorListByDoctorCode(Context context, String token,
            final ResponseListener<BaseResponse> listener) {
        RetrofitManager.getApiUrlManager()
                       .getDoctorListByDoctorCode(token)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, Tasks.GET_DOCTOR_LIST, listener));
    }

    public static void getPatientDetailByPatientCode(Context context, String code, String token,
            final ResponseListener<BaseResponse> listener) {
        RetrofitManager.getApiUrlManager()
                       .getPatientDetailByPatientCode(token, code)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, Tasks.GET_PATIENT_DETAIL_BY_PATIENT_CODE,
                                                                 listener));
    }

    public static void getPatientOrderListByPatientCode(Context context, String patientCode, String token, int pageSize,
            int startPage, boolean showLoadView, final ResponseListener<BaseResponse> listener) {
        Map<String, Object> params = new HashMap<>(16);
        params.put("patientCode", patientCode);
        params.put("pageSize", pageSize);
        params.put("startPage", startPage);
        RetrofitManager.getApiUrlManager()
                       .getPatientOrderListByPatientCode(token, params)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, showLoadView,
                                                                 Tasks.GET_PATIENT_ORDER_LIST_BY_PATIENT_CODE,
                                                                 listener));
    }

    public static void verifyPatient(Context context, String token, String idCard,
            final ResponseListener<BaseResponse> listener) {
        Map<String, String> params = new HashMap<>(16);
        params.put("idCard", idCard);
        RetrofitManager.getApiUrlManager()
                       .verifyPatient(token, params)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, true, Tasks.VERIFY_PATIENT, listener));
    }

    public static void getPatientExistTransfer(Context context, String token, String patientCode,
            final ResponseListener<BaseResponse> listener) {
        RetrofitManager.getApiUrlManager()
                       .getPatientExistTransfer(token, patientCode)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, true, Tasks.GET_PATIENT_EXIST_TRANSFER,
                                                                 listener));
    }

    public static void addReserveCheckOrder(Context context, String token, ReserveCheckBean bean,
            final ResponseListener<BaseResponse> listener) {
        Map<String, Object> params = new HashMap<>(16);
        params.put("age", bean.getAge());
        params.put("allergyHistory", bean.getAllergyHistory());
        params.put("confirmPhoto", bean.getConfirmPhoto());
        params.put("familyHistory", bean.getFamilyHistory());
        params.put("idCardNo", bean.getIdCardNo());
        params.put("initResult", bean.getInitResult());
        //        params.put("isPregnancy", bean.getIsPregnancy());
        params.put("pastHistory", bean.getPastHistory());
        params.put("phone", bean.getPhone());
        params.put("patientCode", bean.getPatientCode());
        params.put("patientName", bean.getPatientName());
        params.put("payType", bean.getPayType());
        params.put("sex", bean.getSex());
        params.put("checkTrans", bean.getCheckTrans());
        RetrofitManager.getApiUrlManager()
                       .addReserveCheckOrder(token, params)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, true, false, Tasks.ADD_RESERVE_CHECK_ORDER,
                                                                 listener));
    }

    public static void getReserveCheckOrderList(Context context, String token, int pageSize, int startPage,
            boolean showLoading, final ResponseListener<BaseResponse> listener) {
        Map<String, Integer> params = new HashMap<>(16);
        params.put("pageSize", pageSize);
        params.put("startPage", startPage);
        RetrofitManager.getApiUrlManager()
                       .getReserveCheckOrderList(token, params)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(
                               new AbstractLoadViewObserver<>(context, showLoading, Tasks.GET_RESERVE_CHECK_ORDER_LIST,
                                                              listener));
    }

    public static void getReserveCheckOrderDetail(Context context, String token, String orderNo,
            final ResponseListener<BaseResponse> listener) {
        RetrofitManager.getApiUrlManager()
                       .getReserveCheckOrderDetail(token, orderNo)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, true, Tasks.GET_RESERVE_CHECK_ORDER_DETAIL,
                                                                 listener));
    }

    public static void getCheckTypeList(Context context, String token, String doctorCode, String projectName, int page,
            final ResponseListener<BaseResponse> listener) {
        Map<String, Object> params = new HashMap<>(16);
        params.put("currentPage", page);
        params.put("docCode", doctorCode);
        if (!TextUtils.isEmpty(projectName)) {
            params.put("projectName", projectName);
        }
        RetrofitManager.getApiUrlManager()
                       .getCheckTypeList(token, params)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, Tasks.GET_CHECK_TYPE, listener));
    }

    public static void getCheckTypeByHospitalList(Context context, String token, String hospitalCode,
            String projectName, String selectCodes, int page, final ResponseListener<BaseResponse> listener) {
        Map<String, Object> params = new HashMap<>(16);
        params.put("currentPage", page);
        params.put("hospitalCode", hospitalCode);
        if (!TextUtils.isEmpty(projectName)) {
            params.put("projectName", projectName);
        }
        if (!TextUtils.isEmpty(selectCodes)) {
            params.put("selectCodes", selectCodes);
        }
        RetrofitManager.getApiUrlManager()
                       .getCheckTypeByHospitalList(token, params)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, true, Tasks.GET_CHECK_TYPE_BY_HOSPITAL,
                                                                 listener));
    }

    public static void addReserveTransferOrder(Context context, String token, ReserveTransferBean bean,
            final ResponseListener<BaseResponse> listener) {
        Map<String, Object> params = new HashMap<>(16);
        params.put("allergyHistory", bean.getAllergyHistory());
        params.put("confirmPhoto", bean.getConfirmPhoto());
        params.put("familyHistory", bean.getFamilyHistory());
        params.put("initResult", bean.getInitResult());
        params.put("pastHistory", bean.getPastHistory());
        params.put("patientAge", bean.getPatientAge());
        params.put("patientCode", bean.getPatientCode());
        params.put("patientIdCardNo", bean.getPatientIdCardNo());
        params.put("patientMobile", bean.getPatientMobile());
        params.put("patientName", bean.getPatientName());
        params.put("payType", bean.getPayType());
        params.put("receiveDoctorCode", bean.getReceiveDoctorCode());
        params.put("sex", bean.getSex());
        params.put("transferTarget", bean.getTransferTarget());
        params.put("transferType", bean.getTransferType());
        RetrofitManager.getApiUrlManager()
                       .addReserveTransferOrder(token, params)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, true, false, Tasks.ADD_RESERVE_TRANSFER_ORDER,
                                                                 listener));
    }

    public static void cancelReserveTransferOrder(Context context, String token, String cancelReason, String orderNo,
            final ResponseListener<BaseResponse> listener) {
        Map<String, String> params = new HashMap<>(16);
        params.put("cancelReason", cancelReason);
        params.put("orderNo", orderNo);
        RetrofitManager.getApiUrlManager()
                       .cancelReserveTransferOrder(token, params)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(
                               new AbstractLoadViewObserver<>(context, true, false, Tasks.CANCEL_RESERVE_TRANSFER_ORDER,
                                                              listener));
    }

    public static void receiveReserveTransferOrder(Context context, String token, String receiveHospitalCode,
            String orderNo, String appointAt, String note, final ResponseListener<BaseResponse> listener) {
        Map<String, String> params = new HashMap<>(16);
        params.put("appointAt", appointAt);
        params.put("orderNo", orderNo);
        params.put("receiveHospitalCode", receiveHospitalCode);
        params.put("note", note);
        RetrofitManager.getApiUrlManager()
                       .receiveReserveTransferOrder(token, params)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, true, false,
                                                                 Tasks.RECEIVE_RESERVE_TRANSFER_ORDER, listener));
    }

    public static void updateReserveTransferOrder(Context context, String token, String receiveHospitalCode,
            String orderNo, String appointAt, String note, final ResponseListener<BaseResponse> listener) {
        Map<String, String> params = new HashMap<>(16);
        params.put("appointAt", appointAt);
        params.put("orderNo", orderNo);
        params.put("receiveHospitalCode", receiveHospitalCode);
        params.put("note", note);
        RetrofitManager.getApiUrlManager()
                       .updateReserveTransferOrder(token, params)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(
                               new AbstractLoadViewObserver<>(context, true, false, Tasks.UPDATE_RESERVE_TRANSFER_ORDER,
                                                              listener));
    }

    public static void rejectReserveTransferOrder(Context context, String token, String rejectReason, String orderNo,
            final ResponseListener<BaseResponse> listener) {
        Map<String, String> params = new HashMap<>(16);
        params.put("orderNo", orderNo);
        params.put("rejectReason", rejectReason);
        RetrofitManager.getApiUrlManager()
                       .rejectReserveTransferOrder(token, params)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(
                               new AbstractLoadViewObserver<>(context, true, false, Tasks.REJECT_RESERVE_TRANSFER_ORDER,
                                                              listener));
    }

    public static void transferAgainOtherDoctor(Context context, String token, String orderNo, String targetDoctorCode,
            String transferReason, final ResponseListener<BaseResponse> listener) {
        Map<String, String> params = new HashMap<>(16);
        params.put("orderNo", orderNo);
        params.put("targetDoctorCode", targetDoctorCode);
        params.put("transferReason", transferReason);
        RetrofitManager.getApiUrlManager()
                       .transferAgainOtherDoctor(token, params)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(
                               new AbstractLoadViewObserver<>(context, true, false, Tasks.TRANSFER_AGAIN_OTHER_DOCTOR,
                                                              listener));
    }

    public static void getInitiateTransferOrderList(Context context, String token, int pageSize, int startPage,
            boolean showLoading, final ResponseListener<BaseResponse> listener) {
        Map<String, Integer> params = new HashMap<>(16);
        params.put("pageSize", pageSize);
        params.put("startPage", startPage);
        RetrofitManager.getApiUrlManager()
                       .getInitiateTransferOrderList(token, params)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, showLoading,
                                                                 Tasks.GET_INITIATE_TRANSFER_ORDER_LIST, listener));
    }

    public static void getTransferStatusOrderList(Context context, String token, int receiveStatus, int pageSize,
            int startPage, final ResponseListener<BaseResponse> listener) {
        Map<String, Integer> params = new HashMap<>(16);
        params.put("receiveStatus", receiveStatus);
        params.put("pageSize", pageSize);
        params.put("startPage", startPage);
        RetrofitManager.getApiUrlManager()
                       .getTransferStatusOrderList(token, params)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(
                               new AbstractLoadViewObserver<>(context, Tasks.GET_TRANSFER_STATUS_ORDER_LIST, listener));
    }

    public static void getTransferOrderDetail(Context context, String token, String orderNo,
            final ResponseListener<BaseResponse> listener) {
        RetrofitManager.getApiUrlManager()
                       .getTransferOrderDetail(token, orderNo)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, true, Tasks.GET_TRANSFER_ORDER_DETAIL,
                                                                 listener));
    }

    public static void getStudioOrderStatistics(Context context, String token,
            final ResponseListener<BaseResponse> listener) {
        RetrofitManager.getApiUrlManager()
                       .getStudioOrderStatistics(token)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, Tasks.GET_STUDIO_ORDER_STATISTICS, listener));
    }

    public static void getBanner(Context context, String token, final ResponseListener<BaseResponse> listener) {
        RetrofitManager.getApiUrlManager()
                       .getBanner(token)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, Tasks.GET_BANNER, listener));
    }

    public static void getVersion(Context context, String token, final ResponseListener<BaseResponse> listener) {
        RetrofitManager.getApiUrlManager()
                       .getVersion(token, BaseData.ADMIN)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, Tasks.GET_VERSION, listener));
    }

    public static void startChat(Context context, String token, String doctorCode, String patientCode,
            final ResponseListener<BaseResponse> listener) {
        Map<String, String> params = new HashMap<>(16);
        params.put("doctorCode", doctorCode);
        params.put("patientCode", patientCode);
        RetrofitManager.getApiUrlManager()
                       .startChat(token, params)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, Tasks.START_CHAT, listener));
    }

    public static void getChatLastTime(Context context, String token, String doctorCode, String patientCode,
            final ResponseListener<BaseResponse> listener) {
        Map<String, String> params = new HashMap<>(16);
        params.put("doctorCode", doctorCode);
        params.put("patientCode", patientCode);
        RetrofitManager.getApiUrlManager()
                       .getChatLastTime(token, params)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, Tasks.GET_CHAT_LAST_TIME, listener));
    }

    public static void endChat(Context context, String token, String doctorCode, String patientCode,
            final ResponseListener<BaseResponse> listener) {
        Map<String, String> params = new HashMap<>(16);
        params.put("doctorCode", doctorCode);
        params.put("patientCode", patientCode);
        RetrofitManager.getApiUrlManager()
                       .endChat(token, params)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, Tasks.END_CHAT, listener));
    }

    public static void getCooperateHospitalList(Context context, String token, int pageSize, int startPage,
            boolean show, final ResponseListener<BaseResponse> listener) {
        Map<String, Integer> params = new HashMap<>(16);
        params.put("pageSize", pageSize);
        params.put("startPage", startPage);
        RetrofitManager.getApiUrlManager()
                       .getCooperateHospitalList(token, params)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, show, Tasks.GET_COOPERATE_HOSPITAL_LIST,
                                                                 listener));
    }

    public static void getCooperateHospitalProjectList(Context context, String token, String hospitalCode,
            final ResponseListener<BaseResponse> listener) {
        RetrofitManager.getApiUrlManager()
                       .getCooperateHospitalProjectList(token, hospitalCode)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, Tasks.GET_COOPERATE_HOSPITAL_PROJECT_LIST,
                                                                 listener));
    }

    public static void getCooperateHospitalPackageList(Context context, String token, String hospitalCode, int pageSize,
            int startPage, final ResponseListener<BaseResponse> listener) {
        Map<String, Object> params = new HashMap<>(16);
        params.put("hospitalCode", hospitalCode);
        params.put("pageSize", pageSize);
        params.put("startPage", startPage);
        RetrofitManager.getApiUrlManager()
                       .getCooperateHospitalPackageList(token, params)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, Tasks.GET_COOPERATE_HOSPITAL_PACKAGE_LIST,
                                                                 listener));
    }

    public static void queryProductDetail(Context context, String token, String productCode,
            final ResponseListener<BaseResponse> listener) {
        RetrofitManager.getApiUrlManager()
                       .queryProductDetail(token, productCode)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, Tasks.GET_COOPERATE_HOSPITAL_PROJECT_DETAIL,
                                                                 listener));
    }

    public static void getDoctorQrCode(Context context, String token, final ResponseListener<BaseResponse> listener) {
        RetrofitManager.getApiUrlManager()
                       .getDoctorQrCode(token)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, Tasks.GET_DOCTOR_QR_CODE, listener));
    }

    public static void getLabel(Context context, String token, String patientCode,
            final ResponseListener<BaseResponse> listener) {
        RetrofitManager.getApiUrlManager()
                       .getLabel(token, patientCode)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, true, Tasks.GET_LABEL, listener));
    }

    public static void savePatientLabel(Context context, String token, String patientCode, List<String> tagList,
            final ResponseListener<BaseResponse> listener) {
        Map<String, Object> params = new HashMap<>(16);
        params.put("patientCode", patientCode);
        params.put("tagList", tagList);
        RetrofitManager.getApiUrlManager()
                       .savePatientLabel(token, params)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, true, false, Tasks.SAVE_PATIENT_LABEL,
                                                                 listener));
    }

    public static void getPatientLabel(Context context, String token, int pageSize, int startPage, boolean showLoading,
            final ResponseListener<BaseResponse> listener) {
        Map<String, Integer> params = new HashMap<>(16);
        params.put("pageSize", pageSize);
        params.put("startPage", startPage);
        RetrofitManager.getApiUrlManager()
                       .getPatientLabel(token, params)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(
                               new AbstractLoadViewObserver<>(context, showLoading, Tasks.GET_PATIENT_LABEL, listener));
    }

    public static void deleteLabel(Context context, String token, long tagId,
            final ResponseListener<BaseResponse> listener) {
        RetrofitManager.getApiUrlManager()
                       .deleteLabel(token, tagId)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, Tasks.DELETE_PATIENT_LABEL, listener));
    }

    public static void getExistLabel(Context context, String token, final ResponseListener<BaseResponse> listener) {
        RetrofitManager.getApiUrlManager()
                       .getExistLabel(token)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, Tasks.GET_EXIST_LABEL, listener));
    }

    public static void getPatientByLabel(Context context, String token, long tagId,
            final ResponseListener<BaseResponse> listener) {
        RetrofitManager.getApiUrlManager()
                       .getPatientByLabel(token, tagId)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, true, Tasks.GET_PATIENT_BY_LABEL, listener));
    }

    public static void getRecentAddPatient(Context context, String token,
            final ResponseListener<BaseResponse> listener) {
        RetrofitManager.getApiUrlManager()
                       .getRecentAddPatient(token)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, Tasks.GET_RECENT_ADD_PATIENT, listener));
    }

    public static void applyRemote(Context context, String token, String codeContent,
            final ResponseListener<BaseResponse> listener) {
        Map<String, String> params = new HashMap<>(16);
        params.put("codeContent", codeContent);
        RetrofitManager.getApiUrlManager()
                       .applyRemote(token, params)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, true, false, Tasks.APPLY_REMOTE, listener));
    }

    public static void applyRemoteAdvice(Context context, String token, String codeContent,
            final ResponseListener<BaseResponse> listener) {
        Map<String, String> params = new HashMap<>(16);
        params.put("codeContent", codeContent);
        RetrofitManager.getApiUrlManager()
                       .applyRemoteAdvice(token, params)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, true, false, Tasks.APPLY_REMOTE_ADVICE,
                                                                 listener));
    }

    public static void getRemoteDetail(Context context, String token, String orderNo,
            final ResponseListener<BaseResponse> listener) {
        RetrofitManager.getApiUrlManager()
                       .getRemoteDetail(token, orderNo)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, true, Tasks.GET_REMOTE_DETAIL, listener));
    }

    public static void getPatientByQrId(Context context, String token, String patientId, int createRelation,
            final ResponseListener<BaseResponse> listener) {
        Map<String, Object> params = new HashMap<>(16);
        params.put("patientId", patientId);
        // 是否创建医患关系 0:不需要 开单第一步 1：需要 扫码添加
        params.put("createRelation", createRelation);
        RetrofitManager.getApiUrlManager()
                       .getPatientByQrId(token, params)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, true, false, Tasks.GET_PATIENT_BY_QR_ID,
                                                                 listener));
    }

    public static void getDoctorByQrId(Context context, String token, String doctorId,
            final ResponseListener<BaseResponse> listener) {
        Map<String, Object> params = new HashMap<>(16);
        params.put("doctorId", doctorId);
        RetrofitManager.getApiUrlManager()
                       .getDoctorByQrId(token, params)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, true, Tasks.GET_DOCTOR_BY_QR_ID, listener));
    }

    public static void addDoctorFriend(Context context, String token, String doctorCode,
            final ResponseListener<BaseResponse> listener) {
        Map<String, Object> params = new HashMap<>(16);
        params.put("doctorCode", doctorCode);
        RetrofitManager.getApiUrlManager()
                       .addDoctorFriend(token, params)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(
                               new AbstractLoadViewObserver<>(context, true, false, Tasks.ADD_DOCTOR_FRIEND, listener));
    }

    public static void getRemoteTime(Context context, String token, String date,
            final ResponseListener<BaseResponse> listener) {
        RetrofitManager.getApiUrlManager()
                       .getRemoteTime(token, date)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, false, Tasks.GET_REMOTE_TIME, listener));
    }

    public static void getRemoteDepartmentInfo(Context context, String token, String startAt, String endAt,
            final ResponseListener<BaseResponse> listener) {
        Map<String, String> params = new HashMap<>(16);
        params.put("endAt", endAt);
        params.put("startAt", startAt);
        RetrofitManager.getApiUrlManager()
                       .getRemoteDepartmentInfo(token, params)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, true, Tasks.GET_REMOTE_DEPARTMENT_INFO,
                                                                 listener));
    }

    public static void addReserveRemoteOrder(Context context, String token, ReserveRemoteBean bean,
            final ResponseListener<BaseResponse> listener) {
        Map<String, Object> params = new HashMap<>(16);
        params.put("hosDeptInfo", bean.getHosDeptInfo());
        params.put("allergy", bean.getAllergy());
        params.put("confirmFile", bean.getConfirmFile());
        params.put("confirmType", bean.getConfirmType());
        params.put("descIll", bean.getDescIll());
        params.put("destination", bean.getDestination());
        params.put("family", bean.getFamily());
        params.put("initResult", bean.getInitResult());
        params.put("patientAge", bean.getPatientAge());
        params.put("past", bean.getPast());
        params.put("patientCode", bean.getPatientCode());
        params.put("patientIdCard", bean.getPatientIdCard());
        params.put("patientMobile", bean.getPatientMobile());
        params.put("patientName", bean.getPatientName());
        params.put("patientResource", bean.getPatientResource());
        params.put("patientSex", bean.getPatientSex());
        params.put("endAt", bean.getEndAt());
        params.put("startAt", bean.getStartAt());
        RetrofitManager.getApiUrlManager()
                       .addReserveRemoteOrder(token, params)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, true, false, Tasks.ADD_RESERVE_REMOTE_ORDER,
                                                                 listener));
    }

    public static void getReserveRemoteOrderList(Context context, String token, int pageSize, int startPage,
            boolean showLoading, final ResponseListener<BaseResponse> listener) {
        Map<String, Integer> params = new HashMap<>(16);
        params.put("pageSize", pageSize);
        params.put("startPage", startPage);
        RetrofitManager.getApiUrlManager()
                       .getReserveRemoteOrderList(token, params)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(
                               new AbstractLoadViewObserver<>(context, showLoading, Tasks.GET_RESERVE_REMOTE_ORDER_LIST,
                                                              listener));
    }

    public static void queryPackageList(Context context, String token, int pageSize, int startPage, boolean showLoading,
            final ResponseListener<BaseResponse> listener) {
        Map<String, Integer> params = new HashMap<>(16);
        params.put("pageSize", pageSize);
        params.put("startPage", startPage);
        RetrofitManager.getApiUrlManager()
                       .queryPackageList(token, params)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, showLoading, Tasks.QUERY_PACKAGE_LIST,
                                                                 listener));
    }

    public static void queryPackageDetail(Context context, String token, String packageCode,
            final ResponseListener<BaseResponse> listener) {
        RetrofitManager.getApiUrlManager()
                       .queryPackageDetail(token, packageCode)
                       .compose(RxJavaHelper.observableIO2Main(context))
                       .subscribe(new AbstractLoadViewObserver<>(context, Tasks.QUERY_PACKAGE_DETAIL, listener));
    }
}

