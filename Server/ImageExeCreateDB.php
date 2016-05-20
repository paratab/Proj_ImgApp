<?php
	include 'Logging.php';

	parse_str(implode('&', array_slice($argv, 1)), $_GET);

	$type = $_GET["type"];
	$id = $_GET["id"];
	
	$command = 'createFisher.exe '. $id . ' ' . $type;
	
	exec($command);

	logging("Create Database", $cmd);
	
?>