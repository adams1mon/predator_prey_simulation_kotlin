package gui

import simulation.Field
import java.awt.FlowLayout
import javax.swing.JButton
import javax.swing.JPanel
import javax.swing.JSpinner
import javax.swing.SpinnerNumberModel

class ControlPanel(field: Field): JPanel() {

    private var spinnerValue = 20

    init {
        layout = FlowLayout()

        val spinner = JSpinner()
        spinner.model = SpinnerNumberModel(spinnerValue, 1, 40, 1)
        spinner.addChangeListener { ++spinnerValue }
        add(spinner)

        val addRabbitsBtn = JButton("Add rabbits")
        addRabbitsBtn.addActionListener { field.addRabbits(spinnerValue) }
        add(addRabbitsBtn)

        val addFoxesBtn = JButton("Add foxes")
        addFoxesBtn.addActionListener { field.addFoxes(spinnerValue) }
        add(addFoxesBtn)

        val cloneAll = JButton("Clone all")
        cloneAll.addActionListener { field.cloneAll() }
        add(cloneAll)

        val clearBtn = JButton("Clear")
        clearBtn.addActionListener { field.clear() }
        add(clearBtn)
    }
}
