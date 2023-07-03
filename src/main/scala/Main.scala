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

  def prettySudokuGrid(grid: SudokuGrid): String = {
    val horizontalLine = "+-------+-------+-------+\n"
    val formattedRows = grid.map { row =>
      val formattedRow = row.map {
        case Some(value) => value.toString
        case None => "0"
      }.grouped(3).map(_.mkString(" ")).mkString(" | ")
      s"| $formattedRow |\n"
    }
    val formattedGrid = formattedRows.grouped(3).map(_.mkString).mkString(horizontalLine)
    s"$horizontalLine$formattedGrid$horizontalLine"
  }


  def run: ZIO[Any, Throwable, Unit] =
    for {
      _ <- Console.print("Enter the path to the JSON file containing the Sudoku problem:")
      path <- Console.readLine
      _ <-  Console.printLine(s"You entered: $path")
      jsonString <- readFile(path)
      // _ <-  Console.printLine(jsonString)
      sudokuGrid  <- parseSudokuGrid(jsonString)
      _ <- Console.printLine(prettySudokuGrid(sudokuGrid))
      // Add your Sudoku solver logic here, utilizing ZIO and interacting with the ZIO Console
    } yield ()
}