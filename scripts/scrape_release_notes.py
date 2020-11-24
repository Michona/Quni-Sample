#!/usr/bin/env python

import yaml
import os
from distutils.version import StrictVersion

with open(os.environ['CHANGELOG_PATH'], 'r') as stream:
    changelog_dict = yaml.safe_load(stream)
    changelog_keys = changelog_dict.keys()
    changelog_keys.sort(key=StrictVersion)
    changelog_keys.reverse()

    latest_release_data = changelog_dict[changelog_keys[0]]
    latest_release_version = changelog_keys[0]

    release_version_name_file = open(os.environ['VERSION_NAME_PATH'], 'w')
    release_version_name_file.write(latest_release_version)
    release_version_name_file.close()

    release_notes_file = open(os.environ['RELEASE_NOTES_PATH'], 'w')

    release_notes_file.write("Features: \n")
    for feature in latest_release_data['features']:
        release_notes_file.write("- {}".format(feature))
        release_notes_file.write('\n')

    if latest_release_data['bug-fixes'][0] != "None":
        release_notes_file.write("Bug fixes: \n")
        for feature in latest_release_data['bug-fixes']:
            release_notes_file.write("- {}".format(feature))
            release_notes_file.write('\n')

    if latest_release_data['changes'][0] != "None":
        release_notes_file.write("Changes: \n")
        for feature in latest_release_data['changes']:
            release_notes_file.write("- {}".format(feature))
            release_notes_file.write('\n')

    release_notes_file.close()
