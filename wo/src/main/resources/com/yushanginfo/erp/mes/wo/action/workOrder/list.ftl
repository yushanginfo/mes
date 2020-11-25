[#ftl]
[@b.head/]
[@b.grid items=workOrders var="workOrder"]
  [@b.gridbar]
    bar.addItem("${b.text("action.new")}",action.add());
    bar.addItem("${b.text("action.modify")}",action.edit());
    bar.addItem("${b.text("action.delete")}",action.remove("确认删除?"));
    bar.addItem("导入",action.method('importForm'));
  [/@]
  [@b.row]
    [@b.boxcol /]
    [@b.col width="7%" property="salesOrderNo" title="订单编号"][@b.a href="!info?id=${workOrder.id}"]${workOrder.salesOrderNo}[/@][/@]
    [@b.col width="7%" property="salesOrderType.name" title="订单类型"/]
    [@b.col width="7%" property="workOrderType.name" title="工单单别"/]
    [@b.col width="9%" property="batchNum" title="生产批号"/]

    [@b.col width="5%"  property="customer.code" title="客户"/]
    [@b.col width="20%" property="product.code" title="产品图号"/]
    [@b.col width="10%" property="product.name" title="产品名称"/]
    [@b.col width="10%" property="technicScheme.name" title="工单工艺"/]
    [@b.col width="5%"  property="amount" title="数量"/]
    [@b.col width="10%" property="deadline" title="客户交期"]${(workOrder.deadline?string("yyyy-MM-dd"))!}[/@]
    [@b.col width="10%" property="status.name" title="工单状态"/]
  [/@]
[/@]
[@b.foot/]
