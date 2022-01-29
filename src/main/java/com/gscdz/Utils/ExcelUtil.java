package com.gscdz.Utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

/**
 * @author Hu
 * @date 2021-09-20 11:48
 * @description:
 */
public class ExcelUtil {
    /**
     * 导入
     */
    public static List importFromFile(Class clazz, MultipartFile file){
        InputStream inputStream =null;
        try{
            inputStream = file.getInputStream();
            List objects = ExcelImportUtil.importExcel(clazz, inputStream);
            return objects;
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }finally {
            try {
                if (inputStream!=null) {
                    inputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }

    }
    /**
     * 导出Excel文件到磁盘
     */

}
