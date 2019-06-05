package hr.fer.zemris.java.web;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static java.lang.Math.pow;

@WebServlet(name = "powers", urlPatterns = { "/powers" })
public class PowersServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (HSSFWorkbook spreadsheet = checkAndCreateSpreadsheet(req, resp)) {
            resp.setContentType("application/vnd.ms-excel");
            resp.setHeader("Content-Disposition", "attachment; filename=\"powers.xls\"");

            spreadsheet.write(resp.getOutputStream());
        }
        catch (IllegalArgumentException e) {
            req.setAttribute("message", e.getMessage());
            req.getRequestDispatcher("/WEB-INF/pages/error.jsp")
               .forward(req, resp);
        }
    }

    private HSSFWorkbook checkAndCreateSpreadsheet(HttpServletRequest req, HttpServletResponse resp) {
        int a = parseIntOrThrow("a", req.getParameter("a"));
        int b = parseIntOrThrow("b", req.getParameter("b"));
        int n = parseIntOrThrow("n", req.getParameter("n"));

        if (a < -100 || a > 100) {
            throw new IllegalArgumentException("'a' must be between -100 and 100 (inclusive).");
        }

        if (b < -100 || b > 100) {
            throw new IllegalArgumentException("'b' must be between -100 and 100 (inclusive).");
        }

        if (n < 1 || n > 5) {
            throw new IllegalArgumentException("'n' must be between 1 and 5 (inclusive).");
        }

        return createExcelSpreadsheet(a, b, n);
    }

    private int parseIntOrThrow(String name, String toParse) {
        if (toParse == null) {
            throw new IllegalArgumentException("'" + name + "' is not set.");
        }

        try {
            return Integer.parseInt(toParse);
        }
        catch (NumberFormatException e) {
            throw new IllegalArgumentException("'" + toParse + "' is not a valid parsable integer.");
        }
    }

    private HSSFWorkbook createExcelSpreadsheet(int start, int end, int powers) {
        HSSFWorkbook spreadsheet = new HSSFWorkbook();

        for (int currentPower = 1; currentPower <= powers; currentPower++) {
            addSheetWithPower(spreadsheet, start, end, currentPower);
        }

        return spreadsheet;
    }

    private void addSheetWithPower(HSSFWorkbook spreadsheet, int start, int end, int currentPower) {
        HSSFSheet sheet = spreadsheet.createSheet(currentPower + "");
        int rowNumber = 0;

        for (int currentNumber = start; currentNumber <= end; currentNumber++) {
            HSSFRow row = sheet.createRow(rowNumber);
            rowNumber++;

            row.createCell(0).setCellValue(currentNumber);
            row.createCell(1).setCellValue(pow(currentNumber, currentPower));
        }
    }

}
