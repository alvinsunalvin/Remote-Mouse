package com.zcl.board.listener;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.zcl.board.mouse.MouseAction;
import com.zcl.board.mouse.SendMouseActionUtils;
import com.zcl.board.service.MyService;

/**
 * ���������Ƶķ��ͺ�ָ���
 * sendMouseActionÿ�η�������count = 5
 * heartbeatSend�жϳ���5��Ϳ�ʼ����������
 * @author ZL
 * @version 2016-5-25 ����11:05:21
 */
public class MouseActionListener implements MouseActionListenerInterface {
	
	/**����ָ����Ƶ���¼���Ҫ�����̳߳�����*/
	private ExecutorService fixedThreadPool;
	/** ���Զ˿� */
	private InetSocketAddress mRemote;
	/** ֹͣ�������ָ��5��֮����߸ճ�ʼ��5��֮��ʼ���������������Զ˼��ֹͣ����ָ��ʱ��Ϊ10�� */
	private int count = 5;

	public MouseActionListener(InetSocketAddress remote) {
		mRemote = remote;
		fixedThreadPool = Executors.newFixedThreadPool(1);
		// ���������̣߳��жϷ���ָ���ļ��ʱ�䳬��5�뿪ʼ����������
		heartbeatSend();
	}
	
	/**
	 * ���������̣߳��жϷ���ָ���ļ��ʱ�䳬��5�뿪ʼ����������
	 */
	private void heartbeatSend(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					if (count > 0) {
						synchronized (this) {
							count--;
						}
					} else {
						// ����������
						fixedThreadPool.execute(new Runnable() {
							@Override
							public void run() {
								MyService.isStartHeartbeat = true;
								SendMouseActionUtils.sendAction(mRemote,MouseAction.HEARTBEAT, 0, 0);
							}
						});

					}

					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	
	@Override
	public void sendMouseAction(final MouseAction action, final int x,final int y) {
		// ���߳�˳������
		fixedThreadPool.execute(new Runnable() {
			@Override
			public void run() {
				SendMouseActionUtils.sendAction(mRemote, action, x, y);
				// �ָ���ʱ��
				synchronized (this) {
					count = 5;
					// ������������־�Ƿ�ʼ
					MyService.isStartHeartbeat = false;
				}
			}
		});
	}

}
