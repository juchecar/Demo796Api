#-*- coding:utf-8 -*-
"""
    796 API Trading Example/DEMO in Python
"""

import urllib
import time
import base64
import hashlib
import hmac
import httplib
import json

def get_796_token(appid,apikey,secretkey):
    timestamp = time.time()#"1414142919" #time.time()
    params = {"apikey": apikey, "appid": appid, "secretkey": secretkey, "timestamp": str(timestamp)}
    params = sorted(params.iteritems(), key=lambda d: d[0], reverse=False)
    message = urllib.urlencode(params)
    print "secretkey=",secretkey
    print "message=",message
    s = hmac.new(secretkey, message, digestmod=hashlib.sha1).hexdigest()
    print "hex=",s
    sig = base64.b64encode(s)
    print "sig=",sig

    payload = urllib.urlencode({'appid': appid, 'apikey': apikey, 'timestamp': timestamp, 'sig': sig})

    c = httplib.HTTPSConnection('796.com')
    c.request("GET", "/oauth/token?"+payload)
    r = c.getresponse()

    if r.status == 200:
        data = r.read()
        jsonDict = json.loads(data);
        errno = jsonDict['errno']
        if errno=="0":
            return jsonDict['data']['access_token']
    return None

if __name__ == "__main__":
    access_token = get_796_token(appid = '##YOUR APPID##',apikey='##YOUR APIKEY##',secretkey='##YOUR SECRETKEY##')
    print "access_token=",access_token
