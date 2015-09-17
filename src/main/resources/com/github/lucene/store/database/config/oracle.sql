sql.table.create=create table %s (\
					name varchar2(50) not null,\
					content blob,\
					size number(10,0),\
					updated timestamp,
					primary key (name)\
				  )   
sql.insert=INSERT INTO %s (name, content, size, updated) VALUES (?, ?, ?, systimestamp)
sql.update=UPDATE %s SET name = ?, updated = systimestamp WHERE name = ?