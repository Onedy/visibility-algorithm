CREATE TABLE IF NOT EXISTS product (
    id BIGINT auto_increment PRIMARY KEY,
    `sequence` INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS `size` (
   id BIGINT auto_increment PRIMARY KEY,
   product_id BIGINT,
   back_soon BOOLEAN NOT NULL,
   special BOOLEAN NOT NULL,
   quantity INTEGER NOT NULL,
   FOREIGN KEY (product_id) REFERENCES product(id)
);

set @lastId=SELECT id FROM FINAL TABLE ( INSERT INTO product (`sequence`) VALUES (2));
INSERT INTO `size` (product_id, back_soon, special, quantity)
    VALUES (@lastId, true, false, 3);

set @lastId=SELECT id FROM FINAL TABLE ( INSERT INTO product (`sequence`) VALUES (5));
INSERT INTO `size` (product_id, back_soon, special, quantity)
    VALUES (@lastId, false, false, 4), (@lastId, false, false, 29);

INSERT INTO product (`sequence`) VALUES (2)
