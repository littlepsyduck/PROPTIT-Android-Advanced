:memo: <span style="color:orange">ANDROID_ADVANCED_005_ARCHITECTURE</span>

# ARCHITECTURE

![Picture 1](p2.png)

## Table of Content

- [ARCHITECTURE](#architecture)
  - [Table of Content](#table-of-content)
  - [I. SOLID, KISS, DRY](#i-solid-kiss-dry)
  - [II. Dependency injection, Manual DI](#ii-dependency-injection-manual-di)
  - [III. Clean architecture](#iii-clean-architecture)
  - [IV. Modularization](#iv-modularization)

## I. SOLID, KISS, DRY

Đây là ba nguyên tắc quan trọng trong phát triển phần mềm giúp viết code sạch, dễ bảo trì và mở rộng.

**1. SOLID:**

SOLID là một tập hợp 5 nguyên tắc thiết kế hướng đối tượng, giúp tạo ra các ứng dụng dễ dàng bảo trì và mở rộng.

- **S - Single Responsibility Principle (Nguyên tắc đơn trách nhiệm):**  Một class chỉ nên có một lý do để thay đổi.  Nói cách khác, mỗi class chỉ nên chịu trách nhiệm về một chức năng cụ thể.
- **O - Open/Closed Principle (Nguyên tắc mở/đóng):**  Các class nên mở để mở rộng (extension) nhưng đóng để sửa đổi (modification).  Bạn nên có thể thêm chức năng mới mà không cần sửa đổi code hiện có.
- **L - Liskov Substitution Principle (Nguyên tắc thay thế Liskov):**  Các đối tượng của class con phải có thể thay thế các đối tượng của class cha mà không làm thay đổi tính đúng đắn của chương trình.
- **I - Interface Segregation Principle (Nguyên tắc phân tách interface):**  Nhiều interface cụ thể (client-specific interfaces) tốt hơn một interface tổng quát (general-purpose interface).  Không nên ép một class phải implement các phương thức mà nó không sử dụng.
- **D - Dependency Inversion Principle (Nguyên tắc đảo ngược phụ thuộc):**  Các module cấp cao không nên phụ thuộc vào các module cấp thấp. Cả hai nên phụ thuộc vào các abstraction.  Abstractions không nên phụ thuộc vào chi tiết. Chi tiết nên phụ thuộc vào abstractions.

**2. KISS (Keep It Stupid Simple - Giữ cho nó đơn giản và ngu ngốc):**

Nguyên tắc KISS khuyến khích giữ cho code đơn giản và dễ hiểu.  Tránh code phức tạp không cần thiết.  Code đơn giản dễ dàng debug, bảo trì và mở rộng hơn.

**3. DRY (Don't Repeat Yourself - Không lặp lại chính mình):**

Nguyên tắc DRY khuyến khích tránh lặp lại code.  Nếu bạn thấy mình viết cùng một đoạn code nhiều lần, hãy tìm cách rút trích nó thành một hàm hoặc một class riêng biệt.  Việc này giúp giảm thiểu lỗi, dễ dàng bảo trì và cập nhật code.

**Ví dụ về việc áp dụng SOLID, KISS và DRY:**

Giả sử bạn đang viết một ứng dụng xử lý các hình dạng khác nhau.  Thay vì viết một class lớn xử lý tất cả các hình dạng, bạn có thể áp dụng SOLID và tạo các class riêng biệt cho từng hình dạng (ví dụ: `Circle`, `Square`, `Rectangle`).  Mỗi class này sẽ implement một interface chung (ví dụ: `Shape`).  Điều này tuân theo nguyên tắc Single Responsibility và Open/Closed.  Bạn cũng có thể sử dụng Dependency Inversion bằng cách inject `Shape` vào các class khác, thay vì phụ thuộc trực tiếp vào các class cụ thể như `Circle` hay `Square`.  Áp dụng KISS bằng cách giữ cho code trong mỗi class đơn giản và dễ hiểu.  Áp dụng DRY bằng cách rút trích các logic chung (ví dụ: tính diện tích) thành các hàm helper hoặc class utility.

Bằng cách áp dụng SOLID, KISS và DRY, bạn có thể viết code sạch hơn, dễ bảo trì hơn, và dễ dàng mở rộng hơn.

## II. Dependency injection, Manual DI

> Dependency Injection (DI) là một kỹ thuật quan trọng trong lập trình hướng đối tượng, giúp giảm sự phụ thuộc giữa các thành phần của ứng dụng bằng cách cung cấp các *dependencies* (phụ thuộc) cho một đối tượng từ bên ngoài, thay vì để đối tượng tự tạo ra chúng.

**Lợi ích của DI:**

- **Giảm sự kết nối (Loose Coupling):** Các thành phần ít phụ thuộc vào nhau, dễ dàng thay đổi và bảo trì.
- **Tăng khả năng tái sử dụng (Reusability):** Các thành phần có thể được tái sử dụng trong các ngữ cảnh khác nhau.
- **Dễ dàng test (Testability):** Việc mock dependencies trở nên dễ dàng hơn, giúp cho việc unit test hiệu quả hơn.
- **Cải thiện khả năng đọc code (Readability):** Code trở nên rõ ràng hơn khi các dependencies được cung cấp từ bên ngoài.

**Các loại DI:**

- **Constructor Injection:** Dependencies được cung cấp thông qua constructor của class.
- **Property Injection (Field Injection):** Dependencies được cung cấp thông qua các thuộc tính (field) của class.
- **Method Injection:** Dependencies được cung cấp thông qua các phương thức của class.

**Manual DI:**

- Manual DI là cách thực hiện DI đơn giản nhất, không sử dụng bất kỳ thư viện DI nào.  Bạn tự viết code để tạo và cung cấp dependencies.

```kotlin
interface Engine {
    fun start()
}

class GasolineEngine : Engine {
    override fun start() {
        println("Gasoline engine started")
    }
}

class ElectricEngine : Engine {
    override fun start() {
        println("Electric engine started")
    }
}

class Car(val engine: Engine) { // Constructor Injection
    fun start() {
        engine.start()
    }
}

fun main() {
    val gasolineEngine = GasolineEngine() // Tạo dependency
    val car = Car(gasolineEngine) // Inject dependency qua constructor
    car.start() // Output: Gasoline engine started

    val electricEngine = ElectricEngine()
    val electricCar = Car(electricEngine)
    electricCar.start() // Output: Electric engine started
}
```

- Ví dụ khác (Kotlin - Payment Processor):

```kotlin
interface PaymentProcessor {
    fun processPayment(amount: Double)
}

class StripePaymentProcessor : PaymentProcessor {
    override fun processPayment(amount: Double) {
        println("Processing payment of $amount via Stripe")
    }
}

class PayPalPaymentProcessor : PaymentProcessor {
    override fun processPayment(amount: Double) {
        println("Processing payment of $amount via PayPal")
    }
}

class CheckoutService(private val paymentProcessor: PaymentProcessor) {
    fun checkout(items: List<String>, total: Double) {
        // ... xử lý items
        paymentProcessor.processPayment(total)
    }
}

fun main() {
    val stripeProcessor = StripePaymentProcessor()
    val checkoutService = CheckoutService(stripeProcessor)
    checkoutService.checkout(listOf("Item A", "Item B"), 100.0)

    val paypalProcessor = PayPalPaymentProcessor()
    val anotherCheckoutService = CheckoutService(paypalProcessor)
    anotherCheckoutService.checkout(listOf("Item C"), 50.0)
}
```

- Hạn chế của Manual DI:

Nhiều boilerplate code: Bạn phải tự viết code để tạo và quản lý dependencies.

Khó quản lý dependencies phức tạp: Khi ứng dụng lớn lên, việc quản lý dependencies bằng tay trở nên khó khăn.

- Khi nào nên sử dụng Manual DI:

Ứng dụng nhỏ, đơn giản.

Muốn hiểu rõ nguyên lý hoạt động của DI trước khi sử dụng thư viện.

- Các framework DI phổ biến (khuyến nghị sử dụng trong dự án thực tế):

Hilt (cho Android): Được Google khuyến nghị, tích hợp tốt với Android lifecycle.

Koin (cho Kotlin): Dễ sử dụng, DSL mạnh mẽ.

Dagger (cho Java/Android): Mạnh mẽ nhưng phức tạp hơn.

- Kết luận:

Manual DI là cách tiếp cận đơn giản để học về DI. Tuy nhiên, đối với các ứng dụng phức tạp, nên sử dụng các framework DI để quản lý dependencies hiệu quả hơn. Các framework này giúp giảm boilerplate code, quản lý lifecycle của dependencies, và hỗ trợ các tính năng nâng cao như scoping và assisted injection.

## III. Clean architecture

Clean Architecture là một kiến trúc phần mềm được đề xuất bởi Robert C. Martin (Uncle Bob) nhằm tạo ra các ứng dụng dễ dàng bảo trì, kiểm thử và độc lập với các framework, UI, database và bất kỳ tác nhân bên ngoài nào.

**Nguyên tắc cốt lõi:**

- **Độc lập với Frameworks:** Kiến trúc không phụ thuộc vào sự tồn tại của bất kỳ thư viện nào. Điều này cho phép bạn sử dụng các framework như công cụ, chứ không phải là ràng buộc ứng dụng của bạn với chúng.
- **Testable:** Logic nghiệp vụ có thể được kiểm thử mà không cần UI, database, web server hoặc bất kỳ tác nhân bên ngoài nào khác.
- **Độc lập với UI:** UI có thể dễ dàng thay đổi mà không ảnh hưởng đến phần còn lại của hệ thống. Bạn có thể thay đổi từ web sang mobile mà không cần sửa đổi logic nghiệp vụ.
- **Độc lập với Database:** Logic nghiệp vụ không bị ràng buộc với bất kỳ cơ sở dữ liệu cụ thể nào. Bạn có thể chuyển đổi giữa SQL, NoSQL hoặc bất kỳ cơ chế lưu trữ dữ liệu nào khác mà không ảnh hưởng đến logic nghiệp vụ.
- **Độc lập với bất kỳ tác nhân bên ngoài nào:** Logic nghiệp vụ được cô lập với thế giới bên ngoài.

**Cấu trúc của Clean Architecture:**

Clean Architecture được tổ chức thành các lớp đồng tâm, với các dependency chỉ hướng vào trong. Các lớp bên ngoài phụ thuộc vào các lớp bên trong, nhưng không ngược lại.

- **Entities:** Chứa các đối tượng nghiệp vụ cốt lõi của ứng dụng. Chúng đại diện cho các khái niệm và quy tắc nghiệp vụ bất biến.
- **Use Cases:** Chứa logic ứng dụng cụ thể, thao tác trên các Entities. Chúng đại diện cho các hành động mà người dùng có thể thực hiện trong ứng dụng.
- **Interface Adapters:** Chứa các lớp chuyển đổi dữ liệu giữa các use case và các framework hoặc trình điều khiển bên ngoài (ví dụ: Presenters, Controllers, Gateways).
- **Frameworks and Drivers:** Lớp ngoài cùng, chứa các chi tiết cụ thể về framework, UI, database, web, v.v. (ví dụ: Android UI, Web Framework, Database).

**Lợi ích của Clean Architecture:**

- **Dễ bảo trì:** Việc thay đổi một phần của ứng dụng không ảnh hưởng đến các phần khác.
- **Dễ kiểm thử:** Các thành phần có thể được kiểm thử độc lập.
- **Linh hoạt:** Dễ dàng thích ứng với các thay đổi trong yêu cầu.
- **Code rõ ràng và dễ hiểu:** Cấu trúc lớp rõ ràng giúp code dễ đọc và dễ hiểu hơn.

**Nhược điểm:**

- **Phức tạp hơn:** Cần nhiều lớp và interface hơn so với các kiến trúc đơn giản hơn.
- **Khó khăn ban đầu:** Có thể mất thời gian để làm quen với kiến trúc.

**Kết luận:**

Clean Architecture là một kiến trúc mạnh mẽ giúp tạo ra các ứng dụng chất lượng cao, dễ bảo trì và kiểm thử. Mặc dù có độ phức tạp ban đầu, nhưng lợi ích lâu dài mà nó mang lại là rất đáng kể.

## IV. Modularization

- Modularization là một kỹ thuật quan trọng trong phát triển phần mềm, đặc biệt là đối với các ứng dụng lớn và phức tạp. Nó liên quan đến việc chia nhỏ một ứng dụng thành các module độc lập, có thể quản lý và bảo trì riêng biệt.

**Lợi ích của Modularization:**

- **Khả năng bảo trì được cải thiện (Improved Maintainability):** Các module nhỏ hơn, tập trung vào một chức năng cụ thể, dễ dàng hiểu, sửa đổi và debug hơn so với một khối code lớn.
- **Tái sử dụng code (Code Reusability):** Các module có thể được tái sử dụng trong các phần khác của ứng dụng hoặc trong các dự án khác.
- **Quản lý dependency rõ ràng (Clear Dependency Management):** Modularization giúp quản lý dependencies giữa các thành phần của ứng dụng một cách rõ ràng và hiệu quả hơn.
- **Build nhanh hơn (Faster Build Times):** Chỉ cần rebuild các module bị thay đổi, thay vì rebuild toàn bộ ứng dụng.
- **Làm việc nhóm hiệu quả hơn (Better Team Collaboration):** Các nhóm khác nhau có thể làm việc trên các module khác nhau một cách độc lập.
- **Kiểm thử dễ dàng hơn (Easier Testing):** Các module nhỏ hơn, dễ dàng viết unit test và integration test hơn.
- **Scalability:** Dễ dàng mở rộng ứng dụng bằng cách thêm các module mới.
- **Tách biệt mối quan tâm (Separation of Concerns):** Mỗi module tập trung vào một nhiệm vụ cụ thể, giúp code dễ hiểu và bảo trì hơn.

**Cách thực hiện Modularization:**

- **Phân tích ứng dụng:** Xác định các chức năng chính của ứng dụng và cách chia chúng thành các module.
- **Tạo các module:** Tạo các module riêng biệt cho từng chức năng.
- **Định nghĩa interface:** Định nghĩa các interface để giao tiếp giữa các module.
- **Quản lý dependencies:** Sử dụng một hệ thống quản lý dependencies (như Gradle, Maven) để quản lý dependencies giữa các module.
- **Tích hợp các module:** Kết hợp các module lại với nhau để tạo thành ứng dụng hoàn chỉnh.

**Ví dụ (Android):**

Trong Android, bạn có thể chia ứng dụng thành các module như:

- **Module `app`:** Module chính, chứa code liên quan đến UI và logic nghiệp vụ chính.
- **Module `features`:** Chứa các module tính năng riêng biệt, ví dụ: `feature_login`, `feature_profile`, `feature_home`.
- **Module `data`:** Chứa code liên quan đến truy cập dữ liệu (database, network).
- **Module `common`:** Chứa các utilities, helper functions, và các thành phần được sử dụng chung bởi các module khác.

**Modularization và các kỹ thuật khác:**

Modularization thường được kết hợp với các kỹ thuật khác như Dependency Injection (DI) để tăng tính linh hoạt và khả năng tái sử dụng của code.

**Kết luận:**

Modularization là một kỹ thuật quan trọng giúp cải thiện chất lượng và khả năng bảo trì của ứng dụng. Nó đặc biệt hữu ích cho các ứng dụng lớn và phức tạp. Bằng cách chia nhỏ ứng dụng thành các module độc lập, bạn có thể làm cho code dễ hiểu hơn, dễ dàng tái sử dụng hơn, và dễ dàng test hơn.
