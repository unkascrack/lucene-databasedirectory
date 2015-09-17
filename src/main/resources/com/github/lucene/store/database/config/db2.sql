sql.table.create=create table %s (\
					name varchar(50) not null,\
					content blob(500000 K),\
					size integer,\
					updated timestamp,\
					primary key (name)\
				 )
sql.insert=INSERT INTO %s (name, content, size, updated) VALUES (?, ?, ?, current_timestamp)
sql.update=UPDATE %s SET name = ?, updated = current_timestamp WHERE name = ?