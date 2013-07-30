package com.gif.eting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.gif.eting.svc.StoryService;
import com.gif.eting.util.HttpUtil;
import com.gif.eting.util.Installation;

public class WriteStoryActivity extends Activity implements OnClickListener {
	
	private StoryService storyService;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.write_story);

		// ��ư�̺�Ʈ ����
		((ImageButton) findViewById(R.id.send_story_btn))
				.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.send_story_btn:
			saveStoryToServer(); // ������ �̾߱�����
			break;
		}

	}

	//������ ���� �� �ڷ� �޾ƿ�
	private void saveStoryToServer() {
		String phoneId = Installation.id(this.getApplicationContext());	//��� ������
		
		EditText et = (EditText) findViewById(R.id.story_content);
		String content = et.getText().toString();	//�̾߱� ����

		HttpUtil http = new HttpUtil();
		String params = "phone_id=" + phoneId+"&content=" + content;	//�Ķ���� ����
		
		//String response = http.doPost("http://lifenjoys.cafe24.com/eting/insert", params);	//�����ּ�
		String response = http.doPost("http://112.144.52.47:8080/eting/insert", params);	//���߼����ּ�
		
		Log.i("json response", response);
		
		//StoryService�ʱ�ȭ
		storyService = new StoryService(this.getApplicationContext());
		storyService.saveToPhoneDB(response);
		storyService.dbTest();

		Toast toast = Toast.makeText(this, "�̾߱Ⱑ ���۵Ǿ����ϴ�", Toast.LENGTH_SHORT);
		toast.show();
		
		//�� �̾߱� �б� ȭ������ �̵�
		startActivity(new Intent(this, ReadMyStoryActivity.class));
	}

}
