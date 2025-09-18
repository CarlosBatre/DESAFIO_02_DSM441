package udb.edu.sv.ventaexpressmuebles

data class Sale(
    var id: String? = null,
    var customerId: String? = null,
    var customerName: String? = null,
    var items: List<SaleItem> = listOf(),
    var total: Float = 0f,
    var date: String? = null,
    var employeeId: String? = null
)

data class SaleItem(
    var productId: String? = null,
    var productName: String? = null,
    var quantity: Int = 0,
    var price: Float = 0f
)