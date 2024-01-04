#!/usr/bin/env bash

./gradlew clean

./gradlew build


echo 'Copy files...'

scp -i ~/.ssh/id_rsa \
  /home/dmitrii/Studies/Проект/biletka/build/libs/biletka-0.0.1-SNAPSHOT.jar \
  root@185.20.224.98:/home/dmitrii/

echo 'Restart server...'

ssh -i ~/.ssh/id_rsa root@185.20.224.98 << EOF

cd ..

cd home

cd dmitrii

pgrep java | xargs  kill -9

nohup java -jar biletka-0.0.1-SNAPSHOT.jar &

EOF

echo 'Bye!'

