<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<meta charset="utf-8">
<link rel="stylesheet" href="ladybird.css">
<title>Ladybird's Gift</title>
</head>
<body>
<article>
	<section>
<?php

require __DIR__.'/init.php';

if(!file_exists($db_file)) {
?>

		<p>No hiscores</p>

<?php
} else {

	$round = isset($_REQUEST['round'])? intval($_REQUEST['round']) : 1;
?>
<h3>Hiscores</h3>
<table>
	<tr>
	<th style="width:14%">Rank</th>
	<th style="width:16%">Score</th>
	<th style="width:50%">Name</th>
	<th style="width:20%">Time</th>
	</tr>
<?php

	$db = new SQLite3($db_file);
	$result = $db->query("SELECT name,time,score FROM scores WHERE round={$round} ORDER BY score DESC,time ASC LIMIT 20");

	for($i=1; $i<=20; ++$i) {
		if($result && $row=$result->fetchArray()) {
			list($nm,$tm,$sc) = $row;
			$n = intval(ceil($tm/FPS));
			$s = $n%60;
			$m = intval($n/60)%60;
			$h = intval($n/3600);
			$tm = ($h>0? $h.':' : '').($m>0? ($m>=10? $m : '0'.$m) : '00').':'.($s>=10? $s : '0'.$s);
		} else {
			$nm = '-';
			$tm = '-';
			$sc = '-';
			$result = false;
		}
?>	<tr>
	<td><?= $i ?></td>
	<td><?= $sc ?></td>
	<td><?= $nm ?></td>
	<td><?= $tm ?></td>
	</tr>
<?php
	}

	$db->close();

?>
</table>

<p>Rounds:
<a href="hiscores.php">1</a>, 
<a href="hiscores.php?round=2">2</a>, 
<a href="hiscores.php?round=3">3</a>, 
<a href="hiscores.php?round=4">4</a>, 
<a href="hiscores.php?round=5">5</a>, 
<a href="hiscores.php?round=6">6</a>, 
<a href="hiscores.php?round=7">7</a>, 
<a href="hiscores.php?round=8">8</a>, 
<a href="hiscores.php?round=9">9</a>, 
<a href="hiscores.php?round=10">10</a>
</p>
<?php
}

?>
</section>
</article>
</body>
</html>
	