package com.zcl.board.utils;

/**
 * �ֽں�int��ת��������
 * @author ZL
 * @version 2016-5-25 ����11:17:52
 */
public class ByteUtils {

	/**
	 * ��2������ת���������ֽ���ɵ��з��ŵ���������
	 * @param x  xƫ����
	 * @param y  yƫ����
	 * @return ����Ϊ2��byte����
	 */
	public static byte[] xyTo2Bytes(int x, int y) {
		byte[] res = new byte[2];
		if (x < -127)
			x = -127;
		if (x > 127)
			x = 127;
		if (y < -127)
			y = -127;
		if (y > 127)
			y = 127;
		res[0] = (byte) ((x & 0x7f) | ((x & 0x80000000) >> 24));// ȡ���7λ��ȡ����һλ
		res[1] = (byte) ((y & 0x7f) | ((y & 0x80000000) >> 24));// ȡ���7λ��ȡ����һλ
		return res;
	}

	/**
	 * ����ת���ֽڱ�ʾָ��
	 * @param action
	 * @return
	 */
	public static byte[] actionTo2Bytes(int action) {
		byte[] res = new byte[2];
		res[0] = (byte) action;// �޷��ŵ���������,���������Ĭ��Ϊ0��
		res[1] = -128;// ������⺬��
		return res;
	}

}
