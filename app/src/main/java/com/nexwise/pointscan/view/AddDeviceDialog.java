package com.nexwise.pointscan.view;

/**
 * 一般的输入框
 * Created by shifan_xiao on 2016/11/2.
 */

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.nexwise.pointscan.R;


public class AddDeviceDialog extends Dialog {
    private EditText device_number;
    private EditText device_name;
    private EditText device_ip;
    private EditText antena_discription;
    private LinearLayout linearLayout_1;
    private LinearLayout linearLayout_2;
    private LinearLayout linearLayout_3;
    private LinearLayout linearLayout_4;
    private RadioGroup radioGroup1;
    private RadioGroup radioGroup2;
    private RadioGroup radioGroup3;
    private TextView btn_commit;
    private TextView btn_cancel;
    private int new_type = 0;
    private int network_type = 0;
    private int antena_type = 0;

    public AddDeviceDialog(Context context) {
        super(context, R.style.noTitleDialog);

        View view = LayoutInflater.from(getContext())
                .inflate(R.layout.add_device, null);
        device_number = view.findViewById(R.id.device_number);
        device_name = view.findViewById(R.id.device_name);
        device_ip = view.findViewById(R.id.ip_value);
        antena_discription = view.findViewById(R.id.antea_description);
        radioGroup1 = view.findViewById(R.id.radiogroup1);
        radioGroup2 = view.findViewById(R.id.radiogroup2);
        radioGroup3 = view.findViewById(R.id.radiogroup3);

        linearLayout_1 = view.findViewById(R.id.linear_l);
        linearLayout_2 = view.findViewById(R.id.linear_2);
        linearLayout_3 = view.findViewById(R.id.linear_3);
        linearLayout_4 = view.findViewById(R.id.linear_4);
        btn_commit = view.findViewById(R.id.btn_commit);
        btn_cancel = view.findViewById(R.id.btn_cancel);
        super.setContentView(view);

        radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int id = radioGroup.getCheckedRadioButtonId();
                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.radio1:
                        new_type = 1;
                        break;
                    case R.id.radio2:
                        new_type = 2;
                        break;

                    default:
                        break;
                }
            }
        });
        radioGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int id = radioGroup.getCheckedRadioButtonId();
                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.radio3:
                        network_type = 1;
                        break;
                    case R.id.radio4:
                        network_type = 2;
                        break;

                    default:
                        break;
                }
            }
        });
        radioGroup3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int id = radioGroup.getCheckedRadioButtonId();
                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.radio5:
                        antena_type = 1;
                        break;
                    case R.id.radio6:
                        antena_type = 2;
                        break;

                    default:
                        break;
                }
            }
        });


    }


    public void setOnClickCommitListener(View.OnClickListener listener) {
        btn_commit.setOnClickListener(listener);
    }

    public void setOnClickCancelListener(View.OnClickListener listener) {
        btn_cancel.setOnClickListener(listener);
    }


    public int getNewType() {
      return new_type;
    }
    public int getNetWorkType() {
        return network_type;
    }
    public int getAntenaType() {
        return antena_type;
    }



}
