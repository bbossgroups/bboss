<?php
header('Content-Type: text/html; charset=utf-8');
$magicQ=get_magic_quotes_gpc();
echo '<div>';
for($i=1;$i<10;$i++)
{
	if(isset($_POST['elm'.$i]))
	{
		if($magicQ)$_POST['elm'.$i]=stripslashes($_POST['elm'.$i]);
		echo '<textarea rows="10" cols="50">'.htmlspecialchars($_POST['elm'.$i]).'</textarea>';
	}
}
echo '</div><p style="text-align:center;"><br /><input type="button" value="点击后退" onclick="javascript:history.back();" /></p>';
?>