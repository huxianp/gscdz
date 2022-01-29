package com.gscdz.dto;

import com.xuxueli.poi.excel.annotation.ExcelField;
import com.xuxueli.poi.excel.annotation.ExcelSheet;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

/**
 * @author Hu
 * @date 2021-09-20 01:14
 * @description:
 */
@Data
@NoArgsConstructor
@ExcelSheet(name = "sheet1", headColor = HSSFColor.HSSFColorPredefined.LIGHT_YELLOW)
public class UserExcelDTO {

    @ExcelField(name="userName",align= HorizontalAlignment.CENTER)
    String userName;

    @ExcelField(name="nickName",align=HorizontalAlignment.CENTER)
    String nickName;

    @ExcelField(name="password",align=HorizontalAlignment.CENTER)
    String password;
}

