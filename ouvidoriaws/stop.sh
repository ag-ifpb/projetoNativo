# PARANDO CONTAINERS
docker-compose down

# REMOVENDO IMAGENS
docker rmi -f natarajan/db-ouvidoria
docker rmi -f natarajan/ws-ouvidoria

# REMOVENDO VOLUMES
docker volume remove ouvidoriaws_postgres-volume-ouvidoria
