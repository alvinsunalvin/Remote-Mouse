package com.zcl.board.mouse;

import java.net.DatagramPacket;
import java.net.InetSocketAddress;

import com.zcl.board.service.MyService;
import com.zcl.board.utils.ByteUtils;

/**
 * �������ָ��Ĺ����࣬��궯����������� 
 * @author ZL
 * @version 2016-5-25 ����11:14:54
 */
public class SendMouseActionUtils {

	public static void sendAction(InetSocketAddress inetSocketAddress,MouseAction action, int x, int y) {
		
		byte[] data = new byte[2];
		
		switch (action) {
		case HEARTBEAT:
			data[0] = -128;
			data[1] = -128;
			break;
		case MOVE: // �ƶ����
			data = ByteUtils.xyTo2Bytes(x, y);
			break;
		case LEFT_DOWN: // ����������
			data[0] = 1;
			data[1] = -128;
			break;
		case LEFT_UP: // ������̧��
			data[0] = 2;
			data[1] = -128;
			break;
		case MIDDLE_DOWN: // ����м�����
			data[0] = 3;
			data[1] = -128;
			break;
		case MIDDLE_UP:// ����м�̧��
			data[0] = 4;
			data[1] = -128;
			break;
		case RIGHT_DOWN: // ����Ҽ�����
			data[0] = 5;
			data[1] = -128;
			break;
		case RIGHT_UP: // ����Ҽ�̧��
			data[0] = 6;
			data[1] = -128;
			break;
		}
		
		try {
			DatagramPacket dp = new DatagramPacket(data, data.length,inetSocketAddress.getAddress(), inetSocketAddress.getPort());
			MyService.mDatagramSocket.send(dp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
