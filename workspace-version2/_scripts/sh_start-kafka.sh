gnome-terminal -e 'bash -c "

cd /opt/kafka
bin/kafka-server-start.sh config/server.properties

exec bash"'
