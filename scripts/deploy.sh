#!/bin/bash
REPOSITORY=/home/ubuntu/app/step2
PROJECT_NAME=bobs
echo "> Build 파일 복사"
cp $REPOSITORY/zip/*.jar $REPOSITORY/
echo "> 8080포트 kill"
fuser -k -n tcp 8080
sleep 5
echo "> 새 애플리케이션 배포"
JAR_NAME=$(ls -tr $REPOSITORY/*.jar | tail -n 1)
echo "> JAR Name: $JAR_NAME"
echo "> $JAR_NAME 에 실행권한 추가"
chmod +x $JAR_NAME 
echo "> $JAR_NAME 실행"
nohup java -jar \
 -Dspring.config.location=/home/ubuntu/app/application.properties\
 -Dspring.profiles.active=real \
 $JAR_NAME > $REPOSITORY/nohup.out 2>&1 &
