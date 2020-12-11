[#ftl]
[@b.head/]
[@b.grid items=workOrders var="workOrder"]
  [@b.gridbar]
    //bar.addItem("${b.text("action.new")}",action.add());
    bar.addItem("${b.text("action.modify")}",action.edit());
    //bar.addItem("${b.text("action.delete")}",action.remove("确认删除?"));
    bar.addItem("${b.text("action.export")}",action.exportData("orderType.name:工单单别,batchNum:工单单号,product.code:品号,product.specification:公司图号,technicScheme.name:工单工艺,amount:数量,deadline:客户交付日期,plannedEndOn:计划交付日期,scheduledOn:评审交付日期,status.name:工单状态,assessStatus:评审状态,materialAssess.ready:是否有料,materialAssess.readyOn:到料日期,factory.name:工厂,createdAt:创建日期",null,'fileName=工单信息'));
  [/@]
  [@b.row]
    [@b.boxcol /]
    [@b.col width="9%" property="batchNum" title="工单单号"]
       [@b.a href="!info?id=${workOrder.id}"]<span title="${workOrder.orderType.name}">${workOrder.batchNum}</span>[/@]
    [/@]
    [@b.col width="18%" property="product.specification" title="产品图号"/]
    [@b.col width="17%" property="product.name" title="产品名称"/]
    [@b.col width="10%" property="technicScheme.name" title="工单工艺"/]
    [@b.col width="5%"  property="amount" title="数量"/]
    [@b.col width="10%" property="deadline" title="客户交期"]${(workOrder.deadline?string("yyyy-MM-dd"))!}[/@]
    [@b.col width="8%" property="assessStatus.name" title="评审状态"/]
    [@b.col width="8%" property="status.name" title="工单状态"/]
  [/@]
[/@]
[@b.foot/]
