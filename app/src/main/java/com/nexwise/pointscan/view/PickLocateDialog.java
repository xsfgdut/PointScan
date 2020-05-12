package com.nexwise.pointscan.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.NumberPicker;

import com.nexwise.pointscan.R;
import com.nexwise.pointscan.bean.CityModel;
import com.nexwise.pointscan.bean.DistrictModel;
import com.nexwise.pointscan.bean.ProvinceModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PickLocateDialog {
    private static String TAG = "PickLocateDialog";
    /**
     * 所有省集合
     */
    protected String[] mProvinceDatas;
    /**
     * 所有市的集合
     * key - 省 value - 市
     */
    protected Map<String, Object> mCitisDatasMap = new HashMap<>();
    protected Map<String, Object> mDistrictMap = new HashMap<>();
    /**
     * 当前省的名称
     */
    protected String mCurrentProviceName;
    /**
     * 当前市的名称
     */
    protected String mCurrentCityName;
    Activity mContext;
    NumberPicker mProvicePicker;
    NumberPicker mCityPicker;
    NumberPicker mDistrictPicker;
//    protected List<Map<String, Object>> cityList = new ArrayList<>();
    List<ProvinceModel> mProvinceList;
    AddressPickedListener mListener;
    private AlertDialog ad;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            locatePickDialog();
        }
    };

    public PickLocateDialog(Activity mContext, AddressPickedListener listener) {
        this.mContext = mContext;
        this.mListener = listener;
        initProvinceDatas();
    }

    public AlertDialog locatePickDialog() {
        FrameLayout locatePickLayout = (FrameLayout) mContext.getLayoutInflater().inflate(R.layout.dialog_pick_locate, null);
        mProvicePicker = (NumberPicker) locatePickLayout.findViewById(R.id.monthpicker);
        mCityPicker = (NumberPicker) locatePickLayout.findViewById(R.id.hourpicker);
        mDistrictPicker = (NumberPicker) locatePickLayout.findViewById(R.id.minuteicker);

        init();

        ad = new AlertDialog.Builder(mContext).setView(locatePickLayout).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ProvinceModel provinceModel = mProvinceList.get(mProvicePicker.getValue());
                CityModel cityModel = provinceModel.getChild().get(mCityPicker.getValue());
                DistrictModel districtModel = null;
                if (cityModel.getChild() != null) {
                    districtModel = cityModel.getChild().get(mDistrictPicker.getValue());
                }

                mListener.onAddressPicked(provinceModel, cityModel, districtModel);
//                String province = mProvinceDatas[mProvicePicker.getValue()];
//                String city = ((String[]) mCitisDatasMap.get(mProvinceDatas[mProvicePicker.getValue()]))[mCityPicker.getValue()];
//                locate.setText(province + "-"
//                        + city
//                        + "-" + ((String[]) mDistrictMap.get(city))[mDistrictPicker.getValue()]);
                /*locate.setText(provinceModel.getName() + "-"
                        + cityModel.getName()
                        + (districtModel == null ? "" : ("-" + districtModel.getName())));*/
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).show();

        return ad;
    }

    void init() {
        mProvicePicker.setWrapSelectorWheel(false);
        mProvicePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
                String proName = mProvinceDatas[newVal];
                String[] pros = (String[]) mCitisDatasMap.get(proName);
                mCityPicker.setDisplayedValues(null);
                mDistrictPicker.setDisplayedValues(null);
                setCityPickerTextSize((String[]) mCitisDatasMap.get(mProvinceDatas[newVal]));
                setDisTrictPickerTextSize((String[]) (mDistrictMap.get(((String[]) mCitisDatasMap.get(mProvinceDatas[newVal]))[0])));
                mCityPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                        mDistrictPicker.setDisplayedValues(null);
                        setDisTrictPickerTextSize((String[]) mDistrictMap.get(mCityPicker.getDisplayedValues()[i1]));
                    }
                });
                mCityPicker.setWrapSelectorWheel(false);
            }
        });
        mCityPicker.setWrapSelectorWheel(false);
        mCityPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        mDistrictPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        setCityPickerTextSize((String[]) mCitisDatasMap.get(mProvinceDatas[0]));
        setDisTrictPickerTextSize((String[]) mDistrictMap.get(((String[]) mCitisDatasMap.get(mProvinceDatas[0]))[0]));
    }

    private void setCityPickerTextSize(String[] cities) {
        if (null != mCityPicker) {
            mCityPicker.setMinValue(0);
            mCityPicker.setMaxValue(cities.length - 1);
            mCityPicker.setDisplayedValues(cities);

            mProvicePicker.setMinValue(0);
            mProvicePicker.setMaxValue(mProvinceDatas.length - 1);
            mProvicePicker.setDisplayedValues(mProvinceDatas);
        }
    }

    protected void setDisTrictPickerTextSize(String[] districts) {
        if (mDistrictPicker != null) {
            if (districts != null && districts.length > 0) {
                mDistrictPicker.setMinValue(0);
                mDistrictPicker.setMaxValue(districts.length > 0 ? districts.length - 1 : 0);
                mDistrictPicker.setWrapSelectorWheel(false);
                mDistrictPicker.setDisplayedValues(districts);
            } else {
                mDistrictPicker.setMinValue(0);
                mDistrictPicker.setMaxValue(0);
                mDistrictPicker.setWrapSelectorWheel(false);
                mDistrictPicker.setDisplayedValues(new String[]{"无"});
            }
        }
    }

    /**
     * 初始化省市数据
     */
    protected void initProvinceDatas() {
        new ResourceManager(new ResourceManager.Callable() {
            @Override
            public void onCall(List<ProvinceModel> list) {
                mProvinceList = list;
                // 初始化默认选中的省、市
                if (mProvinceList != null && !mProvinceList.isEmpty()) {
                    mCurrentProviceName = mProvinceList.get(0).getName();
                    List<CityModel> cityList = mProvinceList.get(0).getChild();
                    if (cityList != null && !cityList.isEmpty()) {
                        mCurrentCityName = cityList.get(0).getName();
                    }
                }
                Log.d("wnw", list.size() + "");
                mProvinceDatas = new String[mProvinceList.size()];
                for (int i = 0; i < mProvinceList.size(); i++) {
                    // 遍历所有省的数据
                    mProvinceDatas[i] = mProvinceList.get(i).getName();
                    List<CityModel> cityList = mProvinceList.get(i).getChild();
                    String[] cityNames = new String[cityList.size()];
                    for (int j = 0; j < cityList.size(); j++) {
                        // 遍历省下面的所有市的数据
                        cityNames[j] = cityList.get(j).getName();

                        List<DistrictModel> districtList = cityList.get(j).getChild();
                        if (districtList != null && districtList.size() > 0) {
                            String[] districtNames = new String[districtList.size()];

                            for (int d = 0; d < districtList.size(); d++) {
                                districtNames[d] = districtList.get(d).getName();
                            }
                            mDistrictMap.put(cityList.get(j).getName(), districtNames);
                        }

                    }
                    // 省-市的数据，保存到mCitisDatasMap
                    mCitisDatasMap.put(mProvinceList.get(i).getName(), cityNames);
                }
                handler.sendMessage(new Message());
            }
        });

    }

    public interface AddressPickedListener {
        abstract void onAddressPicked(ProvinceModel province, CityModel city, DistrictModel district);
    }
}

