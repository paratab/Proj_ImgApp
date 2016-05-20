
	

<?php
	$mode = "clue";
	$id = "75";
	$ext = "notice1463157545.ext";
	
	$cmd = "start php -f C:/inetpub/wwwroot/test2.php mode=" . $mode . " id=" . $id . " ext=" . $ext;
	echo $cmd . PHP_EOL;

   	echo "Start : " . date("Y-m-d",time()) . '-' . date("h:i:sa").PHP_EOL;
	pclose(popen($cmd, 'r'));
    echo "end : " . date("Y-m-d",time()) . '-' . date("h:i:sa").PHP_EOL;
?>