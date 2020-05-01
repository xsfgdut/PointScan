package com.nexwise.pointscan.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.google.gson.Gson;
import com.nexwise.pointscan.R;
import com.nexwise.pointscan.adapter.DeviceListAdapter;
import com.nexwise.pointscan.adapter.ImagesListAdapter;
import com.nexwise.pointscan.adapter.InfoWinAdapter;
import com.nexwise.pointscan.base.BaseAct;
import com.nexwise.pointscan.bean.Cell;
import com.nexwise.pointscan.bean.Detail;
import com.nexwise.pointscan.bean.Device;
import com.nexwise.pointscan.bean.Image;
import com.nexwise.pointscan.bean.JsonBean;
import com.nexwise.pointscan.bean.Point;
import com.nexwise.pointscan.bean.ProvinceCodeModel;
import com.nexwise.pointscan.cloudNet.NetRequest;
import com.nexwise.pointscan.constant.CloudConstant;
import com.nexwise.pointscan.utils.FileChooseUtil;
import com.nexwise.pointscan.utils.FileHelper;
import com.nexwise.pointscan.utils.GCJ2WGS;
import com.nexwise.pointscan.utils.GetJsonDataUtil;
import com.nexwise.pointscan.utils.HorizontalItemDecoration;
import com.nexwise.pointscan.view.AddDeviceDialog;
import com.nexwise.pointscan.view.AddDialog;
import com.nexwise.pointscan.view.IPEditText;
import com.nexwise.pointscan.view.MarkerPop;
import com.nexwise.pointscan.view.ModifyPswDialog;
import com.nexwise.pointscan.view.PointDetailPop;
import com.nexwise.pointscan.view.ShowTimeDialog;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.nexwise.pointscan.utils.StringUtil.md5Decode32;


public class MapActivity extends BaseAct implements LocationSource, AMapLocationListener, AMap.OnMarkerClickListener, AMap.OnCameraChangeListener, GeocodeSearch.OnGeocodeSearchListener {
    LocationManager locationManager;
    Location location;
    private TextureMapView mapView;
    private GeocodeSearch geocoderSearch;
    private AMap aMap;
    private MyLocationStyle myLocationStyle;
    private OnLocationChangedListener mListener;
    private AMapLocationClient locationClient;
    private AMapLocationClientOption clientOption;
    private LatLng latLng;
    private LatLng gpsLatLng;
    //标识，用于判断是否只显示一次定位信息和用户重新定位
    private boolean isFirstLoc = true;
    private Marker marker;
    private List<Marker> markerList = new ArrayList<>();
    private MarkerOptions markerOptions;

    private MarkerPop markerPop;
    private String ipAddress;
    private String[] ipSource;

    private double gpsLatitude;
    private double gpsLongitude;
    private List<LatLng> gpsLatLngList = new ArrayList<>();
    private HashMap<String, Double> hm;
    private Button startBtn;
    private Button showBtn;
    private Button modifyPsw;
    private Button userManage;
    private Button logout;
    private List<JsonBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    private Thread thread;
    private Thread threadCode;
    private static final int MSG_LOAD_DATA = 0x0001;
    private static final int MSG_LOAD_SUCCESS = 0x0002;
    private static final int MSG_LOAD_FAILED = 0x0003;
    private static final int MSG_LOAD_CODE_DATA = 0x0004;
    private ArrayList<ProvinceCodeModel> provinceCodeModels = new ArrayList<>();

    private static boolean isLoaded = false;
    private Point point_l;
    private ModifyPswDialog modifyPswDialog;
    private PointDetailPop pointDetailPop;
    private AddDeviceDialog addDeviceDialog;
    private LatLng clickLatLng;
    private AddDialog addDialog;
    private boolean isPointAdd = false;
    private String oldPsw;
    private String newPsw;
    private String verifyCode;
    private String id;
    private String province;
    private String city;
    private String district;
    private String provinceName;
    private String cityName;
    private String districtName;
    private String name;
    private String inputName;
    private int state;
    private String person;
    private int geoType;
    private String locationStr;
    private String envStr;
    private String detailJsonStr;
    private RecyclerView deviceList;
    private RecyclerView fileList;
    private ImagesListAdapter imagesListAdapter;
    private DeviceListAdapter deviceListAdapter;
    private double lng;
    private double lat;
    private long time;
    private List<Point> points = new ArrayList<>();
    private String filePath;
    private List<File> imagesFile = new ArrayList<>();
    private List<File> cellsFile = new ArrayList<>();
    private List<Image> images = new ArrayList<>();
    private List<Cell> cells = new ArrayList<>();
    private List<Device> devices = new ArrayList<>();

    private static final int TAKE_PHOTO = 189;
    private static final int CHOOSE_PHOTO = 385;
    private static final int CHOOSE_FILE = 285;
    private static final int CITY_SELECT = 286;
    private static final String FILE_PROVIDER_AUTHORITY = "com.nexwise.pointscan.fileprovider";
    private Uri mImageUri, mImageUriFromFile;
    private File imageFile;

    private TextView addressSelect;
    private ImageView clearImageView;
    private EditText addressInput;
    private ImageView searchImageView;

    private ReceiveBroadCast receiveBroadCast;
    private LocationListener locationListener = new LocationListener() {
        Location currentLocation;

        /**
         * 位置信息变化时触发:当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
         * @param location
         */
        @Override
        public void onLocationChanged(Location location) {
            try {
                location = getBestLocation(locationManager);// 每次都去获取GPS_PROVIDER优先的location对象
                updateView(location);
            } catch (SecurityException se) {
                se.printStackTrace();
            }
        }

        /**
         * GPS状态变化时触发:Provider被disable时触发此函数，比如GPS被关闭
         * @param provider
         * @param status
         * @param extras
         */
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {

            }
        }

