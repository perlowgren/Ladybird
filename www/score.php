<?php

require __DIR__.'/init.php';

$name  = $_REQUEST['name'];
$round = intval($_REQUEST['round']);
$time  = intval($_REQUEST['time']);
$score = intval($_REQUEST['score']);

$name = strtr($name,"\n\r\t",' ');
if(mb_strlen($name)>12) $name = mb_substr($name,0,12);
$qname  = SQLite3::escapeString($name);
if(!$qname) $qname = '---';

$name = addslashes($name);

if(!file_exists($db_file)) {
	$table = <<<'SQL'
CREATE TABLE scores (
  id INTEGER PRIMARY KEY NOT NULL,
  name STRING,
  round INTEGER,
  time INTEGER,
  score INTEGER,
  rank INTEGER,
  created INTEGER
)
SQL;

	$db = new SQLite3($db_file);
	$db->exec($table);
	$db->exec('CREATE INDEX scores_round ON scores (round)');
	$db->exec('CREATE INDEX scores_score ON scores (score)');
	$db->close();
}

$db = new SQLite3($db_file);

$rank = $db->querySingle("SELECT count(*) FROM scores WHERE round={$round} AND score>={$score}",false)+1;
$created = time();
$db->exec("INSERT INTO scores (id,name,round,time,score,rank,created) VALUES (NULL,'{$qname}',{$round},{$time},{$score},{$rank},{$created})");

$hiscore = array('','','','');
$result = $db->query("SELECT name,time,score FROM scores WHERE round={$round} ORDER BY score DESC,time ASC LIMIT 4");
for($i=0; $row=$result->fetchArray(); ++$i) {
	list($nm,$tm,$sc) = $row;
	/*$n = intval(ceil($tm/FPS));
	$s = $n%60;
	$m = intval($n/60)%60;
	$h = intval($n/3600);
	$tm = ($h>0? $h.':' : '').($m>0? ($m>=10? $m : '0'.$m) : '00').':'.($s>=10? $s : '0'.$s);*/
	$hiscore[$i] = "{$nm}: {$sc}";
}

$db->close();

echo "{
	\"name\": \"{$name}\",
	\"rank\": {$rank},
	\"hiscore1\": \"{$hiscore[0]}\",
	\"hiscore2\": \"{$hiscore[1]}\",
	\"hiscore3\": \"{$hiscore[2]}\",
	\"hiscore4\": \"{$hiscore[3]}\"
}
";
exit;
