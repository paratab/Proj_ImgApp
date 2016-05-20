<?php

// callExeOfImageFindNotice("clue", "92", "notice1463328184.ext");
// callExeOfImageFindNotice("clue", "88", "notice1463328184.ext");

function createExtFile($type){

	$pathOfExt = "C:/data/ext/";
	$pathOfDatabase = "C:/data/database/";
	$t = time();

	$temp_id = array();

	$con = mysqli_connect("localhost","root","1212312121","proj4d");
	mysqli_set_charset($con, "utf8");

	
	$query = "SELECT id FROM " . $type . "_detail WHERE isValid = 1";
	$statement = mysqli_prepare($con, $query);
	$success = mysqli_stmt_execute($statement); 
	mysqli_stmt_store_result($statement);
   	mysqli_stmt_bind_result($statement, $id);

	$path = $pathOfExt . $type . $t . ".ext";
	$myfile = fopen($path, "w") or die("Unable to open file!");

	while(mysqli_stmt_fetch($statement)){
		array_push($temp_id , $id);
   	}

   	$i = 0;

   	for($i ; $i < sizeof($temp_id)-1 ; $i++){
   		$id = $temp_id[$i];
  		$txt = $pathOfDatabase . $type .  "/" . $id . "/1.png;" . $id . PHP_EOL;
		fwrite($myfile, $txt);
		$txt = $pathOfDatabase . $type .  "/" . $id . "/2.png;" . $id . PHP_EOL;
		fwrite($myfile, $txt);
   	}

   	$id = $temp_id[$i];
   	$txt = $pathOfDatabase . $type .  "/" . $id . "/1.png;" . $id . PHP_EOL;
	fwrite($myfile, $txt);
	$txt = $pathOfDatabase . $type .  "/" . $id . "/2.png;" . $id;
	fwrite($myfile, $txt);

   	fclose($myfile);

   	return $type . $t . ".ext";

}

function pushNotifiactionForDirectClueAdd($clue_id , $notice_id){
	$cmd = "start php -f C:/inetpub/wwwroot/PrepareResultOfFaceRecog.php mode=DirectClueToNotice input=" . $clue_id . " result=" . $notice_id;
	pclose(popen($cmd, 'r'));
}

function createDatabaseImageOnly($type , $id){
	$cmd = "start php -f C:/inetpub/wwwroot/ImageExeCreateDB.php type=" . $type . " id=" . $id;
	pclose(popen($cmd, 'r'));
}

function callExeOfImageFindNotice($typeOfDatabase, $clue_id, $nameOfExt){
	//clue match with all notice
	$mode = "clue";
    $cmd = "start php -f C:/inetpub/wwwroot/ImageExeFisher.php mode=" . $mode . " id=" . $clue_id . " ext=" . $nameOfExt . " type=" . $typeOfDatabase;
	pclose(popen($cmd, 'r'));

}

function callExeOfImageFindClue($typeOfDatabase, $notice_id, $nameOfExt){
	//notice match with all clue
	$mode = "notice";
    $cmd = "start php -f C:/inetpub/wwwroot/ImageExeFisher.php mode=" . $mode . " id=" . $notice_id . " ext=" . $nameOfExt . " type=" . $typeOfDatabase;
	pclose(popen($cmd, 'r'));
	
}

?>