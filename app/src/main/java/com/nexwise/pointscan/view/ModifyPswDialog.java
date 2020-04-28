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
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.nexwise.pointscan.R;


public class ModifyPswDialog extends Dialog {
    private EditText old_psw;
    private EditText new_psw;
    private EditText veryfi_code;
    private TextView btn_commit;
    private TextView btn_cancel;

    public ModifyPswDialog(Context context) {
        super(context, R.style.noTitleDialog);

        View view = LayoutInflater.from(getContext())
                .inflate(R.layout.modify_psw, null);
        old_psw = view.findViewById(R.id.old_psw);
        new_psw = view.findViewById(R.id.new_psw);
        veryfi_code = view.findViewById(R.id.verfy_code);

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
