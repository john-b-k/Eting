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
			sendAndSaveStory();
			break;
		}
	}
	
	private void sendAndSaveStory(){
		
		EditText et = (EditText) findViewById(R.id.story_content);
		String content = et.getText().toString();	//�̾߱� ����

		//StoryService�ʱ�ȭ
		storyService = new StoryService(this.getApplicationContext());
		storyService.saveStoryToServer(content); // ������ �̾߱�����

		Toast toast = Toast.makeText(this, "�̾߱Ⱑ ���۵Ǿ����ϴ�", Toast.LENGTH_SHORT);
		toast.show();
		
		//�� �̾߱� �б� ȭ������ �̵�
		startActivity(new Intent(this, ReadMyStoryActivity.class));
		
	}

}
