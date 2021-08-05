--1. 新增加工中心
insert into mes.machines(id,code,name,updated_at) select next_id('mes.machines'),md.md001,md.md002,now() from shtz.cmsmd md
	where not exists(select * from mes.machines a where md.md001 = a.code);

--1.1. 更新供应商
update base.suppliers s set name=(select coalesce(b.ma002,b.ma003) from shtz.purma b where b.ma001=s.code)
where exists(select * from shtz.purma b where b.ma001=s.code and s.name <> coalesce(b.ma002,b.ma003));

--1.2 新增供应商
insert into base.suppliers (id,code,name,updated_at)
select b.ma001::int4,b.ma001,coalesce(b.ma002,b.ma003),now() from shtz.purma b 
where not exists(select * from base.suppliers s where s.code=b.ma001);

--2. 新增工艺（厂内）
insert into mes.technics(id,code,name,description,machine_id,internal,updated_at)
select next_id('mes.technics'),mw.mw001,mw.mw002,mw.mw003,m.id,true,now() from
shtz.cmsmw mw  left outer join mes.machines m on mw.mw005=m.code
where mw.mw004='1' and not exists(select * from mes.technics  t where t.code=mw.mw001);

--3. 新增工艺（委外）
insert into mes.technics(id,code,name,description,internal,updated_at)
select next_id('mes.technics'),mw.mw001,mw.mw002,mw.mw003,false,now() from shtz.cmsmw mw
where coalesce(mw.mw004,'0')<>'1' and not exists(select * from mes.technics  t where t.code=mw.mw001);

--3.1 根据工艺名称更新评审组
update mes.technics t set assess_group_id = (select g.id from mes.assess_groups g where g.name =t.name )
where t.assess_group_id is null;

--4. 新增品号
insert into mes.materials(id,code,name,specification,material_type_id,unit_id,updated_at)
select datetime_id(),i.mb001,i.mb002,i.mb003,mt.id,mu.id,now() from shtz.invmb i ,mes.material_types mt,mes.measurement_units mu
where i.mb025=mt.code and lower(trim(i.mb004))=lower(mu.code) and not exists(select * from mes.materials m where m.code=i.mb001)
and i.mb002 is not null;

--5. 新增产品
insert into mes.products(id,code,name,specification,material_type_id,unit_id,updated_at)
select datetime_id(),i.mb001,i.mb002,i.mb003,mt.id,mu.id,now() from shtz.invmb i ,mes.material_types mt,mes.measurement_units mu
where i.mb025=mt.code and lower(trim(i.mb004))=lower(mu.code) and not exists(select * from mes.products m where m.code=i.mb001)
and i.mb002 is not null and i.mb003 is not null;

--6. 新增工艺路线
insert into mes.technic_schemes(id,product_id,name,indexno,updated_at)
select datetime_id(),p.id,me.me003,me.me002,now() from shtz.bomme me,mes.products p where p.code=me.me001 and
not exists(select * from mes.technic_schemes ts where ts.product_id=p.id and ts.indexno=me.me002);

--7. 新增产品工艺（厂内）
insert into mes.product_technics(id,scheme_id,indexno,machine_id,technic_id,description,internal,machine_supplier_code)
select datetime_id(),ts.id,mf.mf003,m.id,t.id,mf.mf008,true,mf.mf006 from shtz.bommf mf,mes.technic_schemes ts,mes.products p,
mes.machines m,mes.technics t
where p.id=ts.product_id and mf.mf001=p.code and mf.mf002=ts.indexno and m.code=mf.mf006 and t.code=mf.mf004
and mf.mf005='1'
and not exists(select * from mes.product_technics pt where pt.scheme_id=ts.id and pt.indexno=mf.mf003 and pt.machine_id=m.id);

--8. 新增产品工艺（委外）
insert into mes.product_technics(id,scheme_id,indexno,supplier_id,technic_id,description,internal,machine_supplier_code)
select datetime_id(),ts.id,mf.mf003,m.id,t.id,mf.mf008,false,mf.mf006 from shtz.bommf mf,mes.technic_schemes ts,mes.products p,
base.suppliers m,mes.technics t
where p.id=ts.product_id and mf.mf001=p.code and mf.mf002=ts.indexno and m.code=mf.mf006 and t.code=mf.mf004
and mf.mf005='2'
and not exists(select * from mes.product_technics pt where pt.scheme_id=ts.id and pt.indexno=mf.mf003 and pt.supplier_id=m.id);

