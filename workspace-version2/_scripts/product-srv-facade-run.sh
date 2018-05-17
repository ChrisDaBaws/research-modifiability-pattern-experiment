gnome-terminal -e 'bash -c "

name=ProductSrvFacade

cd ../$name

java -jar target/$name-1.0.0.jar server config.yml

exec bash"'
