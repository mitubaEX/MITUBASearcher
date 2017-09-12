cd /app
# yum -y install git
apt-get install git
git clone https://github.com/tamada/pochi.git
cd pochi/
# sudo wget http://repos.fedorapeople.org/repos/dchen/apache-maven/epel-apache-maven.repo -O /etc/yum.repos.d/epel-apache-maven.repo
# sudo sed -i s/\$releasever/6/g /etc/yum.repos.d/epel-apache-maven.repo
# sudo yum install -y apache-maven
apt-get install maven -y
mvn install
cd /app
sbt run
