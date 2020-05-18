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


public class ServerConfigDialog extends Dialog {
    private Context mContext;
    private IPEditText ip_editText;
    private EditText port_editText;
    private TextView btn_commit;
    private TextView btn_cancel;
    private String[] ipSource;

    public ServerConfigDialog(Context context) {
        super(context, R.style.noTitleDialog);
        mContext = context;
        View view = LayoutInflater.from(getContext())
                .inflate(R.layout.server_config, null);
        ip_editText = view.findViewById(R.id.ip_edit);
        port_editText = view.findViewById(R.id.port_edit);

        btn_commit = view.findViewById(R.id.btn_commit);
        btn_cancel = view.findViewById(R.id.btn_cancel);
        super.setContentView(view);
        ip_editText.setSuperTextWatcher(new IPEditText.SuperTextWatcher() {
            @Override
            public void afterTextChanged(String[] s) {
                ipSource = new String[s.length];
                for (int i = 0; i < s.length; i++) {
                    ipSource[i] = s[i];
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

    public String[] getIpSource() {
        return ipSource;
    }
    public void setIpPortSource(String[] s,String port) {
        ip_editText.setSuperEdittextValue(s);
        port_editText.setText(port);
    }


}
