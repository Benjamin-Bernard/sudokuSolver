import zio.*
import zio.Console.*
import zio.json.*

import java.nio.charset.StandardCharsets
import zio.nio.file.{Files, Path}

import scala.annotation.tailrec

type SudokuGrid = List[List[Option[Int]]]

object Main extends ZIOAppDefault {
    // Read the File store Locally and transform bytes to String
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
    // Display the Sudolu in a prettier way
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

    // Check if a number is containing in the row
    def isInRow(grid: SudokuGrid, row: Int, num: Int): Boolean = {
        grid(row).contains(Some(num))
    }

    // Check if a number is containing in the col
    def isInCol(grid: SudokuGrid, col: Int, num: Int): Boolean = {
        grid.map(_(col)).contains(Some(num))
    }

    // Check if a number is containing in the box 3 * 3
    def isInBox(grid: SudokuGrid, row: Int, col: Int, num: Int): Boolean = {
        val boxRow = row - row % 3
        val boxCol = col - col % 3
        grid.slice(boxRow, boxRow + 3).flatMap(_.slice(boxCol, boxCol + 3)).contains(Some(num))
    }

    // Return true if the number is not in the row, the col and in the box
    def isValid(grid: SudokuGrid, row: Int, col: Int, num: Int): Boolean = {
        !isInRow(grid, row, num) && !isInCol(grid, col, num) && !isInBox(grid, row, col, num)
    }
    
    //Backtracking algorithm
    def solve(sudokuGrid: SudokuGrid): Option[SudokuGrid] = {
        def solveHelper(grid: SudokuGrid, row: Int, col: Int): Option[SudokuGrid] = {
            if (row == 9) {
                Some(grid)
            } 
            else if (col == 9) {
                solveHelper(grid, row + 1, 0)
            } 
            else if (grid(row)(col).isEmpty) {
                val validValues = (1 to 9).filter(value => isValid(grid, row, col, value))
                val solutions = validValues.flatMap { value =>
                val updatedGrid = grid.updated(row, grid(row).updated(col, Some(value)))
                solveHelper(updatedGrid, row, col + 1)
            }
                solutions.headOption
            } 
            else {
                solveHelper(grid, row, col + 1)
            }
        }

        solveHelper(sudokuGrid, 0, 0)
    }


    def run: ZIO[Any, Throwable, Unit] =
    for {
        _ <- Console.print("Enter the path to the JSON file containing the Sudoku problem:")
        path <- Console.readLine
        _ <-  Console.printLine(s"You entered: $path")
        jsonString <- readFile(path)
        _ <-  Console.printLine(jsonString)
        sudokuGrid <- parseSudokuGrid(jsonString)
        _ <- Console.printLine("Provided Sudoku:")
        _ <- Console.printLine(prettySudokuGrid(sudokuGrid))
        solution = solve(sudokuGrid)
        _ <- solution match {
        case Some(grid) => Console.printLine("Solution:") *> Console.printLine(prettySudokuGrid(grid))
        case None => Console.printLine("No solution found")
    }
    } yield ()
}