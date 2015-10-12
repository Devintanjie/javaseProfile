package com.taobao.bird.common.alert;

import java.util.List;

/**
 * @desc
 * @author junyu 2015年2月16日下午12:48:28
 * @version
 **/
public interface Alert {

    public void alertSms(List<String> users, String msg);

    public void alertMail(List<String> users, String title, String msg);
}
