gnome-terminal --title=$(basename "$0") -e 'bash -c "

cd /opt/kafka
bin/zookeeper-server-start.sh config/zookeeper.properties

exec bash"'
