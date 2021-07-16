
--1.更新加工中心名称
update mes.machines m set name=(select md.md002 from shtz.cmsmd md where md.md001=m.code)
	where exists(select * from shtz.cmsmd md where md.md001 = m.code and md.md002<>m.name);

--2. 更新工艺名称
update mes.technics t set name=(select mw.mw002 from shtz.cmsmw mw where mw.mw001=t.code)
	where exists(select * from shtz.cmsmw mw where mw.mw001 = t.code and mw.mw002<>t.name);

--3. 更新工艺性质
update mes.technics t set internal=(select case when mw.mw004 ='1' then true else false end from shtz.cmsmw mw where mw.mw001=t.code)
	where exists(select * from shtz.cmsmw mw where mw.mw001 = t.code and case when mw.mw004 ='1' then true else false end <>t.internal);

--4. 更新工艺的加工中心
update mes.technics t set machine_id = (select m.id from shtz.cmsmw mw,mes.machines m where mw.mw001=t.code and mw.mw005=m.code )
 where exists(select * from shtz.cmsmw mw,mes.machines m where mw.mw001=t.code and mw.mw004='1' and mw.mw005=m.code
 and m.id <> coalesce(machine_id,0));

--5. 更新工艺的委外时设置加工中心为空
update mes.technics t set machine_id = null where exists(select * from shtz.cmsmw mw where mw.mw001=t.code and mw.mw004='2')
	and machine_id is not null;

--6. 更新工艺的说明
update mes.technics t set description=(select mw.mw003 from shtz.cmsmw mw where mw.mw001=t.code)
	where exists(select * from shtz.cmsmw mw where mw.mw001 = t.code and coalesce(mw.mw003,'--')<>coalesce(t.description,'--'));

--7. 更新品号的名称
update mes.materials m set name=(select i.mb002 from shtz.invmb i where i.mb001=m.code)
	where exists(select * from shtz.invmb i where i.mb001 = m.code and i.mb002<>m.name);

--8. 更新品号的规格
update mes.materials m set specification=(select i.mb003 from shtz.invmb i where i.mb001=m.code)
	where exists(select * from shtz.invmb i where i.mb001 = m.code and coalesce(i.mb003,'--')<> coalesce(m.specification,'--'));

--9. 更新品号的属性
update mes.materials m set material_type_id=(select mt.id from shtz.invmb i, mes.material_types mt  where i.mb001=m.code and mt.code=i.mb025)
	where exists(select mt.id from shtz.invmb i, mes.material_types mt  where i.mb001=m.code and mt.code=i.mb025 and m.material_type_id <> mt.id);

--10. 更新品号的单位
update mes.materials m set unit_id=(select mt.id from shtz.invmb i, mes.measurement_units mt  where i.mb001=m.code and mt.code=lower(trim(i.mb004)))
	where exists(select mt.id from shtz.invmb i, mes.material_types mt  where i.mb001=m.code and mt.code=lower(trim(i.mb004)) and m.unit_id <> mt.id);

--11. 更新产品的名称
update mes.products m set name=(select i.mb002 from shtz.invmb i where i.mb001=m.code)
	where exists(select * from shtz.invmb i where i.mb001 = m.code and i.mb002<>m.name);

--12. 更新产品的规格
update mes.products m set specification=(select i.mb003 from shtz.invmb i where i.mb001=m.code)
	where exists(select * from shtz.invmb i where i.mb001 = m.code and coalesce(i.mb003,'--')<> coalesce(m.specification,'--'));

--13. 更新产品的属性
update mes.products m set material_type_id=(select mt.id from shtz.invmb i, mes.material_types mt  where i.mb001=m.code and mt.code=i.mb025)
	where exists(select mt.id from shtz.invmb i, mes.material_types mt  where i.mb001=m.code and mt.code=i.mb025 and m.material_type_id <> mt.id);

--14. 更新产品的单位
update mes.products m set unit_id=(select mt.id from shtz.invmb i, mes.measurement_units mt  where i.mb001=m.code and mt.code=lower(trim(i.mb004)))
	where exists(select mt.id from shtz.invmb i, mes.material_types mt  where i.mb001=m.code and mt.code=lower(trim(i.mb004)) and m.unit_id <> mt.id);

--15. 更新工艺路线名称
update mes.technic_schemes ts set name=
(select  me.me003 from shtz.bomme me,mes.products p where me.me001=p.code and p.id=ts.product_id and me.me002=ts.indexno)
where exists(select * from shtz.bomme me,mes.products p where me.me001=p.code and p.id=ts.product_id and me.me002=ts.indexno
and me.me003<>ts.name);

