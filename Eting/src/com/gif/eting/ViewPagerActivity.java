package com.gif.eting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.gif.eting.dto.StoryDTO;
import com.gif.eting.svc.StoryService;
import com.gif.eting.util.ServiceCompleteListener;

public class ViewPagerActivity extends Activity implements OnClickListener{
	private final int COUNT = 4; // �� 4�� ��� (main, write, read, setting)
	private ViewPager mPager; // �� ������
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewpager);
		this.context = getApplicationContext();
		
		//��ư�̺�Ʈ ����
		((ImageButton) findViewById(R.id.write_et_btn)).setOnClickListener(this);
		((ImageButton) findViewById(R.id.read_et_btn)).setOnClickListener(this);
		((ImageButton) findViewById(R.id.setting_btn)).setOnClickListener(this);
		
		
		/* �����߰� */
		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setAdapter(new BkPagerAdapter(getApplicationContext()));
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.write_et_btn:
			//startActivity(new Intent(this, WriteStoryActivity.class));
			mPager.setCurrentItem(1);
			break;
		case R.id.read_et_btn:
			//startActivity(new Intent(this, ReadMyStoryActivity.class));
			mPager.setCurrentItem(2);
			break;
		case R.id.setting_btn:
			//startActivity(new Intent(this, SettingActivity.class));
			mPager.setCurrentItem(3);
			break;
		}

	}

	//������ �����
	public class BkPagerAdapter extends PagerAdapter {
		private LayoutInflater mInflater;
		private ProgressDialog progressDialog;

		public BkPagerAdapter(Context context) {
			super();
			mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return COUNT;
		}
		
		//�� Ŭ���̺�Ʈ
		private View.OnClickListener mPagerListener = new View.OnClickListener() {
	        @Override
	        public void onClick(View v) {
	    		switch (v.getId()) {
	    		case R.id.inbox_btn:
	    			startActivity(new Intent(context, InboxStoryPopupActivity.class));
	    			break;

	    		case R.id.send_story_btn:
	    			sendAndSaveStory();
	    			break;
	    		}
	        }
	    };
	    
	    //������ Ŭ���̺�Ʈ
	    private OnItemClickListener mOnItemClickListener = new OnItemClickListener() {

	    	@SuppressWarnings("unchecked")
	    	public void onItemClick(AdapterView<?> parentView, View clickedView,
	    			int position, long id) {
	    		
	    		ListAdapter listAdapter = (ListAdapter) parentView.getAdapter();	//ListView���� Adapter �޾ƿ�
	    		Map<String, String> selectedItem = (Map<String, String>) listAdapter.getItem(position);	//������ Row�� �ִ� Object�� �޾ƿ�
	    		String idx = selectedItem.get("idx");	//Object���� idx���� �޾ƿ�
	    		
	    		String toastMessage = idx;

	    		Intent intent =new Intent(context, MyStoryPopupActivity.class);
	    		intent.putExtra("idx", idx);
	    		startActivity(intent);
	    		
	    		/*
	    		String toastMessage = ((TwoLineListItem) clickedView).getText1().getText()
	    				+ " is selected. position is " + position + ", and id is " + id;*/

	    		Toast.makeText(getApplicationContext(), toastMessage,
	    				Toast.LENGTH_SHORT).show();
	    	}
	    	
	    };

		// ������������ ����� �䰴ü ����/���
		@Override
		public Object instantiateItem(View pager, int position) {
			View v = null;
			if (position == 0) {
				v = mInflater.inflate(R.layout.main, null);
				//��ư�̺�Ʈ ����
				((ImageButton) v.findViewById(R.id.inbox_btn)).setOnClickListener(mPagerListener);
			} else if (position == 1) {
				v = mInflater.inflate(R.layout.write_story, null);
				// ��ư�̺�Ʈ ����
				((ImageButton) v.findViewById(R.id.send_story_btn))
						.setOnClickListener(mPagerListener);
			} else if (position == 2) {
				v = mInflater.inflate(R.layout.read_story, null);
				
				drawMyStoryList(v);	//�ڱ� �̾߱� �о����
		        
			} else {
				v = mInflater.inflate(R.layout.setting, null);
			}

			((ViewPager) pager).addView(v, 0);
			Log.i("instantiateItem", String.valueOf(v.getId()));
			return v;
		}

		// �� ��ü ����
		@Override
		public void destroyItem(View pager, int position, Object view) {
			((ViewPager) pager).removeView((View) view);
		}

		// instantiateItem�޼ҵ忡�� ������ ��ü�� �̿��� ������
		@Override
		public boolean isViewFromObject(View view, Object obj) {
			return view == obj;
		}

		@Override
		public void finishUpdate(ViewGroup container) {
			Log.i("finishUpdate", String.valueOf(container.getId()));
			if(container.getId()== R.layout.read_story){
			}
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}
		

		
		// �� �̾߱� ��� �׸���
		public void drawMyStoryList(View view) {
			// StoryService�ʱ�ȭ
			StoryService storyService = new StoryService(
					context);
			List<StoryDTO> myStoryList = storyService.getMyStoryList();

			// ����Ʈ�並 ���� ������
			List<Map<String, String>> list = new ArrayList<Map<String, String>>();
			for (StoryDTO myStory : myStoryList) {
				Map<String, String> item = new HashMap<String, String>();
				item.put("title", myStory.getStory_date());
				item.put("desc", myStory.getContent());
				item.put("idx", String.valueOf(myStory.getIdx()));
				list.add(item);
			}

			// ����Ϳ� ������ ����
			final String[] fromMapKey = new String[] { "title", "desc" };
			final int[] toLayoutId = new int[] { android.R.id.text1,
					android.R.id.text2 };
			ListAdapter adapter = new SimpleAdapter(context, list,
					android.R.layout.simple_list_item_2, fromMapKey, toLayoutId);

			// ����Ʈ�信 ����� ����
			ListView listView = (ListView) view.findViewById(R.id.myStoryListView);
			listView.setAdapter(adapter);
			// Ŭ���̺�Ʈ ����
			listView.setOnItemClickListener(mOnItemClickListener);
		}
		
		private void sendAndSaveStory(){
			View view = mPager.findFocus();
			
			EditText et = (EditText) view.findViewById(R.id.story_content);
			String content = et.getText().toString();	//�̾߱� ����
			
			//���ۻ��� ��Ÿ��
			progressDialog = ProgressDialog.show(ViewPagerActivity.this, "", getResources().getString(R.string.app_name), true, true);

			//StoryService�ʱ�ȭ
			StoryService storyService = new StoryService(context);
			storyService.saveStoryToServer(content, new AfterSendAndSaveStory()); // ������ �̾߱�����, �Ķ���ͷ� �ݹ��Լ� �ѱ�
		}
		
		private class AfterSendAndSaveStory implements ServiceCompleteListener<String>{

			@Override
			public void onServiceComplete(String result) {
				Log.i("onTaskComplete", result);

				if (progressDialog != null)
					progressDialog.dismiss();

				Toast toast = Toast.makeText(context, "�̾߱Ⱑ ���۵Ǿ����ϴ�", Toast.LENGTH_SHORT);
				toast.show();

				// �� �̾߱� �б� ȭ������ �̵�
				//startActivity(new Intent(context, ReadMyStoryActivity.class));
				mPager.setCurrentItem(2);
				
			}
		}
		
	}

}
