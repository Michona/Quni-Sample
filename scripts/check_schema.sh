#!/bin/bash

touch remote_schema.json

# Must be run from authorized ip adresses
./gradlew :apolloservice:downloadApolloSchema \
  --endpoint="https://icanhazdadjoke.com/graphql" \
  --schema=remote_schema.json \
  --variant prodDebug \
  --service service


# Path to the local schema used.
LOCAL_SCHEMA="./apolloservice/src/main/graphql/com/quni/apolloservice/schema.json"

# Utils
green=$(tput setaf 10)
normal=$(tput sgr0)

# We are using beta endpoint
if cmp -s "$LOCAL_SCHEMA" remote_schema.json; then
    echo "${green}Success:${normal} Local schema is up to date!"
else
    rm remote_schema.json
    exit 1
fi

rm remote_schema.json
exit 0
