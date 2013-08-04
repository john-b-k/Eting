package com.gif.eting.svc;

import android.content.Context;

import com.gif.eting.dao.SettingDAO;
import com.gif.eting.dto.SettingDTO;
import com.gif.eting.util.SecureUtil;

public class SettingService {

	private SettingDAO settingDao;

	public SettingService(Context context) {
		this.settingDao = new SettingDAO(context);
	}

	// ��й�ȣ ����
	public void savePassword(String pw) {
		SettingDTO setting = new SettingDTO();
		pw = SecureUtil.toSHA256(pw);

		setting.setKey("password");
		setting.setValue(pw);

		settingDao.open(); // ����
		settingDao.inssetting(setting);
		settingDao.close(); // �ݰ�
	}

	// ��й�ȣ üũ
	public boolean checkPassword(String pw) {
		boolean isValid = false;
		pw = SecureUtil.toSHA256(pw); // ����ڰ� �Է��� ��й�ȣ

		SettingDTO setting = new SettingDTO();

		settingDao.open(); // ����
		setting = settingDao.getsettingInfo("password");
		settingDao.close(); // �ݰ�
		
		if(setting == null){
			isValid = true;	//������ ��й�ȣ�� ������ ��ȿó��
		}else if (pw.equals(setting.getValue())) {
			isValid = true;
		}

		return isValid;
	}

}