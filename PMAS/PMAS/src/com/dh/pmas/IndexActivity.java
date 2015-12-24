package com.dh.pmas;

import com.dh.pmas.R;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

@SuppressLint("HandlerLeak")
public class IndexActivity extends Activity {
	ImageView iv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Window win = getWindow();
		win.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_index);
		iv = (ImageView) this.findViewById(R.id.imageView1);

		welcome();
	}

	public void welcome() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Thread.sleep(3000);
					Message m = new Message();
					lhandler.sendMessage(m);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}).start();
	}

	Handler lhandler = new Handler() {
		public void handleMessage(Message msg) {
			begin();
		}
	};

	public void begin() {
		Intent in = new Intent(IndexActivity.this, MainActivity.class);
		startActivity(in);
		IndexActivity.this.finish();
	}
}
