package com.simon.appmanager.https;

/**
 * 服务器地址
 **/
@SuppressWarnings("all")
public class ApiConstanse {
    // 正式地址
    public static final String BASE_URL = "https://benteng.wemark.tech";

    // 测试地址
    //  public static final String BASE_URL = "https://bentengyuan.automark.cc/";

    // 协议
    public static final String get_user_protocol = "/teacher/Upgrade/get_user_protocol";

    //修改头像-返回值
    public static final int REQUEST_CODE = 0;

    // 安卓更新
    public static final String Upgrade = "/teacher/Upgrade/android";

    //发送验证码
    public static final String sendSms = "/teacher/Login/sendSms";

    //用户注册
    public static final String Register = "/teacher/Login/Register";

    //用户登录
    public static final String Login = "/teacher/Login/Login";

    //忘记密码
    public static final String passwordForget = "/teacher/Login/passwordForget";

    //当前日期
    public static final String topTime = "/teacher/index/topTime";

    //教务管理-全部
    public static final String getOfflineList = "/teacher/offline/getOfflineList";

    //教务管理(全部)-班次列表
    public static final String getOfflineShiftList = "/teacher/shift/getOfflineShiftList";

    //教务管理-我的关注
    public static final String getUserOfflineList = "/teacher/offline/getUserOfflineList";

    //添加关注班次
    public static final String addAttention = "/teacher/shift/addAttention";

    //班次详细
    public static final String getofflineShiftInfo = "/teacher/shift/getofflineShiftInfo";

    //班次详情-签到列表
    public static final String getShiftSignList = "/teacher/shift/getShiftSignList";

    //班次详情-签到详情
    public static final String getShiftSignInfo = "/teacher/shift/getShiftSignInfo";

    //签到二维码
    public static final String getSignQrcode = "/teacher/shift/getSignQrcode";

    //班次详情-考试列表
    public static final String getShiftTestList = "/teacher/shift/getShiftTestList";

    //班次详情-考试详情
    public static final String getShiftTestInfo = "/teacher/shift/getShiftTestInfo";

    //考试二维码
    public static final String getTestQrcode = "/teacher/shift/getTestQrcode";

    //获取用户信息
    public static final String getUserInfo = "/teacher/MeController/getUserInfo";

    //获取所有未读消息的条数
    public static final String get_all_nosee_num = "/teacher/MeController/get_all_nosee_num";

    //获取消息列表
    public static final String get_notice_list = "/teacher/MeController/get_notice_list";

    //获取消息详情
    public static final String get_notice_message = "/teacher/MeController/get_notice_message";

    //修改头像
    public static final String editHeadimgCont = "/teacher/MeController/editHeadimgCont";

    //上传头像
    public static final String upload_pic = "/Admin/Overt/upload_pic.html";

    //退出登录接口
    public static final String loginOut = "/teacher/Login/loginOut";


}
