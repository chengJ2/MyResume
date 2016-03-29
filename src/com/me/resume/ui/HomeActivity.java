package com.me.resume.ui;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.methods.HttpOptions;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.me.resume.BaseActivity;
import com.me.resume.MyApplication;
import com.me.resume.R;
import com.me.resume.utils.ActivityUtils;
import com.me.resume.utils.CommUtil;

/**
 * 
* @ClassName: HomeActivity 
* @Description: 首页
* @author Comsys-WH1510032 
* @date 2016/3/29 下午4:56:41 
*
 */
public class HomeActivity extends BaseActivity implements OnClickListener{

	private List<CityItem> cityList;
	private  RelativeLayout itmel;
	private GridView gridView;
    
	private TextView topText;
    private ImageView rightIcon;
    
    private Button makeResume;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		gridView = (GridView) findViewById(R.id.grid);
		
		if(getPreferenceData("firstInstall",1) == 1){
			ActivityUtils.startActivity(HomeActivity.this, MyApplication.PACKAGENAME 
					+ ".MainActivity",true);
		}
		
		topText = findView(R.id.top_text);
		rightIcon = findView(R.id.right_icon);	
		
		makeResume = findView(R.id.go);
		setData();
        setGridView();
	}
	
	/**设置数据*/
    private void setData() {
    	topText.setText(CommUtil.getStrValue(HomeActivity.this, R.string.resume_center));
    	
    	rightIcon.setOnClickListener(this);
    	rightIcon.setOnClickListener(this);
    	makeResume.setOnClickListener(this);
    	
        cityList = new ArrayList<CityItem>();
        CityItem item = new CityItem();
        item.setCityName("深圳");
        item.setCityCode("0755");
        cityList.add(item);
        item = new CityItem();
        item.setCityName("上海");
        item.setCityCode("021");
        cityList.add(item);
        item = new CityItem();
        item.setCityName("广州");
        item.setCityCode("020");
        cityList.add(item);
        item = new CityItem();
        item.setCityName("北京");
        item.setCityCode("010");
        cityList.add(item);
        item = new CityItem();
        item.setCityName("武汉");
        item.setCityCode("027");
        cityList.add(item);
        item = new CityItem();
        item.setCityName("孝感");
        item.setCityCode("0712");
        cityList.add(item);
        cityList.addAll(cityList);
    }
    
    /**设置GirdView参数，绑定数据*/
    private void setGridView() {
        int size = cityList.size();
        int length = 100;
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        float density = dm.density;
        int gridviewWidth = (int) (size * (length + 4) * density);
        int itemWidth = (int) (length * density);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                gridviewWidth, LinearLayout.LayoutParams.FILL_PARENT);
        gridView.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
        gridView.setColumnWidth(itemWidth); // 设置列表项宽
        gridView.setHorizontalSpacing(5); // 设置列表项水平间距
        gridView.setStretchMode(GridView.NO_STRETCH);
        gridView.setNumColumns(size); // 设置列数量=列表集合数

        GridViewAdapter adapter = new GridViewAdapter(getApplicationContext(),
                cityList);
        gridView.setAdapter(adapter);
    }

    /**GirdView 数据适配器*/
    public class GridViewAdapter extends BaseAdapter {
        Context context;
        List<CityItem> list;
        public GridViewAdapter(Context _context, List<CityItem> _list) {
            this.list = _list;
            this.context = _context;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.home_grilview_item, null);
            TextView tvCity = (TextView) convertView.findViewById(R.id.tvCity);
            TextView tvCode = (TextView) convertView.findViewById(R.id.tvCode);
            CityItem city = list.get(position);

            tvCity.setText(city.getCityName());
            tvCode.setText(city.getCityCode());
            return convertView;
        }
    }

    public class CityItem {
        private String cityName;
        private String cityCode;

        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }

        public String getCityCode() {
            return cityCode;
        }

        public void setCityCode(String cityCode) {
            this.cityCode = cityCode;
        }
    }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.go:
			ActivityUtils.startActivity(HomeActivity.this,MyApplication.PACKAGENAME + ".ui.BaseInfoActivity");
			break;
		case R.id.right_icon:
			ActivityUtils.startActivity(HomeActivity.this,MyApplication.PACKAGENAME + ".ui.SettingActivity");
			break;
		default:
			break;
		}
		
	}
}
