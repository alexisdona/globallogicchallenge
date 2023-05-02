# Proyecto de Spring Security con Spring JPA, securrity y PostgreSQL

Este proyecto es un ejemplo de cómo implementar Spring Security y Spring JPA en una aplicación web que registra usuarios y sus teléfonos en una base de datos PostgreSQL. 
La aplicación utiliza Docker para crear y administrar el contenedor de la base de datos PostgreSQL.

### Requisitos

Antes de comenzar, asegúrese de tener instalado lo siguiente:

* Docker
* Java 8 o superior

### Configuración

Para ejecutar la aplicación, siga estos pasos:

Clone este repositorio en su máquina local.
Abra una terminal y navegue hasta el directorio raíz del proyecto.
Ejecute el comando docker-compose up -d para crear y ejecutar el contenedor de la base de datos PostgreSQL.
Ejecute la aplicación utilizando su IDE de preferencia o mediante el comando ./gradlew bootRun.
Por [postman](https://www.postman.com/) puede operar con la aplicación sin problemas

Se exponen los siguientes endpoints

La aplicación levanta en localhost:8080

1. Regristro de usuario (ejemplo)

curl --location 'http://localhost:8080/api/v1/auth/register' \
--header 'Content-Type: application/json' \
--header 'Cookie: JSESSIONID=DBE6235FFB7A53D02EEE06422B3758E3' \
--data-raw '{
"name":"Alexis Do Nascimento",
"email":"alexis.dona@gmail.com",
"password": "s2ppp3Aabc",
"phones": [
{
"number": 1234567890,
"city_code": 11,
"country_code": "AR"
},
{
"number": 987654123,
"city_code": 11,
"country_code": "AR"
}
]
}'

2. Login de usuario (ejemplo)

curl --location 'http://localhost:8080/api/v1/auth/login' \
--header 'Content-Type: application/json' \
--header 'Cookie: JSESSIONID=DBE6235FFB7A53D02EEE06422B3758E3' \
--data-raw '{
"email":"alexis.dona@gmail.com",
"password": "s2ppp3Aabc"
}'

El response devuelve un Bearer token que debe ser usado para autenticarse en el tercer endpoint (de dominio)

3. Endpoint de respuesta corta para probar autenticación 

curl --location 'http://localhost:8080/api/v1/users' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhbGV4aXMuZG9uYUBnbWFpbC5jb20iLCJpYXQiOjE2ODMwNTY2MzcsImV4cCI6MTY4MzA1NzUzN30.mfnkZRQn5IORtqOV2X2ujxzBlJA7H7Op8ZqyIaYchr8' \
--header 'Cookie: JSESSIONID=DBE6235FFB7A53D02EEE06422B3758E3'

Abajo un pequeño diagrama de secuencia y de componentes básico de cómo está armada la lógica del authenticate y login usando JWT

Diagrama de secuencia
![](/home/alecho/Downloads/plantuml.svg)

Diagrama de componentes
![](/home/alecho/Downloads/components.svg)


