<!-- <script lang="javascript" src="/bboss-mvc/jsp/wordpdf/loadword.js"/> -->
<OBJECT id="DSOFramer" name="DSOFramer" align='middle' style='LEFT: 0px; WIDTH: 100%; TOP: 0px; HEIGHT: 100%'
classid="clsid:00460182-9E5E-11D5-B7C8-B8269041DD57" codeBase="dsoframer.ocx#version=2,2,1,2" >
</OBJECT>


<script lang="javascript">
try
{
//document.getElementById("DSOFramer").HttpInit();
//document.DSOFramer.HttpInit();
//setTimeout(function() { document.getElementById("DSOFramer").Open("http://localhost:8081/bboss-mvc/wordpdf/getWord.htm", false, "Word.Document"); }, 2000);
setTimeout(function() { document.getElementById("DSOFramer").Open("http://localhost:8081/bboss-mvc/wordpdf/getWordTemp.htm", false, "Word.Document"); }, 50);

}
catch(e)
{
	alert(e.message);
	}

</script>  