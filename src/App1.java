import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import jxl.Cell;
import jxl.LabelCell;
import jxl.Range;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

/**
 *
 */

/**
 * @author peidong
 *
 */
public class App {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String currentExecutionFolder = System.getProperty("user.dir");
		String excelFilePath = currentExecutionFolder + "/libs/test.xls";
		try {
			Workbook workbook = Workbook.getWorkbook(new File(excelFilePath));

			Sheet sheet1 = Optional.ofNullable(workbook.getSheet("Sheet1"))
					.orElseThrow(() -> new IllegalArgumentException("There is no sheet1"));
			LabelCell lifeInsuranceCell = Optional.ofNullable(sheet1.findLabelCell("Life Insurance"))
					.orElseThrow(() -> new IllegalArgumentException("There is no life insurance label cell."));
			Cell lifeInsuranceNumberCell = Optional
					.ofNullable(sheet1.getCell(lifeInsuranceCell.getColumn() + 1, lifeInsuranceCell.getRow()))
					.orElseThrow(() -> new IllegalArgumentException("There is no life insurance # cell"));
////////////////////////////////////////////////////
			List<Range> mergedCellRanges = Arrays.asList(sheet1.getMergedCells());

			Range exampleCellRange = mergedCellRanges.stream().filter(range -> {

				return range.getTopLeft().getContents().equals("Example1");
			}).findFirst().orElseThrow(() -> new IllegalArgumentException("There is no merged example Cell"));

			List<Range> testStartCellRanges = mergedCellRanges.stream().filter(range -> {

				return range.getTopLeft().getContents().equals("Example1");
			}).sorted((x, y) -> {
				return Integer.compare(x.getTopLeft().getRow(), y.getTopLeft().getRow());
			}).collect(Collectors.toList());

			List<Range> testEndCellRanges = mergedCellRanges.stream().filter(range -> {

				return range.getTopLeft().getContents().equals("Example1End");
			}).sorted((x, y) -> {
				return Integer.compare(x.getTopLeft().getRow(), y.getTopLeft().getRow());
			}).collect(Collectors.toList());
//////////////////////////////
			IntStream.range(0, testStartCellRanges.size()).forEach(testIndex -> {
				int startColumn = testStartCellRanges.get(testIndex).getTopLeft().getColumn();
				int startRow = testStartCellRanges.get(testIndex).getTopLeft().getRow() + 1;
				int endColumn = testEndCellRanges.get(testIndex).getBottomRight().getColumn();
				int endRow = testEndCellRanges.get(testIndex).getBottomRight().getRow() - 1;

				Cell testName = Optional
						.ofNullable(sheet1.findCell("testname", startColumn, startRow, endColumn, endRow, false))
						.orElseThrow(() -> new IllegalArgumentException("no testname cell found"));
				Cell testNameResult = sheet1.getCell(testName.getColumn() + 1, testName.getRow());

			});
///////////////////////////////////////////////////////////////
			List<Integer> testIndexes = IntStream.range(0, testStartCellRanges.size()).filter(testIndex->{
				int startColumn = testStartCellRanges.get(testIndex).getTopLeft().getColumn();
				int startRow = testStartCellRanges.get(testIndex).getTopLeft().getRow() + 1;
				//add out of index bound check
				int endColumn = testEndCellRanges.get(testIndex).getBottomRight().getColumn();
				int endRow = testEndCellRanges.get(testIndex).getBottomRight().getRow() - 1;

				Cell testName = Optional
						.ofNullable(sheet1.findCell("testname", startColumn, startRow, endColumn, endRow, false))
						.orElseThrow(() -> new IllegalArgumentException("no testname cell found"));
				Cell testNameResult = sheet1.getCell(testName.getColumn() + 1, testName.getRow());
				return testNameResult.getContents().equals("b");
			}).mapToObj(index->index).collect(Collectors.toList());



			testIndexes.forEach(index->{
				System.out.println("test index " + index);
			});
/////////////////////////////////////////////////////////////////////////
			Cell resultCells = IntStream.range(0, testStartCellRanges.size()).filter(testIndex->{
				int startColumn = testStartCellRanges.get(testIndex).getTopLeft().getColumn();
				int startRow = testStartCellRanges.get(testIndex).getTopLeft().getRow() + 1;
				//add out of index bound check
				int endColumn = testEndCellRanges.get(testIndex).getBottomRight().getColumn();
				int endRow = testEndCellRanges.get(testIndex).getBottomRight().getRow() - 1;

				Cell testName = Optional
						.ofNullable(sheet1.findCell("testname", startColumn, startRow, endColumn, endRow, false))
						.orElseThrow(() -> new IllegalArgumentException("no testname cell found"));
				Cell testNameResult = sheet1.getCell(testName.getColumn() + 1, testName.getRow());
				return testNameResult.getContents().equals("b");
			}).mapToObj(index->{
				int startColumn = testStartCellRanges.get(index).getTopLeft().getColumn();
				int startRow = testStartCellRanges.get(index).getTopLeft().getRow() + 1;
				int endColumn = testEndCellRanges.get(index).getBottomRight().getColumn();
				int endRow = testEndCellRanges.get(index).getBottomRight().getRow() - 1;

				Cell testName = Optional
						.ofNullable(sheet1.findCell("testname", startColumn, startRow, endColumn, endRow, false))
						.orElseThrow(() -> new IllegalArgumentException("no testname cell found"));
				Cell testNameResult = sheet1.getCell(testName.getColumn() + 1, testName.getRow());
				return testNameResult;
			}).findFirst().orElseThrow(()->new IllegalArgumentException("There is no result Cell"));

			System.out.println(resultCells.getContents());


			//System.out.println(exampleCellRange.getTopLeft().getContents());

		} catch (BiffException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
