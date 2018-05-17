gnome-terminal -e 'bash -c "

cd /opt/kafka
bin/zookeeper-server-start.sh config/zookeeper.properties

exec bash"'