        /**
         * 方法描述：GPS开启时触发
         * @param provider
         */
        @Override
        public void onProviderEnabled(String provider) {
            try {
                updateView(locationManager
                        .getLastKnownLocation(provider));
            } catch (SecurityException se) {
                se.printStackTrace();
            }

        }

        /**
         * 方法描述： GPS禁用时触发
         * @param provider
         */
        @Override
        public void onProviderDisabled(String provider) {
            try {
                updateView(null);
            } catch (SecurityException se) {
                se.printStackTrace();
            }
        }
    };

    private void initData() {
        mHandler.sendEmptyMessage(MSG_LOAD_DATA);
        mHandler.sendEmptyMessage(MSG_LOAD_CODE_DATA);
    }

    /**
     * 响应逆地理编码
     */
    public void getAddress(final LatLonPoint latLonPoint) {
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200,
                GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        geocoderSearch.getFromLocationAsyn(query);// 设置异步逆地理编码请求
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        Log.d("xsf", "mark click");
        for (int i = 0; i < points.size(); i++) {
            if ((marker.getSnippet()).equals(points.get(i).getId())) {
                point_l = points.get(i);
                markerClickPop(point_l);
            }
        }

        return true;//高德自带默认的弹窗将不会弹出
    }


    private void showAddDialog(LatLng latLng) {
        clickLatLng = latLng;
        addDialog = new AddDialog(this);
        addDialog.setCancelable(false);
        // LatLonPoint latLonPoint = new LatLonPoint(clickLatLng.latitude, clickLatLng.longitude);
        //  getAddress(latLonPoint);
        lng = latLng.longitude;
        lat = latLng.latitude;
        addDialog.setlnglatValue(lng, lat);
        addDialog.findViewById(R.id.addr_select).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MapActivity.this, DialogActivity.class);
                startActivityForResult(intent, CITY_SELECT);
                //              showPickerView();
            }
        });

        addDialog.setOnClickCommitListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(((EditText) addDialog.findViewById(R.id.point_number)).getText().toString())
                        || TextUtils.isEmpty(((EditText) addDialog.findViewById(R.id.point_name)).getText().toString())
                        || TextUtils.isEmpty(String.valueOf(addDialog.getState()))) {
                    showToat("检查非空项值");
                    return;
                }
                id = ((EditText) addDialog.findViewById(R.id.point_number)).getText().toString();
                name = ((EditText) addDialog.findViewById(R.id.point_name)).getText().toString();
                lng = new Double(((EditText) addDialog.findViewById(R.id.lng_value)).getText().toString());
                lat = new Double(((EditText) addDialog.findViewById(R.id.lat_value)).getText().toString());
                state = addDialog.getState();
                doAddPointRequest();
                showProgressDialog("", "正在请求服务器", true);
                addDialog.dismiss();
                isPointAdd = false;
            }
        });
        addDialog.setOnClickCancelListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDialog.dismiss();
                isPointAdd = false;
            }
        });
        addDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                addDialog.dismiss();
            }
        });
        addDialog.show();
    }

    private void markerClickPop(final Point point) {
        markerPop = new MarkerPop(this, point);
        markerPop.setOnClickCommitListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doQueryPointDetailInfoRequest(point);
                showProgressDialog("", "正在请求服务器", true);
                pointDetailShow(point);
                markerPop.dismiss();

            }
        });
        markerPop.setOnClickCancelListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                markerPop.dismiss();
            }
        });
        markerPop.show();
        markerPop.setvalue();
    }

    private void setImageAdapter() {
        fileList = pointDetailPop.findViewById(R.id.file_list);
        LinearLayoutManager ms = new LinearLayoutManager(this);
        ms.setOrientation(LinearLayoutManager.HORIZONTAL);
        fileList.setLayoutManager(ms);
        fileList.addItemDecoration(new HorizontalItemDecoration(5, this));//10表示10dp
//        if (imagesListAdapter == null) {
//            imagesListAdapter = new ImagesListAdapter(point_l.getImages());
//            fileList.setAdapter(imagesListAdapter);
//        } else {
//            imagesListAdapter.setImageList(point_l.getImages());
//            imagesListAdapter.notifyDataSetChanged();
//        }
        imagesListAdapter = new ImagesListAdapter(point_l.getImages());
        fileList.setAdapter(imagesListAdapter);
        imagesListAdapter.setOnItemClickListener(new ImagesListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });

    }

    private void setDeviceAdapter() {
        deviceList = pointDetailPop.findViewById(R.id.device_list);
        LinearLayoutManager ms = new LinearLayoutManager(this);
        ms.setOrientation(LinearLayoutManager.HORIZONTAL);
        deviceList.setLayoutManager(ms);
        deviceList.addItemDecoration(new HorizontalItemDecoration(20, this));//10表示10dp
//        if (deviceListAdapter == null) {
//            deviceListAdapter = new DeviceListAdapter(point_l.getDevices());
//            deviceList.setAdapter(deviceListAdapter);
//        } else {
//            deviceListAdapter.setData(point_l.getDevices());
//            deviceListAdapter.notifyDataSetChanged();
//        }
        deviceListAdapter = new DeviceListAdapter(point_l.getDevices());
        deviceList.setAdapter(deviceListAdapter);
        deviceListAdapter.setOnItemClickListener(new DeviceListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                point_l.getDevices().remove(position);
                deviceListAdapter.notifyDataSetChanged();
            }
        });

    }

    private void setHeight() {
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        final WindowManager.LayoutParams params = pointDetailPop.getWindow().getAttributes();
        params.width = width;
        params.height = height;
        pointDetailPop.getWindow().setAttributes(params);
    }

    private void pointDetailShow(final Point point) {
        if (images.size() > 0) {
            images.clear();
        }
        if (imagesFile.size() > 0) {
            imagesFile.clear();
        }
        if (cellsFile.size() > 0) {
            cellsFile.clear();
        }
        getName(point.getProvince(), point.getCity(), point.getDistrict());
        point.setAddress(provinceName + cityName + districtName);
        pointDetailPop = new PointDetailPop(MapActivity.this, point);

        setHeight();

        pointDetailPop.setOnClickCommitListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id = point.getId();
                name = ((EditText) pointDetailPop.findViewById(R.id.point_name)).getText().toString();
                lng = new Double(((EditText) pointDetailPop.findViewById(R.id.lng_value)).getText().toString());
                lat = new Double(((EditText) pointDetailPop.findViewById(R.id.lat_value)).getText().toString());
                state = pointDetailPop.getState();
                person = ((EditText) pointDetailPop.findViewById(R.id.person_name)).getText().toString();
                geoType = pointDetailPop.getGeoType();
                locationStr = ((EditText) pointDetailPop.findViewById(R.id.location_discription)).getText().toString();
                envStr = ((EditText) pointDetailPop.findViewById(R.id.env_discription)).getText().toString();
                time = pointDetailPop.getLongTime();
                if (state == 0 || TextUtils.isEmpty(person) || time == 0 || geoType == 0 || TextUtils.isEmpty(name)) {
                    showToat("请检查非空项值");
                    return;
                }
                Detail detail = new Detail();
                detail.setId(id);
                detail.setName(name);
                detail.setState(state);
                detail.setLng(lng);
                detail.setLat(lat);
                detail.setGeoType(geoType);
                detail.setOperator(person);
                detail.setTime(time);
                detail.setLocation(locationStr);
                detail.setEnv(envStr);
                detail.setProvince(point.getProvince());
                detail.setCity(point.getCity());
                detail.setDistrict(point.getDistrict());
                detail.setDevices(devices);
                for (int i = 0; i < images.size(); i++) {
                    images.get(i).setUrl(FileHelper.getFileNameWithSuffix(images.get(i).getUrl()));
                }
                detail.setImages(images);
                detail.setCells(cells);
                Gson gson = new Gson();
                detailJsonStr = gson.toJson(detail);
                Log.d("xsf", detailJsonStr + "=detailJsonStr");
                doUpdatePointRequest();
                showProgressDialog("", "正在请求服务器", true);
                pointDetailPop.dismiss();
            }
        });
        pointDetailPop.findViewById(R.id.picSelect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //    openFileSelector();
                String[] items = {"拍照", "从相册选择"};
                showItemDialog("图片选择", items, new ItemDialogLSN() {
                    @Override
                    public void onItemDialogClick(int which) {
                        switch (which) {
                            case 0:
                                takePhoto();
                                break;
                            case 1:
                                openAlbum();
                                break;
                        }
                    }
                });

            }
        });
        pointDetailPop.findViewById(R.id.fileSelect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileSelector();
            }
        });
        pointDetailPop.findViewById(R.id.add_device).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddDeviceDialog();
            }
        });
        pointDetailPop.setOnClickCancelListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pointDetailPop.dismiss();
            }
        });
        pointDetailPop.show();
    }

    private void getIpString() {
        ipSource = addDeviceDialog.getIpSource();
        if (ipSource == null) {
            showToat("IP错误");
            return;
        }
        if (ipSource.length != 4) {
            showToat("IP错误");
            return;
        }
        ipAddress = ipSource[0] + "." + ipSource[1] + "." + ipSource[2] + "." + ipSource[3];

        Log.d("xsf", ipAddress + "=ipAddress");
    }

    private void showAddDeviceDialog() {
        addDeviceDialog = new AddDeviceDialog(MapActivity.this);
        addDeviceDialog.setOnClickCommitListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editText_id = addDeviceDialog.findViewById(R.id.device_number);
                EditText editText_name = addDeviceDialog.findViewById(R.id.device_name);
                IPEditText editText_ip = addDeviceDialog.findViewById(R.id.ip_value);
                EditText editText_description = addDeviceDialog.findViewById(R.id.antea_description);

                getIpString();
                if (TextUtils.isEmpty(ipAddress) || TextUtils.isEmpty(editText_id.getText().toString())
                        || TextUtils.isEmpty(editText_name.getText().toString()) || addDeviceDialog.getAntenaType() == 0
                        || addDeviceDialog.getNewType() == 0 || addDeviceDialog.getNetWorkType() == 0) {
                    showToat("检查非空项值");
                    return;
                }
                Device device = new Device();
                device.setId(editText_id.getText().toString());
                device.setDevType(editText_name.getText().toString());
                device.setIp(ipAddress);
                device.setAntennaInfo(editText_description.getText().toString());
                device.setAntennaType(addDeviceDialog.getAntenaType());
                device.setInstallType(addDeviceDialog.getNewType());
                device.setNetType(addDeviceDialog.getNetWorkType());
                devices.add(device);
                point_l.setDevices(devices);
                setDeviceAdapter();
            }
        });
        addDeviceDialog.setOnClickCancelListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDeviceDialog.dismiss();
            }
        });

        addDeviceDialog.show();
    }

    private void openFileSelector() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        //intent.setType("image/*");//选择图片
        // intent.setType("audio/*"); //选择音频
        //intent.setType("video/*"); //选择视频 （mp4 3gp 是android支持的视频格式）
        //intent.setType("video/*;image/*");//同时选择视频和图片
        intent.setType("*/*");//无类型限制
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, CHOOSE_FILE);
    }


    @Override
    protected void findView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_map);
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        //地理搜索类
        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(this);

        initview();
        initListener();
        //for the best GPS Location
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        // 获取location对象
        location = getBestLocation(locationManager);
        if (location != null) {
            updateView(location);
        }
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1.5f,
                    locationListener);
        } catch (SecurityException se) {
            se.printStackTrace();
        }
        initData();
    }

    private void updateView(Location location) {
        if (location != null) {
            StringBuffer sb = new StringBuffer();
            sb.append("经度：" + location.getLongitude() + "\n" + "纬度："
                    + location.getLatitude() + "\n" + "精度：" + location.getAccuracy());
            Log.d("xsf", sb.toString());
            gpsLatLng = new LatLng(location.getLatitude(), location.getLongitude());
            gpsLatitude = location.getLatitude();
            gpsLongitude = location.getLongitude();
            if (GCJ2WGS.outOfChina(gpsLatitude, gpsLongitude)) {
                gpsLatitude = location.getLatitude();
                gpsLongitude = location.getLongitude();
            } else {
                hm = GCJ2WGS.transformFromWGSToGCJ(gpsLatitude, gpsLongitude);
                gpsLongitude = hm.get("lon");
                gpsLatitude = hm.get("lat");
            }
            gpsLatLngList.add(new LatLng(gpsLatitude, gpsLongitude));//转换后的经纬度地图坐标在保存
        }
    }

    /**
     * 获取location对象，优先以GPS_PROVIDER获取location对象，当以GPS_PROVIDER获取到的locaiton为null时
     * ，则以NETWORK_PROVIDER获取location对象，这样可保证在室内开启网络连接的状态下获取到的location对象不为空
     *
     * @param locationManager
     * @return
     */
    private Location getBestLocation(LocationManager locationManager) {
        Location result = null;
        if (locationManager != null) {
            try {
                result = locationManager
                        .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (result != null) {
                    return result;
                } else {
                    result = locationManager
                            .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    return result;
                }
            } catch (SecurityException se) {
                se.printStackTrace();
            }
        }
        return result;
    }

    private void addMarker(Point point) {
        if (markerOptions == null) {
            markerOptions = new MarkerOptions();
        }
        LatLng latLng = new LatLng(point.getLat(), point.getLng());
        markerOptions.position(latLng);
        markerOptions.draggable(false);//设置Marker可拖动
        switch (point.getState()) {
            case -1:
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon1));
                break;
            case 1:
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon2));
                break;
            case 2:
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon4));
                break;
            case 3:
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon3));
                break;
        }
        markerOptions.title(point.getName());
        markerOptions.snippet(String.valueOf(point.getId()));
        marker = aMap.addMarker(markerOptions);
        markerList.add(marker);
        marker.hideInfoWindow();
        //将地图移动到定位点
