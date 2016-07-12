package com.zcl.board.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.zcl.board.listener.MouseActionListener;
import com.zcl.board.mouse.MouseAction;

/**
 * @author  ZL 
 * @version 2016-5-25 ����10:27:53
 */
public class ControlView extends View{

    /**
     * view�Ŀ�
     */
    private int viewWidth;
    
    /**
     * view�ĸ�
     */
    private int viewHeight;
    
    /**
     * ������ʼ�����y����
     */
    private int btnY;
    
    /**
     * �����x�������
     */
    private int btnLeftXend;
    
    /**
     * �м���x�������
     */
    private int btnMidXend;

    /**
     * ���� �����������x
     */
    private float x;
    
    /**
     * ���㣬 ���������y
     */
    private float y;
    
    /**
     * 2�㴥���µڶ���������x
     */
    private float xT = 0;
    
    /**
     * 2�㴥���µڶ����������y
     */
    private float yT = 0;

    /**
     * ���down
     */
    private boolean isLeftBtnDown;
    
    /**
     * �м���down
     */
    private boolean isMidBtnDown;
    
    /**
     * �Ҽ���down
     */
    private boolean isRightBtnDown;
    

    /**
     * ��־���������״̬�ƶ����ذ壬������ק
     */
    private boolean isLeftMove;
    
    /**
     * ��־����ǰ��������λ��ģʽ�����ģʽ
     */
    private boolean isWY;
    
    
    private MouseActionListener mouseListener;
    /**
     * ���ö�������
     * @param mouse
     */
    public void setOnMouseActionListener(MouseActionListener mouse) {
        mouseListener = mouse;
    }

    public ControlView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        viewWidth = getWidth();
        viewHeight = getHeight();

        // ��������
        btnY = (int) (viewHeight * 9 / 10f);
        // �������x
        btnLeftXend = (int) (viewWidth * 2 / 5f);
        // �м����x
        btnMidXend = (int) (viewWidth * 3 / 5f);

        // ��������
        Paint p = new Paint();

        p.setStyle(Paint.Style.FILL);// ��������
        
        if(isLeftBtnDown){
            p.setColor(Color.YELLOW);// �����ɫ
            // �����
            canvas.drawRect(0, btnY, btnLeftXend, viewHeight, p);
        } else {
            p.setColor(Color.GRAY);// ���û�ɫ
            // �����
            canvas.drawRect(0, btnY, btnLeftXend, viewHeight, p);
        }
        
        if(isMidBtnDown){
            p.setColor(Color.YELLOW);// �����ɫ
            canvas.drawRect(btnLeftXend, btnY, btnMidXend, viewHeight, p);

        } else {
            // ���м�
            p.setColor(Color.DKGRAY);
            canvas.drawRect(btnLeftXend, btnY, btnMidXend, viewHeight, p);
        }
        
        if(isRightBtnDown){
            p.setColor(Color.YELLOW);// �����ɫ
            // ���Ҽ�
            canvas.drawRect(btnMidXend, btnY, viewWidth, viewHeight, p);
        } else {
            p.setColor(Color.GRAY);// ���û�ɫ
            // ���Ҽ�
            canvas.drawRect(btnMidXend, btnY, viewWidth, viewHeight, p);
        }

