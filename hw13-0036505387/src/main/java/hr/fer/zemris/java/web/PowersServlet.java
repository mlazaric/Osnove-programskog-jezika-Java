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

/**
 * Creates and outputs a spreadsheet with n (GET parameter "n") sheets each containing two columns. The first column
 * is a value between a and b (GET parameters "a" and "b") and the second column is that value raised to the same power
 * as the sheet number.
 *
 * @author Marko Lazarić
 */
@WebServlet(name = "powers", urlPatterns = { "/powers" })
public class PowersServlet extends HttpServlet {

    private static final long serialVersionUID = 6428160076347114222L;

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

    /**
     * Checks parameters and creates the spreadsheet if the parameters are valid. Otherwise it throws an {@link IllegalArgumentException}
     * with an appropriate message.
     *
     * @param req the servlet request
     * @param resp the servlet response
     * @return the newly created spreadsheet
     *
     * @throws IllegalArgumentException if the parameters are not valid, with an appropriate message
     */
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

    /**
     * Parses an int or throws {@link IllegalArgumentException} if the argument is not a parsable integer.
     *
     * @param name the name of the attribute, used for the exception message
     * @param toParse the string to parse as an integer
     * @return the parsed integer
     *
     * @throws IllegalArgumentException if the argument is not a parsable number, with an appropriate message
     */
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

    /**
     * Creates the excel spreadsheet as described.
     *
     * @param start the lower inclusive bound (first value)
     * @param end the upper inclusive bound (last value)
     * @param powers the upper inclusive bound for the powers
     * @return the newly created spreadsheet
     */
    private HSSFWorkbook createExcelSpreadsheet(int start, int end, int powers) {
        HSSFWorkbook spreadsheet = new HSSFWorkbook();

        for (int currentPower = 1; currentPower <= powers; currentPower++) {
            addSheetWithPower(spreadsheet, start, end, currentPower);
        }

        return spreadsheet;
    }

    /**
     * Creates a single sheet as described.
     *
     * @param spreadsheet the spreadsheet to use for creating the sheet
     * @param start the lower inclusive bound (first value)
     * @param end the upper inclusive bound (last value)
     * @param currentPower the current power to calculate in this sheet
     */
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
