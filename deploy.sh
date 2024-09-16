#!/bin/bash

# Verifica que se pase el argumento (nombre de usuario)
if [ -z "$1" ]; then
  echo "Por favor, proporciona el nombre de usuario de pampero como parametro"
  exit 1
fi

# Asigna el argumento a una variable
pampero_user=$1

# Ejecuta Maven para compilar y empaquetar la aplicación
echo "Empaquetando la aplicación con Maven..."
mvn package || { echo "Error durante mvn package"; exit 1; }

# Copia el archivo WAR al servidor pampero usando SCP
echo "Subiendo archivo WAR a pampero.itba.edu.ar..."
scp webapp/target/webapp.war $pampero_user@pampero.itba.edu.ar:/home/$pampero_user || { echo "Error durante SCP a pampero"; exit 1; }

echo "Estableciendo conexión con servidor..."
ssh $pampero_user@pampero.itba.edu.ar

# Sube el archivo WAR al servidor a través de SFTP
echo "Subiendo archivo WAR a 10.16.1.110 vía SFTP..."
sftp paw-2024b-09@10.16.1.110

put webapp/target/webapp.war web/app.war

echo "Despliegue completado con éxito."
