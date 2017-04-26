package com.lxs.calendar.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import com.lxs.calendar.GlobalVariable;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Administrator on 2017/4/25.
 */

public class CalendarView extends View {

    // 单元格点击
    private OnCalendarCardItemListener onCalendarCardItemListener;

    private static final int TOTAL_COL = 7; // 7列
    private static int TOTAL_ROW = 0;

    private Paint redCirclePaint; // 选中画笔
    private Paint grayCirclePaint; // 灰色画笔
    private Paint orangeCirclePaint; // 非选中画笔
    private Paint whitePaint; // 白色文本的画笔
//    private Paint grayPaint; // 灰色文本的画笔

    private int mViewWidth; // 视图的宽度
    private int cellSpace; // 单元格间距

    private CalendarViewRow[] rows; // 行数组，每个元素代表一行
    private int touchSlop;

    private CalendarViewCell mClickCell;//列
    private float mDownX;
    private float mDownY;

    private CalendarDate calendarDate;
    private HashMap<Integer, Integer> map;

    public CalendarView(Context context) {
        super(context);
        init(context);
    }

    public CalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (rows != null) {
            for (int i = 0; i < rows.length; i++) {
                if (rows[i] != null) {
                    rows[i].drawCalendar(canvas);
                }
            }
        }
    }

    // 默认执行，计算view的宽高,在onDraw()之前
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = measureWidth(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec);
        // 设置宽高
        setMeasuredDimension(width, height);
    }

    // 根据xml的设定获取宽度
    private int measureWidth(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        return specSize;
    }

    // 根据xml的设定获取高度
    private int measureHeight(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        return GlobalVariable.getInstance().onScreenWidth / TOTAL_COL * TOTAL_ROW;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewWidth = w;
        cellSpace = mViewWidth / TOTAL_COL;
        whitePaint.setTextSize(cellSpace / 3);
//        grayPaint.setTextSize(cellSpace / 3);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = event.getX();
                mDownY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                float disX = event.getX() - mDownX;
                float disY = event.getY() - mDownY;
                if (Math.abs(disX) < touchSlop && Math.abs(disY) < touchSlop) {
                    int col = (int) (mDownX / cellSpace);
                    int row = (int) (mDownY / cellSpace);
                    measureClickCell(col, row);
                }
                break;
            default:
                break;
        }

        return true;
    }

    private void init(Context context) {
        // 白色文本
        whitePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        whitePaint.setColor(Color.parseColor("#FFFFFE"));
        // 灰色文本
//        grayPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        grayPaint.setColor(Color.parseColor("#999999"));

        redCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        redCirclePaint.setStyle(Paint.Style.FILL);
        redCirclePaint.setColor(Color.parseColor("#FE6813")); // 红色圆形

        grayCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        grayCirclePaint.setStyle(Paint.Style.FILL);
        grayCirclePaint.setColor(Color.parseColor("#999999")); // 灰色圆形

        orangeCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        orangeCirclePaint.setStyle(Paint.Style.FILL);
        orangeCirclePaint.setColor(Color.parseColor("#7FB80E")); // 绿色圆形

        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();//移动事件的最短距离
    }

    private void setData() {
        int currentMonthDays = DateUtils.getMonthDays(calendarDate.getYear(), calendarDate.getMonth()); // 当前月的天数
        int firstDayWeek = DateUtils.getWeekDayFromDate(calendarDate.getYear(), calendarDate.getMonth());
        numberLine(currentMonthDays, firstDayWeek);// 计算行数
        int day = 0;
        for (int j = 0; j < TOTAL_ROW; j++) {
            rows[j] = new CalendarViewRow(j);
            for (int i = 0; i < TOTAL_COL; i++) {
                int position = i + j * TOTAL_COL; // 单元格位置
                //只有为当前月份的时候天数加一
                if (position >= firstDayWeek && position < firstDayWeek + currentMonthDays) {
                    day++;
                    Date nowdate = new Date();
                    Date showData = DateUtils.getDateFromString(calendarDate.getYear(), calendarDate.getMonth(), day);
                    CalendarDate customDate = CalendarDate.modifyCalendarDate(calendarDate, day);
                    if (DateUtils.daysOfTwo(nowdate, showData) < 0) {
                        //表示当前月已过去时间
                        customDate.setState(0);
                        rows[j].cells[i] = new CalendarViewCell(customDate, i, j);
                    } else {
                        if (map != null && map.containsKey(day)) {
                            if (map.get(day) == 0) {
                                customDate.setState(1);
                            } else {
                                customDate.setState(map.get(day));
                            }
                        } else {
                            customDate.setState(1);
                        }
                        rows[j].cells[i] = new CalendarViewCell(customDate, i, j);
                    }
                }
            }
        }
    }

    /**
     * 计算行数
     *
     * @param monthDays
     * @param firstDayWeek
     */
    private void numberLine(int monthDays, int firstDayWeek) {
        if ((monthDays + firstDayWeek) % 7 == 0) {
            TOTAL_ROW = (monthDays + firstDayWeek) / 7;
        } else {
            TOTAL_ROW = (monthDays + firstDayWeek) / 7 + 1;
        }
        rows = new CalendarViewRow[TOTAL_ROW];
    }

    /**
     * 计算点击的单元格
     *
     * @param col
     * @param row
     */
    private void measureClickCell(int col, int row) {
        if (col >= TOTAL_COL || row >= TOTAL_ROW)
            return;
        if (mClickCell != null) {
            rows[mClickCell.columnIndex].cells[mClickCell.rowIndex] = mClickCell;
        }
        if (rows[row] != null && rows[row].cells[col] != null) {
            mClickCell = new CalendarView.CalendarViewCell(rows[row].cells[col].date, rows[row].cells[col].rowIndex, rows[row].cells[col].columnIndex);
            CalendarDate customDate = rows[row].cells[col].date;
            customDate.setWeek(col);
            // 点元格点击回调
            if (onCalendarCardItemListener != null) {
                onCalendarCardItemListener.onItemClick(customDate);
            }
        }
    }

    /**
     * 行元素
     */
    private class CalendarViewRow {
        public int index;

        CalendarViewRow(int index) {
            this.index = index;
        }

        public CalendarViewCell[] cells = new CalendarViewCell[TOTAL_COL];

        /**
         * 绘制单元格
         *
         * @param canvas
         */
        public void drawCalendar(Canvas canvas) {
            for (int i = 0; i < cells.length; i++) {
                if (cells[i] != null) {
                    cells[i].drawCalendar(canvas);
                }
            }
        }

    }

    /**
     * 单元格元素
     */
    private class CalendarViewCell {
        public CalendarDate date;
        public int rowIndex;
        public int columnIndex;

        public CalendarViewCell(CalendarDate date, int rowIndex, int columnIndex) {
            this.date = date;
            this.rowIndex = rowIndex;
            this.columnIndex = columnIndex;
        }

        /**
         * 绘制单元格
         *
         * @param canvas
         */
        public void drawCalendar(Canvas canvas) {
            float x = (float) (cellSpace * (rowIndex + 0.5));
            float y = (float) ((columnIndex + 0.5) * cellSpace);
            switch (date.getState()) {
                case 1: // 选中
                    canvas.drawCircle(x, y, cellSpace / 3, redCirclePaint);
                    setText(canvas, whitePaint);
                    break;
                case 2: // 非选中
                    canvas.drawCircle(x, y, cellSpace / 3, orangeCirclePaint);
                    setText(canvas, whitePaint);
                    break;
                default:
                    canvas.drawCircle(x, y, cellSpace / 3, grayCirclePaint);
                    setText(canvas, whitePaint);
                    break;
            }
        }

        /**
         * 画文字
         *
         * @param canvas
         * @param paint
         */
        public void setText(Canvas canvas, Paint paint) {
            // 绘制文字
            String content = String.valueOf(date.getDay());
            float x = (float) ((rowIndex + 0.5) * cellSpace - paint.measureText(content) / 2);
            float y = (float) ((columnIndex + 0.7) * cellSpace - paint.measureText(content, 0, 1) / 2);
            canvas.drawText(content, x, y, paint);
        }
    }

    public void updateData(CalendarDate calendarDate, HashMap<Integer, Integer> map) {
        this.calendarDate = calendarDate;
        this.map = map;
        setData();
        invalidate();
    }

    /**
     * 点击日期监听
     *
     * @param onCalendarCardItemListener
     */
    public void setOnCalendarCardItemListener(OnCalendarCardItemListener onCalendarCardItemListener) {
        this.onCalendarCardItemListener = onCalendarCardItemListener;
    }

}
