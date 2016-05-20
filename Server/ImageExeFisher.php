<?php
	include 'Logging.php';

	parse_str(implode('&', array_slice($argv, 1)), $_GET);

	$mode = $_GET["mode"];
	$id = $_GET["id"];
	$ext = $_GET["ext"];
	$typeOfDatabase = $_GET["type"];

	$command = 'createFisher.exe '. $id . ' ' . $typeOfDatabase;

	logging("Fisher Create DB", $command);

	exec($command);

	$cmd = "Fisher.exe " . $id . " " . $mode . " " . $ext;

	logging("Fisher FaceRecog", $cmd);
	
	exec($cmd);
	
	
?>