package stats

import simulation.Animal
import simulation.Fox
import simulation.Rabbit
import java.util.*

class Statistics {

    var foxCount = 0
    var rabbitCount = 0
    var populationCount = 0

    var foxPercentage = 0.0
    var rabbitPercentage = 0.0

    var foxAvgEnergy = 0.0

    private val changeListeners = LinkedList<() -> Unit>()

    fun computeStats(animals: Collection<Animal>) {
//    fun computeStats(animals: ConcurrentHashMap<Int, Animal>) {
        var newFoxCount = 0
        var newRabbitCount = 0
        var foxAllEnergy = 0.0

    animals.forEach { animal ->
//        animals.forEach { (_, animal) ->
            run {
                if (animal is Rabbit) {
                    ++newRabbitCount
                } else if (animal is Fox) {
                    ++newFoxCount
                    foxAllEnergy += animal.energy
                    println("fox energy: ${animal.energy}")
                }
            }
        }

        foxCount = newFoxCount
        rabbitCount = newRabbitCount
        populationCount = foxCount + rabbitCount

        foxPercentage = foxCount / populationCount.toDouble()
        rabbitPercentage = rabbitCount / populationCount.toDouble()

        foxAvgEnergy = foxAllEnergy / foxCount.toFloat()

        changeListeners.forEach { callback -> callback() }
    }

    fun addChangeListener(callback: () -> Unit) {
        changeListeners.add(callback)
    }
}
