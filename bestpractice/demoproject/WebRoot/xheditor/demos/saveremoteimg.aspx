<%@ Page Language="C#" AutoEventWireup="true" CodePage="65001" %>
<%@ Import namespace="System" %>
<%@ Import namespace="System.Collections" %>
<%@ Import namespace="System.Configuration" %>
<%@ Import namespace="System.Data" %>
<%@ Import namespace="System.Web" %>
<%@ Import namespace="System.Web.Security" %>
<%@ Import namespace="System.Web.UI" %>
<%@ Import namespace="System.Web.UI.HtmlControls" %>
<%@ Import namespace="System.Web.UI.WebControls" %>
<%@ Import namespace="System.Web.UI.WebControls.WebParts" %>
<%@ Import namespace="System.IO" %>
<%@ Import namespace="System.Net" %>

<script runat="server">
// saveremoteimg demo for aspx
// @requires xhEditor
// 
// @author JEDIWOLF<jediwolf@gmail.com>
// @site http://xheditor.com/
// @licence LGPL(http://www.opensource.org/licenses/lgpl-license.php)
// 
// @Version: 0.9.1 (build 110703)
//
// 注：本程序仅为演示用，只实现了最简单的远程抓图及粘贴上传，如果要完善此功能，还需要自行开发以下功能：
//		1，非图片扩展名的URL地址抓取
//		2，大体积的图片转jpg格式，以及加水印等后续操作
//		3，上传记录存入数据库以管理用户上传图片
    
    
private string upExt = ",jpg,jpeg,gif,png,";  //上传扩展名
private string attachDir = "upload";        //上传文件保存路径，结尾不要带/
private int dirType = 1;                    // 1:按天存入目录 2:按月存入目录 3:按扩展名存目录  建议使用按天存
private int maxAttachSize = 2097152;        // 最大上传大小，默认是2M

protected void Page_Load(object sender, EventArgs e)
{
    Response.Charset = "UTF-8";
    
    string[] arrUrl = Request["urls"].Split('|');
    for (int i = 0; i < arrUrl.Length; i++)
    {
        string localUrl = saveRemoteImg(arrUrl[i]);
        if (localUrl != "")arrUrl[i] = localUrl;//有效图片替换
    }
    
    Response.Write(String.Join("|", arrUrl));
    Response.End();
}


string saveRemoteImg(string sUrl)
{
    byte[] fileContent;
    string objStream;
    string sExt;
    string sFile;
    if (sUrl.StartsWith("data:image"))
    {
        // base64编码的图片，可能出现在firefox粘贴，或者某些网站上，例如google图片
        int pstart = sUrl.IndexOf('/') + 1;
        sExt = sUrl.Substring(pstart, sUrl.IndexOf(';') - pstart).ToLower();
        
        if (upExt.IndexOf("," + sExt + ",")==-1) return "";

        fileContent = Convert.FromBase64String(sUrl.Substring(sUrl.IndexOf("base64,") + 7));
    }
    else
    {
        // 图片网址
        sExt = sUrl.Substring(sUrl.LastIndexOf('.') + 1).ToLower();

        if (upExt.IndexOf("," + sExt + ",") == -1) return "";

        fileContent = getUrl(sUrl);
    }

    if (fileContent.Length > maxAttachSize) return "";//超过最大上传大小忽略
	
	//有效图片保存
	sFile = getLocalPath(sExt);
	File.WriteAllBytes(Server.MapPath(sFile), fileContent);

	return sFile;
}

string getLocalPath(string extension)
{
    string attach_dir, attach_subdir, filename, target, tmpfile;
    switch (dirType)
    {
        case 1:
            attach_subdir = "day_" + DateTime.Now.ToString("yyMMdd");
            break;
        case 2:
            attach_subdir = "month_" + DateTime.Now.ToString("yyMM");
            break;
        default:
            attach_subdir = "ext_" + extension;
            break;
    }
    attach_dir = attachDir + "/" + attach_subdir + "/";

    if (!Directory.Exists(Server.MapPath(attach_dir)))
    {
        Directory.CreateDirectory(Server.MapPath(attach_dir));
    }
    filename = DateTime.Now.ToString("yyyyMMddHHmmssfff") + "." + extension;
    return attach_dir + filename;
}

byte[] getUrl(string sUrl)
{
    WebClient wc = new WebClient();
    try
    {
        return wc.DownloadData(sUrl);
    }
    catch 
    {
        return null;
    }
}
</script>
