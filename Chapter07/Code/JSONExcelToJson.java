import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import javax.json.*;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonGeneratorFactory;

public class JSONExcelToJson {
	public static void main(String[] args) {
		Map<String, Object> properties = new HashMap<String, Object>(1);
		properties.put(JsonGenerator.PRETTY_PRINTING, true);
		JsonGeneratorFactory jsonGeneratorFactory = Json.createGeneratorFactory(properties);
		JsonGenerator jsonGenerator = jsonGeneratorFactory.createGenerator(System.out);

		InputStream inp;
		try {
			inp = new FileInputStream("/Users/ramgirm/workbook.xlsx");

			Workbook workbook = WorkbookFactory.create(inp);

			// Start with obtaining the first sheet of the workbook.
			Sheet sheet = workbook.getSheetAt(0);

			jsonGenerator.writeStartObject();

			for (Iterator<Row> rowsIT = sheet.rowIterator(); rowsIT.hasNext();) {
				Row row = rowsIT.next();
				for (Iterator<Cell> cellsIT = row.cellIterator(); cellsIT.hasNext();) {
					Cell cell = cellsIT.next();
					jsonGenerator.write("name",cell.getStringCellValue());
				}
			}
			jsonGenerator.writeEnd();
			jsonGenerator.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (EncryptedDocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