--16. 更新产品工艺信息性质
update mes.product_technics pt set internal = (select distinct case when mf.mf005 ='1' then true else false end from shtz.bommf mf,mes.products p ,mes.technic_schemes ts
where pt.scheme_id=ts.id and ts.product_id=p.id and mf.mf001=p.code and mf.mf002=ts.indexno and mf.mf003 = pt.indexno and mf.mf006=pt.machine_supplier_code)
where exists(select mf.mf008 from shtz.bommf mf,mes.products p ,mes.technic_schemes ts
where pt.scheme_id=ts.id and ts.product_id=p.id and mf.mf001=p.code and mf.mf002=ts.indexno and mf.mf003 = pt.indexno and mf.mf006=pt.machine_supplier_code
and case when mf.mf005 ='1' then true else false end <> pt.internal);

--17. 更新产品工艺信息描述
update mes.product_technics pt set description = (select mf.mf008 from shtz.bommf mf,mes.products p ,mes.technic_schemes ts
where pt.scheme_id=ts.id and ts.product_id=p.id and mf.mf001=p.code and mf.mf002=ts.indexno and mf.mf003 = pt.indexno and mf.mf006=pt.machine_supplier_code)
where exists(select mf.mf008 from shtz.bommf mf,mes.products p ,mes.technic_schemes ts
where pt.scheme_id=ts.id and ts.product_id=p.id and mf.mf001=p.code and mf.mf002=ts.indexno and mf.mf003 = pt.indexno and mf.mf006=pt.machine_supplier_code
 and coalesce(mf.mf008,'-')<> coalesce(pt.description,'-'));

--18. 更新产品工艺使用工艺
update mes.product_technics pt set technic_id = (select tech.id from shtz.bommf mf,mes.products p ,mes.technic_schemes ts,mes.technics tech
where pt.scheme_id=ts.id and ts.product_id=p.id and mf.mf001=p.code and mf.mf002=ts.indexno and mf.mf003 = pt.indexno
 and mf.mf006=pt.machine_supplier_code and tech.code=mf.mf004)
where exists(select * from shtz.bommf mf,mes.products p ,mes.technic_schemes ts,mes.technics tech
where pt.scheme_id=ts.id and ts.product_id=p.id and mf.mf001=p.code and mf.mf002=ts.indexno and mf.mf003 = pt.indexno and mf.mf006=pt.machine_supplier_code
 and tech.code=mf.mf004 and tech.id <> pt.technic_id);

--19. 更新产品工艺的加工中心
update mes.product_technics  pt set machine_id =(select m.id from mes.machines m where m.code=pt.machine_supplier_code)
where pt.internal=true and exists(select * from mes.machines m where m.id=pt.machine_id and m.code<> pt.machine_supplier_code);

--20. 更新产品工艺的供应商
update mes.product_technics  pt set supplier_id =(select m.id from base.suppliers m where m.code=pt.machine_supplier_code)
where pt.internal=false and exists(select * from base.suppliers m where m.id=pt.supplier_id and m.code<> pt.machine_supplier_code);

--21. 更新BOM材料清单-数量
update mes.product_material_items pmi set amount=(select cb.cb008 from shtz.bomcb cb where cb.cb002=pmi.cb002 and cb.cb003=pmi.cb003 and cb.cb004 = pmi.indexno)
where exists(select cb.cb008 from shtz.bomcb cb where cb.cb002=pmi.cb002 and cb.cb003=pmi.cb003 and cb.cb004 = pmi.indexno and pmi.amount<> cb.cb008);

--22. 更新BOM材料清单-材料
update mes.product_material_items pmi set material_id=(select m.id from shtz.bomcb cb,mes.materials m
 where cb.cb002=pmi.cb002 and cb.cb003=pmi.cb003 and cb.cb004 = pmi.indexno and m.code=cb.cb005)
where exists(select cb.cb008 from shtz.bomcb cb,mes.materials m  where cb.cb002=pmi.cb002 and cb.cb003=pmi.cb003
 and cb.cb004 = pmi.indexno and cb.cb005 = m.code and m.id <> pmi.material_id);

--23. 更新工单中的状态
update mes.work_orders wo set status_id=(select wos.id from mes.work_order_statuses wos,mes.work_order_types wot,
shtz.mocta ta where ta.ta001=wot.code and wot.id=wo.order_type_id and ta.ta002=wo.batch_num and ta.ta011=wos.code)
where exists(select wos.id from mes.work_order_statuses wos,mes.work_order_types wot,
shtz.mocta ta where ta.ta001=wot.code and wot.id=wo.order_type_id and ta.ta002=wo.batch_num and ta.ta011=wos.code and wos.id <> wo.status_id);

