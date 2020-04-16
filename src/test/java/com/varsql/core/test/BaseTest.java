package com.varsql.core.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class BaseTest {
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
