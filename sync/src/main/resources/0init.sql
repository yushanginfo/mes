--1. 新增计量单位
insert into mes.measurement_units(id,code,name)
select next_id('mes.measurement_units'), b.name,b.name from(
select distinct i.mb004 as name from shtz.invmb i where not exists(select * from mes.measurement_units mu where lower(trim(mu.name))=lower(i.mb004))
and i.mb004 is not null) b;