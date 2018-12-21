package com.weberapps.ray

import java.io.FileReader
import java.io.Reader
import java.io.StringReader

class Position(val x: Int, val y: Int) {
  override fun equals(other: Any?): Boolean {
    if (other !is Position) return false

    return x == other.x && y == other.y
  }
}

interface Heading
class North: Heading {
  override fun toString() = "^"
}
class South: Heading {
  override fun toString() = "v"
}
class East: Heading {
  override fun toString() = ">"
}
class West: Heading {
  override fun toString() = "<"
}

class Cart(var pos: Position, var heading: Heading, var crossings: Int = 0) {
  val x: Int
    get() = pos.x
  val y: Int
    get() = pos.y

  fun move(route: Route) {
    val cart = when (heading) {
      is North -> route.travelNorth(moveNorth(), crossings)
      is South -> route.travelSouth(moveSouth(), crossings)
      is East  -> route.travelEast(moveEast(), crossings)
      is West  -> route.travelWest(moveWest(), crossings)
      else -> this
    }
    this.pos = cart.pos
    this.heading = cart.heading
    this.crossings = cart.crossings
  }

  override fun toString() = heading.toString()

  private fun moveNorth() = Position(pos.x, pos.y - 1)
  private fun moveSouth() = Position(pos.x, pos.y + 1)
  private fun moveEast()  = Position(pos.x + 1, pos.y)
  private fun moveWest()  = Position(pos.x - 1, pos.y)
}

interface Route {
  val pos: Position
  val x: Int
    get() = pos.x
  val y: Int
    get() = pos.y

  fun travelNorth(pos: Position, crossings: Int) = Cart(pos, North(), crossings)
  fun travelSouth(pos: Position, crossings: Int) = Cart(pos, South(), crossings)
  fun travelEast(pos: Position, crossings: Int)  = Cart(pos, East(),  crossings)
  fun travelWest(pos: Position, crossings: Int)  = Cart(pos, West(),  crossings)
}

class EmptyRoute(x: Int, y: Int): Route {
  override val pos = Position(x, y)
  override fun toString() = " "
}

class NorthSouth(x: Int, y: Int): Route {
  override val pos = Position(x, y)
  override fun toString() = "|"
}
class EastWest(x: Int, y: Int): Route {
  override val pos = Position(x, y)
  override fun toString() = "-"
}
// /
class SouthEast(x: Int, y: Int): Route {
  override val pos = Position(x, y)
  override fun toString() = "/"

  override fun travelNorth(pos: Position, crossings: Int) = Cart(pos, East(), crossings)
  override fun travelSouth(pos: Position, crossings: Int) = Cart(pos, West(), crossings)
  override fun travelEast(pos: Position, crossings: Int) = Cart(pos, North(), crossings)
  override fun travelWest(pos: Position, crossings: Int) = Cart(pos, South(), crossings)
}
// \
class SouthWest(x: Int, y: Int): Route {
  override val pos = Position(x, y)
  override fun toString() = "\\"

  override fun travelNorth(pos: Position, crossings: Int) = Cart(pos, West(), crossings)
  override fun travelSouth(pos: Position, crossings: Int) = Cart(pos, East(), crossings)
  override fun travelEast(pos: Position, crossings: Int) = Cart(pos, South(), crossings)
  override fun travelWest(pos: Position, crossings: Int) = Cart(pos, North(), crossings)
}
class Intersection(x: Int, y: Int): Route {
  override val pos = Position(x, y)
  override fun toString() = "+"

  override fun travelNorth(pos: Position, crossings: Int): Cart {
    val heading = when(crossings % 3) {
      0 -> West()
      1 -> North()
      else -> East()
    }
    return Cart(pos, heading, crossings + 1)
  }

  override fun travelSouth(pos: Position, crossings: Int): Cart {
    val heading = when(crossings % 3) {
      0 -> East()
      1 -> South()
      else -> West()
    }
    return Cart(pos, heading, crossings + 1)
  }

  override fun travelEast(pos: Position, crossings: Int): Cart {
    val heading = when(crossings % 3) {
      0 -> North()
      1 -> East()
      else -> South()
    }
    return Cart(pos, heading, crossings + 1)
  }

  override fun travelWest(pos: Position, crossings: Int): Cart {
    val heading = when(crossings % 3) {
      0 -> South()
      1 -> West()
      else -> North()
    }

    return Cart(pos, heading, crossings + 1)
  }
}

class Map(initMap: Reader) {
  private val coords = arrayListOf(arrayListOf<Route>())
  private val carts = arrayListOf<Cart>()

  init {
    for ((i, line) in initMap.readLines().withIndex()) {
      for ((j, char) in line.withIndex()) {
        this[i, j] = parseChar(char, j, i)
      }
    }
  }

  fun tick(): Position? {
    for ((i, cart) in carts.withIndex()) {
      cart.move(this[cart.x, cart.y])

      for ((j, other) in carts.withIndex()) {
        if (i == j) continue

        if (other.pos == cart.pos) return cart.pos
      }
    }

    return null
  }

  override fun toString(): String {
    var result = ""
    for ((y, row) in coords.withIndex()) {
      for ((x, coord) in row.withIndex()) {
        val cart = cartAt(x, y)
        result += (cart ?: coord).toString()
      }
      result += "\n"
    }
    return result
  }

  private fun cartAt(x: Int, y: Int): Cart? {
    for (cart in carts) {
      if (cart.x == x && cart.y == y) return cart
    }

    return null
  }

  private fun parseChar(char: Char, x: Int, y: Int): Route {
    return when (char) {
      '|'  -> NorthSouth(x, y)
      '-'  -> EastWest(x, y)
      '/'  -> SouthEast(x, y)
      '\\' -> SouthWest(x, y)
      '+'  -> Intersection(x, y)
      'v'  -> {
        carts.add(Cart(Position(x, y), South()))
        parseChar('|', x, y)
      }
      '^'  -> {
        carts.add(Cart(Position(x, y), North()))
        parseChar('|', x, y)
      }
      '<'  -> {
        carts.add(Cart(Position(x, y), West()))
        parseChar('-', x, y)
      }
      '>'  -> {
        carts.add(Cart(Position(x, y), East()))
        parseChar('-', x, y)
      }
      else -> EmptyRoute(x, y)
    }
  }

  operator fun set(x: Int, y: Int, value: Route) {
    while (coords.size <= y) {
      coords.add(arrayListOf())
    }
    while (coords[y].size <= x) {
      coords[y].add(EmptyRoute(x, y))
    }
    coords[y][x] = value
  }

  operator fun get(x: Int, y: Int): Route {
    val row = coords[y]
    return row[x]
  }
}

fun main(args: Array<String>) {
  // val input = FileReader("input13.txt")
  val input = StringReader(
    """
/->-\
|   |  /----\
| /-+--+-\  |
| | |  | v  |
\-+-/  \-+--/
  \------/
    """.trimIndent()
  )
  val map = Map(input)
  var collision: Position? = map.tick()
  println("tick: 0")
  println(map.toString())
  var tick = 1

  while (collision == null) {
    collision = map.tick()
    println("tick: $tick")
    println(map.toString())
    tick += 1
  }

  println("First collision at <${collision?.x},${collision?.y}>")
}
