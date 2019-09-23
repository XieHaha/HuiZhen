//package com.yht.yihuantong
//
//import android.app.Activity
//import android.content.Context
//import android.content.Intent
//import android.net.Uri
//import android.os.Bundle
//import android.support.v4.app.FragmentActivity
//import android.text.TextUtils
//import android.view.LayoutInflater
//import android.view.View
//import android.widget.TextView
//import com.bumptech.glide.Glide
//import com.umeng.analytics.AnalyticsConfig
//import com.yht.frame.api.ApiManager
//import com.yht.frame.api.notify.IChange
//import com.yht.frame.api.notify.NotifyChangeListenerManager
//import com.yht.frame.api.notify.RegisterType
//import com.yht.frame.data.*
//import com.yht.frame.data.bean.*
//import com.yht.frame.http.retrofit.RequestUtils
//import com.yht.frame.permission.Permission
//import com.yht.frame.ui.BaseFragment
//import com.yht.frame.utils.BaseUtils
//import com.yht.frame.utils.glide.GlideHelper
//import com.yht.frame.widgets.menu.MenuItem
//import com.yht.frame.widgets.menu.TopRightMenu
//import com.yht.yihuantong.ui.WebViewActivity
//import com.yht.yihuantong.ui.check.ServiceHistoryActivity
//import com.yht.yihuantong.ui.doctor.DoctorInfoActivity
//import com.yht.yihuantong.ui.hint.NotifyHintActivity
//import com.yht.yihuantong.ui.main.QrCodeActivity
//import com.yht.yihuantong.ui.patient.ChatContainerActivity
//import com.yht.yihuantong.ui.personal.PersonalNewActivity
//import com.yht.yihuantong.ui.remote.ErrorActivity
//import com.yht.yihuantong.ui.remote.RemoteLoginActivity
//import com.yht.yihuantong.ui.reservation.ReservationDisableActivity
//import com.yht.yihuantong.ui.reservation.service.ReservationServiceActivity
//import com.yht.yihuantong.ui.reservation.transfer.ReservationTransferActivity
//import com.yht.yihuantong.ui.transfer.TransferInitiateListActivity
//import com.yht.yihuantong.ui.transfer.TransferReceiveListActivity
//import com.yht.yihuantong.utils.FileUrlUtil
//import com.yht.yihuantong.utils.NotifySettingUtils
//import com.yzq.zxinglibrary.android.CaptureActivity
//import com.yzq.zxinglibrary.common.Constant
//import kotlinx.android.synthetic.main.fragment_worker.*
//import java.util.*
//
///**
// * @author 顿顿
// * @date 19/5/17 14:55
// * @des 工作室
// */
//class TestFragment : BaseFragment(), TopRightMenu.OnMenuItemClickListener {
//    /**
//     * 订单统计
//     */
//    private var orderNumStatisticsBean: OrderNumStatisticsBean? = null
//    /**
//     * 广告banner
//     */
//    private var bannerBeans: MutableList<BannerBean>? = null
//    /**
//     * 消息红点
//     */
//    private val transferApply = IChange<String> {
//        getStudioOrderStatistics()
//    }
//
//    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
//        super.setUserVisibleHint(isVisibleToUser)
//        if (isPrepared) {
//            if (userVisibleHint) {
//                fillNetWorkData()
//            } else if (view_flipper!!.isFlipping) {
//                view_flipper!!.stopFlipping()
//            }
//        }
//    }
//
//    override fun onResume() {
//        super.onResume()
//        //当前用户可见才刷新列表
//        if (userVisibleHint) {
//            fillNetWorkData()
//        }
//    }
//
//    override fun getLayoutID(): Int {
//        return R.layout.fragment_worker
//    }
//
//    override fun initView(view: View, savedInstanceState: Bundle?) {
//        super.initView(view, savedInstanceState)
////        status_bar_fix.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStateBarHeight(activity))
////        public_main_title_scan?.visibility = View.VISIBLE
//        iNotifyChangeListenerServer = ApiManager.getInstance().server
//        view.postOnAnimationDelayed({ this.initNotifyHint() }, 2000)
//    }
//
//    override fun initData(savedInstanceState: Bundle?) {
//        super.initData(savedInstanceState)
////        public_main_title?.text = loginBean.doctorName
//        tv_personal_depart?.text = loginBean.departmentName
//        tv_personal_hospital?.text = loginBean.hospitalName
//        Glide.with(this)
//                .load(FileUrlUtil.addTokenToUrl(loginBean.photo))
//                .apply(GlideHelper.getOptions(BaseUtils.dp2px(Objects.requireNonNull<Context>(context), 4)))
//                .into(iv_personal_image!!)
//    }
//
//    override fun initListener() {
//        super.initListener()
//        iNotifyChangeListenerServer.registerDoctorTransferPatientListener(transferApply, RegisterType.REGISTER)
//        layout_personal_base.setOnClickListener(this)
//        layout_check.setOnClickListener(this)
//        layout_transfer.setOnClickListener(this)
//        layout_remote.setOnClickListener(this)
//        layout_transfer_apply.setOnClickListener(this)
//        view_flipper.setOnClickListener(this)
//        layout_initiate_check.setOnClickListener(this)
//        layout_initiate_transfer.setOnClickListener(this)
//        layout_accepted_transfer.setOnClickListener(this)
//    }
//
//    override fun fillNetWorkData() {
//        super.fillNetWorkData()
//        if (BaseUtils.isNetworkAvailable(Objects.requireNonNull<Context>(context))) {
//            getBanner()
//        } else {
//            initFlipper()
//        }
//        getStudioOrderStatistics()
//        getValidateHospitalList()
//    }
//
//    /**
//     * 扫码后获取居民信息
//     */
//    private fun getPatientByQrId(qrId: String) {
//        RequestUtils.getPatientByQrId(context, loginBean.token, qrId, BaseData.BASE_ONE, this)
//    }
//
//    /**
//     * 扫码后获取医生信息
//     */
//    private fun getDoctorByQrId(qrId: String) {
//        RequestUtils.getDoctorByQrId(context, loginBean.token, qrId, this)
//    }
//
//    /**
//     * banner
//     */
//    private fun getBanner() {
//        RequestUtils.getBanner(context, loginBean.token, this)
//    }
//
//    /**
//     * 获取所有订单数量
//     */
//    private fun getStudioOrderStatistics() {
//        RequestUtils.getStudioOrderStatistics(context, loginBean.token, this)
//    }
//
//    /**
//     * 校验医生是否有预约检查和预约转诊的合作医院
//     */
//    private fun getValidateHospitalList() {
//        RequestUtils.getValidateHospitalList(context, loginBean.token, this)
//    }
//
//    /**
//     * 是否需要提示通知权限   只显示一次
//     */
//    private fun initNotifyHint() {
//        val type = sharePreferenceUtil.getAlwaysInteger(CommonData.KEY_NOTIFICATION_CONTROL)
//        //表示用户未操作过
//        if (type != BaseData.BASE_TWO) {
//            if (!NotifySettingUtils.hasNotify(Objects.requireNonNull<FragmentActivity>(activity))) {
//                startActivity(Intent(context, NotifyHintActivity::class.java))
//                activity!!.overridePendingTransition(R.anim.actionsheet_dialog_in, R.anim.keep)
//            }
//        }
//    }
//
//    /**
//     * 广告轮播
//     */
//    private fun initFlipper() {
//        view_flipper!!.removeAllViews()
//        //添加默认值
//        if (bannerBeans == null || bannerBeans!!.size == 0) {
//            bannerBeans = ArrayList()
//            val bean = BannerBean()
//            bean.bannerRemark = getString(R.string.txt_view_flipper_hint)
//            bean.bannerId = -1
//            bannerBeans!!.add(bean)
//        }
//        for (i in bannerBeans!!.indices) {
//            val view = LayoutInflater.from(context).inflate(R.layout.item_flipper, null)
//            val textView = view.findViewById<TextView>(R.id.tv_flipper)
//            textView.text = bannerBeans!![i].bannerRemark
//            view_flipper!!.addView(view)
//        }
//        if (bannerBeans!!.size > 1) {
//            view_flipper!!.postDelayed({ view_flipper!!.startFlipping() }, 200)
//        }
//    }
//
//    /**
//     * 订单数量
//     */
//    private fun initStatistics() {
//        tv_initiate_check_num!!.text = if (orderNumStatisticsBean!!.initiateOrderCheck <= BaseData.BASE_MEAASGE_DISPLAY_NUM)
//            orderNumStatisticsBean!!.initiateOrderCheck.toString()
//        else
//            getString(R.string.txt_max_num)
//        tv_initiate_transfer_num!!.text = if (orderNumStatisticsBean!!.initiateOrderTransfer <= BaseData.BASE_MEAASGE_DISPLAY_NUM)
//            orderNumStatisticsBean!!.initiateOrderTransfer.toString()
//        else
//            getString(R.string.txt_max_num)
//        tv_accepted_transfer_num!!.text = if (orderNumStatisticsBean!!.receiveOrderTransfer <= BaseData.BASE_MEAASGE_DISPLAY_NUM)
//            orderNumStatisticsBean!!.receiveOrderTransfer.toString()
//        else
//            getString(R.string.txt_max_num)
//        if (orderNumStatisticsBean!!.pendingOrderTransfer != BaseData.BASE_ZERO) {
//            tv_receiving_transfer_num!!.text = if (orderNumStatisticsBean!!.pendingOrderTransfer <= BaseData.BASE_MEAASGE_DISPLAY_NUM)
//                orderNumStatisticsBean!!.pendingOrderTransfer.toString()
//            else
//                getString(R.string.txt_max_num)
//            layout_receiving_transfer_num!!.visibility = View.VISIBLE
//        } else {
//            layout_receiving_transfer_num!!.visibility = View.GONE
//        }
//    }
//
//    private fun initMenu() {
//        val mTopRightMenu = TopRightMenu(activity)
//        val menuItems = ArrayList<MenuItem>()
//        menuItems.add(MenuItem(R.mipmap.ic_patient_richscan, getString(R.string.txt_menu_patient)))
//        menuItems.add(MenuItem(R.mipmap.ic_doctor_richscan, getString(R.string.txt_menu_doctor)))
//        menuItems.add(MenuItem(R.mipmap.ic_richscan, getString(R.string.title_camera_scan)))
////        mTopRightMenu.setHeight(BaseUtils.dp2px(context!!, 200))
////                .addMenuList(menuItems)
////                .setOnMenuItemClickListener(this)
////                .showAsDropDown(public_main_title_scan, -BaseUtils.dp2px(context!!, 124), 10)
//    }
//
//    override fun onClick(v: View?) {
//        super.onClick(v)
//        val intent: Intent
//        when (v?.id) {
//            R.id.public_main_title_scan -> initMenu()
//            R.id.layout_personal_base -> startActivity(Intent(context, PersonalNewActivity::class.java))
//            R.id.layout_check -> if (ZycApplication.getInstance().isServiceAble) {
//                intent = Intent(context, ReservationServiceActivity::class.java)
//                startActivity(intent)
//            } else {
//                intent = Intent(context, ReservationDisableActivity::class.java)
//                startActivity(intent)
//            }
//            R.id.layout_transfer -> if (ZycApplication.getInstance().isTransferAble) {
//                intent = Intent(context, ReservationTransferActivity::class.java)
//                startActivity(intent)
//            } else {
//                intent = Intent(context, ReservationDisableActivity::class.java)
//                intent.putExtra(CommonData.KEY_CHECK_OR_TRANSFER, true)
//                startActivity(intent)
//            }
//            R.id.layout_remote -> {
//                intent = Intent(context, WebViewActivity::class.java)
//                intent.putExtra(CommonData.KEY_PUBLIC, initRemoteUrl())
//                startActivity(intent)
//            }
//            R.id.layout_transfer_apply -> {
//                intent = Intent(context, TransferReceiveListActivity::class.java)
//                intent.putExtra(CommonData.KEY_PUBLIC, true)
//                startActivity(intent)
//            }
//            R.id.view_flipper -> {
//                val bean = bannerBeans!![view_flipper!!.displayedChild]
//                if (bean.bannerId != -1) {
//                    intent = Intent(context, WebViewActivity::class.java)
//                    intent.putExtra(CommonData.KEY_PUBLIC,
//                            ZycApplication.getInstance().baseUrl + BaseNetConfig.BASE_BASIC_BANNER_URL +
//                                    bean.bannerId)
//                    intent.putExtra(CommonData.KEY_TITLE, bean.bannerRemark)
//                    startActivity(intent)
//                }
//            }
//            R.id.layout_initiate_check -> {
//                intent = Intent(context, ServiceHistoryActivity::class.java)
//                if (orderNumStatisticsBean != null) {
//                    intent.putExtra(CommonData.KEY_PUBLIC, orderNumStatisticsBean!!.initiateOrderCheck)
//                }
//                startActivity(intent)
//            }
//            R.id.layout_initiate_transfer -> {
//                intent = Intent(context, TransferInitiateListActivity::class.java)
//                if (orderNumStatisticsBean != null) {
//                    intent.putExtra(CommonData.KEY_PUBLIC, orderNumStatisticsBean!!.initiateOrderTransfer)
//                }
//                startActivity(intent)
//            }
//            R.id.layout_accepted_transfer -> {
//                intent = Intent(context, TransferReceiveListActivity::class.java)
//                startActivity(intent)
//            }
//        }
//    }
//
//    /**
//     * 菜单
//     *
//     * @param position index
//     */
//    override fun onMenuItemClick(position: Int) {
//        val intent: Intent
//        when (position) {
//            BaseData.BASE_ZERO -> {
//                intent = Intent(context, QrCodeActivity::class.java)
//                startActivity(intent)
//            }
//            BaseData.BASE_ONE -> {
//                intent = Intent(context, QrCodeActivity::class.java)
//                intent.putExtra(CommonData.KEY_INTENT_BOOLEAN, true)
//                startActivity(intent)
//            }
//            BaseData.BASE_TWO -> permissionHelper.request(arrayOf(Permission.CAMERA))
//            else -> {
//            }
//        }
//    }
//
//    override fun onResponseSuccess(task: Tasks, response: BaseResponse<*>) {
//        super.onResponseSuccess(task, response)
//        val intent: Intent
//        when (task) {
//            Tasks.GET_STUDIO_ORDER_STATISTICS -> {
//                orderNumStatisticsBean = response.getData() as OrderNumStatisticsBean
//                initStatistics()
//            }
//            Tasks.GET_BANNER -> {
//                bannerBeans = response.getData() as MutableList<BannerBean>
//                initFlipper()
//            }
//            Tasks.GET_VALIDATE_HOSPITAL_LIST -> {
//                val bean = response.getData() as ReservationValidateBean
//                ZycApplication.getInstance().isServiceAble = bean.isJc
//                ZycApplication.getInstance().isTransferAble = bean.isZz
//            }
//            Tasks.GET_PATIENT_BY_QR_ID -> {
//                val patientBean = response.getData() as PatientBean
//                //添加成功  刷新居民列表
//                NotifyChangeListenerManager.getInstance().notifyPatientListChanged("")
//                //跳转到居民信息界面
//                intent = Intent(context, ChatContainerActivity::class.java)
//                intent.putExtra(CommonData.KEY_CHAT_ID, patientBean.code)
//                intent.putExtra(CommonData.KEY_CHAT_NAME, patientBean.name)
//                startActivity(intent)
//            }
//            Tasks.GET_DOCTOR_BY_QR_ID -> {
//                val doctorBean = response.getData() as DoctorQrCodeBean
//                if (doctorBean.isFriend) {
//                    intent = Intent(context, ChatContainerActivity::class.java)
//                    intent.putExtra(CommonData.KEY_CHAT_ID, doctorBean.code)
//                    intent.putExtra(CommonData.KEY_CHAT_NAME, doctorBean.name)
//                    intent.putExtra(CommonData.KEY_DOCTOR_CHAT, true)
//                    startActivity(intent)
//                } else {
//                    intent = Intent(context, DoctorInfoActivity::class.java)
//                    intent.putExtra(CommonData.KEY_DOCTOR_QR_CODE_BEAN, doctorBean)
//                    startActivity(intent)
//                }
//            }
//            else -> {
//            }
//        }
//    }
//
//    /**
//     * 关于远程会诊url处理
//     */
//    private fun initRemoteUrl(): String {
//        val baseBasicRemoteUrl = BaseNetConfig.BASE_BASIC_REMOTE_URL
//        val channel = AnalyticsConfig.getChannel(context)
//        return if (!TextUtils.isEmpty(channel)) {
//            when (channel) {
//                "zyc" -> baseBasicRemoteUrl.replace("hsp", "hsp-pre")
//                "beta" -> baseBasicRemoteUrl.replace("hsp", "hsp-t").replace("https", "http")
//                else -> baseBasicRemoteUrl
//            }
//        } else {
//            baseBasicRemoteUrl
//        }
//    }
//
//    /**
//     * 开启扫一扫
//     */
//    private fun openScan() {
//        val intent = Intent(context, CaptureActivity::class.java)
//        startActivityForResult(intent, REQUEST_CODE_SCAN)
//        Objects.requireNonNull<FragmentActivity>(activity).overridePendingTransition(R.anim.keep, R.anim.keep)
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        if (resultCode != Activity.RESULT_OK) {
//            return
//        }
//        val intent: Intent
//        if (requestCode == REQUEST_CODE_SCAN && data != null) {
//            val content = data.getStringExtra(Constant.CODED_CONTENT)
//            if (!TextUtils.isEmpty(content)) {
//                if (content.startsWith(BaseData.BASE_REMOTE) || content.startsWith(BaseData.BASE_REMOTE_ADVICE)) {
//                    intent = Intent(context, RemoteLoginActivity::class.java)
//                    intent.putExtra(CommonData.KEY_PUBLIC_STRING, content)
//                    startActivity(intent)
//                } else {
//                    //居民、医生二维码
//                    val uri = Uri.parse(content)
//                    if (uri != null && !uri.isOpaque) {
//                        val mode = uri.getQueryParameter("t")
//                        val value = uri.getQueryParameter("p")
//                        if (!TextUtils.isEmpty(value) && !TextUtils.isEmpty(mode)) {
//                            //1为医生  2为居民
//                            if (BaseData.BASE_STRING_ONE_TAG == mode) {
//                                getDoctorByQrId(value)
//                            } else {
//                                getPatientByQrId(value)
//                            }
//                        } else {
//                            qrError()
//                        }
//                    } else {
//                        qrError()
//                    }
//                }
//            } else {
//                qrError()
//            }
//        }
//        super.onActivityResult(requestCode, resultCode, data)
//    }
//
//    private fun qrError() {
//        startActivityForResult(Intent(context, ErrorActivity::class.java), REQUEST_CODE_SCAN)
//    }
//
//    override fun onNoPermissionNeeded(permissionName: Any) {
////        if (permissionName is Array<String>) {
////            if (isSamePermission(Permission.CAMERA, permissionName[0])) {
////                openScan()
////            }
////        }
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        iNotifyChangeListenerServer.registerDoctorTransferPatientListener(transferApply, RegisterType.UNREGISTER)
//    }
//
//    companion object {
//        /**
//         * 扫码
//         */
//        private var REQUEST_CODE_SCAN = 100
//    }
//}
