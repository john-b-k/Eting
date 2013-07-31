package com.gif.eting.svc;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.gif.eting.ReadMyStoryActivity;
import com.gif.eting.dao.InboxDAO;
import com.gif.eting.dao.StoryDAO;
import com.gif.eting.dto.StampDTO;
import com.gif.eting.dto.StoryDTO;
import com.gif.eting.util.HttpUtil;
import com.gif.eting.util.Installation;

public class StoryService {
	
	private Context context;
	private InboxDAO inboxDao;
	private StoryDAO storyDao;
	
	public StoryService(Context context){
		this.context = context;
		this.storyDao = new StoryDAO(context);
		this.inboxDao = new InboxDAO(context);
	}

	//������ ���� �� �ڷ� �޾ƿ�
	public void saveStoryToServer(String content) {
		String phoneId = Installation.id(context);	//��� ������

		HttpUtil http = new HttpUtil();
		String params = "phone_id=" + phoneId+"&content=" + content;	//�Ķ���� ����
		
		//String response = http.doPost("http://lifenjoys.cafe24.com/eting/saveStory", params);	//�����ּ�
		String response = http.doPost("http://112.144.52.47:8080/eting/saveStory", params);	//���߼����ּ�
		
		Log.i("json response", response);
		
		//��DB�� ����
		saveToPhoneDB(response);
		dbTest();
	}

	//�������� �޾ƿ� �ڷ� ����
	public void saveToPhoneDB(String response){

		try {
			JSONObject json = new JSONObject(response);

			if (!json.isNull("myStory")) {
				JSONObject myStory = json.getJSONObject("myStory");

				// �� �̾߱�
				StoryDTO myStoryDto = new StoryDTO();
				String myStoryId = myStory.getString("story_id");
				String myContent = myStory.getString("content");
				String myStoryDate = myStory.getString("story_date");
				myStoryDto.setIdx(Long.parseLong(myStoryId));
				myStoryDto.setContent(myContent);
				myStoryDto.setStory_date(myStoryDate);

				Log.i("returned my story",
						myStoryDto.getIdx() + myStoryDto.getContent() + myStoryDto.getStory_date());
				
				
				saveMyStoryToPhone(myStoryDto); // ��DB�� �� �̾߱� ����
			}

			if (!json.isNull("recievedStory")) {
				JSONObject recievedStory = json.getJSONObject("recievedStory");

				// �޾ƿ� �̾߱�
				StoryDTO recievedStoryDto = new StoryDTO();
				String recievedStoryId = recievedStory.getString("story_id");
				String recievedContent = recievedStory.getString("content");
				String recievedStoryDate = recievedStory.getString("story_date");
				recievedStoryDto.setIdx(Long.parseLong(recievedStoryId));
				recievedStoryDto.setContent(recievedContent);
				recievedStoryDto.setStory_date(recievedStoryDate);
				
				Log.i("returned recieved story",
						recievedStoryDto.getIdx() + recievedStoryDto.getContent() + recievedStoryDto.getStory_date());
				
				saveRecievedStoryToPhone(recievedStoryDto); // ��DB�� �޾ƿ� �̾߱� ����
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

	}
	
	//�� �̾߱⸦ ���� ����
	private void saveMyStoryToPhone(StoryDTO story){
		storyDao.open();	//����		
		storyDao.insStory(story);	//�Է��ϰ�
		storyDao.close();	//�ݰ�
	}
	
	//�޾ƿ� �̾߱⸦ ���� ����
	private void saveRecievedStoryToPhone(StoryDTO story){
		inboxDao.open();	//����		
		inboxDao.insStory(story);	//�Է��ϰ�
		inboxDao.close();	//�ݰ�
	}
	
	//�� �̾߱� ��������
	public List<StoryDTO> getMyStoryList(){
		storyDao.open();
		List<StoryDTO> myStoryList = storyDao.getStoryList();
		storyDao.close();
		return myStoryList;
	}
	
	// �� �̾߱� ��������
	public StoryDTO getMyStory(String idx) {
		StoryDTO story = new StoryDTO();
		story.setIdx(Long.parseLong(idx));
		
		storyDao.open();
		StoryDTO myStory = storyDao.getStoryInfo(story);
		storyDao.close();
		return myStory;
	}

	// �޾ƿ� �̾߱� �ϳ� ��������
	public StoryDTO getInboxStory() {
		inboxDao.open();
		List<StoryDTO> inboxStoryList = inboxDao.getStoryList();
		inboxDao.close();
		
		StoryDTO returnedStory;
		if(inboxStoryList.size()>0){
			returnedStory = inboxStoryList.get(0);
		}else{
			returnedStory = new StoryDTO();
		}
		return returnedStory;
	}
	

	//������ ������ ���� ���� ����
	public void saveStampToServer(String storyId, String stampId) {
		
		HttpUtil http = new HttpUtil();
		String params = "story_id=" + storyId+"&stamp_id=" + stampId;	//�Ķ���� ����
		
		//String response = http.doPost("http://lifenjoys.cafe24.com/eting/saveStamp", params);	//�����ּ�
		String response = http.doPost("http://112.144.52.47:8080/eting/saveStamp", params);	//���߼����ּ�
		
		Log.i("json response", response);
		
		StoryDTO inboxStory = new StoryDTO();
		inboxStory.setIdx(Long.parseLong(storyId));
		
		//���������� �̾߱� ����
		inboxDao.open();
		inboxDao.delStory(inboxStory);
		inboxDao.close();
	}
	
	//�������� ������ ��������
	public String getStampFromServer(String storyId){
		StringBuffer stamps = new StringBuffer();
		
		HttpUtil http = new HttpUtil();
		String params = "storyId=" + storyId;	//�Ķ���� ����
		
		//String response = http.doPost("http://lifenjoys.cafe24.com/eting/getStamp", params);	//�����ּ�
		String response = http.doPost("http://112.144.52.47:8080/eting/getStamp", params);	//���߼����ּ�
		try {
			JSONObject json = new JSONObject(response);

			if (!json.isNull("stampList")) {
				JSONArray stampList = json.getJSONArray("stampList");
				for(int i=0; i<stampList.length(); i++){
					JSONObject stamp = stampList.getJSONObject(i);
					stamps.append(stamp.getString("stamp_name"));
					stamps.append(" , ");
					
					Log.i("returned stamp", stamp.getString("stamp_id") + stamp.getString("stamp_name"));
				}
			}


		} catch (JSONException e) {
			e.printStackTrace();
		}
		return stamps.toString();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	// �׽�Ʈ��
	public void dbTest() {
		storyDao.open();
		List<StoryDTO> myStoryList = storyDao.getStoryList();
		for (StoryDTO story : myStoryList) {
			Log.i("my story list",
					story.getIdx() + story.getContent() + story.getStory_date());
		}
		storyDao.close();

		inboxDao.open();
		List<StoryDTO> recievedStoryList = inboxDao.getStoryList();
		for (StoryDTO story : recievedStoryList) {
			Log.i("inbox story list", story.getIdx() + story.getContent()
					+ story.getStory_date());
		}
		inboxDao.close();
	}

}
