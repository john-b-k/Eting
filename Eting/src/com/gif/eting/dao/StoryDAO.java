package com.gif.eting.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.gif.eting.db.StoryDBHelper;
import com.gif.eting.dto.StoryDTO;

public class StoryDAO {

	// Database fields
	private SQLiteDatabase database;
	private StoryDBHelper dbHelper;
	private String[] allColumns = { StoryDBHelper.COL_IDX,
			StoryDBHelper.COL_CONTENT, StoryDBHelper.COL_STORY_DATE };

	// �����Ҷ� dbHelper �ʱ�ȭ
	public StoryDAO(Context context) {
		dbHelper = new StoryDBHelper(context);
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

		Cursor cursor = database.query(StoryDBHelper.TABLE_STORY_MASTER,
				allColumns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			StoryDTO story = getStoryDTO(cursor);
			storyList.add(story);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		
		Log.i("story list",storyList.toString());
		
		return storyList;
	}

	// Story �Ѱ� ��������
	public StoryDTO getStoryInfo(StoryDTO story) {
		long idx = story.getIdx();
		Cursor cur = database.query(StoryDBHelper.TABLE_STORY_MASTER,
				allColumns, StoryDBHelper.COL_IDX + " = " + idx, null, null,
				null, null);
		cur.moveToFirst(); // Ŀ�� ó������

		StoryDTO returnedStory = getStoryDTO(cur); // ��ȯ�� ��ü
		cur.close();
		
		Log.i("story info",returnedStory.toString());

		return returnedStory;
	}

	// Story �Է�
	public Long insStory(StoryDTO story) {
		ContentValues values = new ContentValues();
		values.put(StoryDBHelper.COL_CONTENT, story.getContent());
		values.put(StoryDBHelper.COL_STORY_DATE, story.getStory_date());
		long insertedId = database.insert(StoryDBHelper.TABLE_STORY_MASTER,
				null, values);
		Log.i("story is inserted",String.valueOf(insertedId));
		return insertedId;
	}

	// Story ����
	public Integer updStory(StoryDTO story) {
		long idx = story.getIdx();
		ContentValues values = new ContentValues();
		values.put(StoryDBHelper.COL_CONTENT, story.getContent());
		values.put(StoryDBHelper.COL_STORY_DATE, story.getStory_date());
		int rtn = database.update(StoryDBHelper.TABLE_STORY_MASTER, values,
				StoryDBHelper.COL_IDX + " = " + idx, null);
		Log.i("story is updated",String.valueOf(rtn));

		return rtn;
	}

	// Story ����
	public Integer delStory(StoryDTO story) {
		long idx = story.getIdx();
		int rtn = database.delete(StoryDBHelper.TABLE_STORY_MASTER,
				StoryDBHelper.COL_IDX + " = " + idx, null);
		Log.i("story is deleted",String.valueOf(rtn));
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
