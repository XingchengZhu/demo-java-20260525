# ---- Build stage ----
FROM maven:3-eclipse-temurin-21-alpine AS builder
WORKDIR /build

# 先拉依赖层（利用 Docker 缓存）
COPY pom.xml ./
RUN mvn dependency:go-offline -B

# 编译并打包
COPY src ./src
RUN mvn package -DskipTests -B

# ---- Runtime stage ----
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# 非 root 用户，更安全
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

COPY --from=builder /build/target/*.jar app.jar

EXPOSE 8080

USER appuser
ENTRYPOINT ["java", "-jar", "app.jar"]
