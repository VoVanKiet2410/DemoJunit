# Sử dụng base image của OpenJDK phù hợp với phiên bản JDK bạn đang dùng (ví dụ: 17)
FROM openjdk:17-jdk-alpine

# Tạo volume cho dữ liệu tạm thời (nếu cần)
VOLUME /tmp

# Copy file JAR vừa build vào image, đổi tên thành app.jar
COPY target/DemoJunit-0.0.1-SNAPSHOT.jar app.jar

# Cấu hình entrypoint để chạy ứng dụng
ENTRYPOINT ["java", "-jar", "/app.jar"]
