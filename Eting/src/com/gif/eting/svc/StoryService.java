package com.gif.eting.svc;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.gif.eting.dao.InboxDAO;
import com.gif.eting.dao.StoryDAO;
import com.gif.eting.dto.StoryDTO;
import com.gif.eting.util.AsyncTaskCompleteListener;
import com.gif.eting.util.HttpUtil;
import com.gif.eting.util.Installation;
import com.gif.eting.util.ServiceCompleteListener;

public class StoryService {
	
	//private String serverContext = "http://lifenjoys.cafe24.com/eting";	//����
	private String serverContext = "http://112.144.52.47:8080/eting";	//���߼���
	private Context context;
	private InboxDAO inboxDao;
	private StoryDAO storyDao;
	
	public StoryService(Context context){
		this.context = context;
		this.storyDao = new StoryDAO(context);
		this.inboxDao = new InboxDAO(context);
	}

	//������ ���� �� �ڷ� �޾ƿ�
	public void saveStoryToServer(String content, ServiceCompleteListener<String> callback) {
		
		String url = this.serverContext + "/saveStory";	//���߼����ּ� URL
		String phoneId = Installation.id(context);	//��� ������
		String params = "phone_id=" + phoneId+"&content=" + content;	//�Ķ���� ����
		
		new HttpUtil(url, params,new AfterSaveStoryToServer(callback)).execute(params);	//Http��û. �ݹ��Լ��� �Ʒ���
		
	}
	
	// Http��� �� ó������
	private class AfterSaveStoryToServer implements
			AsyncTaskCompleteListener<String> {
		
		private ServiceCompleteListener<String> callback;

		public AfterSaveStoryToServer(ServiceCompleteListener<String> callback) {
			super();
			this.callback = callback;
		}

		@Override
		public void onTaskComplete(String response) {

			Log.i("json response", response);	//���� Ȯ��

			// ��DB�� ����
			saveToPhoneDB(response);
			
			dbTest();	//�ڷ��ԷµǾ��� Ȯ���ϴ� �׽�Ʈ�Լ�

			// ȣ���� Ŭ���� �ݹ�
			if (callback != null)
				callback.onServiceComplete("");	//ȭ�鿡 ������ �ѱ���ΰ�?
		}

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
	public void saveStampToServer(String storyId, String stampId, ServiceCompleteListener<String> callback) {
		
		String url = this.serverContext+"/saveStamp";
		String params = "story_id=" + storyId+"&stamp_id=" + stampId;	//�Ķ���� ����
		
		new HttpUtil(url, params, new AfterSaveStampToServer(callback, storyId)).execute("");	//Http ��û
	}

	// Http��� �� ó������
	private class AfterSaveStampToServer implements
			AsyncTaskCompleteListener<String> {
		
		private ServiceCompleteListener<String> callback;
		private String storyId;

		public AfterSaveStampToServer(
				ServiceCompleteListener<String> callback, String storyId) {
			super();
			this.callback = callback;
			this.storyId = storyId;
		}

		@Override
		public void onTaskComplete(String response) {

			Log.i("json response", response);	//����Ȯ��

			StoryDTO inboxStory = new StoryDTO();
			inboxStory.setIdx(Long.parseLong(storyId));

			// ���������� �̾߱� ����
			inboxDao.open();
			inboxDao.delStory(inboxStory);
			inboxDao.close();
			
			// ȣ���� Ŭ���� �ݹ�
			if (callback != null)
				callback.onServiceComplete("");	//ȭ�鿡 ������ �ѱ���ΰ�?
		}
	}
	
	//�������� ������ ��������
	public void getStampFromServer(String storyId, ServiceCompleteListener<String> callback){
		String url = this.serverContext+"/getStamp";
		String params = "storyId=" + storyId;	//�Ķ���� ����
		new HttpUtil(url, params, new AfterGetStampFromServer(callback));	//Http ��û
	}
	
	// Http��� �� ó������
		private class AfterGetStampFromServer implements
				AsyncTaskCompleteListener<String> {
			
			private ServiceCompleteListener<String> callback;

			public AfterGetStampFromServer(
					ServiceCompleteListener<String> callback) {
				super();
				this.callback = callback;
			}

			@Override
			public void onTaskComplete(String response) {

				Log.i("json response", response);	//����Ȯ��
				
				StringBuffer stamps = new StringBuffer();
				
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
				
				// ȣ���� Ŭ���� �ݹ�
				if (callback != null)
					callback.onServiceComplete(stamps.toString());
			}

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
