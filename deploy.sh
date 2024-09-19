#!/bin/bash

# Verifica que se pase el argumento (nombre de usuario)
if [ -z "$1" ]; then
  echo "Por favor, proporciona el nombre de usuario de pampero como parametro"
  exit 1
fi

pampero_user=$1
mvn package || { echo "Error durante mvn package"; exit 1; }
scp webapp/target/webapp.war $pampero_user@pampero.itba.edu.ar:/home/$pampero_user/webapp/target || { echo "Error durante SCP a pampero"; exit 1; }
ssh $pampero_user@pampero.itba.edu.ar
sftp paw-2024b-09@10.16.1.110
put webapp/target/webapp.war /web/app.war
