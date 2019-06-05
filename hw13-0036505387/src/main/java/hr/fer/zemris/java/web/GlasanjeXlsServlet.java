package hr.fer.zemris.java.web;

import hr.fer.zemris.java.web.voting.IBandDefinitionStorage;
import hr.fer.zemris.java.web.voting.IBandVotesStorage;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "glasanje-xls", urlPatterns = { "/glasanje-xls" })
public class GlasanjeXlsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        IBandVotesStorage votes = (IBandVotesStorage) req.getAttribute("votes");
        IBandDefinitionStorage bandDefinition = (IBandDefinitionStorage) req.getAttribute("bandDefinition");

        HSSFWorkbook spreadsheet = createVotesSpreadsheet(votes, bandDefinition);

        resp.setContentType("application/vnd.ms-excel");
        resp.setHeader("Content-Disposition", "attachment; filename=\"glasanje.xls\"");

        spreadsheet.write(resp.getOutputStream());
    }

    private HSSFWorkbook createVotesSpreadsheet(IBandVotesStorage votes, IBandDefinitionStorage bandDefinition) {
        HSSFWorkbook spreadsheet = new HSSFWorkbook();
        HSSFSheet sheet = spreadsheet.createSheet("Rezultati glasanja");
        int[] rowNumber = {0}; // We cannot use a simple int because we would not be able to increment it in the lambda

        votes.sortedByVoteCount(bandDefinition)
             .forEach(bvc -> {
                HSSFRow row = sheet.createRow(rowNumber[0]);
                rowNumber[0]++;

                 row.createCell(0).setCellValue(bvc.getBand().getId());
                 row.createCell(1).setCellValue(bvc.getBand().getName());
                 row.createCell(2).setCellValue(bvc.getBand().getSong());
                 row.createCell(3).setCellValue(bvc.getVoteCount());
             });

        return spreadsheet;
    }
}
