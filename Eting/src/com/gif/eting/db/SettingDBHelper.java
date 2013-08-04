package com.gif.eting.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SettingDBHelper extends SQLiteOpenHelper {

	public static final String TABLE_SETTING = "setting"; // TABLE�̸�

	// �÷�����
	public static final String COL_KEY = "key"; // �޾ƿ� �̾߱��� ������ȣ
	public static final String COL_VALUE = "value"; // �޾ƿ� �̾߱� ����

	private static final String DATABASE_NAME = "eting_setting.db";
	private static final int DATABASE_VERSION = 1;

	// TABLE ������
	private static final String DATABASE_CREATE = "CREATE TABLE " + TABLE_SETTING
			+ "(" + COL_KEY + " text, " + COL_VALUE + " text " + ");";

	// Constructor
	public SettingDBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE); // TABLE����
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(SettingDBHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");

		// TODO ������ �ٸ��� �ӽ÷� ó��. ���� ���� �ʿ�.
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SETTING);
		onCreate(db);
	}

}
