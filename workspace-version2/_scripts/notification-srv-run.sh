gnome-terminal --title=$(basename "$0") -e 'bash -c "

name=NotificationSrv

cd ../$name

java -jar target/$name-1.0.0.jar server config.yml

exec bash"'
