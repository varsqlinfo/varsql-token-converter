package com.varsql.core.pattern.parsing;

import java.util.Arrays;

import com.varsql.core.pattern.convert.ConverterException;

/**
 * -----------------------------------------------------------------------------
* @fileName		: GenericTokenParser.java
* @desc		: Generic Token Parser  
* @author	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2020. 4. 3. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
public class GenericTokenParser implements TokenParser {

	@Override
	public String perform(TokenInfo tokenInfo, String val) {
		if (tokenInfo.getConvertFunction() == null) {
			return tokenInfo.isValueReturn() ? val : "";
		} else {
			return tokenInfo.getConvertFunction().apply(val);
		}
	}

	/**
	 * @method  : isStartDelimiter
	 * @desc : start delimiter check 
	 * @author   : ytkim
	 * @date   : 2022. 1. 23. 
	 * @param tokenInfo
	 * @param cont
	 * @param startIndex
	 * @param chars
	 * @return
	 */
	@Override
	public boolean isStartDelimiter(TokenInfo tokenInfo, String cont, int startIndex, char... chars) {
		boolean startDelimiterCheck = false; 
		int charsLength = chars.length;
		int startDelimiterLen = tokenInfo.getStartDelimiterLen();
		if (startDelimiterLen == charsLength) {
			startDelimiterCheck = Arrays.equals(chars, tokenInfo.getStartDelimiterChars());
		} else if (startDelimiterLen < charsLength) {
			startDelimiterCheck = Arrays.equals(Arrays.copyOf(chars, startDelimiterLen), tokenInfo.getStartDelimiterChars());
		}
		
		if (startDelimiterCheck && tokenInfo.getStartDelimiterFunction() != null) {
			return tokenInfo.getStartDelimiterFunction().test(cont, startIndex, chars);
		}
		return startDelimiterCheck;
	}

	/**
	 * @method  : findEndDelimiterIndex
	 * @desc : find end delimiter
	 * @author   : ytkim
	 * @date   : 2022. 1. 23. 
	 * @param tokenInfo
	 * @param cont
	 * @param startIndex
	 * @return
	 */
	@Override
	public TokenIndexInfo findEndDelimiterIndex(TokenInfo tokenInfo, String cont, int startIndex) {
		if (tokenInfo.getEndDelimiterFunction() != null) {
			int idx = tokenInfo.getEndDelimiterFunction().findDelimiterIndex(cont, startIndex);

			if (idx > -1) {
				if (startIndex > idx) {
					throw new ConverterException(String.format("delimiter not valid start delimeter : %s ,lastidx %s",
							tokenInfo.getStartDelimiter(), idx));
				}
				return new TokenIndexInfo(idx, 0);
			}
		} else {
			if (tokenInfo.isMultipleEndDelimiter()) {
				for (String str : tokenInfo.getEndDelimiter()) {
					int idx = cont.indexOf(str, startIndex);

					if (idx > -1) {
						if (startIndex > idx) {
							throw new ConverterException(
									String.format("delimiter not valid start delimeter : %s , end delimiter %s", tokenInfo.getStartDelimiter(), str));
						}
						return new TokenIndexInfo(idx, str.length());
					}
				}
			} else {
				int idx = cont.indexOf(tokenInfo.getEndDelimiter()[0], startIndex);

				if (idx > -1) {
					if (startIndex > idx) {
						throw new ConverterException(
								String.format("delimiter not valid start delimeter : %s , end delimiter %s", tokenInfo.getStartDelimiter(), tokenInfo.getEndDelimiter()[0]));
					}
					
					return new TokenIndexInfo(idx, tokenInfo.getEndDelimiter()[0].length());
				}
			}
		}

		return null;
	}

}
