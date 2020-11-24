#!/usr/bin/env bash

touch release_version_name.txt
touch release_notes.txt

python /bitrise/src/scripts/scrape_release_notes.py
