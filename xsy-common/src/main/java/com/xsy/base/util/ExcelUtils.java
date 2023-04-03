package com.xsy.base.util;

import com.alibaba.excel.EasyExcel;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author Edge
 * @date 2021/9/16
 */
@Slf4j
public class ExcelUtils {
    /**
     * 常规导出
     *
     * @param response
     * @param clazz
     * @param list
     * @param excelName
     * @param <T>
     */
    public static <T> void excelDown(HttpServletResponse response, Class<T> clazz, List<T> list, String excelName) {
        try (OutputStream outputStream = response.getOutputStream()) {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(excelName + ".xlsx", StandardCharsets.UTF_8.displayName()));
            EasyExcel.write(response.getOutputStream(), clazz).sheet(excelName).doWrite(list);
            outputStream.flush();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
