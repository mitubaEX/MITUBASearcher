for i in 2-gram 3-gram 4-gram 5-gram 6-gram uc ; do find . -name "*.jar" | while read file ; do java -jar ../pochi/pochi-runner/target/pochi-runner-1.0-SNAPSHOT.jar ./extract.js $file $i ;done ; done
for i in 2-gram 3-gram 4-gram 5-gram 6-gram uc ; do find . -name "*.class" | while read file ; do java -jar ../pochi/pochi-runner/target/pochi-runner-1.0-SNAPSHOT.jar ./extract.js $file $i ;done ; done
