import zio._
import zio.Console._
import zio.json._
import java.nio.charset.StandardCharsets
import zio.nio.file.{Path, Files}


type SudokuGrid = List[List[Option[Int]]]

object Main extends ZIOAppDefault {

  def readFile(path: String): ZIO[Any, Throwable, String] =
    Files
      .readAllBytes(Path(path))
      .map(bytes => new String(bytes.toArray, StandardCharsets.UTF_8))

  // Parse the JSON string into a SudokuGrid
  def parseSudokuGrid(jsonString: String): ZIO[Any, Throwable, SudokuGrid] =
    jsonString.fromJson[Map[String, SudokuGrid]] match {
      case Left(error) => ZIO.fail(new RuntimeException(error))
      case Right(data) => ZIO.fromOption(data.get("grid")).orElse(ZIO.succeed(List.empty[List[Option[Int]]]))
    }

  def isValid(sudokuGrid: SudokuGrid, row: Int, col: Int, num: Int): Boolean = {
        def isInRow(row: Int, col: Int): Boolean = {
            sudokuGrid(row)(col).contains(num)
        }

        def isInCol(row: Int, col: Int): Boolean = {
            sudokuGrid(row)(col).contains(num)
        }

        def isInBloc(): Boolean = {
            sudokuGrid(row)
        }

  }


  def run: ZIO[Any, Throwable, Unit] =
    for {
      _ <- Console.print("Enter the path to the JSON file containing the Sudoku problem:")
      path <- Console.readLine
      _ <-  Console.printLine(s"You entered: $path")
      jsonString <- readFile(path)
      _ <-  Console.printLine(jsonString)
      sudokuGrid  <- parseSudokuGrid(jsonString)
      _ <-  Console.printLine(sudokuGrid)
      // Add your Sudoku solver logic here, utilizing ZIO and interacting with the ZIO Console
    } yield ()
}