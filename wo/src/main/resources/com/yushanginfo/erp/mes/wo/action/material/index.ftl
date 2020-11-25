[#ftl]
[@b.head/]
[@b.toolbar title="工单信息"/]
<div class="search-container">
  <div class="search-panel">
    [@b.form name="materialSearchForm" action="!search" target="materiallist" title="ui.searchForm" theme="search"]
      [@b.textfields names="workOrder.salesOrderNo;订单编号"/]
      [@b.textfields names="workOrder.batchNum;生产批号"/]
      [@b.textfield name="workOrder.product.specification" label="产品图号" /]
      [@b.datepicker name="workOrder.deadline" label="客户交期" format="yyyy-MM-dd" /]
      [@b.field label="工单状态"]
        <select name="status">
          <option value="">...</option>
          <option value="0">初始</option>
          <option value="1">评审中</option>
          <option value="2">待复审</option>
          <option value="3">复审中</option>
          <option value="4">通过</option>
          <option value="5">取消</option>
        </select>
      [/@]
      <input type="hidden" name="orderBy" value="salesOrderNo desc"/>
    [/@]
  </div>
  <div class="search-list">[@b.div id="materiallist" href="!search?materialBy=salesOrderNo desc"/]
  </div>
</div>

[@b.foot/]
