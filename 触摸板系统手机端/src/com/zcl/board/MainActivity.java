package com.zcl.board;

import java.net.InetSocketAddress;
import java.util.ArrayList;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zcl.board.adapter.CompIpPortAdapter;
import com.zcl.board.broadcastreceiver.ComputerBroadcastReceiver;
import com.zcl.board.constant.Constant;
import com.zcl.board.listener.BtnSearchClickListener;
import com.zcl.board.listener.LvItemClickListener;
import com.zcl.board.service.MyService;
import com.zcl.board.service.MyService.MyBinder;

/**
 * �������˷��񣬴���ͨѶsocket
 * ���������߳�
 * @author ZL
 * @2016-5-24@����3:24:03
 */
public class MainActivity extends Activity {

	private static final String TAG = MainActivity.class.getSimpleName();
	private MyBinder myBinder;

	private ListView lv_computer;
	private CompIpPortAdapter mAdapter;
	/**��������Χ�����б�����*/
	private ArrayList<InetSocketAddress> computerSockets;
	/**�������շ��񴫵ݵĵ��Զ���Ϣ*/
	private ComputerBroadcastReceiver mComputerBroadcastReceiver;

	private Button btn_search;
	private LinearLayout ll_search;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		initView();
		bindService();
		initdata();
	}

	@Override
	protected void onResume() {
		super.onResume();
		IntentFilter filter = new IntentFilter(Constant.intentActionOnLine);
		registerReceiver(mComputerBroadcastReceiver, filter);
	}

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(mComputerBroadcastReceiver);
	}

	private void initView() {
		lv_computer = (ListView) findViewById(R.id.lv_computer);
		lv_computer.setEmptyView((TextView) findViewById(R.id.tv_empty));

		btn_search = (Button) findViewById(R.id.btn_search);
		ll_search = (LinearLayout) findViewById(R.id.ll_search);
	}

	/**
	 * ServiceConnection���ӷ���Ϊ�첽�ķ���������initview��initdata֮��ִ�У�����Ҫȷ��myBinder�õ�����ʹ��
	 */
	private void bindService() {
		Intent intent = new Intent(this, MyService.class);
		ServiceConnection conn = new ServiceConnection() {
			public void onServiceConnected(ComponentName name, IBinder service) {
				Log.v(TAG, "onServiceConnected");
				myBinder = (MyBinder) service;
				// ���������߳�
				btn_search.setOnClickListener(new BtnSearchClickListener(MainActivity.this,ll_search));			
				lv_computer.setOnItemClickListener(new LvItemClickListener(MainActivity.this, btn_search, ll_search));
			}

			public void onServiceDisconnected(ComponentName name) {
				Log.v(TAG, "onServiceDisconnected");
			}
		};
		bindService(intent, conn, BIND_AUTO_CREATE);
	}
	
	private void initdata() {
		// ��ʾlistview
		computerSockets = new ArrayList<InetSocketAddress>();
		mAdapter = new CompIpPortAdapter(this, computerSockets);
		lv_computer.setAdapter(mAdapter);
		// �½��㲥������
		mComputerBroadcastReceiver = new ComputerBroadcastReceiver(computerSockets, mAdapter);
	}
	
}
