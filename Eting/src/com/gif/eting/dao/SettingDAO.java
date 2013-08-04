package com.gif.eting.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.gif.eting.db.SettingDBHelper;
import com.gif.eting.dto.SettingDTO;

public class SettingDAO {

	// Database fields
	private SQLiteDatabase database;
	private SettingDBHelper dbHelper;
	private String[] allColumns = { SettingDBHelper.COL_KEY,
			SettingDBHelper.COL_VALUE };

	// �����Ҷ� dbHelper �ʱ�ȭ
	public SettingDAO(Context context) {
		dbHelper = new SettingDBHelper(context);
	}

	// �������� open���ְ�
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	// �پ��� close�Ѵ�
	public void close() {
		dbHelper.close();
	}

	// setting ������
	public List<SettingDTO> getsettingList() {
		List<SettingDTO> settingList = new ArrayList<SettingDTO>();

		Cursor cursor = database.query(SettingDBHelper.TABLE_SETTING,
				allColumns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			SettingDTO setting = getSettingDTO(cursor);
			settingList.add(setting);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();

		for (SettingDTO setting : settingList) {
			Log.i("setting setting list", setting.getKey() + setting.getValue());
		}

		return settingList;
	}

	// setting �Ѱ� ��������
	public SettingDTO getsettingInfo(String key) {
		Cursor cur = database.query(SettingDBHelper.TABLE_SETTING, allColumns,
				SettingDBHelper.COL_KEY + " = " + "'"+key+"'", null, null, null, null);
		cur.moveToFirst(); // Ŀ�� ó������

		SettingDTO returnedsetting = getSettingDTO(cur); // ��ȯ�� ��ü
		cur.close();
		
		if(returnedsetting!=null){
			Log.i("setting info", returnedsetting.toString());
		}

		return returnedsetting;
	}

	// setting �Է�
	public Long inssetting(SettingDTO setting) {
		ContentValues values = new ContentValues();
		values.put(SettingDBHelper.COL_KEY, setting.getKey());
		values.put(SettingDBHelper.COL_VALUE, setting.getValue());
		long insertedId = database.insert(SettingDBHelper.TABLE_SETTING, null,
				values);
		Log.i("setting is inserted", String.valueOf(insertedId));

		return insertedId;
	}

	// setting ����
	public Integer updsetting(SettingDTO setting) {
		String key = setting.getKey();
		ContentValues values = new ContentValues();
		values.put(SettingDBHelper.COL_VALUE, setting.getValue());
		int rtn = database.update(SettingDBHelper.TABLE_SETTING, values,
				SettingDBHelper.COL_KEY + " = " + "'"+key+"'", null);
		Log.i("setting is updated", String.valueOf(rtn));

		return rtn;
	}

	// setting ����
	public Integer delsetting(String key) {
		int rtn = database.delete(SettingDBHelper.TABLE_SETTING,
				SettingDBHelper.COL_KEY + " = " + "'"+key+"'", null);
		Log.i("setting is deleted", String.valueOf(rtn));
		return rtn;
	}

	// Ŀ������ DTO�޾ƿ���
	private SettingDTO getSettingDTO(Cursor cursor) {
		if (cursor!=null && cursor.getCount()>0) {
			SettingDTO setting = new SettingDTO(); // ��ü �ʱ�ȭ
			setting.setKey(cursor.getString(0));
			setting.setValue(cursor.getString(1));
			return setting;

		} else {
			return null;
		}
	}
}
