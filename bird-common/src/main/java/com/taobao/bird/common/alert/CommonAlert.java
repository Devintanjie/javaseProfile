package com.taobao.bird.common.alert;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.taobao.bird.common.utils.Constants;

/**
 * @desc
 * @author junyu 2015年2月16日下午12:51:19
 * @version
 **/
public class CommonAlert implements Alert {
	private static final Logger log = LoggerFactory
			.getLogger(Constants.LOGGER_ALERT);

	private AtomicBoolean isInit = new AtomicBoolean(false);

	public void init() {
		if (isInit.compareAndSet(false, true)) {
			// TODO
		} else {
			log.warn("alert inited,not init again.");
		}
	}

	@Override
	public void alertSms(List<String> smss, String msg) {
		if (null != smss && StringUtils.isNotBlank(msg)) {
			for (String sms : smss) {
				try {
					// TODO
				} catch (Exception e) {
					log.error("alert by sms error,sms:" + sms, e);
					continue;
				}
			}
		} else {
			log.error("alert by sms users is null or msg is blank,not send.");
		}
	}

	@Override
	public void alertMail(List<String> mails, String title, String msg) {
		if (null != mails && StringUtils.isNotBlank(msg)) {
			for (String mail : mails) {
				try {
					// TODO
				} catch (Exception e) {
					log.error("alert by email error,mail:" + mail, e);
					continue;
				}
			}
		} else {
			log.error("alert by email, mails is null or msg is blank,not send.");
		}
	}
}
