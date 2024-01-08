package taxipark

/*
 * Task #1. Find all the drivers who performed no trips.
 */
fun TaxiPark.findFakeDrivers(): Set<Driver> =
    allDrivers.filterNot {
        trips.map { trip -> trip.driver }
            .toSet()
            .contains(it)
    }.toSet()

/*
 * Task #2. Find all the clients who completed at least the given number of trips.
 */
fun TaxiPark.findFaithfulPassengers(minTrips: Int): Set<Passenger> {
    val passengerTripsCount = allPassengers.associateWith {
            passenger -> trips.count {
                trip -> passenger in trip.passengers
            }
    }

    return passengerTripsCount.filter { it.value >= minTrips }.keys
}

/*
 * Task #3. Find all the passengers, who were taken by a given driver more than once.
 */
fun TaxiPark.findFrequentPassengers(driver: Driver): Set<Passenger> {
    return allPassengers.associateWith {
            passenger -> trips.count {
            trip -> passenger in trip.passengers && trip.driver == driver } }
        .filterValues { it > 1 }.keys
}
/*
 * Task #4. Find the passengers who had a discount for majority of their trips.
 */
fun TaxiPark.findSmartPassengers(): Set<Passenger> =
    allPassengers.filter { p ->
        this.trips.count { passengerHadDiscount(p, it, true) } > this.trips.count { passengerHadDiscount(p, it, false) }
    }.toSet()


private fun passengerHadDiscount(p: Passenger, it: Trip, hadDiscount: Boolean): Boolean {
    return if (hadDiscount)
        p in it.passengers && it.discount.isNotNullOrZero()
    else
        p in it.passengers && !it.discount.isNotNullOrZero()
}

/*
 * Task #5. Find the most frequent trip duration among minute periods 0..9, 10..19, 20..29, and so on.
 * Return any period if many are the most frequent, return `null` if there're no trips.
 */
fun TaxiPark.findTheMostFrequentTripDurationPeriod(): IntRange? {
    val mostFrequentRange = trips.groupBy { it.duration / 10 * 10 }
        .mapValues { (_, trips) -> trips.count() }
        .mapKeys { (range, _) -> IntRange(range, range + 9) }
        .maxByOrNull { it.value }
    return mostFrequentRange?.key
}

/*
 * Task #6.
 * Check whether 20% of the drivers contribute 80% of the income.
 */
fun TaxiPark.checkParetoPrinciple(): Boolean {
    if (trips.isEmpty() || allDrivers.size < 5)
        return false

    var topPerformersCount: Int = allDrivers.size.div(5)
    if (allDrivers.count() == 5)
        topPerformersCount = 1

    val allIncome: Double = trips.sumOf(Trip::cost)

    val sumOfAllDrivers: Map<Driver, Double> = trips
        .groupBy { it.driver }
        .mapValues { (_, trips) -> trips.sumOf { it.cost } }

    val sortedSumOfAllDrivers = sumOfAllDrivers.toSortedMap(compareByDescending { sumOfAllDrivers[it] })

    var sumOfTopPerformers = 0.0

    for ((index, value) in sortedSumOfAllDrivers.values.withIndex()) {
        if (index == topPerformersCount) {
            break
        }
        sumOfTopPerformers += value
    }

    return sumOfTopPerformers.compareTo(allIncome*0.8) != -1

}

fun Double?.isNullOrZero(): Boolean = this == null || this == 0.0
fun Double?.isNotNullOrZero(): Boolean = !isNullOrZero()