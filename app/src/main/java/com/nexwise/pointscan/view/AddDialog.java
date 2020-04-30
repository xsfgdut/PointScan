package com.nexwise.pointscan.view;

/**
 * 一般的输入框
 * Created by shifan_xiao on 2016/11/2.
 */

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.google.gson.Gson;
import com.nexwise.pointscan.R;
import com.nexwise.pointscan.activity.DialogActivity;
import com.nexwise.pointscan.bean.JsonBean;
import com.nexwise.pointscan.utils.GetJsonDataUtil;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;


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

    private List<JsonBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    private Thread thread;
    private Thread threadCode;
    private static final int MSG_LOAD_DATA = 0x0001;
    private static final int MSG_LOAD_SUCCESS = 0x0002;
    private static final int MSG_LOAD_FAILED = 0x0003;
    private static final int MSG_LOAD_CODE_DATA = 0x0004;
    private static boolean isLoaded = false;
    private String province;
    private String city;
    private String district;
    private Context mContext;

    public AddDialog(Context context) {
        super(context, R.style.noTitleDialog);
        mContext = context;
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
     //   initData();
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
              showPickerView();
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

    public void setlnglatValue(double lng, double lat) {
        lng_value.setText(String.valueOf(lng));
        lat_value.setText(String.valueOf(lat));
    }


    private void initData() {
        mHandler.sendEmptyMessage(MSG_LOAD_DATA);
        mHandler.sendEmptyMessage(MSG_LOAD_CODE_DATA);
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOAD_DATA:
                    if (thread == null) {//如果已创建就不再重新创建子线程了

                        Toast.makeText(mContext, "Begin Parse Data", Toast.LENGTH_SHORT).show();
                        thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // 子线程中解析省市区数据
                                initJsonData();
                            }
                        });
                        thread.start();
                    }
                    break;

                case MSG_LOAD_SUCCESS:
                    Toast.makeText(mContext, "Parse Succeed", Toast.LENGTH_SHORT).show();
                    isLoaded = true;
                    if (isLoaded) {
                        showPickerView();
                    } else {
                        Toast.makeText(mContext, "Please waiting until the data is parsed", Toast.LENGTH_SHORT).show();
                    }
                    break;

                case MSG_LOAD_FAILED:
                    Toast.makeText(mContext, "Parse Failed", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private void initJsonData() {//解析数据

        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         *
         * */
        String JsonData = new GetJsonDataUtil().getJson(mContext, "country2020.json");//获取assets目录下的json文件数据

        ArrayList<JsonBean> jsonBean = parseData(JsonData);//用Gson 转成实体

        /**
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items = jsonBean;

        for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
            ArrayList<String> cityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int c = 0; c < jsonBean.get(i).getCityList().size(); c++) {//遍历该省份的所有城市
                String cityName = jsonBean.get(i).getCityList().get(c).getName();
                cityList.add(cityName);//添加城市
                ArrayList<String> city_AreaList = new ArrayList<>();//该城市的所有地区列表

                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                /*if (jsonBean.get(i).getCityList().get(c).getArea() == null
                        || jsonBean.get(i).getCityList().get(c).getArea().size() == 0) {
                    city_AreaList.add("");
                } else {
                    city_AreaList.addAll(jsonBean.get(i).getCityList().get(c).getArea());
                }*/
                city_AreaList.addAll(jsonBean.get(i).getCityList().get(c).getArea());
                province_AreaList.add(city_AreaList);//添加该省所有地区数据
            }

            /**
             * 添加城市数据
             */
            options2Items.add(cityList);

            /**
             * 添加地区数据
             */
            options3Items.add(province_AreaList);
        }

        mHandler.sendEmptyMessage(MSG_LOAD_SUCCESS);

    }


    public ArrayList<JsonBean> parseData(String result) {//Gson 解析
        ArrayList<JsonBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                JsonBean entity = gson.fromJson(data.optJSONObject(i).toString(), JsonBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
            mHandler.sendEmptyMessage(MSG_LOAD_FAILED);
        }
        return detail;
    }

    private void showPickerView() {// 弹出选择器

        OptionsPickerView pvOptions = new OptionsPickerBuilder(getContext(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String opt1tx = options1Items.size() > 0 ?
                        options1Items.get(options1).getPickerViewText() : "";

                String opt2tx = options2Items.size() > 0
                        && options2Items.get(options1).size() > 0 ?
                        options2Items.get(options1).get(options2) : "";

                String opt3tx = options2Items.size() > 0
                        && options3Items.get(options1).size() > 0
                        && options3Items.get(options1).get(options2).size() > 0 ?
                        options3Items.get(options1).get(options2).get(options3) : "";

                String tx = opt1tx + opt2tx + opt3tx;
                province = opt1tx;
                city = opt2tx;
                district = opt3tx;
                Toast.makeText(mContext, tx, Toast.LENGTH_SHORT).show();
            }
        })

                .setTitleText("城市选择")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(14)
                .build();

        /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pvOptions.show();
        pvOptions.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }


}
