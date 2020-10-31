[#ftl]
[@b.head/]
[@b.grid items=salesOrders var="salesOrder"]
  [@b.gridbar]
    bar.addItem("${b.text("action.new")}",action.add());
    bar.addItem("${b.text("action.modify")}",action.edit());
    bar.addItem("${b.text("action.delete")}",action.remove("确认删除?"));
  [/@]
  [@b.row]
    [@b.boxcol /]
    [@b.col width="10%" property="code" title="订单编号"][@b.a href="!info?id=${salesOrder.id}"]${salesOrder.code}[/@][/@]
    [@b.col width="10%" property="batchNum" title="生产批号"/]
    [@b.col width="10%" property="orderType.name" title="订单类型"/]
    [@b.col width="5%"  property="customer.code" title="客户"/]
    [@b.col width="20%" property="product.code" title="产品图号"/]
    [@b.col width="10%" property="product.name" title="产品名称"/]
    [@b.col width="10%" property="technicScheme.name" title="订单工艺"/]
    [@b.col width="5%"  property="count" title="数量"/]
    [@b.col width="10%" property="requireOn" title="计划交付日期"]${(salesOrder.requireOn?string("yyyy-MM-dd"))!}[/@]
    [@b.col width="10%" property="status.name" title="订单状态"/]
  [/@]
[/@]
[@b.foot/]
