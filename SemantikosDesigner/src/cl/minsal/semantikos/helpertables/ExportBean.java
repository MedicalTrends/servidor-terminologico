package cl.minsal.semantikos.helpertables;

import static com.sun.org.apache.xalan.internal.lib.ExsltStrings.align;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.*;

import cl.minsal.semantikos.model.helpertables.HelperTable;
import cl.minsal.semantikos.model.helpertables.HelperTableColumn;
import cl.minsal.semantikos.model.helpertables.HelperTableData;
import cl.minsal.semantikos.model.helpertables.HelperTableRow;
import org.apache.poi.hssf.usermodel.HSSFAnchor;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;

/**
 * Created by des01c7 on 27-12-18.
 */
@ManagedBean(name = "exportBean")
@ViewScoped
public class ExportBean {

        private static final String NEW_LINE = System.getProperty("line.separator");

        private static final String SEPARATOR = "/";

        public int export(HelperTable helperTable, List<HelperTableRow> helperTableRows) {

            try {

                // Inicializando estilos

                FileOutputStream fileOut = new FileOutputStream(helperTable.getName()+".xls");

                HSSFWorkbook workbook = new HSSFWorkbook();
                HSSFSheet worksheet = workbook.createSheet(helperTable.getName());

                HSSFCellStyle cellStyle = workbook.createCellStyle();
                cellStyle.setFillForegroundColor(HSSFColor.YELLOW.index);
                cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                HSSFFont font = workbook.createFont();
                font.setFontHeightInPoints((short) 12);
                font.setFontName("Calibri");
                font.setItalic(false);
                font.setBold(true);
                font.setColor(HSSFColor.BLACK.index);
                cellStyle.setFont(font);
                cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
                cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
                cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

                //workdayStyle.setWrapText(true);

                // index from 0,0... cell A1 is cell(0,0)
                HSSFRow header = worksheet.createRow((short) 0);
                header.setHeight((short)500);

                // Generando encabezado

                int j = 0;
                HSSFCell cellHeader;

                cellHeader = header.createCell((short) j);
                cellHeader.setCellValue("ID");
                j++;

                cellHeader = header.createCell((short) j);
                cellHeader.setCellValue("CREACIÓN");
                j++;

                cellHeader = header.createCell((short) j);
                cellHeader.setCellValue("CREADO POR");
                j++;

                cellHeader = header.createCell((short) j);
                cellHeader.setCellValue("EDITADO POR");
                j++;

                cellHeader = header.createCell((short) j);
                cellHeader.setCellValue("ÚLTIMA EDICIÓN");
                j++;

                cellHeader = header.createCell((short) j);
                cellHeader.setCellValue("VIGENTE");
                j++;

                cellHeader = header.createCell((short) j);
                cellHeader.setCellValue("DESCRIPCIÓN");
                j++;

                for (HelperTableColumn helperTableColumn : helperTable.getColumnsButSTK()) {
                    cellHeader = header.createCell((short) j);
                    cellHeader.setCellValue(helperTableColumn.getDescription());
                    j++;
                }
                
                // Generando registros

                int i = 1;

                for (HelperTableRow helperTableRow : helperTableRows) {
                    j = 0;
                    HSSFRow row = worksheet.createRow((short) i);
                    HSSFCell cellBody;

                    cellBody = row.createCell((short) j);
                    cellBody.setCellValue(helperTableRow.getId());
                    j++;

                    cellBody = row.createCell((short) j);
                    cellBody.setCellValue(helperTableRow.getDateCreationFormat());
                    j++;

                    cellBody = row.createCell((short) j);
                    cellBody.setCellValue(helperTableRow.getCreationUsername());
                    j++;

                    cellBody = row.createCell((short) j);
                    cellBody.setCellValue(helperTableRow.getLastEditUsername());
                    j++;

                    cellBody = row.createCell((short) j);
                    cellBody.setCellValue(helperTableRow.getLastEditDate());
                    j++;

                    cellBody = row.createCell((short) j);
                    cellBody.setCellValue(helperTableRow.isValid());
                    j++;

                    cellBody = row.createCell((short) j);
                    cellBody.setCellValue(helperTableRow.getDescription());
                    j++;

                    for (HelperTableData helperTableData : helperTableRow.getCells()) {
                        cellBody = row.createCell((short) j);
                        cellBody.setCellValue(helperTableData.toString());
                        j++;
                    }

                    i++;

                }

                Sheet sheet = workbook.getSheetAt(0);
                sheet.setColumnWidth(0, 5000);
                sheet.createFreezePane(1, 3);
                // Freeze just one row
                //sheet.createFreezePane( 0, 1, 0, 1 );

                workbook.write(fileOut);
                fileOut.flush();
                fileOut.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return -1;
            } catch (IOException e) {
                e.printStackTrace();
                return -2;
            }
            return 1;
        }
    }