//        aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
//        aMap.moveCamera(CameraUpdateFactory.zoomTo(14));//设置地图的放缩 16
    }

    private void initListener() {
        aMap.setOnMapClickListener(new AMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (isPointAdd) {
                    Log.d("xsf", "click");
                    showAddDialog(latLng);
                }
            }

        });
        aMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {

            }

            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition) {

            }
        });
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToat("请点击地图上任意点进行经纬度获取增加堪点");
                isPointAdd = true;
            }
        });

        showBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doQueryPointListRequest();
                showProgressDialog("", "正在请求服务器", true);
            }
        });
        modifyPsw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showModifyPsw();
            }
        });
        addressSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearImageView.setVisibility(View.GONE);
                if (isLoaded) {
                    showPickerView();
                } else {
                    Toast.makeText(MapActivity.this, "Please waiting until the data is parsed", Toast.LENGTH_SHORT).show();
                }
            }
        });
        clearImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addressSelect.setText("请选择查询地址");
                clearImageView.setVisibility(View.GONE);
                province = "";
                city = "";
                district = "";
            }
        });
        searchImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputName = addressInput.getText().toString();
                doQueryPointListRequest();
                showProgressDialog("", "正在请求服务器", true);
            }
        });
        userManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MapActivity.this, UserManageActivity.class);
                startActivity(intent);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doLogoutRequest();
            }
        });
    }

    private void showModifyPsw() {
        modifyPswDialog = new ModifyPswDialog(this);
        modifyPswDialog.setOnClickCommitListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oldPsw = ((EditText) modifyPswDialog.findViewById(R.id.old_psw)).getText().toString();
                newPsw = ((EditText) modifyPswDialog.findViewById(R.id.new_psw)).getText().toString();
                verifyCode = ((EditText) modifyPswDialog.findViewById(R.id.verfy_code)).getText().toString();
                if (TextUtils.isEmpty(oldPsw) || TextUtils.isEmpty(newPsw)) {
                    showToat("新旧密码为空");
                    return;
                }
                doModifyPswRequest();
                showProgressDialog("", "正在请求服务器", true);
                modifyPswDialog.dismiss();
            }
        });
        modifyPswDialog.setOnClickCancelListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modifyPswDialog.dismiss();
            }
        });
        modifyPswDialog.show();
    }

    private void initview() {
        startBtn = findViewById(R.id.startBtn);
        showBtn = findViewById(R.id.showBtn);
        addressSelect = findViewById(R.id.address_select);
        addressInput = findViewById(R.id.address_input);
        clearImageView = findViewById(R.id.clear);
        searchImageView = findViewById(R.id.search);
        modifyPsw = findViewById(R.id.modifyPsw);
        userManage = findViewById(R.id.user_manage);
        logout = findViewById(R.id.logout);
        if (aMap == null) {
            aMap = mapView.getMap();
        }
        mapSetting();
    }

    private void mapSetting() {
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.getUiSettings().setMyLocationButtonEnabled(true);
        aMap.setLocationSource(this);
        aMap.setMyLocationEnabled(true);
        aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
        aMap.getUiSettings().setGestureScaleByMapCenter(true);
        aMap.setOnCameraChangeListener(this);
        mapView.setOnDragListener(null);
    }

    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (locationClient == null) {
            locationClient = new AMapLocationClient(this);
            clientOption = new AMapLocationClientOption();
            locationClient.setLocationListener(this);
            clientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//高精度定位
            clientOption.setOnceLocationLatest(true);//设置单次精确定位
            locationClient.setLocationOption(clientOption);
            locationClient.startLocation();
        }

    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (locationClient != null) {
            locationClient.stopLocation();
            locationClient.onDestroy();
        }
        locationClient = null;
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mListener != null && aMapLocation != null) {
            if (aMapLocation != null
                    && aMapLocation.getErrorCode() == 0) {
                if (isFirstLoc) {//定位flag，添加会控制定位的时序，不然会一直处于定位状态，地图拖动之后又会立马回到定位目标位置
                    mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
                    //将地图移动到定位点
                    aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude())));
                    aMap.moveCamera(CameraUpdateFactory.zoomTo(14));//设置地图的放缩 16
                    latLng = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                }
            } else {
                String errText = "定位失败," + aMapLocation.getErrorCode() + ": " + aMapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
            }
            isFirstLoc = false;
        }
    }

    /**
     * 必须重写以下方法
     */
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        receiveBroadCast = new ReceiveBroadCast();
        IntentFilter filter = new IntentFilter();
        registerReceiver(receiveBroadCast, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        unregisterReceiver(receiveBroadCast);//ondestroy中防止不能销毁掉，造成泄漏
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        if (locationClient != null) {
            locationClient.onDestroy();
        }
        locationManager.removeUpdates(locationListener);
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {

    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
        String addressName = "";
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getRegeocodeAddress() != null
                    && result.getRegeocodeAddress().getFormatAddress() != null) {
                addressName = result.getRegeocodeAddress().getFormatAddress()
                        + "附近";
                aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(clickLatLng.latitude, clickLatLng.longitude)));
                aMap.moveCamera(CameraUpdateFactory.zoomTo(14));//设置地图的放缩 16
                showToat(addressName);
                addDialog.setAddress(addressName);
            } else {
                showToat("没有结果");
            }
        } else {
            showToat(String.valueOf(rCode));
        }

    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }

    class ReceiveBroadCast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("xsf", "MapActivity");
            switch (intent.getAction()) {

                default:
                    break;
            }
        }

    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOAD_DATA:
                    if (thread == null) {//如果已创建就不再重新创建子线程了

//                        Toast.makeText(MapActivity.this, "Begin Parse Data", Toast.LENGTH_SHORT).show();
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
                case MSG_LOAD_CODE_DATA:
                    if (threadCode == null) {//如果已创建就不再重新创建子线程了

//                        Toast.makeText(MapActivity.this, "Begin Parse Data", Toast.LENGTH_SHORT).show();
                        threadCode = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // 子线程中解析省市区数据
                                initJsonCodeData();
                            }
                        });
                        threadCode.start();
                    }
                    break;

                case MSG_LOAD_SUCCESS:
