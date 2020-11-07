[#ftl]
[@b.head/]
[@b.grid items=workOrders var="workOrder"]
  [@b.gridbar]
    bar.addItem("${b.text("编辑到料日期")}",action.edit());
  [/@]
  [@b.row]
    [@b.boxcol /]
    [@b.col width="5%" property="customer.code" title="顾客"]${workOrder.customer.code}[/@]
    [@b.col width="10%" property="code" title="工单编号"][@b.a href="!info?id=${workOrder.id}"]${workOrder.code}[/@][/@]
    [@b.col width="15%" property="product.code" title="产品图号"]${workOrder.product.specification!}[/@]
    [@b.col width="5%" property="orderType.name" title="工单类型"/]
    [@b.col width="10%" property="deadline" title="客户交期"]${(workOrder.deadline?string("yyyy-MM-dd"))!}[/@]
    [@b.col width="8%" property="amount" title="计划数量"/]
    [@b.col width="20%"  title="材料"]
      [#list workOrder.product.bom as i]${i.material.name} ${i.material.specification!} [#if i.material.amount??]${i.material.amount*workOrder.amount} ${workOrder.product.unit.name}[#else]??[/#if][#if i_has_next]<br>[/#if][/#list]
    [/@]
    [@b.col width="10%" property="materialDate" title="到料日期"]${(workOrder.materialDate?string("yyyy-MM-dd"))!}[/@]
    [@b.col width="7%" property="status.name" title="工单状态"/]
  [/@]
[/@]
[@b.foot/]
