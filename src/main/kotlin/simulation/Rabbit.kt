package simulation

import java.awt.Color
import java.awt.Image
import javax.imageio.ImageIO

class Rabbit: Animal() {

    override val image: Image = rabbitImage
    override val color: Color = Color.LIGHT_GRAY

    private var breedTime = BREED_TIME

    companion object {
        const val BREED_TIME = 20
        val rabbitImage: Image = ImageIO.read(javaClass.getResource("/bunny.png"))
    }

    override fun clone(): Rabbit {
        val rabbit = Rabbit()
        rabbit.setPosition(x, y)
        return rabbit
    }

    /**
     * Prey rabbits move randomly to free neighboring cells
     */
    override fun move(field: Field) {
        val (newX, newY) = getNewPosition(field)
        if (!field.cellTaken(newX, newY)) {
            field.move(x, y, newX, newY)
            setPosition(newX, newY)
        }
    }

    /**
     * Once the breed time is up a new rabbit spawns in a free neighboring cell and the parents breed time is reset.
     */
    override fun spawnOffspring(field: Field) {
        if (--breedTime <= 0) {
            breedTime = BREED_TIME
            val (newX, newY) = getNewPosition(field)
            if (!field.cellTaken(newX, newY)) {
                val rabbit = clone()
                rabbit.setPosition(newX, newY)
                field.add(newX, newY, rabbit)
            }
        }
    }
}
