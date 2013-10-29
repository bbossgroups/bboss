package test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.frameworkset.common.util.FileUtil;

public class TestSecondIO {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		FileUtil filrUtil = new FileUtil();
		String path = "E:/地税提交区/statspack收集sql/20081015/resultStat20081015Test.txt";
		String writepath = "E:/地税提交区/statspack收集sql/20081009/resultStat20081010three.txt";
		StringBuffer result = new StringBuffer();
		try {
			FileReader fileReader = new FileReader(path);
			BufferedReader bf = new BufferedReader(fileReader);
			boolean state = false;
			boolean isMethod = false;
			String s = null;
			StringBuffer ss = new StringBuffer();
			int j = 0;
			String taxpS = null;
			while((s = bf.readLine()) != null){
				if(s.startsWith("taxp")){
					isMethod = true;
					j = 0;
				}
				if(!isMethod){
//					System.out.println(s);
					if(j == 0){
						
						if(s.equals("INSERT INTO STATS$BG_EVENT_SUMMARY ( SNAP_ID , DBID , INSTANCE_N")){
							System.out.println(taxpS);
						}
					}
					j++;
				}
				if(isMethod){
					taxpS = s;
//					System.out.println(s);
					isMethod = false;
				}
				
				
			}
//			filrUtil.writeFile(writepath, result.toString(),false);
			fileReader.close();
			bf.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}

}
