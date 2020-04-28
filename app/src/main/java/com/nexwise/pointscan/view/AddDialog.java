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


public class AddDialog extends Dialog {
    private EditText point_number;
    private EditText point_name;
    private EditText lng_value;
    private EditText lat_value;
    private EditText focusEditText;
    private LinearLayout linearLayout_1;
    private LinearLayout linearLayout_2;
    private LinearLayout linearLayout_3;
    private LinearLayout linearLayout_4;
    private TextView addressTextView;
    private RadioGroup radioGroup;
    private TextView btn_commit;
    private TextView btn_cancel;
    private int editStart;
    private int editEnd;
    private int maxLen; // the max byte
    private int state = 0;

    public AddDialog(Context context) {
        super(context, R.style.noTitleDialog);

        View view = LayoutInflater.from(getContext())
                .inflate(R.layout.add_dialog, null);
        point_number = view.findViewById(R.id.point_number);
        point_name = view.findViewById(R.id.point_name);
        lng_value = view.findViewById(R.id.lng_value);
        lat_value = view.findViewById(R.id.lat_value);
        addressTextView = view.findViewById(R.id.addr_select);
        radioGroup = view.findViewById(R.id.radiogroup);

        linearLayout_1 = view.findViewById(R.id.linear_l);
        linearLayout_2 = view.findViewById(R.id.linear_2);
        linearLayout_3 = view.findViewById(R.id.linear_3);
        linearLayout_4 = view.findViewById(R.id.linear_4);
        btn_commit = view.findViewById(R.id.btn_commit);
        btn_cancel = view.findViewById(R.id.btn_cancel);
        super.setContentView(view);
        point_number.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                focusEditText = point_number;
                //  多余的从新输入的位置删除，而不是最后
                editStart = point_number.getSelectionStart();
                editEnd = point_number.getSelectionEnd();
                if (!TextUtils.isEmpty(point_number.getText())) {
                    int varlength = 0;
                    int size = 0;
                    String etstring = point_number.getText().toString().trim();
                    char[] ch = etstring.toCharArray();
                    for (int i = 0; i < ch.length; i++) {
                        size++;
                        if (ch[i] >= 0x4e00 && ch[i] <= 0x9fbb) {
                            varlength = varlength + 2;
                        } else
                            varlength++;
                        if (varlength > maxLen) {
                            break;
                        }
                    }
                    if (varlength > maxLen) {
//                        Toast.makeText(getContext(),R.string.max_character,
//                                Toast.LENGTH_SHORT).show();
//                        s.delete(size - 1, etstring.length());
                        //  多余的从新输入的位置删除，而不是最后
                        // s.delete(editStart - 1, editEnd); // crash stackoverflow，解决方法参考上边方案一
                    }
                }
            }

        });

        point_name.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                focusEditText = point_name;
                //  多余的从新输入的位置删除，而不是最后
                editStart = point_name.getSelectionStart();
                editEnd = point_name.getSelectionEnd();
                if (!TextUtils.isEmpty(point_name.getText())) {
                    int varlength = 0;
                    int size = 0;
                    String etstring = point_name.getText().toString().trim();
                    char[] ch = etstring.toCharArray();
                    for (int i = 0; i < ch.length; i++) {
                        size++;
                        if (ch[i] >= 0x4e00 && ch[i] <= 0x9fbb) {
                            varlength = varlength + 2;
                        } else
                            varlength++;
                        if (varlength > maxLen) {
                            break;
                        }
                    }
                    if (varlength > maxLen) {
//                        Toast.makeText(getContext(),R.string.max_character,
//                                Toast.LENGTH_SHORT).show();
//                        s.delete(size - 1, etstring.length());
                        //  多余的从新输入的位置删除，而不是最后
                        // s.delete(editStart - 1, editEnd); // crash stackoverflow，解决方法参考上边方案一
                    }
                }
            }

        });
        lng_value.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                focusEditText = lng_value;
                //  多余的从新输入的位置删除，而不是最后
                editStart = lng_value.getSelectionStart();
                editEnd = lng_value.getSelectionEnd();
                if (!TextUtils.isEmpty(lng_value.getText())) {
                    int varlength = 0;
                    int size = 0;
                    String etstring = lng_value.getText().toString().trim();
                    char[] ch = etstring.toCharArray();
                    for (int i = 0; i < ch.length; i++) {
                        size++;
                        if (ch[i] >= 0x4e00 && ch[i] <= 0x9fbb) {
                            varlength = varlength + 2;
                        } else
                            varlength++;
                        if (varlength > maxLen) {
                            break;
                        }
                    }
                    if (varlength > maxLen) {
//                        Toast.makeText(getContext(),R.string.max_character,
//                                Toast.LENGTH_SHORT).show();
//                        s.delete(size - 1, etstring.length());
                        //  多余的从新输入的位置删除，而不是最后
                        // s.delete(editStart - 1, editEnd); // crash stackoverflow，解决方法参考上边方案一
                    }

                }
            }

        });

        lat_value.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                focusEditText = lat_value;
                //  多余的从新输入的位置删除，而不是最后
                editStart = lat_value.getSelectionStart();
                editEnd = lat_value.getSelectionEnd();
                if (!TextUtils.isEmpty(lat_value.getText())) {
                    int varlength = 0;
                    int size = 0;
                    String etstring = lat_value.getText().toString().trim();
                    char[] ch = etstring.toCharArray();
                    for (int i = 0; i < ch.length; i++) {
                        size++;
                        if (ch[i] >= 0x4e00 && ch[i] <= 0x9fbb) {
                            varlength = varlength + 2;
                        } else
                            varlength++;
                        if (varlength > maxLen) {
                            break;
                        }
                    }
                    if (varlength > maxLen) {
//                        Toast.makeText(getContext(),R.string.max_character,
//                                Toast.LENGTH_SHORT).show();
//                        s.delete(size - 1, etstring.length());
                        //  多余的从新输入的位置删除，而不是最后
                        // s.delete(editStart - 1, editEnd); // crash stackoverflow，解决方法参考上边方案一
                    }

                }
            }

        });

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

        addressTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    public EditText getEditText() {
        return focusEditText;
    }

    public void setOnClickCommitListener(View.OnClickListener listener) {
        btn_commit.setOnClickListener(listener);
    }

    public void setOnClickCancelListener(View.OnClickListener listener) {
        btn_cancel.setOnClickListener(listener);
    }

    public void setAddress(String str) {
        addressTextView.setText(str);
    }

    public int getState() {
      return state;
    }

    public void setlnglatValue(double lng,double lat) {
        lng_value.setText(String.valueOf(lng));
        lat_value.setText(String.valueOf(lat));
    }

}
