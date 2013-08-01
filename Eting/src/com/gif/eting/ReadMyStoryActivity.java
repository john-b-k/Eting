package com.gif.eting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.TwoLineListItem;

import com.gif.eting.dto.StoryDTO;
import com.gif.eting.svc.StoryService;

public class ReadMyStoryActivity extends Activity implements OnItemClickListener{
	
	private StoryService storyService;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.read_story);
		
		drawMyStoryList();	//�� �̾߱� ��� �׸���
	}
	
	//�� �̾߱� ��� �׸���
	public void drawMyStoryList(){
		//StoryService�ʱ�ȭ
		storyService = new StoryService(this.getApplicationContext());
		List<StoryDTO> myStoryList = storyService.getMyStoryList();
		
		//����Ʈ�並 ���� ������
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		for(StoryDTO myStory:myStoryList){
	        Map<String,String> item = new HashMap<String,String>();
			item.put("title", myStory.getStory_date());
	        item.put("desc", myStory.getContent());
	        item.put("idx", String.valueOf(myStory.getIdx()));
	        list.add(item);
		}
		
		
		// ����Ϳ� ������ ����
		final String[] fromMapKey = new String[] { "title", "desc" };
		final int[] toLayoutId = new int[] { android.R.id.text1,
				android.R.id.text2 };
		ListAdapter adapter = new SimpleAdapter(this, list,
				android.R.layout.simple_list_item_2, fromMapKey, toLayoutId);

        // ����Ʈ�信 ����� ����
        ListView listView = (ListView)findViewById(R.id.myStoryListView);
        listView.setAdapter(adapter);
        //Ŭ���̺�Ʈ ����
        listView.setOnItemClickListener(this);

	}

	@SuppressWarnings("unchecked")
	public void onItemClick(AdapterView<?> parentView, View clickedView,
			int position, long id) {
		
		ListAdapter listAdapter = (ListAdapter) parentView.getAdapter();	//ListView���� Adapter �޾ƿ�
		Map<String, String> selectedItem = (Map<String, String>) listAdapter.getItem(position);	//������ Row�� �ִ� Object�� �޾ƿ�
		String idx = selectedItem.get("idx");	//Object���� idx���� �޾ƿ�
		
		String toastMessage = idx;

		Intent intent =new Intent(this, MyStoryPopupActivity.class);
		intent.putExtra("idx", idx);
		startActivity(intent);
		
		/*
		String toastMessage = ((TwoLineListItem) clickedView).getText1().getText()
				+ " is selected. position is " + position + ", and id is " + id;*/

		Toast.makeText(getApplicationContext(), toastMessage,
				Toast.LENGTH_SHORT).show();
	}
	
}