--24. 更新工单中的数量
update mes.work_orders wo set amount=(select ta.ta015 from mes.work_order_types wot,
shtz.mocta ta where ta.ta001=wot.code and wot.id=wo.order_type_id and ta.ta002=wo.batch_num)
where exists(select * from mes.work_order_types wot,
shtz.mocta ta where ta.ta001=wot.code and wot.id=wo.order_type_id and ta.ta002=wo.batch_num and ta.ta015 <> wo.amount);

--25. 更新工单中的创建日期
update mes.work_orders wo set created_at=(select  TO_TIMESTAMP(substr(ta.create_date,1,14),'YYYYMMDDHH24MISS') from mes.work_order_types wot,
shtz.mocta ta where ta.ta001=wot.code and wot.id=wo.order_type_id and ta.ta002=wo.batch_num)
where exists(select * from mes.work_order_types wot,
shtz.mocta ta where ta.ta001=wot.code and wot.id=wo.order_type_id and ta.ta002=wo.batch_num and  wo.created_at<> TO_TIMESTAMP(substr(ta.create_date,1,14),'YYYYMMDDHH24MISS'));

--26. 更新工单中的工厂
update mes.work_orders wo set factory_id=(select f.id from base.factories f,mes.work_order_types wot,
shtz.mocta ta where ta.ta001=wot.code and wot.id=wo.order_type_id and ta.ta002=wo.batch_num and ta.ta019=f.code)
where exists(select f.id from base.factories f,mes.work_order_types wot,
shtz.mocta ta where ta.ta001=wot.code and wot.id=wo.order_type_id and ta.ta002=wo.batch_num and ta.ta019=f.code and f.id <> wo.factory_id);

--27. 更新工单中的品号
update mes.work_orders wo set product_id=(select p.id from mes.products p,mes.work_order_types wot,
shtz.mocta ta where ta.ta001=wot.code and wot.id=wo.order_type_id and ta.ta002=wo.batch_num and ta.ta006=p.code)
where exists(select p.id from mes.products p,mes.work_order_types wot,
shtz.mocta ta where ta.ta001=wot.code and wot.id=wo.order_type_id and ta.ta002=wo.batch_num and ta.ta006=p.code and p.id <> wo.product_id);

--29 更新工单工艺-性质
update mes.work_order_technics tech set internal = (select case when ta.ta005 ='1' then true else false end from mes.work_orders wo,mes.work_order_types wot,shtz.sfcta ta
where wo.order_type_id = wot.id and ta.ta001=wot.code and ta.ta002=wo.batch_num and ta.ta003=tech.indexno and tech.work_order_id=wo.id
and ta.ta006=tech.machine_supplier_code)
where exists (select * from mes.work_orders wo,mes.work_order_types wot,shtz.sfcta ta
where wo.order_type_id = wot.id and ta.ta001=wot.code and ta.ta002=wo.batch_num and ta.ta003=tech.indexno and tech.work_order_id=wo.id
and ta.ta006=tech.machine_supplier_code and case when ta.ta005 ='1' then true else false end <> tech.internal);

--30 更新工单工艺-描述
update mes.work_order_technics tech set description = (select ta024 from mes.work_orders wo,mes.work_order_types wot,shtz.sfcta ta
where wo.order_type_id = wot.id and ta.ta001=wot.code and ta.ta002=wo.batch_num and ta.ta003=tech.indexno and tech.work_order_id=wo.id
and ta.ta006=tech.machine_supplier_code)
where exists (select * from mes.work_orders wo,mes.work_order_types wot,shtz.sfcta ta
where wo.order_type_id = wot.id and ta.ta001=wot.code and ta.ta002=wo.batch_num and ta.ta003=tech.indexno and tech.work_order_id=wo.id
and ta.ta006=tech.machine_supplier_code and coalesce(ta024,'-')<> coalesce(tech.description,'-'));

--31 更新工单工艺-工艺
update mes.work_order_technics tech set technic_id = (select t.id from mes.work_orders wo,mes.work_order_types wot,shtz.sfcta ta,mes.technics t
where wo.order_type_id = wot.id and ta.ta001=wot.code and ta.ta002=wo.batch_num and ta.ta003=tech.indexno and tech.work_order_id=wo.id
and t.code=ta.ta004
and ta.ta006=tech.machine_supplier_code)
where exists (select * from mes.work_orders wo,mes.work_order_types wot,shtz.sfcta ta,mes.technics t
where wo.order_type_id = wot.id and ta.ta001=wot.code and ta.ta002=wo.batch_num and ta.ta003=tech.indexno and tech.work_order_id=wo.id
and ta.ta006=tech.machine_supplier_code and t.code=ta.ta004 and t.id <> tech.technic_id);
