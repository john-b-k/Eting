package com.gif.eting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LockScreenActivity extends Activity implements OnClickListener {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i("Eting", "onCreate");

		super.onCreate(savedInstanceState);
		setContentView(R.layout.lock_screen);

		// ��ư ��Ŭ���̺�Ʈ ���
		Button btn = (Button) findViewById(R.id.lockScreenButton);
		btn.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		Log.i("LickScreenOnClick", String.valueOf(view.getId()));
		// ��ȣȮ�� ��ư Ŭ����
		if (view.getId() == R.id.lockScreenButton) {
			this.checkPassword();
		}

	}

	private void checkPassword() {
		boolean isValid = false;
		EditText et = (EditText) findViewById(R.id.lockScreenPassword); // �Է¾�ȣ
		String pw = et.getText().toString();

		Log.i("password", pw);

		// ��ȣüũ �ӽ÷���
		if (pw.equals("1234")) {
			isValid = true;
		}

		// ��ȣ ����/���� �б�ó��
		if (isValid) {
			Intent intent = new Intent(LockScreenActivity.this,
					MainActivity.class);
			startActivity(intent);
		} else {
			// ��й�ȣ Ʋ������
			Toast toast = Toast.makeText(this, "��й�ȣ�� ���� �ʽ��ϴ�.",
					Toast.LENGTH_SHORT);
			toast.show();

			et.setText(""); // ��ȣ�Է��ʵ� �ʱ�ȭ
		}
	}

}
