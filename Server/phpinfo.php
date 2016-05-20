<?php 
 	
 	//include 'ImageExeCalling.php';

 	for($i = 1 ; $i<=81 ; $i++){
 	  	$command = 'createFisher.exe '. $i . ' ' . "clue";
		exec($command);
 	}


?>

