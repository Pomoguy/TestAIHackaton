# ===== Build stage =====
FROM eclipse-temurin:22-jdk-alpine AS build
WORKDIR /app
COPY src ./src
RUN javac -d /app/out /app/src/Server.java

# ===== Runtime stage =====
FROM eclipse-temurin:22-jre-alpine AS runner
WORKDIR /app
ENV PORT=8080
COPY --from=build /app/out /app
# Непривилегированный пользователь
RUN addgroup -S app && adduser -S app -G app
USER app

EXPOSE 8080
HEALTHCHECK --interval=30s --timeout=3s --start-period=10s \
    CMD wget -qO- http://127.0.0.1:${PORT}/health || exit 1

CMD ["java", "Server"]
