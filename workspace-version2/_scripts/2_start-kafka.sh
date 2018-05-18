gnome-terminal --title=$(basename "$0") -e 'bash -c "

cd /opt/kafka
bin/kafka-server-start.sh config/server.properties

exec bash"'
