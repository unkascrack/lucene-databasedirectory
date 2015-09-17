sql.table.create=create table %s (\
					name varchar(50) not null,\
					content longvarbinary,\
					size integer,\
					updated timestamp,
					primary key (name)\
				  )   
sql.insert=INSERT INTO %s (name, content, size, updated) VALUES (?, ?, ?, now())
sql.update=UPDATE %s SET name = ?, updated = now() WHERE name = ?