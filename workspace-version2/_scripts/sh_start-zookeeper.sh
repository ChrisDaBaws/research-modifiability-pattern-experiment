gnome-terminal -e 'bash -c "

cd C:/dev/apache-kafka
bin/zookeeper-server-start.sh config/zookeeper.properties

exec bash"'
