# 1. Java 21 (OpenJDK 22 SDK) 기반 이미지 사용
FROM openjdk:21-slim
# 2. 앱 실행을 위한 디렉토리 생성
WORKDIR /app
# 3. JAR 파일 복사
COPY build/libs/spvue-0.0.1-SNAPSHOT.jar app.jar
# 4. 컨테이너 실행 시 Java 애플리케이션 실행
CMD ["java", "-jar", "app.jar"]
# 5. 8080 포트 개방
EXPOSE 8080
