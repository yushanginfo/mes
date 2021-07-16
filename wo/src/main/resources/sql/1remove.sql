--1. 删除工单工艺
delete from mes.work_order_technics tech
where not exists(select * from mes.work_orders wo,mes.work_order_types wot,shtz.sfcta ta
 where wo.order_type_id = wot.id and ta.ta001=wot.code and ta.ta002=wo.batch_num and ta.ta003=tech.indexno and ta.ta006 = tech.machine_supplier_code and tech.work_order_id=wo.id)
 and tech.days is null;

--2. 删除工单
delete from mes.work_orders wo where not exists(select * from shtz.mocta ta,mes.work_order_types mot where mot.id=wo.order_type_id and mot.code=ta.ta001 and wo.batch_num=ta.ta002)
and not exists(select * from mes.work_order_technics wot where wot.work_order_id=wo.id and wot.days is not null);

--2.0 检查被删除的工单是否重用了工单号
update mes.work_orders wo set remark=null,assess_status=0 where assess_status=5
 and exists(select * from shtz.mocta ta,mes.work_order_types mot where mot.id=wo.order_type_id and mot.code=ta.ta001 and wo.batch_num=ta.ta002);

--2.1 更新被删除的工单
update mes.work_orders wo set remark='removed',assess_status=5 where not exists(select * from shtz.mocta ta,mes.work_order_types mot
where mot.id=wo.order_type_id and mot.code=ta.ta001 and wo.batch_num=ta.ta002);

--3. 删除bom清单
delete from mes.product_material_items pmi
where not exists(select * from shtz.bomcb cb where cb.cb002=pmi.cb002 and cb.cb003=pmi.cb003 and cb.cb004 = pmi.indexno);

--4. 删除工艺路线工艺
delete from mes.product_technics pt
where not exists(select mf.mf008 from shtz.bommf mf,mes.products p ,mes.technic_schemes ts
where pt.scheme_id=ts.id and ts.product_id=p.id and mf.mf001=p.code and mf.mf002=ts.indexno and mf.mf003 = pt.indexno and mf.mf006=pt.machine_supplier_code);

--5.0 更新工单中不存在的工艺路线
update mes.work_orders wo set technic_scheme_id=null
where not exists(select * from shtz.bomme me,mes.products p,mes.technic_schemes ts
where me.me001=p.code and me.me002=ts.indexno and p.id=ts.product_id
and ts.id=wo.technic_scheme_id)and wo.technic_scheme_id is not null;

--5. 删除工艺路线
delete from mes.technic_schemes ts
where not exists(select * from shtz.bomme me,mes.products p where me.me001=p.code and p.id=ts.product_id and me.me002=ts.indexno)
and not exists(select * from mes.work_orders wo where wo.technic_scheme_id=ts.id);

--6. 删除产品
delete from mes.products m 	where not exists(select * from shtz.invmb i where i.mb001 = m.code)
and not exists(select * from mes.technic_schemes ts where ts.product_id=m.id);

--7. 删除品号
delete from mes.materials m where not exists(select * from shtz.invmb i where i.mb001 = m.code)
and not exists(select * from mes.technic_schemes ts where ts.product_id=m.id);

--8. 删除工艺
delete from mes.technics t
	where not exists(select * from shtz.cmsmw mw where mw.mw001 = t.code);