--9. 新增bom清单
insert into mes.product_material_items(id,product_id,cb002,cb003,indexno,material_id,amount,updated_at)
select datetime_id(),p.id,cb.cb002,cb.cb003,cb.cb004,m.id,cb.cb008,now() from shtz.bomcb cb,mes.products p,mes.materials m
where cb.cb001 = p.code and cb.cb005=m.code and not exists(select * from mes.product_material_items pmi where pmi.product_id=p.id
and pmi.cb002=cb.cb002 and pmi.cb003=cb.cb003 and pmi.indexno=cb.cb004);

--10. 新增生产工单
insert into mes.work_orders(id,assess_status,product_id,amount,updated_at,batch_num,factory_id,created_at,order_type_id,status_id,technic_scheme_id)
select datetime_id(),0,p.id,ta.ta015,now(),ta.ta002,f.id, TO_TIMESTAMP(substr(ta.create_date,1,14),'YYYYMMDDHH24MISS'),wot.id,wos.id,
(select min(ts.id) from mes.technic_schemes ts where ts.product_id=p.id and ts.indexno like '%001')
from shtz.mocta ta,mes.products p,base.factories f,mes.work_order_types wot,mes.work_order_statuses wos
where ta.ta001 = wot.code and ta.ta019=f.code and ta.ta011=wos.code and ta.ta006=p.code
 and not exists(select * from mes.work_orders wo where wo.order_type_id=wot.id and wo.batch_num=ta.ta002);

--11. 新增工单工艺（厂内）
insert into mes.work_order_technics(id,work_order_id,indexno,machine_id,technic_id,description,internal,machine_supplier_code,updated_at,factory_id)
select datetime_id(),wo.id,ta.ta003,m.id,t.id,ta.ta024,true,ta.ta006,now(),wo.factory_id from shtz.sfcta ta,mes.work_orders wo,
mes.machines m,mes.technics t,mes.work_order_types wotp
where wo.order_type_id=wotp.id and wotp.code=ta.ta001 and ta.ta002=wo.batch_num and m.code=ta.ta006 and t.code=ta.ta004
and ta.ta005='1'
and not exists(select * from  mes.work_order_technics tech where tech.work_order_id=wo.id and tech.indexno=ta.ta003);

--12. 新增工单工艺（委外）
insert into mes.work_order_technics(id,work_order_id,indexno,supplier_id,technic_id,description,internal,machine_supplier_code,updated_at,factory_id)
select datetime_id(),wo.id,ta.ta003,m.id,t.id,ta.ta024,false,ta.ta006,now(),wo.factory_id from shtz.sfcta ta,mes.work_orders wo,
base.suppliers m,mes.technics t,mes.work_order_types wotp
where wo.order_type_id=wotp.id and wotp.code=ta.ta001 and ta.ta002=wo.batch_num and m.code=ta.ta006 and t.code=ta.ta004
and ta.ta005='2'
and not exists(select * from  mes.work_order_technics tech where tech.work_order_id=wo.id and tech.indexno=ta.ta003);


--13 更新工单中的计划完工
update mes.work_orders wo set planned_end_on=(select to_date(ta.ta010,'yyyyMMdd') from mes.work_order_types wot,
shtz.mocta ta where ta.ta001=wot.code and wot.id=wo.order_type_id and ta.ta002=wo.batch_num)
where exists(select * from mes.work_order_types wot,
shtz.mocta ta where ta.ta001=wot.code and wot.id=wo.order_type_id and ta.ta002=wo.batch_num and case when length(coalesce(ta.ta010,'--'))=0 then '--' else ta.ta010 end <> coalesce(to_char(wo.planned_end_on,'yyyyMMdd'),'--'));

--14 更新工单中的客户交期
update mes.work_orders wo set deadline=planned_end_on where planned_end_on  is not null and deadline is null;

--14 样品工单无需评审
update mes.work_orders set assess_status=4,remark='样品或者返厂工单无需评审' where assess_status<>4  and order_type_id in(5103,5201) ;

--15 更新完工或者指定完工的工单为已通过评审
update mes.work_orders wo set assess_status=4,remark='完工或者指定完工的工单为已通过评审' where assess_status<>4 and status_id in(4,5);

--16 更新工单中的业务员
update mes.work_orders wo set saler_id=
(select min(c.saler_id) from mes.products p,base.customers c 
 where c.quick_code=substr(p.specification,1,3) and c.saler_id is not null and p.id=wo.product_id)
where saler_id is null;

