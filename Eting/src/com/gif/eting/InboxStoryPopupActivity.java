package com.gif.eting;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gif.eting.dto.StampDTO;
import com.gif.eting.dto.StoryDTO;
import com.gif.eting.svc.StoryService;
import com.gif.eting.util.ServiceCompleteListener;

public class InboxStoryPopupActivity extends Activity implements OnClickListener{

	private StoryService storyService;
	private Long inboxStoryIdx;
	private ProgressDialog progressDialog;
	private List<String> stamps = new ArrayList<String>();;
	

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
		((Button) findViewById(R.id.inbox_confirm_btn)).setOnClickListener(this);
		
		//������ �ڵ��ϼ�
		List<StampDTO> list = storyService.getStampList();
		
		AutoCompleteTextView stampAC = (AutoCompleteTextView) findViewById(R.id.stamp_auto_complete);
		ArrayAdapter<StampDTO> adapter = new ArrayAdapter<StampDTO>(this, android.R.layout.simple_dropdown_item_1line, list);
		stampAC.setAdapter(adapter);
		// Ŭ���̺�Ʈ ����
		stampAC.setOnItemClickListener(mOnItemClickListener);
	}
	
	 //������ Ŭ���̺�Ʈ
    private OnItemClickListener mOnItemClickListener = new OnItemClickListener() {

    	@SuppressWarnings("unchecked")
		public void onItemClick(AdapterView<?> parentView, View clickedView,
    			int position, long id) {

    		Log.i("position",String.valueOf(position));
    		Log.i("id",String.valueOf(id));
			
    		ArrayAdapter<StampDTO> adapter = (ArrayAdapter<StampDTO>) parentView.getAdapter();	// Adapter �޾ƿ�
    		StampDTO stamp = adapter.getItem(position);	//������ Row�� �ִ� StampDTO�� �޾ƿ�
    		String stampId = stamp.getStamp_id();
    		String stampName = stamp.getStamp_name();
    		
    		Log.i("getItem", stampId+stampName);
    		
    		/*
    		String toastMessage = ((TwoLineListItem) clickedView).getText1().getText()
    				+ " is selected. position is " + position + ", and id is " + id;*/
    		
    		addStamp(stampId, stampName);
    		
    		Toast.makeText(getApplicationContext(), stampId,
    				Toast.LENGTH_SHORT).show();
    	}
    	
    };
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.inbox_confirm_btn:
			
			//���ۻ��� ��Ÿ��
			progressDialog = ProgressDialog.show(this, "", getResources().getString(R.string.app_name), true, true);
			
			//������1
			//storyService.saveStampToServer(String.valueOf(inboxStoryIdx), "5", new AfterSaveStampToServer());	//������ID 5�� �ϵ��ڵ� ���� �����ʿ�
			saveStampsToServer();
			break;
		}
	}
	
	//��������� Http ��û �� ����
	private class AfterSaveStampToServer implements ServiceCompleteListener<String>{
		@Override
		public void onServiceComplete(String response) {

			if (progressDialog != null)
				progressDialog.dismiss();
			
			finish();
		}
	}

	// ������ �Է�
	private void addStamp(String stampId, String stampName) {
		stamps.add(stampId);
		LinearLayout stampArea = (LinearLayout) findViewById(R.id.inbox_stamp_area); // ����������

		TextView stampView = new TextView(this);
		stampView.setText(stampName);
		stampView.setGravity(Gravity.LEFT);
		
		stampArea.addView(stampView);

	}

	private void saveStampsToServer(){
		storyService.saveStampToServer(String.valueOf(inboxStoryIdx), stamps, new AfterSaveStampToServer());
	}

}
