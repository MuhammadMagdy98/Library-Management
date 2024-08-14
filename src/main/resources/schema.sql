CREATE TABLE IF NOT EXISTS books (
                                     id BIGSERIAL PRIMARY KEY,
                                     title VARCHAR(255) NOT NULL,
                                     author VARCHAR(255) NOT NULL,
                                     isbn VARCHAR(255) UNIQUE NOT NULL,
                                     publication_year TIMESTAMP
);

CREATE TABLE IF NOT EXISTS patrons (
                                       id BIGSERIAL PRIMARY KEY,
                                       name VARCHAR(255) NOT NULL,
                                       contact_information VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS borrowing_records (
                                                 id BIGSERIAL PRIMARY KEY,
                                                 book_id BIGINT REFERENCES books(id) ON DELETE CASCADE,
                                                 patron_id BIGINT REFERENCES patrons(id) ON DELETE CASCADE,
                                                 borrow_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                                 return_date TIMESTAMP NULL
);