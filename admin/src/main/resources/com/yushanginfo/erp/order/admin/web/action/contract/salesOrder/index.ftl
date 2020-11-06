[#ftl]
[@b.head/]
[@b.toolbar title="订单信息"/]
<div class="search-container">
  <div class="search-panel">
    [@b.form name="salesOrderSearchForm" action="!search" target="salesOrderlist" title="ui.searchForm" theme="search"]
      [@b.textfields names="salesOrder.code;订单编号"/]
      [@b.textfields names="salesOrder.batchNum;生产批号"/]
      [@b.textfield name="salesOrder.product.specification" label="产品图号" /]
      [@b.select name="salesOrder.orderType.id" label="订单类型" items=orderTypes empty="..." option="id,name"/]
      [@b.datepicker name="salesOrder.deadline" label="客户交期" format="yyyy-MM-dd" /]
      [@b.select name="salesOrder.factory.id" label="所在厂区" items=factories empty="..." style="width:100px"/]
      [@b.field label="订单状态"]
        <select name="salesOrder.status">
          <option value="">...</option>
          <option value="0">初始</option>
          <option value="1">评审中</option>
          <option value="2">待复审</option>
          <option value="3">复审中</option>
          <option value="4">通过</option>
          <option value="5">取消</option>
        </select>
      [/@]
      <input type="hidden" name="orderBy" value="salesOrder.code desc"/>
    [/@]
  </div>
  <div class="search-list">[@b.div id="salesOrderlist" href="!search?salesOrderBy=salesOrder.code desc"/]
  </div>
</div>

[@b.foot/]
