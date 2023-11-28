package battleship

import java.lang.IndexOutOfBoundsException
import kotlin.math.abs

object Game {

    private var gameIsOver = false
    private var player1Turn = true
    private val player1 = Player("Player 1")
    private val player2 = Player("Player 2")

    fun startGame() {
        player1.setShips()
        player2.setShips()
        while (!gameIsOver) {
            if (player1Turn) takeAShot(player1)
            else takeAShot(player2)
            player1Turn = !player1Turn
        }
    }

    private fun takeAShot(playerOnMove: Player) {
        val playerWaiting = if (playerOnMove == player1) player2 else player1

        playerOnMove.game.printField()
        println("---------------------")
        playerOnMove.ships.printField()

        println("${playerOnMove.name}, it's your turn:")
        val coords = readln()
        if (coords.matches("[A-J]([123456789]|(10))".toRegex())) {
            val x = coords.substring(0, 1).single() - 'A'
            val y = coords.substring(1).toInt() - 1
            when (playerWaiting.ships[x][y]) {
                Symbols.SHIP -> {
                    playerWaiting.ships[x][y] = Symbols.HIT
                    playerOnMove.game[x][y] = Symbols.HIT
                    if (playerWaiting.shipIsSanked(x, y)) {
                        if (playerWaiting.shipsLeft == 0) {
                            println("You sank the last ship. You won. Congratulations!")
                            gameIsOver = true
                        } else println("You sank a ship! Specify a new target:")
                    } else {
                        println("You hit a ship!\n" +
                                "Press Enter and pass the move to another player")
                    }
                }

                Symbols.UNKNOWN -> {
                    playerWaiting.ships[x][y] = Symbols.MISS
                    playerOnMove.game[x][y] = Symbols.MISS
                    playerOnMove.game.printField()
                    println("You missed!\nPress Enter and pass the move to another player")

                }

                else -> {
                    println("You've already used this cell!\n" +
                            "Press Enter and pass the move to another player")
                    playerOnMove.game.printField()
                }
            }
        } else println("Error! You entered the wrong coordinates! Try again:")
        readln()
    }

    class Player(val name: String) {

        val game = MutableList(10) { MutableList(10) { Symbols.UNKNOWN } }
        val ships = MutableList(10) { MutableList(10) { Symbols.UNKNOWN } }
        var shipsLeft = 5

        private fun setShip(ship: SHIPS) {
            while (true) {
                println("Enter the coordinates of the ${ship.title} (${ship.length} cells):")
                val line = readln().split(" ")
                val code = setIsPossible(line[0], line[1], ship)
                when (code) {
                    1 -> println("Error! Wrong ship location! Try again:\n")
                    2 -> println("Error! Wrong length of the Submarine! Try again:\n")
                    3 -> println("Error! You placed it too close to another one. Try again:\n")
                    else -> break
                }
            }
        }

        private fun setIsPossible(c1: String, c2: String, ship: SHIPS): Int {
            val regex = "[A-J]([123456789]|(10))".toRegex()
            if (!c1.matches(regex) || !c2.matches(regex)) return 1

            val x1 = c1.substring(0, 1).single() - 'A'
            val y1 = c1.substring(1).toInt() - 1
            val x2 = c2.substring(0, 1).single() - 'A'
            val y2 = c2.substring(1).toInt() - 1
            if (x1 != x2 && y1 != y2) return 1

            val length = if (x1 == x2) abs(y2 - y1) + 1
            else abs(x2 - x1) + 1
            if (length != ship.length) return 2


            val startX = minOf(x1, x2)
            val endX = maxOf(x1, x2)
            val startY = minOf(y1, y2)
            val endY = maxOf(y1, y2)
            try {
                for (x in startX - 1..endX + 1) {
                    for (y in startY - 1..endY + 1) {
                        if (x in startX..endX && y in startY..endY) continue
                        if (ships[x][y] == Symbols.SHIP) {
                            return 3
                        }
                    }
                }
            } catch (_: IndexOutOfBoundsException) {
            }

            for (x in startX..endX) {
                for (y in startY..endY) {
                    ships[x][y] = Symbols.SHIP
                }
            }
            ships.printField()
            return 0
        }

        fun shipIsSanked(x: Int, y: Int): Boolean {
            for (i in x downTo 0) {
                if (ships[i][y] == Symbols.UNKNOWN || ships[x][y] == Symbols.MISS) break
                if (ships[i][y] == Symbols.SHIP) return false
            }
            for (i in x..ships.lastIndex) {
                if (ships[i][y] == Symbols.UNKNOWN || ships[x][y] == Symbols.MISS) break
                if (ships[i][y] == Symbols.SHIP) return false

            }
            for (j in y downTo 0) {
                if (ships[x][j] == Symbols.UNKNOWN || ships[x][y] == Symbols.MISS) break
                if (ships[x][j] == Symbols.SHIP) return false
            }
            for (j in y..ships[0].lastIndex) {
                if (ships[x][j] == Symbols.UNKNOWN || ships[x][y] == Symbols.MISS) break
                if (ships[x][j] == Symbols.SHIP) return false
            }
            shipsLeft--
            return true
        }

        fun setShips() {
            println("${this.name}, place your ships on the game field")
            this.ships.printField()
            for (ship in SHIPS.values()) {
                setShip(ship)
            }
            println("Press Enter and pass the move to another player")
            readln()
        }
    }

    fun MutableList<MutableList<Symbols>>.printField() {
        println("  1 2 3 4 5 6 7 8 9 10")
        var start = 'A'
        this.forEach {
            println("$start ${it.joinToString(" ") { it.cell }}")
            start++
        }
    }
}

enum class SHIPS(val title: String, val length: Int) {
    AIRCRAFT_CARRIER("Aircraft Carrier" ,5),
    BATTLESHIP("Battleship",4),
    SUBMARINE("Submarine ",3),
    CRUISER("Cruiser",3),
    DESTROYER("Destroyer",2)
}

enum class Symbols(val cell: String) {
    UNKNOWN("~"),
    SHIP("O"),
    HIT("X"),
    MISS("M"),
    EMPTY(" ")
}