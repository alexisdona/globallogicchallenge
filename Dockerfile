FROM postgres:latest

ENV POSTGRES_DB default
ENV POSTGRES_USER alexis
ENV POSTGRES_PASSWORD 1234

COPY sql/init.sql /docker-entrypoint-initdb.d/
