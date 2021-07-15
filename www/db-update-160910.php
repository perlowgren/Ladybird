<?php

require __DIR__.'/init.php';

$update = file_exists($db_file);

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

if($update) {
	$db->exec('ALTER TABLE scores RENAME TO scores_160910');
}

$db->exec($table);
$db->exec('CREATE INDEX scores_round ON scores (round)');
$db->exec('CREATE INDEX scores_score ON scores (score)');

if($update) {
	$db->exec('INSERT INTO scores (id,name,round,time,score,rank,created) SELECT NULL,name,round,time,score,rank,created FROM scores_160910');
	$db->exec('DROP TABLE scores_160910');
}

$db->close();

exit;
