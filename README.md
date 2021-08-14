# channel

- https://github.com/PacktPublishing/Hands-On-Microservices-with-Spring-Boot-and-Spring-Cloud

## component
- 서비스 검색 : 넷플릭스 유레카 및 스프링 클라우드 로드 밸런서
- 에지 서버 : 스프링 클라우드 게이트웨이 및 스프링 시큐리티 OAuth
- 구성 중앙화 : 스프링 클라우드 컨피그 서버
- 서킷 브레이커 : Resillience4j
- 분산 추적 : 스프링 클라우드 슬루스 및 집킨

## create projects
```cmd
./create-projects.bash
```

## 인증서
```cmd
keytool -genkeypair -alias localhost -keyalg RSA -keysize 2048 -storetype PKCS12 -keystore edge.p12 -validity 3650
```

## local

### build
```cmd
./gradlew clean build
```

### run
```cmd
java -jar microservices/product-service/build/libs/*.jar &
```

### test
```cmd
./test-em-all.bash
```

## docker 

### build and run
```cmd
# gradle build를 안할 경우 수정내용이 반영이 안된다. (application.yml)
./gradlew clean build
docker-compose build
docker-compose up -d
```

### log
```cmd
docker-compose logs -f
docker-compose logs -f product review
```

### error
```cmd
# disk error
docker system prune -f --volumes

# restart service
docker-compose up -d --scale product=0
docker-compose up -d --scale product=1 
```

### test with docker
```cmd
./test-em-all.bash start stop
```

### check
- java SE 10 이상 사용
    - 그 이하 버전에서는 리소스 제한이 안됨.
    
## Continuous Integration

### build and test
```cmd
./gradlew clean build && docker-compose build && ./test-em-all.bash start stop
```