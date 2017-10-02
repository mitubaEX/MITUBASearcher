docker-compose up -d
sleep 5s

if [ $1 = "crawler" ]; then
    git submodule foreach git pull origin master
    docker exec -it crawler bash -c "cd /app && ./gradlew build && java -jar ./build/libs/crawlerOfJava.jar"
    docker exec -it solr bin/solr create -c 2gram
    docker exec -it solr bin/solr create -c 3gram
    docker exec -it solr bin/solr create -c 4gram
    docker exec -it solr bin/solr create -c 5gram
    docker exec -it solr bin/solr create -c 6gram
    docker exec -it solr bin/solr create -c uc
    docker exec -it solr bin/post -c 2gram /opt/solr/json/2-gram.json
    docker exec -it solr bin/post -c 3gram /opt/solr/json/3-gram.json
    docker exec -it solr bin/post -c 4gram /opt/solr/json/4-gram.json
    docker exec -it solr bin/post -c 5gram /opt/solr/json/5-gram.json
    docker exec -it solr bin/post -c 6gram /opt/solr/json/6-gram.json
    docker exec -it solr bin/post -c uc /opt/solr/json/uc.json
else
    docker exec --user root -it solr bash -c "apt-get update && apt-get install -y git maven default-jdk python-dev && git clone https://github.com/tamada/pochi.git && cd ./pochi && mvn package && cd ../data && bash ./script.sh"
    docker exec --user root -it solr bash -c "cd ./data && for i in 2\-gram 3\-gram 4\-gram 5\-gram 6\-gram uc ; do echo `pwd` && python birthmark_xml_create.py $i ;done"
    docker exec -it solr bin/solr create -c 2gram
    docker exec -it solr bin/solr create -c 3gram
    docker exec -it solr bin/solr create -c 4gram
    docker exec -it solr bin/solr create -c 5gram
    docker exec -it solr bin/solr create -c 6gram
    docker exec -it solr bin/solr create -c uc
    docker exec -it solr bin/post -c 2gram /opt/solr/data/birth_2gram.xml
    docker exec -it solr bin/post -c 3gram /opt/solr/data/birth_3gram.xml
    docker exec -it solr bin/post -c 4gram /opt/solr/data/birth_4gram.xml
    docker exec -it solr bin/post -c 5gram /opt/solr/data/birth_5gram.xml
    docker exec -it solr bin/post -c 6gram /opt/solr/data/birth_6gram.xml
    docker exec -it solr bin/post -c uc /opt/solr/data/birth_uc.xml
fi
