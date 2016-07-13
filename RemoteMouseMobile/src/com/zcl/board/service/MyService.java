package com.zcl.board.service;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;

import com.zcl.board.constant.Constant;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

/**
 * ��̨���������ڴ򿪶˿ڣ���������
 * @author ZL
 * @version 2016-5-24 ����9:11:28
 */
public class MyService extends Service {

	/** �շ����ݵ�UDPsocket */
	public static DatagramSocket mDatagramSocket;
	/** ��ʼ���������� ����MouseActionListenerÿ�η���������������isStartHeartbeat=true��*/
	public static boolean isStartHeartbeat = false;

	/** ���ڽ��յ����������ݺ󣬼�����һ�����������յļ��ʱ�䣬����10�룬������� */
	public static int nextHeartBeatTime = 10;
	
	private MyBinder myBinder;
	
	@Override
	public void onCreate() {
		super.onCreate();
		createSocket();
		startAppSocketListener();
	}

	@Override
	public IBinder onBind(Intent intent) {
		myBinder = new MyBinder();
		return myBinder;
	}

	/** ����binder���� */
	public class MyBinder extends Binder  {
		//��չ�������service�ķ���
	};

	private void createSocket() {
		try {
			// ֻ�ڳ���տ�ʼʱ������ֻ�ܱ�����һ�Σ�ȫ��ʹ�ã��˿�ָ��Ϊ0���Զ�����
			mDatagramSocket = new DatagramSocket(Constant.autoPort);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

	private void startAppSocketListener() {
		// ���������̣߳�������������������ֻ�ܿ���һ��
		new Thread(new Runnable() {
			@Override
			public void run() {

				byte data[] = new byte[2];
				// ���ݱ�
				DatagramPacket recPacket = new DatagramPacket(data, 2);
				while (true) {
					try {

						mDatagramSocket.receive(recPacket);
						// ���Ϊ00����Ϊ���Զ�����������00������Ϊ-128 -128
						if ((data[0] == 0) && (data[1] == 0)) {
							synchronized (this) {
								// ���µ���ʱ�������������10�룬����
								nextHeartBeatTime = 10;
							}

						} else {
							// �����ͼ������action
							Intent intent = new Intent(
									Constant.intentActionOnLine);
							// �ɴ��ݽ��յ������ݣ��˴�����Ҫ
							// intent.putExtra("data", data);
							// ��ȡ���Ե�ip�Ͷ˿�
							SocketAddress socketAddress = recPacket.getSocketAddress();
							//ǿתΪSocketAddress��ʵ����
							InetSocketAddress computerSocketAddress = (InetSocketAddress) socketAddress;
							intent.putExtra(Constant.computerSocketAddress, computerSocketAddress);
							sendBroadcast(intent);
						}

					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

}
