package com.gif.eting;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.gif.eting.svc.StoryService;
import com.gif.eting.util.ServiceCompleteListener;

public class WriteStoryActivity extends Activity implements OnClickListener{
	
	private StoryService storyService;
	private ProgressDialog progressDialog;
	private Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.write_story);
		this.context = getApplicationContext();

		
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
		
		//���ۻ��� ��Ÿ��
		progressDialog = ProgressDialog.show(this, "", getResources().getString(R.string.app_name), true, true);

		//StoryService�ʱ�ȭ
		storyService = new StoryService(this.getApplicationContext());
		storyService.saveStoryToServer(content, new AfterSendAndSaveStory()); // ������ �̾߱�����, �Ķ���ͷ� �ݹ��Լ� �ѱ�
	}
	
	public class AfterSendAndSaveStory implements ServiceCompleteListener<String>{

		@Override
		public void onServiceComplete(String result) {
			Log.i("onTaskComplete", result);

			if (progressDialog != null)
				progressDialog.dismiss();

			Toast toast = Toast.makeText(context, "�̾߱Ⱑ ���۵Ǿ����ϴ�", Toast.LENGTH_SHORT);
			toast.show();

			// �� �̾߱� �б� ȭ������ �̵�
			startActivity(new Intent(context, ReadMyStoryActivity.class));
			
		}
	}

}
