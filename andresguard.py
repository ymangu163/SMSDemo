#!/usr/bin/python
# -*- coding: UTF-8 -*-

import os
import shutil

def deleteLastApks():
    apk_path = '/Users/all/AndroidStudioProjects/SMSDemo/app/build/outputs/apk/release/'
    if os.path.isdir(apk_path):
        shutil.rmtree(apk_path, ignore_errors=True)

deleteLastApks()
cmd_resguard = r'/Users/all/AndroidStudioProjects/SMSDemo/gradlew assembleRelease'
os.system(cmd_resguard)
