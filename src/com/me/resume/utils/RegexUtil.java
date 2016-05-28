package com.me.resume.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtil {
	// 非空
	public static boolean checkNotNull(String s) {
		if (s != null && !s.trim().equals(""))
			return true;
		else
			return false;
	}

	// 非空字符长度限制
	public static boolean checkStringLength(String s, int length) {
		Pattern pattern = Pattern.compile("\\S{1," + length + "}");
		Matcher matcher = pattern.matcher(s);
		return matcher.matches();

	}

	// 整数
	public static boolean checkIsDigital(String s) {
		Pattern pattern = Pattern.compile("\\d{1,}");
		Matcher matcher = pattern.matcher(s);
		return matcher.matches();

	}

	// 是否是100整除数
	public static boolean checkIsDigitalTo100(String s) {
		boolean flag = false;
		int value = Integer.valueOf(s);
		if (value % 100 == 0) {
			flag = true;
		}
		return flag;

	}

	// 邮箱
	public static boolean checkEmail(String s) {
		Pattern pattern = Pattern
				.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
		Matcher matcher = pattern.matcher(s);
		return matcher.matches();
	}

	// 浮点数
	public static boolean checkFloat(String s) {
		Pattern pattern = Pattern.compile(
				"^-?([1-9]\\d*.\\d*|0.\\d*[1-9]\\d*|0?.0+|0)$",
				Pattern.CASE_INSENSITIVE);
		// Pattern pattern =
		// Pattern.compile("^(?!0(\\d|\\.0+$|$))\\d+(\\.\\d{1,2})?$",Pattern.CASE_INSENSITIVE);
		// 非负
		// Pattern pattern =
		// Pattern.compile("^[1-9]*.d*|0.d*[1-9]d*$",Pattern.CASE_INSENSITIVE);
		// Pattern pattern =
		// Pattern.compile("^[1-9]\\d*.\\d*$",Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(s);
		return matcher.matches();
	}

	// 非零浮点数 保留两位小数
	public static boolean checkFloat2(String s) {
		// Pattern pattern =
		// Pattern.compile("^-?([1-9]\\d*.\\d*|0.\\d*[1-9]\\d*|0?.0+|0)$",Pattern.CASE_INSENSITIVE);
		Pattern pattern = Pattern.compile(
				"^(?!0(\\d|\\.0+$|$))\\d+(\\.\\d{1,2})?$",
				Pattern.CASE_INSENSITIVE);
		// 非负
		// Pattern pattern =
		// Pattern.compile("^[1-9]*.d*|0.d*[1-9]d*$",Pattern.CASE_INSENSITIVE);
		// Pattern pattern =
		// Pattern.compile("^[1-9]\\d*.\\d*$",Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(s);
		return matcher.matches();
	}

	// 电话
	public static boolean checkTelephone(String s) {
		Pattern pattern = Pattern
				.compile("(^(\\d{3,4}-)?\\d{7,8})$|(1[0-9]{10})");
		Matcher matcher = pattern.matcher(s);
		return matcher.matches();
	}

	// 验证手机号码
	public static boolean isPhone(String mobiles) {
		Pattern pattern = Pattern
				.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Matcher matcher = pattern.matcher(mobiles);
		return matcher.matches();
	}

	// 邮政编码
	public static boolean checkZip(String s) {
		Pattern pattern = Pattern.compile("[1-9]\\d{5}(?!\\d)");
		Matcher matcher = pattern.matcher(s);
		return matcher.matches();
	}

	// 身份证
	public static boolean checkIDCard(String s) {
		Pattern pattern = Pattern.compile("\\d{15}|\\d{18}|\\d{17}x",
				Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(s);
		return matcher.matches();
	}

	// 自定义
	public static boolean checkByRegexString(String s, String regexString) {
		Pattern pattern = Pattern.compile(regexString);
		Matcher matcher = pattern.matcher(s);
		return matcher.matches();
	}

	/**
	 * 18位或者15位身份证验证 18位的最后一位可以是字母x
	 * 
	 * @param text
	 * @return
	 */
	public static boolean personIdValidation(String text) {
		boolean flag = false;
		String regx = "[0-9]{17}x";
		String regx1 = "[0-9]{17}X";
		String reg1 = "[0-9]{15}";
		String regex = "[0-9]{18}";
		flag = text.matches(regx) || text.matches(regx1) || text.matches(reg1)
				|| text.matches(regex);
		return flag;
	}

	// 汉字
	public static boolean checkChs(String str) {
		boolean mark = false;

		Pattern pattern = Pattern.compile("[\u4E00-\u9FA5]");
		Matcher matc = pattern.matcher(str);
		StringBuffer stb = new StringBuffer();
		while (matc.find()) {
			mark = true;
			stb.append(matc.group());
		}
		if (mark) {
			System.out.println("匹配的字符串为：" + stb.toString());
		}

		return mark;
	}

}
