package gui

import simulation.Field
import java.awt.Dimension
import java.awt.Graphics
import javax.swing.JPanel

class SimulationCanvas(
    private val field: Field
): JPanel() {

    init {
        preferredSize = Dimension(field.width * CELL_SIZE, field.height * CELL_SIZE)
    }

    companion object {
        const val CELL_SIZE = 10
    }

    override fun paintComponent(g: Graphics?) {
        super.paintComponent(g)
        field.draw(g)
    }
}
