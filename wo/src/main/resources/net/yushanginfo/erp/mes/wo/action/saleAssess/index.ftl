[#ftl]
[@b.head/]
[@b.toolbar title="工单业务评审"/]
<div class="search-container">
  <div class="search-panel">
    [@b.form name="workOrderSearchForm" action="!search" target="workOrderlist" title="ui.searchForm" theme="search"]
      [@b.select name="workOrder.orderType.id" label="工单单别" items=orderTypes empty="..." option="id,name"/]
      [@b.textfields names="workOrder.batchNum;工单单号,workOrder.product.specification;产品图号"/]
      [@b.textfield name="customerCode"  label="客户编号" placeholder="多个用逗号隔开"/]
      [@b.select name="workOrder.status.id" label="工单状态" items=orderStatuses empty="..." option="id,name"/]
      [@b.field label="评审状态"]
        <select name="workOrder.assessStatus">
          <option value="">...</option>
          <option value="0">初始</option>
          <option value="1">评审中</option>
          <option value="2">待复审</option>
          <option value="3">复审中</option>
          <option value="4">通过</option>
          <option value="5">取消</option>
        </select>
      [/@]
      [@b.field label="复审轮次"]
        <select name="reviewRound">
          <option value="">...</option>
          <option value="1">第一轮</option>
          <option value="2">第二轮</option>
          <option value="3">第三轮</option>
        </select>
      [/@]
      [@b.textfield name="workOrder.saler.name"  label="业务员" placeholder="姓名"/]
      [@b.date name="createdOn"  label="开单日期" /]
      <input type="hidden" name="orderBy" value="workOrder.createdAt desc"/>
    [/@]
  </div>
  <div class="search-list">[@b.div id="workOrderlist" href="!search?orderBy=workOrder.createdAt desc"/]
  </div>
</div>

[@b.foot/]
