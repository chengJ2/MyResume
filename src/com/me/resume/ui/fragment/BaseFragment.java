package com.me.resume.ui.fragment;

import java.util.List;
import java.util.Map;

import android.support.v4.app.Fragment;

import com.whjz.android.text.Info;
import com.whjz.android.util.common.CommonUtil;
import com.whjz.android.util.common.DbUtilImplement;
import com.whjz.android.util.interfa.BaseCommonUtil;
import com.whjz.android.util.interfa.DbLocalUtil;

/**
 * 
* @ClassName: BaseFragment 
* @Description: Fragment基类
* @author Comsys-WH1510032 
* @date 2016/4/22 下午5:27:02 
*
 */
public class BaseFragment extends Fragment {
	
	protected DbLocalUtil dbUtil = new DbUtilImplement();;// 本地数据库对象
	protected BaseCommonUtil baseCommon = new CommonUtil();;// 通用工具对象实例
	protected Info info = new Info();
	
	protected List<String> mList = null;
	
	protected String queryWhere;
	
	protected Map<String, String[]> commap = null;
}
