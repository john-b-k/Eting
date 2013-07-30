package com.gif.eting.svc;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.gif.eting.dao.InboxDAO;
import com.gif.eting.dao.StoryDAO;
import com.gif.eting.dto.StoryDTO;

public class StoryService {
	
	Context context;
	
	public StoryService(Context context){
		this.context = context;		
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
				saveRecievedStoryToPhone(recievedStoryDto); // ��DB�� �޾ƿ� �̾߱� ����
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

	}
	
	//�� �̾߱⸦ ���� ����
	private void saveMyStoryToPhone(StoryDTO story){
		
		StoryDAO storyDao = new StoryDAO(context);
		storyDao.open();	//����		
		storyDao.insStory(story);	//�Է��ϰ�
		storyDao.close();	//�ݰ�
	}
	
	//�޾ƿ� �̾߱⸦ ���� ����
	private void saveRecievedStoryToPhone(StoryDTO story){
		
		InboxDAO inboxDao = new InboxDAO(context);
		inboxDao.open();	//����		
		inboxDao.insStory(story);	//�Է��ϰ�
		inboxDao.close();	//�ݰ�
	}

	// �׽�Ʈ��
	public void dbTest() {
		StoryDAO storyDao = new StoryDAO(context);
		storyDao.open();
		List<StoryDTO> myStoryList = storyDao.getStoryList();
		for (StoryDTO story : myStoryList) {
			Log.i("my story list",
					story.getIdx() + story.getContent() + story.getStory_date());
		}
		storyDao.close();

		InboxDAO inboxDao = new InboxDAO(context);
		inboxDao.open();
		List<StoryDTO> recievedStoryList = inboxDao.getStoryList();
		for (StoryDTO story : recievedStoryList) {
			Log.i("inbox story list", story.getIdx() + story.getContent()
					+ story.getStory_date());
		}
		inboxDao.close();
	}

}
