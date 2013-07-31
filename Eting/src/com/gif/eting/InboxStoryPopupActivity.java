package com.gif.eting;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.gif.eting.dto.StoryDTO;
import com.gif.eting.svc.StoryService;

public class InboxStoryPopupActivity extends Activity implements OnClickListener{

	private StoryService storyService;
	private Long inboxStoryIdx;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
		layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		layoutParams.dimAmount = 0.7f;
		getWindow().setAttributes(layoutParams);
		setContentView(R.layout.inbox_popup);
		
		//StoryService�ʱ�ȭ
		storyService = new StoryService(this.getApplicationContext());
		
		StoryDTO inboxStory = storyService.getInboxStory();
		Long idx = inboxStory.getIdx();
		inboxStoryIdx = idx;
		String content = inboxStory.getContent();
		String storyDate = inboxStory.getStory_date();
		
		TextView contentView = (TextView) findViewById(R.id.popup_content);
		contentView.setText(content);
		
		TextView storyDateView = (TextView) findViewById(R.id.popup_date);
		storyDateView.setText(storyDate);

		//��ư�̺�Ʈ ����
		((Button) findViewById(R.id.stamp1)).setOnClickListener(this);
		((Button) findViewById(R.id.stamp2)).setOnClickListener(this);
	}
	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.stamp1:
			//������1
			storyService.saveStampToServer(String.valueOf(inboxStoryIdx), "5");	//������ID 5�� �ϵ��ڵ� ���� �����ʿ�
			finish();
			break;
		case R.id.stamp2:
			//������2
			storyService.saveStampToServer(String.valueOf(inboxStoryIdx), "6");	//������ID 6�� �ϵ��ڵ� ���� �����ʿ�
			finish();
			break;
		}

	}

}
