sql.table.create=create table %s (\
					name varchar(50) not null,\
					content image,\
					size int,\
					updated datetime,
					primary key (name)\
				  )   
sql.insert=INSERT INTO %s (name, content, size, updated) VALUES (?, ?, ?, getdate())
sql.update=UPDATE %s SET name = ?, updated = getdate() WHERE name = ?