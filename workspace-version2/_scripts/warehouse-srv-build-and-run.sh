gnome-terminal --title=$(basename "$0") -e 'bash -c "

name=WarehouseSrv

cd ../$name

mvn clean install

java -jar target/$name-1.0.0.jar server config.yml

exec bash"'
