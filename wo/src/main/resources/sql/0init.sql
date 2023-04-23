--1. 新增计量单位
insert into mes.measurement_units(id,code,name)
select next_id('mes.measurement_units'), b.name,b.name from(
select distinct i.mb004 as name from shtz.invmb i where not exists(select * from mes.measurement_units mu where lower(trim(mu.name))=lower(i.mb004))
and i.mb004 is not null) b;

--2. 新增用户
insert into base.users(id,code,name,department_id,updated_at,begin_on)
select datetime_id(),mf001,mf002,1,now(),current_date from shtz.admmf a where
not exists(select * from base.users u where u.code=a.mf001)
and not exists(select * from shtz.admmf b where a.mf001=b.mf001 and a.mf004 > b.mf004);

--3. 新增客户信息
insert into base.customers(id,code,quick_code,short_name,name,updated_at)
select datetime_id(),ma001,ma102,ma002,ma003,now() from shtz.copma m where not exists(select * from base.customers c where c.code=m.ma001);

--4 更改用户姓名
update base.users u set name=(select min(m.mf002) from shtz.admmf m where m.mf001=u.code)
where not exists(select * from shtz.admmf m where m.mf001=u.code and u.name=m.mf002)
and exists(select * from shtz.admmf m where m.mf001=u.code);

--5 更改用户部门
update base.users u set department_id=(select min(d.id) from shtz.admmf m,base.departments d where m.mf001=u.code and d.code = m.mf007)
where exists(select * from shtz.admmf m,base.departments d where m.mf001=u.code and u.department_id <> d.id and d.code = m.mf007);

--6 更新客户名称
update base.customers  c set (quick_code,short_name,name)=(select m.ma102,m.ma002,m.ma003 from shtz.copma m where m.ma001=c.code);

--7 更新客户对应业务人员
update base.customers  c set saler_id=(select u.id from base.users u,shtz.copma m where m.ma001=c.code and trim(m.MA016)=u.code);
