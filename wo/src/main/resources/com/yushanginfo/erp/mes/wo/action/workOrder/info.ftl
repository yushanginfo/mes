[#ftl]
[@b.head/]
[@b.toolbar title="工单信息"]
  bar.addBack("${b.text("action.back")}");
[/@]
<table class="infoTable">
  <tr>
    <td class="title" width="20%">工单单别</td>
    <td class="content">${workOrder.orderType.name}</td>
    <td class="title" width="20%">工单单号</td>
    <td class="content">${workOrder.batchNum}</td>
  </tr>
  <tr>
    <td class="title">品号</td>
    <td class="content">${(workOrder.product.code)!}</td>
    <td class="title">产品图号</td>
    <td class="content">${(workOrder.product.specification)!}</td>
  </tr>
  <tr>
    <td class="title">工单数量</td>
    <td class="content">${workOrder.amount!}</td>
    <td class="title">所在厂区</td>
    <td class="content">${workOrder.factory.name}</td>
  </tr>
  <tr>
    <td class="title">工单状态</td>
    <td class="content">${(workOrder.status.name)!}</td>
    <td class="title">评审状态</td>
    <td class="content">${workOrder.assessStatus.name!}</td>
  </tr>
  <tr>
    <td class="title">客户交期</td>
    <td class="content">${(workOrder.deadline?string("yyyy-MM-dd" ))!}</td>
    <td class="title">开单时间</td>
    <td class="content">${workOrder.createdAt?string("yyyy-MM-dd HH:mm:ss")}</td>
  </tr>
  <tr>
    <td class="title">到料日期</td>
    <td class="content">[#if workOrder.materialAssess??] [#if workOrder.materialAssess.ready]有料[#else] ${(workOrder.materialAssess.readyOn?string("yyyy-MM-dd"))!}[/#if] <span style="font-size:0.8rem;color: #999;">${(workOrder.materialAssess.assessedBy.name)!} ${(workOrder.materialAssess.createdAt)?string("yyyy-MM-dd HH:mm")}</span>[/#if]</td>
    <td class="title">评审交期</td>
    <td class="content">${(workOrder.scheduledOn?string("yyyy-MM-dd" ))!}</td>
  </tr>
  <tr>
    <td class="title">材料清单</td>
    <td class="content" colspan="3">
      [#list workOrder.product.bom as i]${i.material.name} ${i.material.specification!} [#if i.amount??]${i.amount*workOrder.amount} ${i.material.unit.name}[#else]??[/#if][#if i_has_next]<br>[/#if][/#list]
    </td>
  </tr>
  <tr>
    <td class="title">工艺列表</td>
    <td class="content"  colspan="3">
       [#list workOrder.technics as wt]<span title="${(wt.technic.assessGroup.name)!'--'}">${wt.technic.name}</span>[#if wt_has_next],[/#if][/#list]
    </td>
  </tr>
  <tr>
    <td class="title">评审信息</td>
    <td class="content" colspan="3">
      <div class="row">
        <div class="col-md-3">工艺（描述）机器/供应商</div>
        [#list assessRecords as ar]
        <div class="col-md-1">${ar.updatedAt?string("MM-dd HH:mm")}</div>
        [/#list]
        <div class="col-md-${12-3-assessRecords?size}">最新评审信息</div>
      </div>
      [#list workOrder.technics?sort_by("indexno") as wt]
       <div class="row">
         <div class="col-md-3">${wt.indexno} ${wt.technic.name}(${wt.technic.description!}) ${wt.machineOrSupplierName}</div>
         [#list assessRecords as ar]
         <div class="col-md-1">${ar.getItem(wt).days!'?'}天</div>
         [/#list]
         <div class="col-md-${12-3-assessRecords?size}">
         [#if wt.days??]
         ${wt.days}天 <span style="font-size:0.8rem;color: #999;">${(wt.assessedBy.name)!} ${wt.updatedAt?string("yyyy-MM-dd HH:mm")} [#if !wt.passed]待评审[/#if]</span>
         [#else]尚未评审[/#if]
         </div>
       </div>
      [/#list]
    </td>
  </tr>
  <tr>
    <td class="title">说明</td>
    <td class="content" colspan="3">${workOrder.remark!}</td>
  </tr>
  [#if  workOrder.reviewEvents?size>0]
  [#list workOrder.reviewEvents?sort_by("updatedAt") as reviewEvent]
  <tr>
    <td class="title">第${reviewEvent_index+1}轮复审</td>
    <td class="content" colspan="3">
         <div>
          原因：${reviewEvent.comments}<br>
          说明：${reviewEvent.remark!}<br>
          反馈：[#list reviewEvent.watchers as w]${w.name}&nbsp;[/#list]&nbsp;<br>
         </div>
    </td>
  </tr>
  [/#list]
  [/#if]
  [#if logs?? && logs?size >0 ]
  <tr>
    <td class="title">审核日志</td>
    <td class="content" colspan="3">
      [#assign lastStatus="--"]
      [#list logs as log]
        [#if log.fromStatus.name != lastStatus]${log.fromStatus.name}[#else]&nbsp;&nbsp;&nbsp;&nbsp;[/#if]
        [#assign lastStatus=log.toStatus.name]
        -> ${log.toStatus.name} ${log.user.name} ${log.updatedAt?string("yyyy-MM-dd HH:mm:ss")} ${log.ip} ${log.remark!} [#if log_has_next]<br>[/#if]
      [/#list]
    </td>
  </tr>
  [/#if]
  <tr>
    <td class="title">业务员</td>
    <td class="content" colspan="3">
      ${(workOrder.saler.name)!}
    </td>
  </tr>
</table>

[@b.foot/]
