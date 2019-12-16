CREATE USER 'snsmeta_rw'@'192.168.56.1' IDENTIFIED BY 'wB2p8A0Y4I02F6y';
GRANT select,update,delete,insert ON snsmeta.* TO 'snsmeta_rw'@'192.168.56.1';
ALTER USER 'snsmeta_rw'@'192.168.56.1' WITH MAX_QUERIES_PER_HOUR 10;


CREATE TABLE `meta_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `hostip` char(15) COLLATE utf8mb4_bin NOT NULL DEFAULT '',
  `dbname` varchar(20) COLLATE utf8mb4_bin NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=129 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin