# channel

- https://github.com/PacktPublishing/Hands-On-Microservices-with-Spring-Boot-and-Spring-Cloud

## create projects
```cmd
./create-projects.bash
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