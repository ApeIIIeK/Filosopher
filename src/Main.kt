import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock
import kotlin.random.Random

class Fork(val id: Int) {
    private val lock = ReentrantLock()

    fun pickUp() = lock.lock()
    fun putDown() = lock.unlock()
}

class Philosopher(val id: Int, val leftFork: Fork, val rightFork: Fork) : Runnable {
    private val random = Random.Default

    override fun run() {
        // Логика философа при обеде
        // Например, попытка взять вилки и обедать

        Thread.sleep(random.nextInt(1000).toLong()) // имитация размышлений

        if (id % 2 == 0) {
            // Философ с четным id берет сначала левую вилку, затем правую
            leftFork.pickUp()
            rightFork.pickUp()
        } else {
            // Философ с нечетным id берет сначала правую вилку, затем левую
            rightFork.pickUp()
            leftFork.pickUp()
        }

        println("Философ $id обедает") // Выводим информацию о том, что философ обедает

        // Отправляемся на имитацию обеда
        Thread.sleep(random.nextInt(1000).toLong())

        // Положим вилки обратно на стол
        leftFork.putDown()
        rightFork.putDown()

        println("Философ $id размышляет") // Выводим информацию о том, что философ размышляет
    }
}
fun main() {
    val numberOfPhilosophers = 5 // количество философов
    val forks = List(numberOfPhilosophers) { Fork(it) } // создаем список вилок
    val philosophers = List(numberOfPhilosophers) {
        Philosopher(it, forks[it], forks[(it + 1) % numberOfPhilosophers]) // создаем философов с их вилками
    }

    val threads = philosophers.map { Thread(it) }
    threads.forEach(Thread::start)
    threads.forEach { it.join() }
}
