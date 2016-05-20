<?php
	include 'NotificationCenter.php';
	include 'logging.php';

	parse_str(implode('&', array_slice($argv, 1)), $_GET);
	$mode = $_GET["mode"];
	$input = $_GET["input"];
	$result = $_GET["result"];

	// echo $mode . "\n";
	// echo $input . "\n";
	// echo $result . "\n";

	logging("ResultCompare", json_encode($_GET));

	//Log file
	$pathOfLog = "C:/data/log/";
	$t = time();
	$logName = 'CompareLog-' . date("Y-m-d",$t) . '.txt';
	$logText = date("Y-m-d",$t) . '-' . date("h:i:sa") . " " .json_encode($_GET) . PHP_EOL;

	file_put_contents($pathOfLog . $logName, $logText, FILE_APPEND);

	if(strcmp($result,"") != 0){

		if(strcmp($mode, "NoticeFindClue") == 0){
			$notice_id = $input;
			$clue_id = $result;

			$clue_list = explode(",", $clue_id);

			for ($i = 0; $i < sizeof($notice_list); $i++) { 
				logging("ResultGCM", prepareNotificationOneClueOneNotice($clue_list[$i],$notice_id));
			}
		}else if(strcmp($mode, "ClueFindNotice") == 0){
			$clue_id = $input;
			$notice_id = $result;

			logging("ResultGCM", prepareNotificationOneClueManyNotice($clue_id,$notice_id));		
		}else if(strcmp($mode, "DirectClueToNotice") == 0){
			$clue_id = $input;
			$notice_id = $result;

			logging("ResultGCM", prepareNotificationOneClueOneNotice($clue_id, $notice_id));	
		}

	}

	exit;

?>