        p.setColor(Color.RED);// ���ú�ɫ
        // ���ּ�
        canvas.drawText("���", (viewWidth * 0.7f / 5f),
                (viewHeight * 9.5f / 10f), p);
        canvas.drawText("�м�", (viewWidth * 2.3f / 5f),
                (viewHeight * 9.5f / 10f), p);
        canvas.drawText("�Ҽ�", (viewWidth * 3.7f / 5f),
                (viewHeight * 9.5f / 10f), p);
    }

    @SuppressLint("NewApi")
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:// ���㰴��ʱ���ص�
                motionOneDown(event);
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                if (event.getPointerCount() == 1) {
                    // ���㴥��ʱ������ص��˷���
                } else if (event.getPointerCount() == 2) {// ��¼��ϼ����ܣ��Ȱ�����������Ҽ�
                    motionTwoDown(event);
                }
                break;

            case MotionEvent.ACTION_MOVE:// ֻ�����ƶ�
                motionMove(event);
                break;

            case MotionEvent.ACTION_UP:
                motionUp(event);
                break;

            default:
                break;
        }
        return true;
    }

    /**
     * ���㰴�´���
     */
    private void motionOneDown(MotionEvent event) {
        // ��¼�������꣬����ʱ��ֻ���ǰ����¼�
        x = event.getX();
        y = event.getY();

        // ������
        if (x < btnLeftXend && y > btnY) {// ������������������
            isLeftBtnDown = true;// ������
            mouseListener.sendMouseAction(MouseAction.LEFT_DOWN, 0, 0);
            //�����ɫ
            invalidate();
            
        }

        // �м����
        if (x > btnLeftXend && x < btnMidXend && y > btnY) {
            mouseListener.sendMouseAction(MouseAction.MIDDLE_DOWN, 0, 0);
            //�����ɫ
            isMidBtnDown = true;
            invalidate();
        }

        // �Ҽ����
        if (x > btnMidXend && y > btnY) {
            mouseListener.sendMouseAction(MouseAction.RIGHT_DOWN, 0, 0);
            //�����ɫ
            isRightBtnDown = true;
            invalidate();
        }
    }

    /**
     * ˫�㰴�´���
     */
    @SuppressLint("NewApi") private void motionTwoDown(MotionEvent event) {
        if (!isLeftMove) {// �������������ƶ�����Ϲ���״̬
            // ��¼��һ���������
            x = event.getX(0);
            y = event.getY(0);
            // �����������
            if (x < btnLeftXend && y > btnY) {
                isLeftBtnDown = true;
                mouseListener.sendMouseAction(MouseAction.LEFT_DOWN, 0, 0);
            }
            // �����Ҽ�����
            if (x > btnMidXend && y > btnY) {
                isRightBtnDown = true;
                mouseListener.sendMouseAction(MouseAction.RIGHT_DOWN, 0, 0);
            }
        }
        xT = event.getX(1);
        yT = event.getY(1);
    }

    /**
     * �ƶ�����
     */
    @SuppressLint("NewApi") private void motionMove(MotionEvent event) {
        if (event.getPointerCount() == 1 && event.getY() < btnY) {// �ƶ�ǰ���㴥�ذ�����
            // ���㴥��������ȡ��������µ�״̬
            if (isLeftBtnDown) {
                mouseListener.sendMouseAction(MouseAction.LEFT_UP, 0, 0);
                isLeftBtnDown = false;
            }

            // �ص���¼λ��
            x = (event.getX() - x);
            y = (event.getY() - y);

            if (x == 0 && y == 0) {
                // �����ģʽ
                isWY = false;
            } else {
                // ����λ��ģʽ
                isWY = true;
                // ����λ��
                mouseListener.sendMouseAction(MouseAction.MOVE, (int) x, (int) y);
            }
            x = event.getX();
            y = event.getY();

        } else if (event.getPointerCount() == 2) {
            // ����ƶ�,���͵ڶ�������꣬�ҵڶ������ڴ����������ƶ�����ϼ�����
            xT = event.getX(1) - xT;
            yT = event.getY(1) - yT;
            if (isLeftBtnDown || isRightBtnDown) {// ���Ҽ���ϼ�����
                // ��ק��������ק״̬
                isLeftMove = true;
                mouseListener.sendMouseAction(MouseAction.MOVE, (int) xT, (int) yT);
            }
            xT = event.getX(1);
            yT = event.getY(1);
        }
    }

    /**
     * ̧����
     */
    private void motionUp(MotionEvent event) {
        // �����
        if (x < btnLeftXend && y > btnY) {
            isLeftBtnDown = false;
            isLeftMove = false;
            mouseListener.sendMouseAction(MouseAction.LEFT_UP, 0, 0);
            //�����ɫ
            invalidate();

        }
        // �м���
        if (x > btnLeftXend && x < btnMidXend && y > btnY) {
            mouseListener.sendMouseAction(MouseAction.MIDDLE_UP, 0, 0);
            //�����ɫ
            isMidBtnDown = false;
            invalidate();
        }
        // �Ҽ���
        if (x > btnMidXend && y > btnY) {
            isRightBtnDown = false;
            isLeftMove = false;
            mouseListener.sendMouseAction(MouseAction.RIGHT_UP, 0, 0);
            //�����ɫ
            isRightBtnDown = false;
            invalidate();
        }

        if (y < btnY && !isWY) {// �������λ��ģʽ�������ڴ��ذ����򣬿��ٵ��̧�������൱�ڣ�����¼�
            mouseListener.sendMouseAction(MouseAction.LEFT_DOWN, 0, 0);
            mouseListener.sendMouseAction(MouseAction.LEFT_UP, 0, 0);

        }

        if (y < btnY && isWY) {// ȡ��λ��ģʽ״̬
            isWY = false;
        }
        x = 0;
        y = 0;
        xT = 0;
        yT = 0;
    }
}
