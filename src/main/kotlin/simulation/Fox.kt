package simulation

import java.awt.Color
import java.awt.Image
import javax.imageio.ImageIO

class Fox: Animal() {

    override val image: Image = foxImage
    override val color: Color = Color.RED

    var energy = ENERGY_DEFAULT

    companion object {
        const val ENERGY_DEFAULT = 14
        const val ENERGY_LIMIT = 26
        const val ENERGY_PER_RABBIT = 10
        val foxImage: Image = ImageIO.read(javaClass.getResource("/fox.png"))
    }

    override fun clone(): Fox {
        val fox = Fox()
        fox.setPosition(x, y)
        return fox
    }

    /**
     * Foxes move randomly to neighboring cells that are free or occupied by rabbits.
     * If the cell to which the fox is moving is occupied by other rabbits it is consumed.
     * The energy of the shark increases by a predefined value.
     */
    override fun move(field: Field) {
        val (newX, newY) = getNewPosition(field)

        val rabbitOnCell = field.rabbitOnCell(newX, newY)
        if (field.cellTaken(newX, newY) && !rabbitOnCell)
            return

        if (rabbitOnCell) {
            field.remove(newX, newY)
            energy += ENERGY_PER_RABBIT
        }

        field.move(x, y, newX, newY)
        setPosition(newX, newY)
    }

    /**
     * If the fox has enough energy it spawns offspring in a free neighboring cell.
     * The energy is split evenly between the parent and the child.
     */
    override fun spawnOffspring(field: Field) {
        if (energy >= ENERGY_LIMIT) {
            val (newX, newY) = getNewPosition(field)
            if (!field.cellTaken(newX, newY)) {
                val fox = clone()
                fox.setPosition(newX, newY)
                energy /= 2
                fox.energy = energy
                field.add(newX, newY, fox)
            }
        }
    }

    /**
     * Foxes lose a small fixed amount of energy with every time step.
     * A fox dies if its energy level drops to zero.
     */
    override fun loseEnergy(field: Field) {
        if (--energy <= 0) {
            field.remove(x, y)
        }
    }
}
