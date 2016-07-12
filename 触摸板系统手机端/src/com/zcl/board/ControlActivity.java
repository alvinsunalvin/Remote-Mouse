package com.zcl.board;

import java.net.InetSocketAddress;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import com.zcl.board.constant.Constant;
import com.zcl.board.listener.MouseActionListener;
import com.zcl.board.service.MyService;
import com.zcl.board.view.ControlView;

/**
 * ����ҳ��
 * �������ҳ�������������thread
 * @author ZL
 * @version 2016-5-25 ����9:42:31
 */
public class ControlActivity extends Activity {

	private ControlView v_control;
	/** ���Զ˿ں�ip */
	private InetSocketAddress mSocketAddress;	
	/** ��־���Զ�����*/
	private boolean isOnLine = true;
	//MouseActionListener������������thread
	private MouseActionListener mMouseActionListener;
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			offLine();
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_control);
		// ��ȡ���л����ļ�����
		Intent intent = getIntent();
		mSocketAddress = (InetSocketAddress) intent.getSerializableExtra(Constant.computerSocketAddress);
		//��־���룬����ֹͣBtnSearchClickListener������ѭ��
		Constant.isInControlActivity = true;

		initView();
		initData();
		startNextHeartBeatTimeListener();
	}
	
	@SuppressLint("NewApi") @Override
	public void onBackPressed() {
	 	mMouseActionListener = null;
    	isOnLine =false;
		super.onBackPressed();
	}
	
	private void initView() {
		v_control = (ControlView) findViewById(R.id.v_control);
	}
	
	private void initData() {
		mMouseActionListener = new MouseActionListener(mSocketAddress);
		v_control.setOnMouseActionListener(mMouseActionListener);
	}

	/**
	 * ����������ķ������ж���һ���������ķ���
	 */
	private void startNextHeartBeatTimeListener() {

		new Thread(new Runnable() {
			@Override
			public void run() {
				while (isOnLine) {
					//ÿ�νӵ����Զ˵���ϢnextHeartBeatTime����Ϊ10���ݼ�10���жϣ�10��֮����Զ���
					timeTask();	
				}
			}
		}).start();	
	}
	
	/**
	 * ʱ���ж�
	 */
	private void timeTask() {
		synchronized (this) {
			if (MyService.isStartHeartbeat) {
				if (MyService.nextHeartBeatTime > 0) {
					MyService.nextHeartBeatTime--;
				} else {
					//���Զ��˳���
					isOnLine =false;
					mHandler.sendEmptyMessage(0);
				}
			}

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * ��ʾ���Զ˶���
	 */
	private void offLine() {

        AlertDialog ad=new AlertDialog.Builder(this).create();  
        ad.setTitle("�Ͽ���ʾ");  
        ad.setMessage("���Զ˶Ͽ�");  
        ad.setButton("ȷ��", new DialogInterface.OnClickListener() {                
            @Override  
            public void onClick(DialogInterface dialog, int which) {  
            	mMouseActionListener = null;
            	isOnLine =false;
            	//�˳�����
            	android.os.Process.killProcess(android.os.Process.myPid());    //��ȡPID 
            	System.exit(0);   //����java��c#�ı�׼�˳���������ֵΪ0���������˳�
            }  
        });  
        ad.show();	
	}
	
}
