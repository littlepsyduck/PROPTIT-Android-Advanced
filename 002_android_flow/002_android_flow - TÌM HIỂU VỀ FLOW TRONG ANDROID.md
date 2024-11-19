:memo: <span style="color:orange">ANDROID_ADVANCED_002_ANDROID_FLOW</span>

# ANDROID FLOW

![Picture 1](p1.png)

## Table of Content

- [ANDROID FLOW](#android-flow)
  - [Table of Content](#table-of-content)
  - [I. Flow overview](#i-flow-overview)
    - [1. Flow là gì?](#1-flow-là-gì)
    - [2. Cách tạo ra flow](#2-cách-tạo-ra-flow)
      - [1. Sử dụng khối flow{ }](#1-sử-dụng-khối-flow-)
      - [2. Sử dụng hàm flowOf()](#2-sử-dụng-hàm-flowof)
      - [3. Sử dụng .asFlow() extension function](#3-sử-dụng-asflow-extension-function)
  - [II. Các toán tử của flow](#ii-các-toán-tử-của-flow)
    - [1. take()](#1-take)
    - [2. map()](#2-map)
    - [3. transform()](#3-transform)
    - [4. onEach()](#4-oneach)
    - [5. reduce()](#5-reduce)
    - [6. fold()](#6-fold)
    - [7. debounce()](#7-debounce)
    - [8. sample()](#8-sample)
    - [9. flatMapMerge()](#9-flatmapmerge)
    - [10. flatMapConcat()](#10-flatmapconcat)
    - [11. combine()](#11-combine)
    - [12. zip()](#12-zip)
  - [III. Cold flow vs Hot flow. Flow vs StateFlow vs SharedFlow](#iii-cold-flow-vs-hot-flow-flow-vs-stateflow-vs-sharedflow)
    - [1. Cold flow vs Hot flow](#1-cold-flow-vs-hot-flow)
      - [1. Cold Flow](#1-cold-flow)
      - [2. Hot Flow](#2-hot-flow)
    - [2. Flow vs StateFlow vs SharedFlow](#2-flow-vs-stateflow-vs-sharedflow)
      - [1. Flow](#1-flow)
      - [2. StateFlow](#2-stateflow)
      - [3. SharedFlow](#3-sharedflow)
      - [4. So sánh](#4-so-sánh)
  - [IV. So sánh với LiveData](#iv-so-sánh-với-livedata)
    - [1. Khi nào nên sử dụng Flow?](#1-khi-nào-nên-sử-dụng-flow)
    - [2. Khi nào nên sử dụng LiveData?](#2-khi-nào-nên-sử-dụng-livedata)

## I. Flow overview

### 1. Flow là gì?

> Về cơ bản, flow là một dòng dữ liệu có thể được tính toán không đồng bộ. Flow được xây dựng dựa trên coroutine.

- Là một loại dữ liệu có thể phát ra nhiều giá trị tuần tự, khác với suspend function (hàm tạm ngưng) chỉ trả về một giá trị duy nhất.
- Có 3 thực thể tham gia vào flow:
  - Thực thể tạo (producer) có vai trò tạo dữ liệu để thêm vào dòng dữ liệu. Nhờ coroutine, flow cũng có thể tạo ra dữ liệu một cách không đồng bộ. emit() flow
  - Thực thể trung gian (intermediary, nếu có) có thể sửa đổi từng giá trị được phát vào dòng dữ liệu hoặc sửa đổi chính dòng dữ liệu.
  - Thực thể tiêu thụ (consumer) sử dụng các giá trị trong dòng dữ liệu. collect() flow

![Picture 2](p2.png)

### 2. Cách tạo ra flow

#### 1. Sử dụng khối flow{ }

```java
fun foo(): Flow<Int> = flow{
    for(i in 1..3){
        delay(100)
        emit(i)
    }
}

fun main() = runBlocking{
    val time = measureTimeMillis {
        foo().collect{value -> println(value)}
    }
    println("$time millis")
}
```

- Khối `flow{}` là 1 builder function để tạo ra đối tượng Flow.
- Code bên trong `flow{}` có thể là ***suspend function***. Hàm `fun foo()` không cần phải là ***suspend function.***
- Hàm `emit` dùng để emit các giá trị từ flow, là 1 ***suspend function***.
- Hàm `collection` dùng để get các giá trị được emit, là 1 ***suspend function.***

#### 2. Sử dụng hàm flowOf()

```java
fun main() = runBlocking{
    val data = flowOf("1 2 3", "a b c", 1, 2, 3)
    data.collect { println(it) }
}

// Output:
1 2 3
a b c
1
2
3
```

#### 3. Sử dụng .asFlow() extension function

- Chuyển đổi kiểu dữ liệu như Collections, Sequences ,…thành các luồng.

```java
fun main() = runBlocking {
    listOf("1 2 3", "a b c", "1", "2", "3") 
        .asFlow()
        .collect { println(it) }
}

// Output:
1 2 3
a b c
1
2
3
```

```java
fun main() = runBlocking {
    listOf<Any>("1 2 3", "a b c", 1, 2, 3) 
        .asFlow()
        .collect { println(it) }
}
```

## II. Các toán tử của flow

### 1. take()

- Giới hạn số lượng phần tử được phát ra.

```java
flowOf(1, 2, 3, 4, 5)
    .take(3)
    .collect { println(it) } // Output: 1, 2, 3
```

### 2. map()

- Chuyển đổi từng giá trị của luồng đầu vào thành giá trị khác bằng một hàm đơn giản.
- Chỉ phát ra một giá trị duy nhất cho mỗi giá trị nhận được từ upstream.

```java
flowOf(1, 2, 3)
    .map { it * 2 }
    .collect { println(it) } // Output: 2, 4, 6
```

### 3. transform()

- Cung cấp nhiều khả năng tùy chỉnh hơn so với `map`.
- Cho phép phát ra nhiều giá trị hoặc không phát ra giá trị nào cho mỗi phần tử đầu vào.
- Có thể thực hiện thêm các tác vụ phức tạp như truy vấn dữ liệu, điều kiện lọc, hoặc phát ra nhiều giá trị.

```java
flowOf(1, 2, 3)
    .transform { emit(it); emit(it * 2) }
    .collect { println(it) } // Output: 1, 2, 2, 4, 3, 6
```

### 4. onEach()

- Thực hiện hành động phụ trên mỗi phần tử mà không thay đổi nó.

```java
flowOf(1, 2, 3)
    .onEach { println("Processing: $it") }
    .collect { println(it) }
// Output:
// Processing: 1
// 1
// Processing: 2
// 2
// Processing: 3
// 3
```

### 5. reduce()

- Tích lũy các phần tử thành một giá trị duy nhất.

```java
val sum = flowOf(1, 2, 3)
    .reduce { acc, value -> acc + value }
println(sum) // Output: 6
```

### 6. fold()

- Tương tự `reduce`, nhưng có giá trị khởi tạo.

```java
val sum = flowOf(1, 2, 3)
    .fold(10) { acc, value -> acc + value }
println(sum) // Output: 16
```

### 7. debounce()

- Chỉ phát phần tử cuối cùng sau một khoảng thời gian chờ (giảm tần suất sự kiện).

```java
flow {
    emit(1)
    delay(50)
    emit(2)
    delay(300)
    emit(3)
}.debounce(200)
    .collect { println(it) } // Output: 3
```

### 8. sample()

- Phát ra phần tử sau một khoảng thời gian cố định.

```java
flow {
    repeat(10) {
        emit(it)
        delay(100)
    }
}.sample(300)
    .collect { println(it) } // Output: 2, 5, 8
```

### 9. flatMapMerge()

- Kết hợp các luồng con một cách song song.
- Dữ liệu từ các luồng con được phát ra bất kỳ khi nào sẵn sàng, không cần tuân theo thứ tự phát ra của luồng chính.
- Tăng tốc xử lý vì các luồng con có thể chạy đồng thời.

```java
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

fun main() = runBlocking {
    val mainFlow = flowOf(1, 2, 3) // Luồng chính phát ra các giá trị 1, 2, 3

    println("=== flatMapMerge Example ===")
    val startTime = System.currentTimeMillis()

    mainFlow.flatMapMerge { number ->
        flow {
            println("Processing $number in flatMapMerge (Start: ${System.currentTimeMillis() - startTime}ms)")
            delay(100L * number) // Giả lập thời gian xử lý khác nhau
            emit("$number processed")
        }
    }.collect { result ->
        println("Collected in flatMapMerge: $result (Elapsed: ${System.currentTimeMillis() - startTime}ms)")
    }
}

// Output:
=== flatMapMerge Example ===
Processing 1 in flatMapMerge (Start: 0ms)
Processing 2 in flatMapMerge (Start: 1ms)
Processing 3 in flatMapMerge (Start: 1ms)
Collected in flatMapMerge: 1 processed (Elapsed: 102ms)
Collected in flatMapMerge: 2 processed (Elapsed: 202ms)
Collected in flatMapMerge: 3 processed (Elapsed: 302ms)
```

### 10. flatMapConcat()

- Kết hợp các luồng con một cách tuần tự.
- Dữ liệu từ các luồng con được phát ra tuần tự theo thứ tự phát sinh từ luồng chính.
- Các luồng con chạy nối tiếp, mỗi luồng phải hoàn thành trước khi luồng tiếp theo bắt đầu.

```java
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

fun main() = runBlocking {
    val mainFlow = flowOf(1, 2, 3) // Luồng chính phát ra các giá trị 1, 2, 3

    println("=== flatMapConcat Example ===")
    val startTime = System.currentTimeMillis()

    mainFlow.flatMapConcat { number ->
        flow {
            println("Processing $number in flatMapConcat (Start: ${System.currentTimeMillis() - startTime}ms)")
            delay(100L * number) // Giả lập thời gian xử lý khác nhau
            emit("$number processed")
        }
    }.collect { result ->
        println("Collected in flatMapConcat: $result (Elapsed: ${System.currentTimeMillis() - startTime}ms)")
    }
}

// Output:
=== flatMapConcat Example ===
Processing 1 in flatMapConcat (Start: 0ms)
Collected in flatMapConcat: 1 processed (Elapsed: 102ms)
Processing 2 in flatMapConcat (Start: 103ms)
Collected in flatMapConcat: 2 processed (Elapsed: 303ms)
Processing 3 in flatMapConcat (Start: 304ms)
Collected in flatMapConcat: 3 processed (Elapsed: 604ms)
```

### 11. combine()

- Mỗi khi một trong hai luồng phát ra giá trị, giá trị mới nhất từ cả hai luồng sẽ được kết hợp.
- Không dừng lại ngay cả khi một luồng đã phát hết giá trị (nếu là luồng `hot`).

```java
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

fun main() = runBlocking {
    val flow1 = flow {
        emit(1)
        delay(100)
        emit(2)
    }

    val flow2 = flow {
        emit("A")
        delay(50)
        emit("B")
        delay(50)
        emit("C")
    }

    println("=== Using zip ===")
    flow1.zip(flow2) { number, letter ->
        "$number - $letter"
    }.collect { result ->
        println(result)
    }

    println("\n=== Using combine ===")
    flow1.combine(flow2) { number, letter ->
        "$number - $letter"
    }.collect { result ->
        println(result)
    }
}

// Output:
=== Using combine ===
1 - A
1 - B
1 - C
2 - C
```

### 12. zip()

- Lấy lần lượt từng giá trị từ hai luồng (flow).
- Kết hợp các giá trị tương ứng tại cùng thời điểm phát ra.
- Nếu một luồng phát hết giá trị trước, luồng kết quả sẽ dừng lại.

```java
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

fun main() = runBlocking {
    val flow1 = flow {
        emit(1)
        delay(100)
        emit(2)
    }

    val flow2 = flow {
        emit("A")
        delay(50)
        emit("B")
        delay(50)
        emit("C")
    }

    println("=== Using zip ===")
    flow1.zip(flow2) { number, letter ->
        "$number - $letter"
    }.collect { result ->
        println(result)
    }

    println("\n=== Using combine ===")
    flow1.combine(flow2) { number, letter ->
        "$number - $letter"
    }.collect { result ->
        println(result)
    }
}

// Output:
=== Using zip ===
1 - A
2 - B
```

## III. Cold flow vs Hot flow. Flow vs StateFlow vs SharedFlow

### 1. Cold flow vs Hot flow

> Cold Flow và Hot Flow là hai loại luồng dữ liệu. Phân loại dựa trên cách chúng phát và chia sẻ giá trị.

#### 1. Cold Flow

- Luồng **chỉ bắt đầu phát giá trị** khi có một **collector** (người thu thập) đăng ký.
- Mỗi collector sẽ nhận một **luồng dữ liệu độc lập** từ đầu.
- Giá trị được tạo và phát lại mỗi khi có collector mới.
- Được sử dụng nhiều trong các use case mà dữ liệu cần được xử lý riêng lẻ cho từng collector.

```java
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

fun main() = runBlocking {
    val coldFlow = flow {
        println("Flow started")
        for (i in 1..3) {
            delay(100) // Giả lập quá trình tạo dữ liệu
            emit(i)
        }
    }

    println("Collector 1 subscribing")
    coldFlow.collect { value ->
        println("Collector 1: $value")
    }

    println("Collector 2 subscribing")
    coldFlow.collect { value ->
        println("Collector 2: $value")
    }
}

// Output:
Collector 1 subscribing
Flow started
Collector 1: 1
Collector 1: 2
Collector 1: 3
Collector 2 subscribing
Flow started
Collector 2: 1
Collector 2: 2
Collector 2: 3
```

- Dữ liệu được tạo **riêng biệt** cho mỗi collector.
- Mỗi lần collector đăng ký, `Flow` bắt đầu lại từ đầu.

#### 2. Hot Flow

- Luồng **bắt đầu phát giá trị ngay lập tức** (hoặc theo một trigger) và phát liên tục.
- Giá trị được **chia sẻ** giữa tất cả các collector.
- Collector có thể tham gia và rời khỏi **bất kỳ lúc nào** và chỉ nhận được giá trị từ thời điểm tham gia.
- Thường được dùng trong các use case như phát giá trị trạng thái hoặc dữ liệu theo thời gian thực.

```java
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*

fun main() = runBlocking {
    val hotFlow = MutableSharedFlow<Int>(replay = 0) // Một dạng hot flow

    launch {
        for (i in 1..3) {
            delay(100) // Giả lập thời gian phát dữ liệu
            println("Emitting $i")
            hotFlow.emit(i)
        }
    }

    delay(150) // Collector 1 tham gia sau một thời gian
    println("Collector 1 subscribing")
    hotFlow.collect { value ->
        println("Collector 1: $value")
    }
}

// Output:
Emitting 1
Emitting 2
Collector 1 subscribing
Collector 1: 2
Collector 1: 3
```

- `hotFlow` phát giá trị ngay lập tức.
- `Collector 1` chỉ nhận được giá trị từ thời điểm nó tham gia (2 và 3), bỏ qua giá trị trước đó (1).

### 2. Flow vs StateFlow vs SharedFlow

#### 1. Flow

- Là Cold Flow, chỉ phát dữ liệu khi có collector đăng ký.
- Mỗi collector có một luồng riêng biệt.
- Không duy trì trạng thái giữa các collector.
- Không hỗ trợ phát lại giá trị (replay).

#### 2. StateFlow

- Là một Hot Flow, luôn phát giá trị mới nhất.
- Chỉ giữ duy nhất một trạng thái (state) tại một thời điểm.
- Mỗi collector mới sẽ luôn nhận giá trị hiện tại ngay lập tức.
- Không hỗ trợ phát lại nhiều giá trị.

```java
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

fun main() = runBlocking {
    val stateFlow = MutableStateFlow(0)

    launch {
        for (i in 1..3) {
            delay(100)
            stateFlow.value = i
        }
    }

    delay(150)
    println("Collector subscribing")
    stateFlow.collect { println("StateFlow: $it") }
}

// Output:
Collector subscribing
StateFlow: 3
```

- Vì collector tham gia muộn, nó không nhận các giá trị trước đó (1 và 2).
- Khi collector bắt đầu thu thập (collect), nó nhận ngay giá trị hiện tại mới nhất, là 3.

#### 3. SharedFlow

- Là một Hot Flow, không giữ trạng thái.
- Hỗ trợ phát lại (replay) một số giá trị gần nhất.
- Các collector chỉ nhận giá trị từ thời điểm tham gia (hoặc từ giá trị replay nếu có).
- Có thể phát giá trị tới nhiều collector.

```java
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

fun main() = runBlocking {
    val sharedFlow = MutableSharedFlow<Int>(replay = 1)

    launch {
        for (i in 1..3) {
            delay(100)
            sharedFlow.emit(i)
        }
    }

    delay(150)
    println("Collector subscribing")
    sharedFlow.collect { println("SharedFlow: $it") }
}

// Output:
Collector subscribing
SharedFlow: 2
SharedFlow: 3
```

- `MutableSharedFlow<Int>(replay = 1)`: thiết lập replay 1 giá trị gần nhất tới bất kỳ collector nào tham gia sau khi giá trị đó được phát.

#### 4. So sánh

| **Thuộc tính**            | **Flow**                  | **StateFlow**          | **SharedFlow**                |
| ------------------------- | ------------------------- | ---------------------- | ----------------------------- |
| **Loại Flow**             | Cold Flow                 | Hot Flow               | Hot Flow                      |
| **Giá trị khởi tạo**      | Không                     | Bắt buộc               | Không                         |
| **Phát giá trị khi nào?** | Khi có collector          | Khi giá trị thay đổi   | Khi có emit                   |
| **Phát lại (replay)**     | Không                     | Không                  | Có, tùy chỉnh replay          |
| **Giữ trạng thái**        | Không                     | Có                     | Không                         |
| **Collector nhận được**   | Tất cả giá trị từ đầu     | Giá trị hiện tại       | Giá trị từ thời điểm tham gia |
| **Use Case phổ biến**     | Xử lý dữ liệu bất đồng bộ | Theo dõi trạng thái UI | Phát sự kiện nhiều nơi        |

## IV. So sánh với LiveData

### 1. Khi nào nên sử dụng Flow?

- Dữ liệu đến từ nguồn bất đồng bộ như Room, API, hoặc sensors.
- Dữ liệu không phụ thuộc vào vòng đời (lifecycle) của Android Components.
- Cần thao tác mạnh mẽ trên luồng dữ liệu với các toán tử như map, flatMap, combine,...

### 2. Khi nào nên sử dụng LiveData?

- Khi dữ liệu cần gắn liền với Lifecycle (như Fragment, Activity).
- Cần cập nhật UI một cách tự động và an toàn khi Lifecycle thay đổi.
- Dữ liệu không thay đổi liên tục hoặc không yêu cầu các thao tác phức tạp trên luồng dữ liệu.
