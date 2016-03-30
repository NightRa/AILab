package knapsack

case class Item(weight: Double, value: Double) {
  def valueRatio = value / weight
}
