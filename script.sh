for i in 2-gram 3-gram 4-gram 5-gram 6-gram uc ; do find . (-name "*.jar" -o -name "*.class") | while read file ; do java -jar ~/.m2/repository/com/github/pochi/pochi-runner/1.0-SNAPSHOT/pochi-runner-1.0-SNAPSHOT.jar ./extract.js $file $i ;done ; done
for i in 2-gram 3-gram 4-gram 5-gram 6-gram uc ; do python ./birthmark_xml_create.py $i  ;done

/root/solr-5.5.0/bin/solr start
for i in 2gram 3gram 4gram 5gram 6gram uc ; do /root/solr-5.5.0/bin/solr create -c $i ; done
for i in 2gram 3gram 4gram 5gram 6gram uc ; do /root/solr-5.5.0/bin/post -c "$i" /root/data/birth_"$i".xml; done

