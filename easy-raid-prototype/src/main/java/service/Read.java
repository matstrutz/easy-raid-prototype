package service;

import entity.Member;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public final class Read {

    public static List<Member> mapFile(String path) {
        List<Member> memberList = new ArrayList<>();
        try {

            File file = new File(path);

            OPCPackage pkg = OPCPackage.open(file);

            XSSFWorkbook wb = new XSSFWorkbook(pkg);
            for (Sheet sheet : wb) {
                int firstRow = sheet.getFirstRowNum();
                int lastRow = sheet.getLastRowNum();

                for (int index = firstRow; index <= lastRow; index++) {
                    Row row = sheet.getRow(index);
                    if (row != null) {
                        Member member = new Member();

                        for (int cellIndex = row.getFirstCellNum(); cellIndex < row.getLastCellNum(); cellIndex++) {
                            Cell cell = row.getCell(cellIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);

                            setMemberValue(cellIndex, getCellValue(cell), member);
                        }

                        memberList.add(member);
                        firstRow++;
                    }
                }
            }

            return memberList;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return memberList;
    }


    private static void setMemberValue(int cellIndex, String cellValue, Member member){
        if(cellIndex == 1){
            member.setName(cellValue);
        }
        if(cellIndex == 3){
            member.setClassName(cellValue);
        }
        if(cellIndex == 4){
            member.setIlvl(cellValue);
        }
        if(cellIndex == 6){
            member.setDifficulty(cellValue);
        }
        if(cellIndex == 7){
            String[] dayList = StringUtils.split(cellValue, ",");
            List<String> days = new ArrayList<>();

            for (int i = 0; i < dayList.length; i++) {
                String day = StringUtils.deleteWhitespace(dayList[i]);

                days.add(day);
            }

            member.setDay(days);
        }
        if(cellIndex == 8){
            String[] hourList = StringUtils.split(cellValue, ",");
            List<String> hours = new ArrayList<>();

            for (int i = 0; i < hourList.length; i++) {
                String day = StringUtils.deleteWhitespace(hourList[i]);

                if("TodososHorários".equals(day)){
                    hours.add("Manhã");
                    hours.add("Tarde");
                    hours.add("Noite");
                } else {
                    hours.add(day);
                }
            }

            member.setHour(hours);
        }
        if(cellIndex == 9){
            member.setKnowledge(cellValue);
        }
    }

    public static String getCellValue(Cell cell) {
        CellType cellType = cell.getCellType().equals(CellType.FORMULA)
                ? cell.getCachedFormulaResultType() : cell.getCellType();
        if (cellType.equals(CellType.STRING)) {
            return cell.getStringCellValue();
        }
        if (cellType.equals(CellType.NUMERIC)) {
            if (DateUtil.isCellDateFormatted(cell)) {
                return cell.getDateCellValue().toString();
            } else {
                return Double.toString(cell.getNumericCellValue());
            }
        }
        if (cellType.equals(CellType.BOOLEAN)) {
            return Boolean.toString(cell.getBooleanCellValue());
        }

        return cell.getStringCellValue();
    }
}
