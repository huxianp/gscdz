package com.gscdz.dto;


import com.xuxueli.poi.excel.annotation.ExcelField;
import com.xuxueli.poi.excel.annotation.ExcelSheet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

/**
 * @author Hu
 * @date 2021-09-21 01:14
 * @description:
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@ExcelSheet(name = "sheet1", headColor = HSSFColor.HSSFColorPredefined.LIGHT_YELLOW)
public class SolvedProblemExcel {
	@ExcelField(name="题干",align= HorizontalAlignment.CENTER)
	private String subject;
	@ExcelField(name="分析",align= HorizontalAlignment.CENTER)
	private String analysis;
	@ExcelField(name="相关知识点",align= HorizontalAlignment.CENTER)
	private String relevantKnowledgePoint;
	@ExcelField(name="提示",align= HorizontalAlignment.CENTER)
	private String tip;
	@ExcelField(name="解答",align= HorizontalAlignment.CENTER)
	private String answer;
}
