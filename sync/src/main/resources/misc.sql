
--8. 删除加工中心
delete from mes.machines a where not exists(select * from shtz.cmsmd md where md.md001 = a.code);
