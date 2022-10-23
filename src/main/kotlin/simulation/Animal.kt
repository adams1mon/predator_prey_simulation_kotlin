package simulation

import gui.SimulationCanvas
import java.awt.Color
import java.awt.Graphics
import java.awt.Image
import kotlin.random.Random

abstract class Animal: Cloneable<Animal> {

    protected abstract val image: Image

    protected abstract val color: Color

    protected var x = 0
    protected var y = 0

    /**
     * Animal entities can move left, up, right and down on the grid
     */
    companion object {
        val DIRECTIONS = arrayOf(Pair(-1, 0), Pair(0, -1), Pair(1, 0), Pair(0, 1))
    }

    fun setPosition(x: Int, y: Int) {
        this.x = x
        this.y = y
    }

    fun getPosition(): Pair<Int, Int> {
        return Pair(x, y)
    }

    fun drawToCanvas(graphics: Graphics?) {

//        graphics?.drawImage(
//            image,
//            x * SimulationCanvas.CELL_SIZE,
//            y * SimulationCanvas.CELL_SIZE,
//            SimulationCanvas.CELL_SIZE,
//            SimulationCanvas.CELL_SIZE,
//            null
//        )

        graphics?.color = color
        graphics?.fillRect(
            x * SimulationCanvas.CELL_SIZE,
            y * SimulationCanvas.CELL_SIZE,
            SimulationCanvas.CELL_SIZE,
            SimulationCanvas.CELL_SIZE,
        )
    }

    protected fun getNewPosition(field: Field): Pair<Int, Int> {
        val directionIndex = Random.nextInt(DIRECTIONS.size)
        var newX = x + DIRECTIONS[directionIndex].first
        var newY = y + DIRECTIONS[directionIndex].second

        newX = if (newX >= field.width) newX - field.width else if (newX < 0) field.width + newX else newX
        newY = if (newY >= field.height) newY - field.height else if (newY < 0) field.height + newY else newY

        return Pair(newX, newY)
    }

    abstract fun move(field: Field)
    abstract fun spawnOffspring(field: Field)
    open fun loseEnergy(field: Field) {}

    abstract override fun clone(): Animal
}
