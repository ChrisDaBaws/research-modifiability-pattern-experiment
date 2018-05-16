gnome-terminal -e 'bash -c "

name=OrderSrv

cd ../$name

mvn clean install

java -jar target/$name-1.0.0.jar server config.yml

exec bash"'
