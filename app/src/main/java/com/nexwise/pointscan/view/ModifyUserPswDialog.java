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


public class ModifyUserPswDialog extends Dialog {
    private EditText user_name;
    private EditText new_psw;
    private TextView btn_commit;
    private TextView btn_cancel;

    public ModifyUserPswDialog(Context context) {
        super(context, R.style.noTitleDialog);

        View view = LayoutInflater.from(getContext())
                .inflate(R.layout.modify_user_psw, null);
        user_name = view.findViewById(R.id.username);
        new_psw = view.findViewById(R.id.new_psw);

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

    public void setValue(String name) {
        user_name.setText(name);
    }


}
