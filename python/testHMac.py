#-*- coding:utf-8 -*-
"""
    test HMac_SHA
"""

import urllib
import time
import base64
import hashlib
import hmac
import httplib
import json

def testHMacSHA(secretkey,message):
    print "secretkey=",secretkey
    print "message=",message
    s = hmac.new(secretkey, message, digestmod=hashlib.sha1).hexdigest()
    print "hex=",s

if __name__ == "__main__":
    testHMacSHA("HF94bR940e1d9YZwfgickG5HR07SFJQGscgO+E3vFPQGwSzyGtUQLxIh6blv",
        "apikey=5999a1ce-4312-8a3c-75a5-327c-f5cf5251&appid=11040&secretkey=HF94bR940e1d9YZwfgickG5HR07SFJQGscgO%2BE3vFPQGwSzyGtUQLxIh6blv&timestamp=1414142919")
