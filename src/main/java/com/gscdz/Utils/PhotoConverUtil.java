package com.gscdz.Utils;

import javax.servlet.ServletOutputStream;
import java.io.*;


public class PhotoConverUtil {
    /***
     * byte[]=>image
     *
     * 	@param imaBytes：对应的byte字符数组
     * 	@param 	os：输出流
     * */
    public static void byteToImage(byte[] imaBytes, ServletOutputStream os) {
        InputStream is=new ByteArrayInputStream(imaBytes);
        try {
            //输出
            int len=0;
            byte[] buf=new byte[1024];
            while((len=is.read(buf,0,1024))!=-1) {
                os.write(buf, 0, len);
            }
            os.flush();
            os.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * image=>byte[]
     *
     * @param filePath :文件路径
    * */
    public static byte[] imageToBytes(String filePath){
        byte[] buffer = null;
        try {
            java.io.File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(8192);
            byte[] b = new byte[8192];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

}
