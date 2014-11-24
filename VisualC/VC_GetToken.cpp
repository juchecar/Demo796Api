
/*
  796 API Trading Example/DEMO in Visual C++
  Authored by a 796 trader.
  You can contact the author directly through Tencent QQ if you have any question.
  QQ ID: 2944302.
  796 is not responsible for the integrity of this piece of code.
  If you are interested in joining the discussion of the 796 API set up by the company itself, feel free to join the QQ Chat Group.
  The Chat Group ID is 223436446.
*/

CString CoinService::GetToken(const char* szAppKey,const char* szAppId,char* szSecretKey)
{
	time_t t = time(0);
	CString strPostFullParam;
	strPostFullParam.Format("apikey=%s&appid=%s&secretkey=%s&timestamp=%d",
		szAppKey,szAppId,PubService::HtmlEncode(szSecretKey).c_str(),(long)t);
	char  sha1Str[21] = "";
	SHA1_Encode(szSecretKey,strlen(szSecretKey),strPostFullParam.GetBuffer(0),strlen(strPostFullParam),sha1Str,sizeof(sha1Str));

	CString strHaValue;
	for (int i = 0;i < 20;i++)
	{
		CString strByte;
		strByte.Format("%02x",(unsigned char)sha1Str[i]);
		strHaValue = strHaValue + strByte;
	}
	char szSign[64];
	memset(szSign,0,64);
	sprintf(szSign,"%s",strHaValue);
	TCHAR szSig[64] = {0};
	BASE64_Encode((BYTE*)szSign,strlen(szSign),szSig);

	CString strRead,strUrl;
	strUrl.Format("https://796.com/oauth/token?apikey=%s&appid=%s&timestamp=%d&sig=%s",
		PubService::HtmlEncode(szAppKey).c_str(),szAppId,(long)t,
		PubService::HtmlEncode(szSig).c_str());
	strRead = PubService::Srv_GetWeb(strUrl).c_str();
	Json::Reader reader;
	Json::Value valRet;
	CString strValue;
	strValue.Format("%s",PubService::Utf8ToAnsi(strRead).c_str());

	if (reader.parse(strValue.GetBuffer(0),valRet))
	{
		if (lstrcmp(valRet["errno"].asCString(),"0") == 0)
		{
			return valRet["data"]["access_token"].asCString();
		}
	}
	return "";
}