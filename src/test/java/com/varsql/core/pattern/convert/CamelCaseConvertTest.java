package com.varsql.core.pattern.convert;

import static com.varsql.core.pattern.StringRegularUtils.regExpSpecialCharactersCheck;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Test;

import com.varsql.core.pattern.parsing.TokenInfo;
import com.varsql.core.pattern.parsing.function.EndDelimiterFunction;

public class CamelCaseConvertTest extends AbstractConverter {
	final static TokenInfo SINGLEQUOTE = new TokenInfo.Builder("'", new String[] { "'" }, (val) -> "'" + val + "'")
	.setEndDelimiterFunction((val, idx) -> {
		return regExpSpecialCharactersCheck('\'', val, idx);
	}).build();


	//new line -> mac = "\r" , window = "\r\n" , linux = "\n"
	private static String[] NEW_LINE_ARR = new String[] { "\n", "\r" };
	
	// newline check
	private static EndDelimiterFunction newLineEndDelFn = (val, startIdx) -> {
		int newLineIdx = val.indexOf('\n', startIdx);
		int newLineIdx2 = val.indexOf("\r", startIdx);

		return Math.min(newLineIdx, newLineIdx2);
	};

	// line comment
	final static TokenInfo LINE = new TokenInfo.Builder("//", NEW_LINE_ARR, (val) -> LINE_SEPARATOR)
			.setEndDelimiterFunction(newLineEndDelFn).build();

	final static TokenInfo BLOCK = new TokenInfo.Builder("/*", new String[] { "*/" }).setValueReturn(false).build();

	final static TokenInfo BLOCK_JSP = new TokenInfo.Builder("<%--", new String[] { "--%>" }).setValueReturn(false)
			.build();

	final static TokenInfo ELEMENT_IGNORE_START = new TokenInfo.Builder("<", new String[] { ">" }, (val) -> {
		return "<" + val + ">";
	}).build();
	final static TokenInfo ELEMENT_IGNORE_END = new TokenInfo.Builder("</", new String[] { ">" }, (val) -> "</" + val + ">").build();
	final static TokenInfo BLOCK_HTML = new TokenInfo.Builder("<!--", new String[] { "-->" }).setValueReturn(false).build();

	final static TokenInfo DOUBLEQUOTE = new TokenInfo.Builder("\"", new String[] { "\"" }, (val) -> "\"" + val + "\"")
	.setEndDelimiterFunction((val, idx) -> {
		return regExpSpecialCharactersCheck('"', val, idx);
	}).build();

	final static TokenInfo PARAM1 = new TokenInfo.Builder("{{", new String[] { "}}" }).build();
	final static TokenInfo PARAM2 = new TokenInfo.Builder("v-model=\"", new String[] { "\"" }).build();
	
	public ConvertResult sqlParameter(String cont) {
		return super.tokenData(cont, new TokenHandler() {
			
			@Override
			public String beforeHandleToken(String str, TokenInfo converter) {
				
				if("{{".equals(converter.getStartDelimiter())) {
					int idx =str.indexOf("."); 
					if(idx > -1) {
						str = str.substring(0,idx+1) + convert2CamelCase(str.substring(idx+1));
					}else {
						str = convert2CamelCase(str);
					}
					
					str = converter.getStartDelimiter() + str + converter.getEndDelimiter()[0];
				}else if("v-model".equals(converter.getStartDelimiter())) {
					
				}
				return str;
			}
		}, DOUBLEQUOTE,SINGLEQUOTE, BLOCK, PARAM1, PARAM2);

	}
	
	public static String convert2CamelCase(String underScore) {

        // '_' 가 나타나지 않으면 이미 camel case 로 가정함.
        // 단 첫째문자가 대문자이면 camel case 변환 (전체를 소문자로) 처리가
        // 필요하다고 가정함. --> 아래 로직을 수행하면 바뀜
        if (underScore.indexOf('_') < 0
            && Character.isLowerCase(underScore.charAt(0))) {
            return underScore;
        }
        StringBuilder result = new StringBuilder();
        boolean nextUpper = false;
        int len = underScore.length();

        for (int i = 0; i < len; i++) {
            char currentChar = underScore.charAt(i);
            if (currentChar == '_') {
                nextUpper = true;
            } else {
                if (nextUpper) {
                    result.append(Character.toUpperCase(currentChar));
                    nextUpper = false;
                } else {
                    result.append(Character.toLowerCase(currentChar));
                }
            }
        }
        return result.toString();
    }
	
	@Test
	public void testMain() {
		CamelCaseConvertTest ccc = new CamelCaseConvertTest();
		
		System.out.println(ccc.sqlParameter(getResourceContent("/txt/camelCase.txt")).getParameterInfo());
	}
	
	public String getResourceContent(String filePath) {
		
		BufferedReader br = null;
        StringBuffer sb = new StringBuffer(); // 테스트용 변수
         try {
            br = new BufferedReader(new FileReader(new File(getClass().getResource(filePath).getFile())));
            String line = null;
            
            while ((line = br.readLine()) != null) {
                sb.append(line).append(System.lineSeparator());
            }
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        } finally {
            try { 
                if (br!=null) 
                    br.close(); 
            } catch (Exception e) {}
        }

		return sb.toString();
	}
}