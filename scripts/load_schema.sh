#!/bin/bash

# Must be run from authorized ip adresses
# Saves the schema from api to our local schema file
# Call it from base dir
# sh ./scripts/load_schema.sh
./gradlew :apolloservice:downloadApolloSchema \
  --endpoint="https://icanhazdadjoke.com/graphql" \
  --schema="./apolloservice/src/main/graphql/com/quni/apolloservice/schema.json" \
  --variant betaDebug \
  --service service
