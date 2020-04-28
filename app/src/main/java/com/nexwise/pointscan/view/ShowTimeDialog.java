package com.nexwise.pointscan.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.nexwise.pointscan.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ShowTimeDialog extends Dialog{

    String data,time = "" ;
    public ShowTimeDialog(Context c, final ShowTimeIn s){
        super(c, R.style.noTitleDialog);
        View view = LayoutInflater.from(c).inflate(R.layout.timescheh, null);

        final DatePicker da = (DatePicker) view.findViewById(R.id.time_data);
        Calendar calendar=Calendar.getInstance();
        int year=calendar.get(Calendar.YEAR);
        int monthOfYear=calendar.get(Calendar.MONTH);
        int dayOfMonth=calendar.get(Calendar.DAY_OF_MONTH);

        data = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        da.init(year, monthOfYear, dayOfMonth, new DatePicker.OnDateChangedListener(){
            public void onDateChanged(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {
                //dateEt.setText("您选择的日期是："+year+"年"+(monthOfYear+1)+"月"+dayOfMonth+"日。");
                data = "" ;

                data += year ;
                data += "-" ;
                if ((monthOfYear+1) < 10) {
                    data += "0" ;
                    data += (monthOfYear+1) ;
                }else {
                    data += (monthOfYear+1) ;
                }
                data += "-" ;
                if (dayOfMonth < 10) {
                    data += "0" ;
                    data += dayOfMonth ;
                }else {
                    data += dayOfMonth ;
                }
            }
        });
        final TimePicker ti = (TimePicker) view.findViewById(R.id.time_time);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int m = calendar.get(Calendar.MINUTE);
        final int ss = calendar.get(Calendar.SECOND);
        ti.setCurrentHour(hour);
        //ti.setIs24HourView(true);
        if (hour < 10){
            time += "0" ;
            time += hour ;
        }else {
            time += hour ;
        }
        time += ":" ;
        if (m < 10){
            time += "0" ;
            time += m ;
        }else {
            time += m ;
        }
        time += ":" ;
        if (ss < 10){
            time += "0" ;
            time += ss ;
        }else {
            time += ss ;
        }

        ti.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                // TODO Auto-generated method stub
                time = "" ;

                if (hourOfDay < 10){
                    time += "0" ;
                    time += hourOfDay ;
                }else {
                    time += hourOfDay ;
                }
                time += ":" ;
                if (minute < 10){
                    time += "0" ;
                    time += minute ;
                }else {
                    time += minute ;
                }
                time += ":" ;
                if (ss < 10){
                    time += "0" ;
                    time += ss ;
                }else {
                    time += ss ;
                }
            }
        });

        Dialog dialog = new AlertDialog.Builder(c)
                //.setIcon(android.R.drawable.ic_menu_week)
                //注入自己的布局文件
                .setView(view)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        s.GetData(data, time);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        dialog.dismiss();
                    }
                })
                .create();
        dialog.show();
    }

    // 这里写了一个回调函数的接口
    public interface ShowTimeIn {
        void GetData(String data ,String time);

    }
}
