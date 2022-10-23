package gui

import simulation.Field
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import javax.swing.JFrame

class MainWindow(
    title: String = "gui.MainWindow",
    fieldWidth: Int = 120,
    fieldHeight: Int = 80
): JFrame(title) {

    private val field = Field(fieldWidth, fieldHeight)
    private val canvas = SimulationCanvas(field)

    init {
        setDefaultCloseOperation(EXIT_ON_CLOSE)
        setLocationRelativeTo(null)
        setLayout(GridBagLayout())
    }

    init {
        canvas.setBounds(0, 0, fieldWidth * SimulationCanvas.CELL_SIZE, fieldHeight * SimulationCanvas.CELL_SIZE)
        val constraints = GridBagConstraints()
        constraints.gridx = 0
        constraints.gridy = 0
        add(canvas, constraints)
    }

    init {
        val controlPanel = ControlPanel(field)
        val constraints = GridBagConstraints()
        constraints.fill = GridBagConstraints.HORIZONTAL
        constraints.gridx = 0
        constraints.gridy = 1
        add(controlPanel, constraints)
    }

    init {
        val statsPanel = StatisticsPanel(field)
        val constraints = GridBagConstraints()
        constraints.fill = GridBagConstraints.HORIZONTAL
        constraints.gridx = 0
        constraints.gridy = 2
        add(statsPanel, constraints)
    }

    init {
        pack()
        setVisible(true)
    }

    init {
        Thread {
            while (true) {
                field.simulate()
                Thread.sleep(100)
            }
        }.start()
    }

    init {
        Thread {
            while (true) {
                canvas.repaint()
                Thread.sleep(100)
            }
        }.start()
    }
}
