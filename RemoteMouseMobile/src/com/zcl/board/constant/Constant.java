package com.zcl.board.constant;

/**
 * ����
 * @author  ZL 
 * @version 2016-5-24 ����9:44:42
 */
public class Constant {
	
	/** ���յ����Իظ����ߵ�action */
	public static final String intentActionOnLine = "com.zcl.board.online";
	/** ���ߵ�action */
	public static final String intentActionOffLine = "com.zcl.board.offline";
	
	/** ���Զ˹㲥����socket��Ĭ�϶˿� */
	public static final int defaultPort = 48080;
	/** ����appsocket�Ķ˿�Ϊ0���Զ������ֻ��˿� */
	public static final int autoPort = 0;
	/** ͨ�õĹ㲥��ַ */
	public static final String defaultBoartCastIp = "255.255.255.255";
	/** ����intent�����Զ�ip��,�� */
	public static final String computerSocketAddress = "socketAddress";
	
	
	/** ��¼��ǰӦ�����ĸ�ҳ��������ҳ�� */
	public static boolean isInControlActivity = false;
	/** ��¼�Ƿ������� */
	public static boolean isSearch = false;
		
}
