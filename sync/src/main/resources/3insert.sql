1. 新增加工中心
insert into erp_mes.machines(id,code,name,updated_at) select next_id('erp_mes.machines'),md.md001,md.md002,now() from shtz.cmsmd md
	where not exists(select * from erp_mes.machines a where md.md001 = a.code);

2. 新增工艺（厂内）
insert into erp_mes.technics(id,code,name,description,machine_id,internal,updated_at)
select next_id('erp_mes.technics'),mw.mw001,mw.mw002,mw.mw003,m.id,true,now() from
shtz.cmsmw mw  left outer join erp_mes.machines m on mw.mw005=m.code
where mw.mw004='1' and not exists(select * from erp_mes.technics  t where t.code=mw.mw001);

3. 新增工艺（委外）
insert into erp_mes.technics(id,code,name,description,internal,updated_at)
select next_id('erp_mes.technics'),mw.mw001,mw.mw002,mw.mw003,false,now() from shtz.cmsmw mw
where coalesce(mw.mw004,'0')<>'1' and not exists(select * from erp_mes.technics  t where t.code=mw.mw001);

4. 新增品号
insert into erp_mes.materials(id,code,name,specification,material_type_id,unit_id,updated_at)
select datetime_id(),i.mb001,i.mb002,i.mb003,mt.id,mu.id,now() from shtz.invmb i ,erp_mes.material_types mt,erp_mes.measurement_units mu
where i.mb025=mt.code and lower(trim(i.mb004))=mu.code and not exists(select * from erp_mes.materials m where m.code=i.mb001);

5. 新增产品
insert into erp_mes.products(id,code,name,specification,material_type_id,unit_id,updated_at)
select datetime_id(),i.mb001,i.mb002,i.mb003,mt.id,mu.id,now() from shtz.invmb i ,erp_mes.material_types mt,erp_mes.measurement_units mu
where i.mb025=mt.code and lower(trim(i.mb004))=mu.code and not exists(select * from erp_mes.products m where m.code=i.mb001)
and i.mb003 is not null;

6. 新增工艺路线
insert into erp_mes.technic_schemes(id,product_id,name,indexno,updated_at)
select datetime_id(),p.id,me.me003,me.me002,now() from shtz.bomme me,erp_mes.products p where p.code=me.me001 and
not exists(select * from erp_mes.technic_schemes ts
where ts.product_id=p.id and ts.indexno=me.me002);

7. 新增产品工艺（厂内）
insert into erp_mes.product_technics(id,scheme_id,indexno,machine_id,technic_id,description,internal,machine_supplier_code)
select datetime_id(),ts.id,mf.mf003,m.id,t.id,mf.mf008,true,mf.mf006 from shtz.bommf mf,erp_mes.technic_schemes ts,erp_mes.products p,
erp_mes.machines m,erp_mes.technics t
where p.id=ts.product_id and mf.mf001=p.code and mf.mf002=ts.indexno and m.code=mf.mf006 and t.code=mf.mf004
and mf.mf005='1'
and not exists(select * from erp_mes.product_technics pt where pt.scheme_id=ts.id and pt.indexno=mf.mf003 and pt.machine_id=m.id);

8. 新增产品工艺（委外）
insert into erp_mes.product_technics(id,scheme_id,indexno,machine_id,technic_id,description,internal,machine_supplier_code)
select datetime_id(),ts.id,mf.mf003,m.id,t.id,mf.mf008,true,mf.mf006 from shtz.bommf mf,erp_mes.technic_schemes ts,erp_mes.products p,
erp_base.suppliers m,erp_mes.technics t
where p.id=ts.product_id and mf.mf001=p.code and mf.mf002=ts.indexno and m.code=mf.mf006 and t.code=mf.mf004
and mf.mf005='2'
and not exists(select * from erp_mes.product_technics pt where pt.scheme_id=ts.id and pt.indexno=mf.mf003 and pt.supplier_id=m.id);

9. 新增bom清单
insert into erp_mes.product_material_items(id,product_id,cb002,cb003,indexno,material_id,amount,updated_at)
select datetime_id(),p.id,cb.cb002,cb.cb003,cb.cb004,m.id,cb.cb008,now() from shtz.bomcb cb,erp_mes.products p,erp_mes.materials m
where cb.cb001 = p.code and cb.cb005=m.code and not exists(select * from erp_mes.product_material_items pmi where pmi.product_id=p.id
and pmi.cb002=cb.cb002 and pmi.cb003=cb.cb003 and pmi.indexno=cb.cb004);

10. 新增生产工单
