package com.zcl.board.listener;

import com.zcl.board.mouse.MouseAction;

/**
 * ����¼�ִ�м����Ľӿ�
 * @author ZL
 * @version 2016-5-25 ����10:59:08
 */
public interface MouseActionListenerInterface {
	void sendMouseAction(MouseAction action, int x, int y);
}
