package com.zcl.board.listener;

import java.net.DatagramPacket;
import java.net.InetAddress;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.zcl.board.R;
import com.zcl.board.constant.Constant;
import com.zcl.board.service.MyService;

/**
 * ���������߳�
 * ������ѭ��ÿ���뷢��һ�������㲥
 * @author ZL
 * @version 2016-5-25 ����9:50:20
 */
public class BtnSearchClickListener implements OnClickListener {

	private LinearLayout llProgress;
	private boolean isSearch;
	private Context mContext;
	public BtnSearchClickListener(Context context, LinearLayout llProgress) {
		this.llProgress = llProgress;
		mContext = context;
		//������ѭ��ÿ���뷢��һ�������㲥
		new Thread(new Runnable() {
			@Override
			public void run() {
				//�����ڿ���ҳ�棬����ҳ��ʱ����������
				while (!Constant.isInControlActivity) {
					// ÿ2�뷢��һ������ָ��
					if (isSearch) {
						search();
					}
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	/**
	 * �ھ������ڷ���һ�ι㲥������������
	 */
	public void search() {
		try {
			byte[] data = new byte[2];
			data[0] = 0;
			data[1] = (byte) 255;
			InetAddress adds = InetAddress.getByName(Constant.defaultBoartCastIp);
			// ������Զ˵Ķ˿�48080
			DatagramPacket dp = new DatagramPacket(data, data.length,adds, Constant.defaultPort);
			MyService.mDatagramSocket.send(dp);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void onClick(View v) {
		Button btn = (Button) v;
		String str = btn.getText().toString();
		//�л���ʾ״̬
		if (str.equals(mContext.getString(R.string.click_start_searching))) {
			btn.setText(mContext.getString(R.string.click_stop_searching));
			llProgress.setVisibility(View.VISIBLE);
			isSearch = true;
		} else {
			btn.setText(mContext.getString(R.string.click_start_searching));
			llProgress.setVisibility(View.INVISIBLE);
			isSearch = false;
		}
	}
	
}
