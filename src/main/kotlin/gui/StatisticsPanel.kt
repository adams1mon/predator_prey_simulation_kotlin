package gui

import simulation.Field
import stats.Statistics
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import javax.swing.JButton
import javax.swing.JLabel
import javax.swing.JPanel

class StatisticsPanel(field: Field): JPanel() {

    private var statistics = Statistics()

    private val populationCountTemplate = "Population count: %d"
    private val foxCountTemplate = "Fox count: %d (%.2f)"
    private val rabbitCountTemplate = "Rabbit count: %d (%.2f)"
    private val foxEnergyTemplate = "Fox avg energy: %.2f"

    private val populationCountLabel = JLabel(populationCountTemplate.format(statistics.populationCount))
    private val foxCountLabel = JLabel(foxCountTemplate.format(statistics.foxCount, statistics.foxPercentage))
    private val rabbitCountLabel = JLabel(rabbitCountTemplate.format(statistics.rabbitCount, statistics.rabbitPercentage))
    private val foxEnergyLabel = JLabel(foxEnergyTemplate.format(statistics.foxAvgEnergy))

    private var rollingStatsActive = false

    init {
        statistics.addChangeListener {
            populationCountLabel.text = populationCountTemplate.format(statistics.populationCount)
            foxCountLabel.text = foxCountTemplate.format(statistics.foxCount, statistics.foxPercentage)
            rabbitCountLabel.text = rabbitCountTemplate.format(statistics.rabbitCount, statistics.rabbitPercentage)
            foxEnergyLabel.text = foxEnergyTemplate.format(statistics.foxAvgEnergy)
        }
    }

    // init block for labels
    init {
        layout = GridBagLayout()

        val constraints = GridBagConstraints()
        constraints.fill = GridBagConstraints.HORIZONTAL
        constraints.gridx = 0
        constraints.gridy = 0
        add(populationCountLabel, constraints)

        constraints.gridy = 1
        add(rabbitCountLabel, constraints)

        constraints.gridx = 1
        add(foxCountLabel, constraints)

        constraints.gridx = 2
        add(foxEnergyLabel, constraints)
    }

    // init block for buttons
    init {
        val constraints = GridBagConstraints()
        constraints.fill = GridBagConstraints.HORIZONTAL

        val oneTimeStatsBtn = JButton("Get current stats")
        oneTimeStatsBtn.addActionListener { field.getOneTimeStatistics(statistics) }

        constraints.gridy = 3
        add(oneTimeStatsBtn, constraints)

        val startStatsBtn = JButton("Start rolling stats")
        val stopStatsBtn = JButton("Stop rolling stats")
        stopStatsBtn.isEnabled = false

        startStatsBtn.addActionListener {
            field.startStatisticsThread(statistics)
            rollingStatsActive = true
            startStatsBtn.isEnabled = !rollingStatsActive
            stopStatsBtn.isEnabled = rollingStatsActive
        }

        stopStatsBtn.addActionListener {
            field.stopStatisticsThread()
            rollingStatsActive = false
            startStatsBtn.isEnabled = !rollingStatsActive
            stopStatsBtn.isEnabled = rollingStatsActive
        }

        constraints.gridx = 1
        add(startStatsBtn, constraints)

        constraints.gridx = 2
        add(stopStatsBtn, constraints)
    }
}
