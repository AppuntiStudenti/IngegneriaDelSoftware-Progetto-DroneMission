CREATE TABLE IF NOT EXISTS missions (
    id INT UNSIGNED AUTO_INCREMENT NOT NULL, 
    t_start LONG,
    t_end LONG,
        PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARACTER SET 'UTF8' COLLATE 'UTF8_GENERAL_CI';

CREATE TABLE IF NOT EXISTS commands (
    id INT UNSIGNED AUTO_INCREMENT NOT NULL,
    mission_id INT UNSIGNED NOT NULL,
    type INT NOT NULL,
    value INT DEFAULT 0,
    time LONG,
        PRIMARY KEY (id),
        FOREIGN KEY (mission_id) REFERENCES missions(id)
) ENGINE=InnoDB DEFAULT CHARACTER SET 'UTF8' COLLATE 'UTF8_GENERAL_CI';

CREATE TABLE IF NOT EXISTS replies (
    id INT UNSIGNED AUTO_INCREMENT NOT NULL, 
    type INT NOT NULL,
    value VARCHAR(256),
    command_id INT UNSIGNED NOT NULL,
    time LONG,
        PRIMARY KEY (id),
        FOREIGN KEY (command_id) REFERENCES commands(id)
) ENGINE=InnoDB DEFAULT CHARACTER SET 'UTF8' COLLATE 'UTF8_GENERAL_CI';

CREATE TABLE IF NOT EXISTS notifies (
    id INT UNSIGNED AUTO_INCREMENT NOT NULL, 
    mission_id INT UNSIGNED NOT NULL,
    type INT NOT NULL,
    value VARCHAR(256),
    time LONG,
        PRIMARY KEY (id),
        FOREIGN KEY (mission_id) REFERENCES missions(id)
) ENGINE=InnoDB DEFAULT CHARACTER SET 'UTF8' COLLATE 'UTF8_GENERAL_CI';

CREATE TABLE IF NOT EXISTS sensors (
    id INT UNSIGNED AUTO_INCREMENT NOT NULL,
    mission_id INT UNSIGNED NOT NULL,
    data VARCHAR(4096),
    has_picture TINYINT(1),
    time LONG,
        PRIMARY KEY (id),
        FOREIGN KEY (mission_id) REFERENCES missions(id)
) ENGINE=InnoDB DEFAULT CHARACTER SET 'UTF8' COLLATE 'UTF8_GENERAL_CI';

CREATE TABLE IF NOT EXISTS pictures (
    id INT UNSIGNED AUTO_INCREMENT NOT NULL,
    mission_id INT UNSIGNED NOT NULL,
    name VARCHAR(256),
    sensors_id INT UNSIGNED NOT NULL,
    time_creation LONG,
        PRIMARY KEY (id),
        FOREIGN KEY (sensors_id) REFERENCES sensors(id),
        FOREIGN KEY (mission_id) REFERENCES missions(id)
) ENGINE=InnoDB DEFAULT CHARACTER SET 'UTF8' COLLATE 'UTF8_GENERAL_CI';