//                    Toast.makeText(MapActivity.this, "Parse Succeed", Toast.LENGTH_SHORT).show();
                    isLoaded = true;
                    break;

                case MSG_LOAD_FAILED:
                    Toast.makeText(MapActivity.this, "Parse Failed", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private void initJsonCodeData() {//解析带code数据
        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         *
         * */
        String JsonData = new GetJsonDataUtil().getJson(this, "country.json");//获取assets目录下的json文件数据

        provinceCodeModels = parseCodeData(JsonData);//用Gson 转成实体
        Log.d("xsf", "init json code");
    }

    public ArrayList<ProvinceCodeModel> parseCodeData(String result) {//Gson 解析
        ArrayList<ProvinceCodeModel> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                ProvinceCodeModel provinceCodeModel = gson.fromJson(data.getString(i), ProvinceCodeModel.class);
                detail.add(provinceCodeModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
            mHandler.sendEmptyMessage(MSG_LOAD_FAILED);
        }
        return detail;
    }

    private void initJsonData() {//解析数据

        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         *
         * */
        String JsonData = new GetJsonDataUtil().getJson(this, "country2020.json");//获取assets目录下的json文件数据

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

        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
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
                getCode(opt1tx, opt2tx, opt3tx);
                addressSelect.setText(tx);
                clearImageView.setVisibility(View.VISIBLE);
                Toast.makeText(MapActivity.this, tx, Toast.LENGTH_SHORT).show();
            }
        })

                .setTitleText("城市选择")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(16)
                .build();

        /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pvOptions.show();
    }

    private void getCode(String provinceName, String cityName, String areaName) {
        for (int i = 0; i < provinceCodeModels.size(); i++) {
            if (provinceName.equals(provinceCodeModels.get(i).getName())) {
                province = provinceCodeModels.get(i).getCode();
            }
            for (int j = 0; j < provinceCodeModels.get(i).getCity().size(); j++) {
                if (cityName.equals(provinceCodeModels.get(i).getCity().get(j).getName())) {
                    city = provinceCodeModels.get(i).getCity().get(j).getCode();
                }
                for (int k = 0; k < provinceCodeModels.get(i).getCity().get(j).getArea().size(); k++) {
                    if (areaName.equals(provinceCodeModels.get(i).getCity().get(j).getArea().get(k).getName())) {
                        district = provinceCodeModels.get(i).getCity().get(j).getArea().get(k).getCode();
                    }
                }
            }

        }
        Log.d("xsf", province + "=province");
        Log.d("xsf", city + "=city");
        Log.d("xsf", district + "=district");
    }

    private void getName(String provinceCode, String cityCode, String areaCode) {
        for (int i = 0; i < provinceCodeModels.size(); i++) {
            if (provinceCode.equals(provinceCodeModels.get(i).getCode())) {
                provinceName = provinceCodeModels.get(i).getName();
            }
            for (int j = 0; j < provinceCodeModels.get(i).getCity().size(); j++) {
                if (cityCode.equals(provinceCodeModels.get(i).getCity().get(j).getCode())) {
                    cityName = provinceCodeModels.get(i).getCity().get(j).getName();
                }
                for (int k = 0; k < provinceCodeModels.get(i).getCity().get(j).getArea().size(); k++) {
                    if (areaCode.equals(provinceCodeModels.get(i).getCity().get(j).getArea().get(k).getCode())) {
                        districtName = provinceCodeModels.get(i).getCity().get(j).getArea().get(k).getName();
                    }
                }
            }

        }
        Log.d("xsf", provinceName + "=province");
        Log.d("xsf", cityName + "=city");
        Log.d("xsf", districtName + "=district");
    }


    private void doAddPointRequest() {
        Map<String, String> map = new HashMap<>();
        map.put(CloudConstant.ParameterKey.ID, String.valueOf(id));
        map.put(CloudConstant.ParameterKey.PROVINCE, province);
        map.put(CloudConstant.ParameterKey.CITY, city);
        map.put(CloudConstant.ParameterKey.DISTRICT, district);
        map.put(CloudConstant.ParameterKey.NAME, name);
        map.put(CloudConstant.ParameterKey.STATE, String.valueOf(state));
        map.put(CloudConstant.ParameterKey.LNG, String.valueOf(lng));
        map.put(CloudConstant.ParameterKey.LAT, String.valueOf(lat));
        NetRequest.postJsonRequest(this, CloudConstant.CmdValue.ADD_POINT, map, new NetRequest.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                disMissProgressDialog();
                Log.d("xsf", "Add success====" + result);
                JSONObject dataJson = new JSONObject(result);
                JSONObject response = dataJson.getJSONObject("result");
                String code = response.getString("code");
                if (code.equals("0000")) {
                    showToat("新增成功");
                    doQueryPointListRequest();
                } else if (code.equals("3001")) {
                    showToat("站点已存在");
                }
            }

            @Override
            public void requestFailure(Request request, IOException e) {
                showToat(e.getMessage());
            }
        });
    }

    private void doQueryPointListRequest() {
        Map<String, String> map = new HashMap<>();
        if (!TextUtils.isEmpty(province) && !TextUtils.isEmpty(city) && !TextUtils.isEmpty(district)) {
            map.put(CloudConstant.ParameterKey.PROVINCE, province);
            map.put(CloudConstant.ParameterKey.CITY, city);
            map.put(CloudConstant.ParameterKey.DISTRICT, district);
        }
        if (!TextUtils.isEmpty(province) && TextUtils.isEmpty(city) && TextUtils.isEmpty(district)) {
            map.put(CloudConstant.ParameterKey.PROVINCE, province);
        }
        if (!TextUtils.isEmpty(province) && !TextUtils.isEmpty(city) && TextUtils.isEmpty(district)) {
            map.put(CloudConstant.ParameterKey.PROVINCE, province);
            map.put(CloudConstant.ParameterKey.CITY, city);
        }
        if (!TextUtils.isEmpty(inputName)) {
            map.put(CloudConstant.ParameterKey.NAME, inputName);
        }

//        map.put(CloudConstant.ParameterKey.STATE, String.valueOf(state));
        map.put(CloudConstant.ParameterKey.PAGE, String.valueOf(1));
        map.put(CloudConstant.ParameterKey.COUNT, String.valueOf(2000));
        NetRequest.postJsonRequest(this, CloudConstant.CmdValue.POINT_LIST, map, new NetRequest.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                disMissProgressDialog();
                if (points.size() > 0) {
                    points.clear();
                }
                Log.d("xsf", "query success====" + result);
                JSONObject dataJson = new JSONObject(result);
                JSONObject response = dataJson.getJSONObject("result");
                String code = response.getString("code");
                if (code.equals("0000")) {
                    showToat("查询成功");
                    int total = dataJson.getInt("total");
                    Log.d("xsf", "total====" + total);
                    JSONArray jsonArray = dataJson.getJSONArray("points");
                    Gson gson = new Gson();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        Point point = gson.fromJson(jsonArray.getString(i), Point.class);
                        points.add(point);
                    }
                    for (int i = 0; i < points.size(); i++) {
                        addMarker(points.get(i));
                    }
                }
            }

            @Override
            public void requestFailure(Request request, IOException e) {
                showToat(e.getMessage());
            }
        });
    }

    private void doUpdatePointRequest() {
        Map<String, String> map = new HashMap<>();
        map.put(CloudConstant.ParameterKey.DETAIL, detailJsonStr);
        String url = CloudConstant.Source.SERVER + "/point/detail/update";
        Log.d("xsf", imagesFile.size() + "files size");
        for (int i = 0; i < imagesFile.size(); i++) {
            Log.d("xsf", imagesFile.get(i).length() + "files length");
            if (imagesFile.get(i).length() == 0) {
                showToat("文件大小为0，请检查");
                return;
            }
        }
        NetRequest.imageFileRequest(this, url, map, "images", imagesFile, "cells", cellsFile, new NetRequest.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                disMissProgressDialog();
                Log.d("xsf", "update success====" + result);
                doQueryPointListRequest();
            }

            @Override
            public void requestFailure(Request request, IOException e) {

            }
        });

    }

    private void doQueryPointDetailInfoRequest(final Point point) {
        Map<String, String> map = new HashMap<>();
        map.put(CloudConstant.ParameterKey.ID, String.valueOf(point.getId()));
        NetRequest.postJsonRequest(this, CloudConstant.CmdValue.DETAIL_INFO, map, new NetRequest.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                disMissProgressDialog();
                Log.d("xsf", "query detail info success====" + result);
                JSONObject dataJson = new JSONObject(result);
                JSONObject response = dataJson.getJSONObject("result");
                String code = response.getString("code");
                if (code.equals("0000")) {
                    showToat("查询详情成功");
                    String id = dataJson.getString("id");
                    String name = dataJson.getString("name");
                    int state = dataJson.getInt("state");
                    String province = dataJson.getString("province");
                    String city = dataJson.getString("city");
                    String district = dataJson.getString("district");
//                    String address = dataJson.getString("address");
                    double lng = dataJson.getDouble("lng");
                    double lat = dataJson.getDouble("lat");
                    int geoType = dataJson.getInt("geoType");
                    String operator = dataJson.getString("operator");
                    long time = dataJson.getLong("time");
                    String location = dataJson.getString("location");
                    String env = dataJson.getString("env");

                    JSONArray jsonArray = dataJson.getJSONArray("devices");
                    List<Device> deviceList = new ArrayList<>();
                    Gson gson = new Gson();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        Device device = gson.fromJson(jsonArray.getString(i), Device.class);
                        deviceList.add(device);
                    }

                    JSONArray jsonArray1 = dataJson.getJSONArray("images");
                    List<Image> imageList = new ArrayList<>();
                    for (int i = 0; i < jsonArray1.length(); i++) {
                        Image image = gson.fromJson(jsonArray1.getString(i), Image.class);
                        imageList.add(image);
                    }

                    JSONArray jsonArray2 = dataJson.getJSONArray("cells");
                    List<Cell> cellList = new ArrayList<>();
                    for (int i = 0; i < jsonArray2.length(); i++) {
                        Cell cell = gson.fromJson(jsonArray2.getString(i), Cell.class);
                        cellList.add(cell);
                    }
                    point_l.setId(id);
                    point_l.setName(name);
                    point_l.setState(state);
                    point_l.setProvince(province);
                    point_l.setCity(city);
                    point_l.setDistrict(district);
                    point_l.setLng(lng);
                    point_l.setLat(lat);
                    point_l.setGeoType(geoType);
                    point_l.setOperator(operator);
                    point_l.setTime(time);
                    point_l.setLocation(location);
                    point_l.setEnv(env);
                    point_l.setDevices(deviceList);
                    point_l.setImages(imageList);
                    point_l.setCells(cellList);
                    pointDetailPop.setvalue(point_l);
                    setImageAdapter();
                    setDeviceAdapter();

                }
            }

            @Override
            public void requestFailure(Request request, IOException e) {
                showToat(e.getMessage());
            }
        });
    }

    private void doModifyPswRequest() {
        Map<String, String> map = new HashMap<>();
        map.put(CloudConstant.ParameterKey.OLD_PWD, md5Decode32(oldPsw));
        map.put(CloudConstant.ParameterKey.NEW_PWD, md5Decode32(newPsw));
        map.put(CloudConstant.ParameterKey.CAPTCHA, verifyCode);
        NetRequest.postJsonRequest(this, CloudConstant.CmdValue.PWD, map, new NetRequest.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                JSONObject dataJson = new JSONObject(result);
                JSONObject response = dataJson.getJSONObject("result");
                String code = response.getString("code");
                if (code.equals("0000")) {
                    showToat("修改密码成功");
                }
                disMissProgressDialog();
                Log.d("xsf", "modify psw success====" + result);
            }

            @Override
            public void requestFailure(Request request, IOException e) {

            }
        });

    }
    private void doLogoutRequest() {
        Map<String, String> map = new HashMap<>();
        NetRequest.postJsonRequest(this, CloudConstant.CmdValue.LOGOUT, map, new NetRequest.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                JSONObject dataJson = new JSONObject(result);
                JSONObject response = dataJson.getJSONObject("result");
                String code = response.getString("code");
                if (code.equals("0000")) {
                    showToat("登出成功");
                    finish();
                }
                disMissProgressDialog();
                Log.d("xsf", "log out success====" + result);
            }

            @Override
            public void requestFailure(Request request, IOException e) {

            }
        });

    }

    /**
     * 打开相册
     */
    private void openAlbum() {
        Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
        openAlbumIntent.setType("image/*");
        startActivityForResult(openAlbumIntent, CHOOSE_PHOTO);//打开相册
    }

    /**
     * 拍照
     */
    private void takePhoto() {
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//打开相机的Intent
        if (takePhotoIntent.resolveActivity(getPackageManager()) != null) {//这句作用是如果没有相机则该应用不会闪退，要是不加这句则当系统没有相机应用的时候该应用会闪退
            imageFile = createImageFile();//创建用来保存照片的文件
            mImageUriFromFile = Uri.fromFile(imageFile);
            Log.i("xsf", "takePhoto: uriFromFile " + mImageUriFromFile);
            if (imageFile != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    /*7.0以上要通过FileProvider将File转化为Uri*/
                    mImageUri = FileProvider.getUriForFile(this, FILE_PROVIDER_AUTHORITY, imageFile);
                } else {
                    /*7.0以下则直接使用Uri的fromFile方法将File转化为Uri*/
                    mImageUri = Uri.fromFile(imageFile);
                }
                takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);//将用于输出的文件Uri传递给相机
                startActivityForResult(takePhotoIntent, TAKE_PHOTO);//打开相机
            }
        }
    }

    /**
     * 创建用来存储图片的文件，以时间来命名就不会产生命名冲突
     *
     * @return 创建的图片文件
     */
    private File createImageFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File imageFile = null;
        try {
            imageFile = File.createTempFile(imageFileName, ".jpg", storageDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageFile;
    }

    /**
     * 4.4版本以下对返回的图片Uri的处理：
     * 就是从返回的Intent中取出图片Uri，直接显示就好
     *
     * @param data 调用系统相册之后返回的Uri
     */
    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }

    /**
     * 4.4版本以上对返回的图片Uri的处理：
     * 返回的Uri是经过封装的，要进行处理才能得到真实路径
     *
     * @param data 调用系统相册之后返回的Uri
     */
    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            //如果是document类型的Uri，则提供document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];//解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            //如果是content类型的uri，则进行普通处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            //如果是file类型的uri，则直接获取路径
            imagePath = uri.getPath();
        }
        displayImage(imagePath);
    }

    /**
     * 将imagePath指定的图片显示到ImageView上
     */
    private void displayImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
//            mPicture.setImageBitmap(bitmap);
        } else {
            Toast.makeText(this, "failed to get image", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 将Uri转化为路径
     *
     * @param uri       要转化的Uri
     * @param selection 4.4之后需要解析Uri，因此需要该参数
     * @return 转化之后的路径
     */
    private String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    /**
     * 将拍的照片添加到相册
     *
     * @param uri 拍的照片的Uri
     */
    private void galleryAddPic(Uri uri) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(uri);
        sendBroadcast(mediaScanIntent);
    }

    @Override

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        /*如果拍照成功，将Uri用BitmapFactory的decodeStream方法转为Bitmap*/
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(mImageUri));
                        Log.i("xsf", "onActivityResult: imageUri " + mImageUri);
                        galleryAddPic(mImageUriFromFile);
//                        mPicture.setImageBitmap(bitmap);//显示到ImageView上
//                        File file = new File(filePath);
                        Log.d("xsf", imageFile.getName() + "=file name");
                        Log.d("xsf", imageFile.length() + "=file ");
                        Image image = new Image();
                        image.setType(1);
                        String path = FileChooseUtil.getInstance(this).getChooseFileResultPath(mImageUri);
                        Log.d("xsf", path + "=path ");
                        image.setUrl(path);
                        images.add(image);
                        File file = new File(path);
                        imagesFile.add(file);
                        point_l.getImages().add(image);
                        setImageAdapter();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CHOOSE_PHOTO:
                if (data == null) {//如果没有拍照或没有选取照片，则直接返回
                    return;
                }
                Log.i("xsf", "onActivityResult: ImageUriFromAlbum: " + data.getData());
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    String path = FileChooseUtil.getInstance(this).getChooseFileResultPath(uri);
                    File file = new File(path);
                    Log.d("xsf", file.getName() + "=file name");
                    Log.d("xsf", file.length() + "=file ");
                    Image image = new Image();
                    image.setType(1);
                    image.setUrl(path);
                    images.add(image);
                    imagesFile.add(file);
                    point_l.getImages().add(image);
                    setImageAdapter();

//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                        handleImageOnKitKat(data);//4.4之后图片解析
//                    } else {
//                        handleImageBeforeKitKat(data);//4.4之前图片解析
//                    }
                }
                break;
            case CHOOSE_FILE:
                Uri uri = data.getData();
                filePath = FileChooseUtil.getInstance(this).getChooseFileResultPath(uri);
                File file = new File(filePath);
                cellsFile.add(file);
                Cell cell = new Cell();
                cell.setType(1);
                cell.setUrl(file.getName());
                cells.add(cell);
                Log.d("xsf", "选择文件返回：" + filePath);
                break;
            case CITY_SELECT:
                if (data == null) {//如果没有拍照或没有选取照片，则直接返回
                    return;
                }
                String provinceName = data.getStringExtra("province");
                String cityName = data.getStringExtra("city");
                String districtName = data.getStringExtra("district");
                getCode(provinceName, cityName, districtName);
                ((TextView) addDialog.findViewById(R.id.addr_select)).setText(provinceName + cityName + districtName);
                break;
            default:
                break;
        }

    }

}
