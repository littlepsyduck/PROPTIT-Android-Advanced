# 006_service

## Lý thuyết

- Service
  - Khái niệm về Service. Các loại Service, khi nào sử dụng các loại đó
  - Lifecycle của Service
  - Phân biệt startService() và bindService()
- Broadcast Receiver
  - Khái niệm về Broadcast Receiver. Phân biệt giữa Ordered Broadcast và Normal Broadcast, Implicit Broadcasts và Explicit Broadcasts. Khi nào sử dụng
  - Khai báo và xin cấp các quyền trong Android giữa các API Level khác nhau
  - Cách đăng ký sử dụng Broadcast Receiver

## Bài tập

- Áp dụng vào Chat App: Kết hợp Service, Broadcast receiver, Firebase Cloud Messaging (FCM) cho các tính năng:
  - Nhận thông báo cho tin nhắn mới đến
  - Nhận biết trạng thái có/không kết nối mạng. Gửi lại tin nhắn chưa gửi do mất kết nối mạng khi có mạng trở lại.
  