#-*- coding:utf-8 -*-

"""
    796 API Trading Example/DEMO in Python

    After getToken.

"""

import urllib2
import time
import base64
import hashlib
import hmac
import httplib
import json

from getToken import get_796_token


def getUserInfo(sAccessToken):
    sUrl = "/v1/user/get_info?access_token=%s" % (sAccessToken)
    c = httplib.HTTPSConnection('796.com')
    c.request("GET", sUrl)
    r = c.getresponse()
    print "r.status=",r.status
    print r.read()

def getUserInfo1(sAccessToken):
    sUrl = "https://796.com/v1/user/get_info?access_token=%s" % (sAccessToken)
    response = urllib2.urlopen(sUrl)
    print response.read()

def getUserInfo2(sAccessToken):
    import requests
    sUrl = "https://796.com/v1/user/get_info?access_token=%s" % (sAccessToken)
    response = requests.get(sUrl, timeout=20)
    print response.content

def getUserInfoError(sAccessToken):
    """
        May be return {u'msg': u'Access_token repealed', u'errno': u'-102', u'data': []}
    """
    import urllib
    payload = urllib.urlencode({'access_token': sAccessToken})
    c = httplib.HTTPSConnection('796.com')
    c.request("GET", "/v1/user/get_info?"+payload)
    r = c.getresponse()
    data = r.read()
    jsonDict = json.loads(data);
    print jsonDict

if __name__ == "__main__":
    access_token = get_796_token(appid = '##YOUR APPID##',apikey='##YOUR APIKEY##',secretkey='##YOUR SECRETKEY##')
    print "access_token=",access_token

    getUserInfo(access_token)
    getUserInfo1(access_token)
    getUserInfo2(access_token)
    getUserInfoError(access_token)
