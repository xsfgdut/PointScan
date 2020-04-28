package com.nexwise.pointscan.view;

/**
 * 一般的输入框
 * Created by shifan_xiao on 2016/11/2.
 */

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.nexwise.pointscan.R;
import com.nexwise.pointscan.bean.Point;
import com.nexwise.pointscan.utils.DateUtils;

import java.text.ParseException;


public class PointDetailPop extends Dialog {
    private EditText point_number;
    private EditText point_name;
    private EditText lng_value;
    private EditText lat_value;
    private TextView timeTextView;
    private EditText personTextView;
    private TextView addressTextView;
    private EditText locationTextView;
    private EditText envTextView;
    private RadioGroup radioGroup;
    private RadioGroup geoRadioGroup;
    private RadioButton radioButton1;
    private RadioButton radioButton2;
    private RadioButton radioButton3;
    private RadioButton radioButton4;
    private RadioButton radioButton5;
    private RadioButton radioButton6;
    private RadioButton radioButton7;
    private TextView btn_commit;
    private TextView btn_cancel;
    private Button addDevice;
    private Button picSelect;
    private Button fileSelect;
    private int state = 0;
    private int geoType = 0;
    private long timeValue = 0;
    private Point point;
    private ShowTimeDialog showTimeDialog;

    public PointDetailPop(final Context context, Point point) {
        super(context);

        View view = LayoutInflater.from(getContext())
                .inflate(R.layout.point_detail_pop, null);
        point_number = view.findViewById(R.id.point_number);
        point_name = view.findViewById(R.id.point_name);
        lng_value = view.findViewById(R.id.lng_value);
        lat_value = view.findViewById(R.id.lat_value);
        addressTextView = view.findViewById(R.id.addr_select);
        personTextView = view.findViewById(R.id.person_name);
        timeTextView = view.findViewById(R.id.time_value);
        locationTextView = view.findViewById(R.id.location_discription);
        envTextView = view.findViewById(R.id.env_discription);

        radioGroup = view.findViewById(R.id.radiogroup);
        geoRadioGroup = view.findViewById(R.id.radiogroup1);
        radioButton1 = view.findViewById(R.id.radio1);
        radioButton2 = view.findViewById(R.id.radio2);
        radioButton3 = view.findViewById(R.id.radio3);
        radioButton4 = view.findViewById(R.id.radio4);
        radioButton5 = view.findViewById(R.id.radio_gateway);
        radioButton6 = view.findViewById(R.id.radio_highway);
        radioButton7 = view.findViewById(R.id.radio_village);

        btn_commit = view.findViewById(R.id.btn_commit);
        btn_cancel = view.findViewById(R.id.btn_cancel);
        this.point = point;
        setvalue(point);
        super.setContentView(view);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int id = radioGroup.getCheckedRadioButtonId();
                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.radio1:
                        state = -1;
                        break;
                    case R.id.radio2:
                        state = 1;
                        break;
                    case R.id.radio3:
                        state = 2;
                        break;
                    case R.id.radio4:
                        state = 3;
                        break;
                    default:
                        break;
                }
            }
        });

        geoRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int id = radioGroup.getCheckedRadioButtonId();
                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.radio_gateway:
                        geoType = 1;
                        break;
                    case R.id.radio_highway:
                        geoType = 2;
                        break;
                    case R.id.radio_village:
                        geoType = 3;
                        break;
                    default:
                        break;
                }
            }
        });

        timeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//点击组件的点击事件
                showTimeDialog(context);

            }
        });

    }

    private void showTimeDialog(Context context) {
        Log.d("xsf","showTimeDialog");
        showTimeDialog = new ShowTimeDialog(context, new ShowTimeDialog.ShowTimeIn() {
            @Override
            public void GetData(String data, String time)  {
                Log.d("xsf","data =" + data);
                Log.d("xsf","time =" + time);
                timeTextView.setText(data + "  "+ time);
                String str = data + " " + time;
                try {
                    getLongTimeVlaue(str);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });
        showTimeDialog.show();
    }
    private void getLongTimeVlaue(String str) throws ParseException{
        timeValue = DateUtils.stringToLong(str,DateUtils.DATE_TO_STRING_DETAIAL_PATTERN);
        Log.d("xsf","timeValue =" + timeValue);
    }

    public void setvalue(Point point) {
        point_number.setText(String.valueOf(point.getId()));
        point_name.setText(point.getName());
        lng_value.setText(String.valueOf(point.getLng()));
        lat_value.setText(String.valueOf(point.getLat()));
        addressTextView.setText(point.getAddress());
        personTextView.setText(point.getOperator());
        timeTextView.setText(DateUtils.ms2Date(point.getTime()));
        locationTextView.setText(point.getLocation());
        envTextView.setText(point.getEnv());
        switch (point.getState()) {
            case -1:
                state = -1;
                radioButton1.setChecked(true);
                break;
            case 1:
                radioButton2.setChecked(true);
                state = 1;
                break;
            case 2:
                radioButton3.setChecked(true);
                state = 2;
                break;
            case 3:
                radioButton4.setChecked(true);
                state = 3;
                break;
            default:
                break;
        }
        switch (point.getGeoType()) {
            case 1:
                radioButton5.setChecked(true);
                geoType = 1;
                break;
            case 2:
                radioButton6.setChecked(true);
                geoType = 2;
                break;
            case 3:
                radioButton7.setChecked(true);
                geoType = 3;
                break;
            default:
                break;
        }

    }

    public void setOnClickCommitListener(View.OnClickListener listener) {
        btn_commit.setOnClickListener(listener);
    }

    public void setOnClickCancelListener(View.OnClickListener listener) {
        btn_cancel.setOnClickListener(listener);
    }

    public int getState() {
        return state;
    }
    public int getGeoType() {
        return geoType;
    }
    public long getLongTime() {
        return timeValue;
    }

}
