package com.gif.eting.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.gif.eting.db.InboxDBHelper;
import com.gif.eting.dto.StoryDTO;

public class InboxDAO {

	// Database fields
	private SQLiteDatabase database;
	private InboxDBHelper dbHelper;
	private String[] allColumns = { InboxDBHelper.COL_IDX,
			InboxDBHelper.COL_CONTENT, InboxDBHelper.COL_STORY_DATE };

	// �����Ҷ� dbHelper �ʱ�ȭ
	public InboxDAO(Context context) {
		dbHelper = new InboxDBHelper(context);
	}

	// �������� open���ְ�
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	// �پ��� close�Ѵ�
	public void close() {
		dbHelper.close();
	}

	// Story ������
	public List<StoryDTO> getStoryList() {
		List<StoryDTO> storyList = new ArrayList<StoryDTO>();

		Cursor cursor = database.query(InboxDBHelper.TABLE_INBOX,
				allColumns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			StoryDTO story = getStoryDTO(cursor);
			storyList.add(story);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();

		for (StoryDTO story : storyList) {
			Log.i("inbox story list",
					story.getIdx() + story.getContent() + story.getStory_date());
		}
		
		return storyList;
	}

	// Story �Ѱ� ��������
	public StoryDTO getStoryInfo(StoryDTO story) {
		long idx = story.getIdx();
		Cursor cur = database.query(InboxDBHelper.TABLE_INBOX,
				allColumns, InboxDBHelper.COL_IDX + " = " + idx, null, null,
				null, null);
		cur.moveToFirst(); // Ŀ�� ó������

		StoryDTO returnedStory = getStoryDTO(cur); // ��ȯ�� ��ü
		cur.close();
		
		Log.i("inbox info",returnedStory.toString());

		return returnedStory;
	}

	// Story �Է�
	public Long insStory(StoryDTO story) {
		ContentValues values = new ContentValues();
		values.put(InboxDBHelper.COL_IDX, story.getIdx());
		values.put(InboxDBHelper.COL_CONTENT, story.getContent());
		values.put(InboxDBHelper.COL_STORY_DATE, story.getStory_date());
		long insertedId = database.insert(InboxDBHelper.TABLE_INBOX,
				null, values);
		Log.i("inbox is inserted",String.valueOf(insertedId));

		return insertedId;
	}

	// Story ����
	public Integer updStory(StoryDTO story) {
		long idx = story.getIdx();
		ContentValues values = new ContentValues();
		values.put(InboxDBHelper.COL_CONTENT, story.getContent());
		values.put(InboxDBHelper.COL_STORY_DATE, story.getStory_date());
		int rtn = database.update(InboxDBHelper.TABLE_INBOX, values,
				InboxDBHelper.COL_IDX + " = " + idx, null);
		Log.i("inbox is updated",String.valueOf(rtn));


		return rtn;
	}

	// Story ����
	public Integer delStory(StoryDTO story) {
		long idx = story.getIdx();
		int rtn = database.delete(InboxDBHelper.TABLE_INBOX,
				InboxDBHelper.COL_IDX + " = " + idx, null);
		Log.i("inbox is deleted",String.valueOf(rtn));
		return rtn;
	}

	// Ŀ������ DTO�޾ƿ���
	private StoryDTO getStoryDTO(Cursor cursor) {
		StoryDTO story = new StoryDTO(); // ��ü �ʱ�ȭ
		story.setIdx(cursor.getLong(0));
		story.setContent(cursor.getString(1));
		story.setStory_date(cursor.getString(2));
		return story;
	}
}
