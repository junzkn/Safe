package jun.safe.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class Md5Util {


    /**
     *
     * @param psd
     * @return
     */
	public static String encoder(String psd) {
	    psd=psd+"junzkn" ;
		try {
			//1,指定加密算法类型
			MessageDigest digest = MessageDigest.getInstance("MD5");
			//2,将需要加密的字符串中转换成byte类型的数组,然后进行随机哈希过程
			byte[] bs = digest.digest(psd.getBytes());
			//3,循环遍历bs,然后让其生成32位字符串,固定写法
			//4,拼接字符串过程
			StringBuffer codedPsd = new StringBuffer();
			for (byte b : bs) {
				int i = b & 0xff;
				//int类型的i需要转换成16机制字符
				String hexString = Integer.toHexString(i);
				if(hexString.length()<2){
					hexString = "0"+hexString;
				}
                codedPsd.append(hexString);
			}
			return codedPsd.toString() ;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "" ;
	}
}
