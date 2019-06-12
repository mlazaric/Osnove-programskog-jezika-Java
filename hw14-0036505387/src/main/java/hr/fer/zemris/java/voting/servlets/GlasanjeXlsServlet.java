package hr.fer.zemris.java.voting.servlets;

import hr.fer.zemris.java.voting.dao.DAOProvider;
import hr.fer.zemris.java.voting.model.PollOption;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Creates the votes spreadsheet. It contains a single sheet with 4 columns: poll option id, title, link and
 * the number of votes it received.
 *
 * @author Marko Lazarić
 */
@WebServlet(urlPatterns = "/servleti/glasanje-xls")
public class GlasanjeXlsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("pollID"));

        List<PollOption> options = DAOProvider.getDao().getPollOptionsByPollId(id);

        HSSFWorkbook spreadsheet = createVotesSpreadsheet(options);

        resp.setContentType("application/vnd.ms-excel");
        resp.setHeader("Content-Disposition", "attachment; filename=\"poll.xls\"");

        spreadsheet.write(resp.getOutputStream());
    }

    /**
     * Create the votes spreadsheet as described.
     *
     * @param options the votes to tabulate in the spreadsheet
     * @return the newly created spreadsheet
     */
    private HSSFWorkbook createVotesSpreadsheet(List<PollOption> options) {
        HSSFWorkbook spreadsheet = new HSSFWorkbook();
        HSSFSheet sheet = spreadsheet.createSheet("Rezultati glasanja");
        int rowNumber = 0;

        for (PollOption option : options) {
            HSSFRow row = sheet.createRow(rowNumber);
            rowNumber++;

            row.createCell(0).setCellValue(option.getId());
            row.createCell(1).setCellValue(option.getTitle());
            row.createCell(2).setCellValue(option.getLink());
            row.createCell(3).setCellValue(option.getVoteCount());
        }

        return spreadsheet;
    }
}
