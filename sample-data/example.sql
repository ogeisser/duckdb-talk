#!/usr/bin/env -S duckdb -f

create table if not exists numbers(v int);
insert into numbers values(0);
insert into numbers from range(1,5);
select sum(v) as total from numbers;
