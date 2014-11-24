using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using System.Security.Cryptography;


namespace WindowsFormsApplication1
{
    public partial class Form1 : Form
    {
        public Form1()
        {
            InitializeComponent();
        }


        private void button1_Click(object sender, EventArgs e)
        {
           textBox1.Text= Func.getaccesstoken("##YOUR APPID##","##YOUR APIKEY##","##YOUR SECRETKEY##");
        }
    }
}

static class Func
{
    public static string get(string url)
    {
        try
        {
            System.Net.HttpWebRequest httpWebRequest = (System.Net.HttpWebRequest)System.Net.HttpWebRequest.Create(url);
            System.Net.HttpWebResponse httpWebResponse = (System.Net.HttpWebResponse)httpWebRequest.GetResponse();

            System.IO.Stream stream = httpWebResponse.GetResponseStream();
            System.IO.StreamReader streamReader = new System.IO.StreamReader(stream, System.Text.Encoding.Default);
            string result = streamReader.ReadToEnd();
            httpWebResponse.Close();
            return result;
        }
        catch
        {
            return "error";
        }
    }


    public static string getaccesstoken(string appid, string apikey, string secretkey)
    {
        string timestamp = GenerateTimeStamp();
        string param_uri = "apikey=" + apikey + "&appid=" + appid + "&secretkey=" + UrlEncode(secretkey) + "&timestamp=" + timestamp;
        string sig = hash_hmac(param_uri, secretkey);

        string token_url = "https://796.com/oauth/token?appid=" + appid + "&timestamp=" + timestamp + "&apikey=" + apikey + "&sig=" + sig;
        string jsonText = @get(token_url);
        /*
        AccessToken accesstoken = JsonConvert.DeserializeObject<AccessToken>(jsonText);
        string tokensecret = accesstoken.data.access_token;
        return tokensecret;
        */
        return jsonText;
    }


    public static string hash_hmac(string signatureString, string secretKey)
    {
        HMACSHA1 hmac = new HMACSHA1(Encoding.UTF8.GetBytes(secretKey));
        byte[] buffer = Encoding.UTF8.GetBytes(signatureString);
        string result = BitConverter.ToString(hmac.ComputeHash(buffer)).Replace("-", "").ToLower();
        return Convert.ToBase64String(Encoding.UTF8.GetBytes(result));
    }


    public static string GenerateTimeStamp()
    {
        TimeSpan ts = DateTime.UtcNow - new DateTime(1970, 1, 1, 0, 0, 0, 0);
        return Convert.ToInt64(ts.TotalSeconds).ToString();
    }


    public static string UrlEncode(string value)
    {
        StringBuilder result = new StringBuilder();
        string unreservedChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_.~";

        foreach (char symbol in value)
        {
            if (unreservedChars.IndexOf(symbol) != -1)
            {
                result.Append(symbol);
            }
            else
            {
                result.Append('%' + String.Format("{0:X2}", (int)symbol));
            }
        }

        return result.ToString();
    }
}