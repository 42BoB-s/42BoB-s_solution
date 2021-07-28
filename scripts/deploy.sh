#!/bin/bash
REPOSITORY=/home/ubuntu/app/step2
PROJECT_NAME=bobs
echo "> Build 파일 복사"
cp $REPOSITORY/zip/*.jar $REPOSITORY/
echo "> 8080포트 kill"
sudo fuser -k 8080/tcp
sleep 3
echo "> 8443포트 kill"
sudo fuser -k 8443/tcp
sleep 3
echo "> 새 애플리케이션 배포"
JAR_NAME=$(ls -tr $REPOSITORY/*.jar | tail -n 1)
echo "> JAR Name: $JAR_NAME"
echo "> $JAR_NAME 에 실행권한 추가"
chmod +x $JAR_NAME 
echo "> $JAR_NAME 실행"
nohup java -jar \
 $JAR_NAME > $REPOSITORY/nohup.out 2>&1 &
