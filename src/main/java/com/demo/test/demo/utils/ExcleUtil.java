package com.demo.test.demo.utils;

import com.demo.test.demo.entity.VideoEntity;
import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ExcleUtil {
    /**
     * 产生Excel试卷
     *@param list 题目集合
     */
    public static String exportExcelPaper(List<VideoEntity> list) {
        String fileUrl = "";
        try {
           // 标题
           String[] title = { "名字", "code", "演员", "简介", "首播日期", "上下线", "标签", "类型", "总集数" };
           // 创建一个工作簿
           HSSFWorkbook workbook = new HSSFWorkbook();
           // 创建一个工作表sheet
           HSSFSheet sheet = workbook.createSheet();
           // 设置列宽
           setColumnWidth(sheet, 9);
           // 创建第一行
           HSSFRow row = sheet.createRow(0);
           // 创建一个单元格
           HSSFCell cell = null;
           // 创建表头
           for (int i = 0; i < title.length; i++) {
               cell = row.createCell(i);
               // 设置样式
               HSSFCellStyle cellStyle = workbook.createCellStyle();
               cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 设置字体居中
               // 设置字体
               HSSFFont font = workbook.createFont();
               font.setFontName("宋体");
               font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 字体加粗
               // font.setFontHeight((short)12);
               font.setFontHeightInPoints((short) 13);
               cellStyle.setFont(font);
               cell.setCellStyle(cellStyle);
               cell.setCellValue(title[i]);
           }
           // 从第二行开始追加数据
           for (int i = 1; i <= list.size(); i++) {
               // 创建第i行
               HSSFRow nextRow = sheet.createRow(i);
               for (int j = 0; j < 9; j++) {
                   VideoEntity videoEntity = list.get(i-1);
                   HSSFCell cell2 = nextRow.createCell(j);
                   if (j == 0) {
                       cell2.setCellValue( videoEntity.getName());
                   }
                   if (j == 1) {
                       cell2.setCellValue(videoEntity.getCode());
                   }
                   if (j == 2) {
                       cell2.setCellValue(videoEntity.getActorDisplay());
                   }
                   if (j == 3) {
                       cell2.setCellValue(videoEntity.getDescription());
                   }
                   if (j == 4) {
                       cell2.setCellValue(videoEntity.getOrgAirDate());
                   }
                   if (j == 5) {
                       cell2.setCellValue(videoEntity.getStatus());
                   }
                   if (j == 6) {
                       cell2.setCellValue(videoEntity.getTags());
                   }
                   if (j == 7) {
                       cell2.setCellValue(videoEntity.getType());
                   }
                   if (j == 8) {
                       cell2.setCellValue(videoEntity.getVolumnCount());
                   }
               }
           }

            //时间格式化格式
            SimpleDateFormat simpleDateFormat =new SimpleDateFormat("yyyyMMddHHmmssSSS");
            //获取当前时间并作为时间戳
            String timeStamp=simpleDateFormat.format(new Date());
            //拼接新的文件名
            String newName = timeStamp+".xls";
            // 创建一个文件
           File file = new File("D:/"+ newName);
           try {
               file.createNewFile();
               // 打开文件流
               FileOutputStream outputStream = FileUtils.openOutputStream(file);
               workbook.write(outputStream);
               outputStream.close();
               fileUrl = "D:/"+ newName;
           } catch (IOException e) {
               e.printStackTrace();
               return "";
           }
       }catch (Exception e){
            e.printStackTrace();
            return "";
       }
        return fileUrl;
    }

    // 设置列宽()
    private static void setColumnWidth(HSSFSheet sheet, int colNum) {
        for (int i = 0; i < colNum; i++) {
            int v = 0;
            v = Math.round(Float.parseFloat("15.0") * 37F);
            v = Math.round(Float.parseFloat("20.0") * 267.5F);
            sheet.setColumnWidth(i, v);
        }
    }
}
