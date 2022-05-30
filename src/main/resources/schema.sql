CREATE TABLE book (
    id SERIAL NOT NULL PRIMARY KEY,
    title TEXT NOT NULL,
    overview TEXT,
    totalevaluation DOUBLE PRECISION NOT NULL DEFAULT 0
);

CREATE TABLE review (
    id SERIAL NOT NULL PRIMARY KEY,
    evaluation INTEGER NOT NULL DEFAULT 0,
    content TEXT,
    bookid INTEGER NOT NULL,
    userid INTEGER NOT NULL
);