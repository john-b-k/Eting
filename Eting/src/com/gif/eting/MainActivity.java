package com.gif.eting;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Audio;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends Activity implements OnClickListener {

	//���� �ɹ�����
	private NotificationManager mNotification;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		mNotification = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		PendingIntent pendingIntent = pendingIntent();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
	}

	private PendingIntent pendingIntent() {
		Intent i = new Intent(getApplicationContext(), ReadMyStoryActivity.class);
		PendingIntent pi = PendingIntent.getActivity(this, 0, i, 0);
		// �� ȭ���� ��ܿ� ���� ������, ����, ����������
		int icon = R.drawable.ic_launcher;
		String notiText = "ùȭ�鿡�˸�";
		long when = System.currentTimeMillis();

		
		// Noti��� ��ü ����, ���� ���� ����� ������ �޾ƿ��鼭 �˶��� �۵��Ǹ� �� ȭ�� ��ܿ� ����
		Notification noti = new Notification(icon, notiText, when);

		// �˶��� �����Ǹ鼭 Activity�����ϰ���
		noti.setLatestEventInfo(MainActivity.this, "111", "222", pi);
		// Notification_id�� ���� id�� ������ notification�� ǥ����
		mNotification.notify(1234, noti);
		// �߰�***********************��

		return pi;
	}
	
}
