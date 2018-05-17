gnome-terminal -e 'bash -c "

name=ProductSrv

cd ../$name

java -jar target/$name-1.0.0.jar server config.yml

exec bash"'
