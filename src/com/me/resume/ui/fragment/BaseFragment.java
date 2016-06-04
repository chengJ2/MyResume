package com.me.resume.ui.fragment;

import java.util.List;
import java.util.Map;

import android.support.v4.app.Fragment;
import android.widget.EditText;
import android.widget.TextView;

import com.me.resume.utils.RegexUtil;
import com.whjz.android.text.Info;
import com.whjz.android.util.common.CommonUtil;
import com.whjz.android.util.common.DbUtilImplement;
import com.whjz.android.util.interfa.BaseCommonUtil;
import com.whjz.android.util.interfa.DbLocalUtil;

/**
 * 
* @ClassName: BaseFragment 
* @Description: Fragment基类
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
	
	/**
	 * 获取输入框值
	 * @param editText
	 * @return
	 */
	protected  String getEditTextValue(EditText editText) {
		String value = editText.getText().toString().trim();
		if (RegexUtil.checkNotNull(value)) {
			return value;
		}
		return "";
	}
	
	/**
	 * 获取文本值
	 * @param editText
	 * @return
	 */
	protected String getTextValue(TextView textView) {
		String value = textView.getText().toString().trim();
		if (RegexUtil.checkNotNull(value)) {
			return value;
		}
		return "";
	}
	
}
