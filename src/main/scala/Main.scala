import zio._
import zio.Console._
import zio.json._
import java.nio.charset.StandardCharsets
import zio.nio.file.{Path, Files}

object Main extends ZIOAppDefault {

  def readJsonFile(path: String): ZIO[Any, Throwable, String] =
    Files
      .readAllBytes(Path(path))
      .map(bytes => new String(bytes.toArray, StandardCharsets.UTF_8))


  def run: ZIO[Any, Throwable, Unit] =
    for {
      _ <- Console.print("Enter the path to the JSON file containing the Sudoku problem:")
      path <- Console.readLine
      _ <-  Console.printLine(s"You entered: $path")
      jsonFile <- readJsonFile(path)
      _ <-  Console.printLine(jsonFile)
      // Add your Sudoku solver logic here, utilizing ZIO and interacting with the ZIO Console
    } yield ()
}