package com.nexwise.pointscan.view;

/**
 * 一般的输入框
 * Created by shifan_xiao on 2016/11/2.
 */

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.nexwise.pointscan.R;



public class AddUserDialog extends Dialog {
    private EditText userName;
    private EditText psw;
    private EditText name;
    private EditText phone;
    private EditText address;
    private EditText remark;

    private TextView btn_commit;
    private TextView btn_cancel;



    public AddUserDialog(Context context) {
        super(context, R.style.noTitleDialog);
        View view = LayoutInflater.from(getContext())
                .inflate(R.layout.add_user, null);
        userName = view.findViewById(R.id.user_name);
        psw = view.findViewById(R.id.psw);
        name = view.findViewById(R.id.name_value);
        phone = view.findViewById(R.id.number_value);
        address = view.findViewById(R.id.address_value);
        remark = view.findViewById(R.id.remark_value);


        btn_commit = view.findViewById(R.id.btn_commit);
        btn_cancel = view.findViewById(R.id.btn_cancel);
        super.setContentView(view);

    }


    public void setOnClickCommitListener(View.OnClickListener listener) {
        btn_commit.setOnClickListener(listener);
    }

    public void setOnClickCancelListener(View.OnClickListener listener) {
        btn_cancel.setOnClickListener(listener);
    }


}
