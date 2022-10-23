package simulation

import stats.Statistics
import java.awt.Graphics
import java.util.*
import kotlin.random.Random


class Field(
    val width: Int,
    val height: Int,
    rabbitCount: Int = 50,
    foxCount: Int = 50,
) {

    private var grid = Array<Array<Animal?>>(height) { Array(width) { null } }
    private var animals = Collections.synchronizedSet(HashSet<Animal>())
    private var addBuffer = Collections.synchronizedList(LinkedList<Animal>())
    private var removeBuffer = Collections.synchronizedList(LinkedList<Animal>())

    private var statisticsThreadRunning = false

    init {
        addRabbits(rabbitCount)
        addFoxes(foxCount)
    }

    fun addRabbits(count: Int) {
        val firstRabbit = Rabbit()
        tryToPlaceOnFreeCell(firstRabbit)

        for (i in 0 until count) {
            tryToPlaceOnFreeCell(firstRabbit.clone())
        }
    }

    fun addFoxes(count: Int) {
        val firstFox = Fox()
        tryToPlaceOnFreeCell(firstFox)

        for (i in 0 until count) {
            tryToPlaceOnFreeCell(firstFox.clone())
        }
    }

    private fun tryToPlaceOnFreeCell(animal: Animal) {
        val iterLimit = 50
        var i = 0
        var x: Int
        var y: Int
        do {
            ++i
            x = Random.nextInt(width)
            y = Random.nextInt(height)
        } while (cellTaken(x, y) && i < iterLimit)

        if (i >= iterLimit) {
            println("placeOnField() iteration limit $iterLimit reached")
        }

        animal.setPosition(x, y)
        add(x, y, animal)
    }

    fun cellTaken(x: Int, y: Int): Boolean {
        return grid[y][x] != null
    }

    fun rabbitOnCell(x: Int, y: Int): Boolean {
        return grid[y][x] is Rabbit
    }

    fun add(x: Int, y: Int, animal: Animal) {
        addBuffer.add(animal)
        grid[y][x] = animal
    }

    fun remove(x: Int, y: Int) {
        removeBuffer.add(grid[y][x])
        grid[y][x] = null
    }

    fun move(oldX: Int, oldY: Int, newX: Int, newY: Int) {
        grid[newY][newX] = grid[oldY][oldX]
        grid[oldY][oldX] = null
    }

    fun draw(graphics: Graphics?) {
        animals.forEach { animal -> animal.drawToCanvas(graphics) }
    }

    fun simulate() {
        updateAnimals()
        animals.forEach { animal ->
            run {
                animal.move(this)
                animal.spawnOffspring(this)
                animal.loseEnergy(this)

            }
        }
    }

    /**
     * There is a concurrency issue here that i am not aware of...
     * Without copying the array a second time (newAnimalsCopy),
     * foxes with negative don't get deleted properly from the 'animals' array
     * in spite of the remove buffer
     */
    private fun updateAnimals() {
        val newAnimals = HashSet(animals)
        newAnimals.addAll(addBuffer)
        newAnimals.removeAll(removeBuffer)

        addBuffer = Collections.synchronizedList(LinkedList())
        removeBuffer = Collections.synchronizedList(LinkedList())

//        animals = newAnimals

        val newAnimalsCopy = HashSet(newAnimals)

        newAnimals.forEach { animal -> run {
            if (animal is Fox) {
                if (animal.energy <= 0) {
//                    this gets run a lof of times...
//                    println("update: removing fox energy: ${animal.energy}")
                    newAnimalsCopy.remove(animal)
//                    val (x, y) = animal.getPosition()
//                    grid[y][x] = null
                }
            }
        }
        }

//        animals = newAnimals
        animals = newAnimalsCopy
    }

    fun clear() {
        animals = Collections.synchronizedSet(HashSet())
        grid = Array(height) { Array(width) { null } }
        addBuffer = Collections.synchronizedList(LinkedList())
        removeBuffer = Collections.synchronizedList(LinkedList())
    }

    fun cloneAll() {
        animals.forEach { animal -> tryToPlaceOnFreeCell(animal.clone()) }
    }

    fun getOneTimeStatistics(statistics: Statistics) {
        statistics.computeStats(animals)
    }

    fun startStatisticsThread(statistics: Statistics) {
        statisticsThreadRunning = true
        Thread {
            while (statisticsThreadRunning) {
                statistics.computeStats(animals)
                Thread.sleep(1000)
            }
        }.start()
    }

    fun stopStatisticsThread() {
        statisticsThreadRunning = false
    }
}
