[#ftl]
[@b.head/]
[@b.grid items=customers var="customer"]
  [@b.gridbar]
    bar.addItem("${b.text("action.new")}",action.add());
    bar.addItem("${b.text("action.modify")}",action.edit());
    bar.addItem("${b.text("action.delete")}",action.remove("确认删除?"));
    bar.addItem("导入",action.method('importForm'));
    bar.addItem("${b.text('action.export')}",action.exportData("code:客户编码,name:客户全称,saler.user.code:业务人员工号",null,'fileName=客户信息'));
  [/@]
  [@b.row]
    [@b.boxcol /]
    [@b.col width="10%" property="code" title="编号"]${customer.code}[/@]
    [@b.col width="15%" property="shortName" title="简称" /]
    [@b.col width="55%" property="name" title="全称"][@b.a href="!info?id=${customer.id}"]${customer.name}[/@][/@]
    [@b.col width="15%" property="saler.name" title="业务人" /]
  [/@]
[/@]
[@b.foot/]
