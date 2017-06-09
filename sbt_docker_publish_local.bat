set DOCKER_CERT_PATH=%USERPROFILE%\.docker\machine\machines\default
set DOCKER_HOST=tcp://192.168.99.100:2376
set DOCKER_MACHINE_NAME=default
set DOCKER_TLS_VERIFY=1

sbt docker:publishLocal