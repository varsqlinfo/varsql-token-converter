package com.varsql.core.pattern;

/**
 * -----------------------------------------------------------------------------
* @fileName : StringRegularUtils.java
* @desc		: string regular expression 
* @author	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2020. 3. 17 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
public final class StringRegularUtils {
	private static String BLANK_SPACE_REMOVE = "(?m)^\\s*\\r?\\n|\\r?\\n\\s*(?!.*\\r?\\n)";

	private static String LTRIM = "^\\s+";

	private static String RTRIM = "\\s+$";
	
	private static String ALLTRIM = "\\s";
	
	/**
	 * @method  : removeBlank
	 * @desc : text remove blank space
	 * @author   : ytkim
	 * @date   : 2020. 3. 17
	 * @param str
	 * @return
	 */
	public static String removeBlank(String str) {
		return removeBlank(str, System.lineSeparator());
	}
	
	/**
	 * @method  : removeBlank
	 * @desc : text remove blank space
	 * @author   : ytkim
	 * @date   : 2022. 1. 23. 
	 * @param str
	 * @param multiSpaceLineSeparator
	 * @return
	 */
	public static String removeBlank(String str, String multiSpaceLineSeparator) {
		return trim(str.replaceAll(BLANK_SPACE_REMOVE, multiSpaceLineSeparator));
	}
	
	/**
	 * @method  : ltrim
	 * @desc : left trim
	 * @author   : ytkim
	 * @date   : 2022. 1. 23. 
	 * @param str
	 * @return
	 */
	public static String ltrim(String str) {
		return str.replaceAll(LTRIM, "");
	}
	
	/**
	 * @method  : rtrim
	 * @desc : right trim
	 * @author   : ytkim
	 * @date   : 2022. 1. 23. 
	 * @param str
	 * @return
	 */
	public static String rtrim(String str) {
		return str.replaceAll(RTRIM, "");
	}
	
	/**
	 * @method  : trim
	 * @desc : left , right trim
	 * @author   : ytkim
	 * @date   : 2022. 1. 23. 
	 * @param str
	 * @return
	 */
	public static String trim(String str) {
		return rtrim(ltrim(str));
	}
	
	public static String allTrim(String str) {
		return str.replaceAll(ALLTRIM, "");
	}
	
	public static int regExpSpecialCharactersCheck(char checkChar, String val, int startIdx) {
		int reIdx;
		int valLen = val.length();
		for (int i = startIdx; i < valLen; i++) {
			reIdx = val.indexOf(checkChar, i);

			if (reIdx > -1) {
				int backSlashCount = -1;
				if (val.charAt(reIdx - 1) == '\\') {
					backSlashCount = 1;
					for (int j = reIdx - 2; j >= startIdx; j--) {
						if (val.charAt(j) == '\\') {
							backSlashCount += 1;
						} else
							break;
					}
				}

				if (backSlashCount > -1) {
					if (backSlashCount % 2 == 0) {
						return reIdx;
					} else {
						i = reIdx;
					}
				} else {
					return reIdx;
				}
			} else {
				return -1;
			}
		}

		return -1;
	}
}
