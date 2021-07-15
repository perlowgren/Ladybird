<?php

ini_set('display_errors',1);
error_reporting(E_ALL);

define('FPS',6.667);

$private_dirs = array(
	'localhost'=>'/var/www/private/spirangle/ladybird',
	'ladybird.spirangle.net'=>'/customers/6/b/a/spirangle.net/httpd.private/ladybird',
);

$host = $_SERVER['HTTP_HOST'];
$private_dir = isset($private_dirs[$host])? $private_dirs[$host] : __DIR__;


$db_file = "{$private_dir}/ladybird.db";
$db = null;

